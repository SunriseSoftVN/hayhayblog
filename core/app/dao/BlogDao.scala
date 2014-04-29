package dao

import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import se.radley.plugin.salat._
import model.{BlogStatus, Blog}
import play.api.Play.current
import com.mongodb.casbah.commons.MongoDBObject
import org.joda.time.DateTime

/**
 * The Class BlogDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 3:18 AM
 *
 */
object BlogDao extends BaseDao[Blog, ObjectId] {
  override def dao = new SalatDAO[Blog, ObjectId](collection = mongoCollection("blog")) {}

  /**
   * Blog need to update is a feed is not updating and has last updated is 30m ago.
   * @return
   */
  def needToUpdate = find(
    MongoDBObject(
      "status" -> MongoDBObject("$ne" -> BlogStatus.UPDATING),
      "isEnable" -> true,
      "lastUpdated" -> MongoDBObject("$lt" -> DateTime.now.minusMinutes(30))
    )
  ).toList

  def findByCatId(id: ObjectId) = find(MongoDBObject("categoryId" -> id)).sort(MongoDBObject("read" -> -1)).toList

  def findByBlogName(blogName: String) = findOne(MongoDBObject("uniqueName" -> blogName))

  def increaseRead(id: ObjectId) = findOneById(id).map(blog => {
    save(blog.copy(read = blog.read + 1))
  })

  def findByName(name: String) = findOne(MongoDBObject("name" -> name))

  def top = find(MongoDBObject("homePage" -> true)).sort(MongoDBObject("read" -> -1)).take(10).toList

  def canShowInHomePage = find(MongoDBObject("homePage" -> true)).toList

  def showAll() = all.foreach(blog => {
    save(blog.copy(homePage = true))
  })
}
