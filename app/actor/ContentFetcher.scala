package actor

import akka.actor.{ActorRef, Props, Actor}
import model.Article
import org.apache.http.client.HttpClient
import play.api.libs.concurrent.Akka
import akka.routing.RoundRobinRouter
import play.api.Play.current
import org.apache.http.client.methods.HttpGet
import play.api.Logger
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import java.io.ByteArrayInputStream
import org.jsoup.Jsoup
import collection.JavaConversions._
import org.apache.commons.lang3.StringUtils
import utils.Options._

/**
 * The Class ContentFetcher.
 *
 * @author Nguyen Duc Dung
 * @since 2/24/14 9:19 AM
 *
 */
class ContentFetcher(httpClient: HttpClient, persistent: ActorRef) extends Actor {

  val imageFetcher = Akka.system.actorOf(Props(new ImageFetcher(httpClient, persistent))
    .withRouter(RoundRobinRouter(nrOfInstances = 10)))

  override def receive = {
    case article: Article =>
      var commentRss: Option[String] = None
      if (article.commentRss.isEmpty) {
        try {
          val response = httpClient.execute(new HttpGet(article.url))
          Logger.info(s"Download ${response.getStatusLine.getStatusCode} : ${article.url}")
          if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
            val entity = response.getEntity
            val content = EntityUtils.toByteArray(entity)
            val input = new ByteArrayInputStream(content)
            val doc = Jsoup.parse(input, null, article.url)
            //try to find comment rss link
            val links = doc.select("link[rel=alternate]")
            links.foreach(link => {
              val href = link.attr("href")
              if (StringUtils.isNotBlank(href) && href.contains("/comments/")) {
                commentRss = href
              }
            })
          }
          EntityUtils.consume(response.getEntity)
        } catch {
          case ex: Exception =>
            Logger.error("Error: " + article.url, ex)
        }
      }

      imageFetcher ! article.copy(commentRss = commentRss)
  }
}
