package actor

import akka.actor.Actor
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
import org.apache.http.client.utils.URIUtils
import java.net.URI

/**
 * The Class CrawlActor.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 4:45 AM
 *
 */
class CrawlActor(httpClient: HttpClient) extends Actor {

  val parser = new RssParser
  val newsParser = new NewsParser
  val urlValidator = new UrlValidator

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
    val url = URLCanonicalizer.getCanonicalURL(StringUtils.trimToEmpty(syndEntry.getLink), blog.url)

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
            val featureImage = extractor._3.headOption

            var tags = ""
            syndEntry.getCategories.foreach {
              case sCat: SyndCategory =>
                if (StringUtils.isNotBlank(sCat.getName)) {
                  val tagName = StringUtils.stripAccents(StringUtils.trimToEmpty(sCat.getName)).toLowerCase
                  tags += "," + tagName
                  val tag = TagDao.findOrCreate(tags)
                  TagDao.save(tag.copy(count = tag.count + 1))
                }
            }

            tags.replaceFirst(",", "")

            val cleanAuthor = if (StringUtils.isNotBlank(syndEntry.getAuthor)) Jsoup.parse(syndEntry.getAuthor).text() else ""
            val author = if (StringUtils.isNotBlank(cleanAuthor)) Some(cleanAuthor) else None

            val hostName = URIUtils.extractHost(new URI(blog.url))
              .getHostName
              .replaceAll("[^a-zA-Z\\d\\s:]", "")

            val article = Article(
              _id = normalizationUrl,
              url = url,
              title = StringUtils.capitalize(title),
              uniqueTitle = genUniqueTitle(title),
              domain = hostName,
              description = des,
              featureImage = featureImage,
              author = author,
              tags = if (StringUtils.isNotBlank(tags)) Some(tags) else None,
              descriptionHtml = rssHtml.getBytes("UTF-8"),
              blogId = blog._id,
              publishedDate = pubDate.getOrElse(DateTime.now())
            )

            val exist = ArticleDao.findOne(MongoDBObject("description" -> article.description))
            if (exist.isEmpty) {
              ArticleDao.save(article)
            }
          }
        }
      }
    }
  }

  private def genUniqueTitle(title: String) = StringUtils.stripAccents(title)
    .replaceAll("[^a-zA-Z\\d\\s:]", "")
    .replaceAll(" ", "-")
    .toLowerCase

}
