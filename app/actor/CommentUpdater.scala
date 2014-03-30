package actor

import akka.actor.Actor
import org.apache.http.client.HttpClient
import parser.RssParser
import org.apache.http.client.methods.HttpGet
import play.api.Logger
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import collection.JavaConversions._
import dao.ArticleDao

/**
 * The Class CommentUpdater.
 *
 * @author Nguyen Duc Dung
 * @since 3/30/14 12:59 PM
 *
 */
class CommentUpdater(httpClient: HttpClient) extends Actor {

  val parser = new RssParser

  override def receive: Receive = {
    case UpdateComment(article) =>
      article.commentRss.map(url => {
        try {
          val response = httpClient.execute(new HttpGet(url))
          Logger.info(s"Download ${response.getStatusLine.getStatusCode} : $url")
          if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
            val entity = response.getEntity
            val content = EntityUtils.toByteArray(entity)
            if (content != null && content.length > 0) {
              parser.parse(content, url).map(rssFeed => {
                val commentTotal = rssFeed.getEntries.length
                ArticleDao.save(article.copy(commentTotal = commentTotal))
              })
            }
          }
          EntityUtils.consume(response.getEntity)
        } catch {
          case ex: Exception =>
            Logger.error("Error: " + url, ex)
        }
      })
  }
}
