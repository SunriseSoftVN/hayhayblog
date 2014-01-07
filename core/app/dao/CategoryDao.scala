package dao

import com.novus.salat.dao.SalatDAO
import model.{Category, User}
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject
import org.apache.commons.codec.digest.DigestUtils

/**
 * The Class UserDao.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:35 PM
 *
 */
object CategoryDao extends BaseDao[Category, ObjectId] {

  def dao = new SalatDAO[Category, ObjectId](collection = mongoCollection("category")) {}

}