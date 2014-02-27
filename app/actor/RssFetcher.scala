package actor

import akka.actor.{ActorRef, Props, Actor}
import org.apache.http.client.HttpClient
import parser.{NewsParser, RssParser}
import org.apache.commons.validator.routines.UrlValidator
import dao.{TagDao, ArticleDao, BlogDao}
import org.joda.time.DateTime
import model.{Article, Blog, BlogStatus}
import org.apache.http.client.methods.HttpGet
import play.api.Logger
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import com.sun.syndication.feed.synd.{SyndCategory, SyndEntry}
import org.apache.commons.lang3.StringUtils
import com.mongodb.casbah.commons.MongoDBObject
import org.jsoup.Jsoup
import collection.JavaConversions._
import url.URLCanonicalizer
import ch.sentric.URL
import play.api.libs.concurrent.Akka
import akka.routing.RoundRobinRouter
import play.api.Play.current
import scala.collection.mutable
import org.rometools.feed.module.mediarss.{MediaEntryModuleImpl, MediaModule}
import org.rometools.feed.module.slash.Slash
import org.rometools.feed.module.feedburner.FeedBurner
import utils.Options._
import utils.String2Int

/**
 * The Class RssFetcher.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 4:45 AM
 *
 */
class RssFetcher(httpClient: HttpClient, persistent: ActorRef) extends Actor {

  val parser = new RssParser
  val newsParser = new NewsParser
  val urlValidator = new UrlValidator

  val contentFetcher = Akka.system.actorOf(Props(new ContentFetcher(httpClient, persistent))
    .withRouter(RoundRobinRouter(nrOfInstances = 10)))

