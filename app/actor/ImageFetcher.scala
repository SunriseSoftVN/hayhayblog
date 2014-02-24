package actor

import org.apache.http.client.HttpClient
import akka.actor.Actor
import org.apache.http.client.methods.HttpGet
import play.api.Logger
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import javax.imageio.ImageIO
import java.io.ByteArrayInputStream

/**
 * The Class ImageFetcher.
 *
 * @author Nguyen Duc Dung
 * @since 2/23/14 3:42 PM
 *
 */
class ImageFetcher(httpClient: HttpClient) extends Actor {

  val prefer_size = 300

  override def receive = {
    case Fetch(url) =>
      try {
        val response = httpClient.execute(new HttpGet(url))
        if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
          val entity = response.getEntity
          val content = EntityUtils.toByteArray(entity)
          if (content != null && content.length > 0) {
            val input = new ByteArrayInputStream(content)
            val image = ImageIO.read(input)
            val width = image.getWidth
            val height = image.getHeight
            val size = width + height
            if (size >= prefer_size) {
              sender ! FetchResult(Some(Image(url)))
            } else {
              sender ! FetchResult(None)
            }
            input.close()
          }
        }
        EntityUtils.consume(response.getEntity)
      } catch {
        case ex: Exception =>
          Logger.error("Error: " + url, ex)
          sender ! FetchResult(None)
      }
  }
}
