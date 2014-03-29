package dao

import org.bson.types.ObjectId
import com.novus.salat.dao.{SalatDAO, DAO}
import se.radley.plugin.salat._
import model.Image
import play.api.Play.current

/**
 * The Class ImageDao.
 *
 * @author Nguyen Duc Dung
 * @since 3/29/14 10:47 PM
 *
 */
object ImageDao extends BaseDao[Image, ObjectId] {
  override def dao: DAO[Image, ObjectId] = new SalatDAO[Image, ObjectId](collection = mongoCollection("image")) {}
}
