package controllers

import play.api.mvc.{Action, Controller}
import org.bson.types.ObjectId
import dao.{ImageDao, BlogDao}
import actor.HttpClientBuilder
import org.apache.http.client.methods.HttpGet
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import org.apache.http.HttpStatus
import org.apache.http.util.EntityUtils
import play.api.Logger
import model.Image
import org.joda.time.DateTime

/**
 * The Class ImageCtr.
 *
 * @author Nguyen Duc Dung
 * @since 3/29/14 11:01 PM
 *
 */
object ImageCtr extends Controller with ControllerHelper {

  val httpClient = HttpClientBuilder.build()

  def googleFetchFaviconUrl(url: String) = s"http://www.google.com/s2/favicons?domain=$url&alt=feed"

  def favicon(blogId: ObjectId) = Action.async {
    Future {
      BlogDao.findOneById(blogId).mapRender(blog => {
        blog.iconId.map(iconId => {
          ImageDao.findOneById(blog.iconId.get).mapRender(image => {
            //update image if the image age is too old
            if (image.lastUpdated.isBefore(DateTime.now().minusDays(30))) {
              download(image.url).mapRender(content => {
                ImageDao.save(image.copy(content = content, lastUpdated = DateTime.now()))
                Ok(content).as("image/png")
              })
            } else {
              Ok(image.content).as("image/png")
            }
          })
        }).getOrElse {
          //download image in case the blog does not have icon
          val imageUrl = googleFetchFaviconUrl(blog.url)
          download(imageUrl).mapRender(content => {
            val image = Image(
              url = imageUrl,
              content = content
            )
            ImageDao.save(image)
            BlogDao.save(blog.copy(iconId = Some(image._id)))
            Ok(content).as("image/png")
          })
        }
      })
    }
  }

  private def download(imageUrl: String) = {
    var result: Option[Array[Byte]] = None
    try {
      val response = httpClient.execute(new HttpGet(imageUrl))
      Logger.info(s"Download ${response.getStatusLine.getStatusCode} : $imageUrl")
      if (response.getStatusLine.getStatusCode == HttpStatus.SC_OK) {
        val entity = response.getEntity
        val content = EntityUtils.toByteArray(entity)
        result = if (content != null && content.length > 0) {
          Some(content)
        } else None
        EntityUtils.consume(response.getEntity)
      }
    } catch {
      case ex: Exception =>
        Logger.error("Error: " + imageUrl, ex)
    }
    result
  }

}
