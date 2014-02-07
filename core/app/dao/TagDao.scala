package dao

import com.novus.salat.dao.SalatDAO
import model.Tag
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
object TagDao extends BaseDao[Tag, ObjectId] {

  def dao = new SalatDAO[Tag, ObjectId](collection = mongoCollection("tag")) {}

  def top = find(MongoDBObject.empty)
    .sort(MongoDBObject("read" -> -1))
    .take(10)
    .toList

  def findOrCreate(tagName: String) = findOne(MongoDBObject(
    "name" -> tagName
  )).getOrElse {
    Tag(name = tagName)
  }

}