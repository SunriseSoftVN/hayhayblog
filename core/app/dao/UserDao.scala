package dao

import com.novus.salat.dao.SalatDAO
import model.User
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
object UserDao extends BaseDao[User, ObjectId] {

  def dao = new SalatDAO[User, ObjectId](collection = mongoCollection("user")) {}

  def findByEmail(email: String) = findOne(MongoDBObject("email" -> email))

  def login(email: String, password: String): Boolean = {
    val user = findByEmail(email)
    user.isDefined && user.get.password == DigestUtils.md5Hex(password)
  }

}