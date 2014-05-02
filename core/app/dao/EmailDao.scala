package dao

import com.novus.salat.dao.SalatDAO
import model.Email
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject

/**
 * The Class UserDao.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:35 PM
 *
 */
object EmailDao extends BaseDao[Email, ObjectId] {

  def dao = new SalatDAO[Email, ObjectId](collection = mongoCollection("email")) {}

  def findByEmail(email: String) = find(
    MongoDBObject("email" -> email)
  )

}