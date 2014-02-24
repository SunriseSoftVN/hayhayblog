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
class FeedSpout(nrOfActor: Int = 20) extends Actor {

  val httpClient = HttpClientBuilder.build()

  val persistent = Akka.system.actorOf(Props[Persistent], name = "persistent")

  val rssFetcher = Akka.system.actorOf(Props(new RssFetcher(httpClient, persistent))
    .withRouter(RoundRobinRouter(nrOfInstances = nrOfActor)), name = "rssFetcher")

  override def receive = {
    case Start =>
      for (blog <- BlogDao.needToUpdate) {
        rssFetcher ! Crawl(blog)
      }
  }
}
