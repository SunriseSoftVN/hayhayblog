package actor

import akka.actor.{ActorRef, Props, Actor}
import model.Article
import play.api.Logger
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import parser.HtmlExtractor
import org.jsoup.Jsoup
import java.io.ByteArrayInputStream
import play.api.libs.concurrent.Akka
import akka.routing.RoundRobinRouter

/**
 * The Class ContentFetcher.
 *
 * @author Nguyen Duc Dung
 * @since 2/24/14 9:19 AM
 *
 */
class ContentFetcher(httpClient: HttpClient, persistent: ActorRef) extends Actor {

  val imageFetcher = Akka.system.actorOf(Props(new ImageFetcher(httpClient, persistent))
    .withRouter(RoundRobinRouter(nrOfInstances = 10)), name = "imageFetcher")

  override def receive = {
    case article: Article =>
      try {
        val response = httpClient.execute(new HttpGet(article.url))
        Logger.info(s"Download ${response.getStatusLine.getStatusCode} : ${article.url}")
        if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
          val entity = response.getEntity
          val content = EntityUtils.toByteArray(entity)
          val htmlExtractor = new HtmlExtractor
          val input = new ByteArrayInputStream(content)
          val doc = Jsoup.parse(input, null, article.url)
          htmlExtractor.extract(doc)
          article.potentialImages = htmlExtractor.images.toList

          imageFetcher ! article
        }
        EntityUtils.consume(response.getEntity)
      } catch {
        case ex: Exception =>
          Logger.error("Error: " + article.url, ex)
      }
  }
}
