package actor

import org.apache.http.client.HttpClient
import akka.actor.{ActorRef, Actor}
import org.apache.http.client.methods.HttpGet
import play.api.Logger
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream
import model.Article
import scala.util.control.Breaks._

/**
 * The Class ImageFetcher.
 *
 * @author Nguyen Duc Dung
 * @since 2/23/14 3:42 PM
 *
 */
class ImageFetcher(httpClient: HttpClient, persistent: ActorRef) extends Actor {

  // youtube thumb image size
  val best_size = 900
  val min_size = 200

  override def receive = {
    case article: Article =>
      var bestImage: Option[String] = None
      var bestSize: Int = 0
      breakable {
        article.potentialImages.foreach(url => {
          fetch(url).map(tuple => {
            if (bestSize < tuple._2) {
              bestImage = Some(tuple._1)
              bestSize = tuple._2
              if (bestSize >= best_size) {
                break()
              }
            }
          })
        })
      }
      persistent ! article.copy(featureImage = bestImage)
  }

  def fetch(url: String): Option[(String, Int)] = {
    var result: Option[(String, Int)] = None
    try {
      val response = httpClient.execute(new HttpGet(url))
      Logger.info(s"Download ${response.getStatusLine.getStatusCode} : $url")
      if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
        val entity = response.getEntity
        val content = EntityUtils.toByteArray(entity)
        if (content != null && content.length > 0) {
          val input = new ByteArrayInputStream(content)
          val image = ImageIO.read(input)
          if (image != null) {
            val width = image.getWidth
            val height = image.getHeight
            val size = width + height
            if (size >= min_size) {
              result = Some(url, size)
            }
          }
          input.close()
        }
      }
      EntityUtils.consume(response.getEntity)
    } catch {
      case ex: Exception =>
        Logger.error("Error: " + url, ex)
    }
    result
  }
}
