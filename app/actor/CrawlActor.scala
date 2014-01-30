package actor

import akka.actor.Actor
import org.apache.http.client.HttpClient
import parser.{NewsParser, RssParser}
import org.apache.commons.validator.routines.UrlValidator

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
    case Crawl(blog) => {

    }
  }
}
