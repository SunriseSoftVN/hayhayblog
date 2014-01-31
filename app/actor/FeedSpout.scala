package actor

import akka.actor.{Props, Actor}
import play.api.libs.concurrent.Akka
import akka.routing.RoundRobinRouter
import play.api.Play.current
import dao.BlogDao

/**
 * The Class FeedSpout.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 4:38 AM
 *
 */
class FeedSpout(nrOfCrawlActor: Int = 20) extends Actor {

  val httpClient = HttpClientBuilder.build()

  val crawlActor = Akka.system.actorOf(Props(new CrawlActor(httpClient))
    .withRouter(RoundRobinRouter(nrOfInstances = nrOfCrawlActor)), name = "crawlActor")

  override def receive = {
    case Start =>
      for (blog <- BlogDao.needToUpdate) {
        crawlActor ! Crawl(blog)
      }
  }
}