  override def receive = {
    case Crawl(blog) =>
      //update blog status
      val _blog = blog.copy(status = BlogStatus.UPDATING, lastUpdated = DateTime.now)
      BlogDao.save(_blog)

      try {
        val response = httpClient.execute(new HttpGet(blog.rssUrl))
        Logger.info(s"Download ${response.getStatusLine.getStatusCode} : ${blog.rssUrl}")

        if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
          val entity = response.getEntity
          val content = EntityUtils.toByteArray(entity)
          if (content != null && content.length > 0) {
            parser.parse(content, blog.rssUrl).map(rssFeed => {
              rssFeed.getEntries.foreach {
                case entry: SyndEntry => parse(entry, blog)
              }
            })
          }
        }

        EntityUtils.consume(response.getEntity)

        BlogDao.save(_blog.copy(status = BlogStatus.UPDATED))
      } catch {
        case ex: Exception =>
          BlogDao.save(_blog.copy(status = BlogStatus.ERROR))
          Logger.error("Error: " + _blog.url, ex)
      }

  }


  private def parse(syndEntry: SyndEntry, blog: Blog): Any = {
    var url = URLCanonicalizer.getCanonicalURL(StringUtils.trimToEmpty(syndEntry.getLink), blog.url)

    //check feedbunner link
    val feedBunnerModule = syndEntry.getModule(FeedBurner.URI)
    if (feedBunnerModule != null) {
      val origLink = feedBunnerModule.asInstanceOf[FeedBurner].getOrigLink
      if (StringUtils.isNotEmpty(origLink)) {
        url = URLCanonicalizer.getCanonicalURL(StringUtils.trimToEmpty(origLink), blog.url)
      }
    }

    if (urlValidator.isValid(url)) {
      val normalizationUrl = new URL(url).getNormalizedUrl
      if (ArticleDao.findOneById(normalizationUrl).isEmpty) {
        //double check to avoid duplicate item
        val exist = StringUtils.isNotBlank(syndEntry.getTitle) && ArticleDao.findOne(MongoDBObject(
          "blogId" -> blog._id,
          "title" -> StringUtils.trimToEmpty(syndEntry.getTitle)
        )).isDefined
        if (!exist) {

          val cleanTitle = if (StringUtils.isNotBlank(syndEntry.getTitle)) Jsoup.parse(syndEntry.getTitle).text() else url
          val title = if (StringUtils.isNotBlank(cleanTitle)) cleanTitle else url

          if (StringUtils.isNotBlank(title)) {
            val extractor = newsParser.extract(syndEntry, url)
            var pubDate: Option[DateTime] = None
            if (syndEntry.getPublishedDate != null) {
              pubDate = Some(new DateTime(syndEntry.getPublishedDate))
            }
            if (pubDate.isEmpty && syndEntry.getUpdatedDate != null) {
              pubDate = Some(new DateTime(syndEntry.getUpdatedDate))
            }
            val des = extractor._1.getOrElse("")
            val rssHtml = extractor._2.getOrElse("")
            val potentialImages = extractor._3

            //get thumbnail by rome module
            var featureImage: Option[String] = None
            val mediaModule = syndEntry.getModule(MediaModule.URI)
            if (mediaModule != null) {
              val mediaModuleImpl = mediaModule.asInstanceOf[MediaEntryModuleImpl]
              if (mediaModuleImpl.getMetadata != null) {
                val thumbnails = mediaModuleImpl.getMetadata.getThumbnail
                featureImage = thumbnails.headOption.map(_.getUrl.toString)
              }
            }

            //get number of comments
            val slashModule = syndEntry.getModule(Slash.URI)
            var commentTotal = 0
            if (slashModule != null && slashModule.asInstanceOf[Slash].getComments != null) {
              commentTotal = slashModule.asInstanceOf[Slash].getComments
            }

            //get number of comments with feedbuner
            var commentRss: Option[String] = None
            syndEntry.getForeignMarkup.foreach(markup => {
              if (markup.getName == "commentrss"
                && markup.getNamespaceURI == "http://wellformedweb.org/CommentAPI/") {
                commentRss = markup.getValue
              } else if (markup.getName == "total"
                && markup.getNamespaceURI == "http://purl.org/syndication/thread/1.0") {
                commentTotal = String2Int.unapply(markup.getValue).getOrElse(0)
              }
            })

            val tagSet = new mutable.HashSet[String]()
            syndEntry.getCategories.foreach {
              case sCat: SyndCategory =>
                if (StringUtils.isNotBlank(sCat.getName)) {
                  val tagName = clean(StringUtils.stripAccents(StringUtils.trimToEmpty(sCat.getName)).toLowerCase)
                  tagSet += tagName
                  val tag = TagDao.findOrCreate(tagName)
                  TagDao.save(tag.copy(count = tag.count + 1))
                }
              case _ => //ignore
            }

            val tags = tagSet.mkString("", ",", "")

            val cleanAuthor = if (StringUtils.isNotBlank(syndEntry.getAuthor)) Jsoup.parse(syndEntry.getAuthor).text() else ""
            val author = if (StringUtils.isNotBlank(cleanAuthor)) Some(cleanAuthor) else None

            val article = Article(
              _id = normalizationUrl,
              url = url,
              title = StringUtils.capitalize(title),
              uniqueTitle = genUniqueTitle(title),
              blogName = blog.uniqueName,
              description = des,
              featureImage = featureImage,
              author = author,
              tags = if (StringUtils.isNotBlank(tags)) Some(tags) else None,
              descriptionHtml = rssHtml.getBytes("UTF-8"),
              blogId = blog._id,
              commentTotal = commentTotal,
              commentRss = commentRss,
              publishedDate = pubDate.getOrElse(DateTime.now())
            )

            article.potentialImages ++= potentialImages
            contentFetcher ! article
          }
        }
      }
    }
  }

  private def genUniqueTitle(title: String) = StringUtils.stripAccents(title)
    .replaceAll("[^a-zA-Z\\d\\s:]", "")
    .replaceAll(" ", "-")
    .replaceAll(":", "-")
    .toLowerCase

  private def clean(title: String) = title
    .replaceAll("\"", "")

}