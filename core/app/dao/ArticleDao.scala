package dao

import com.novus.salat.dao.SalatDAO
import se.radley.plugin.salat._
import model.Article
import play.api.Play.current
import org.bson.types.ObjectId
import com.mongodb.casbah.commons.MongoDBObject

/**
 * The Class ArticleDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 4:03 AM
 *
 */
object ArticleDao extends BaseDao[Article, String] {
  override def dao = new SalatDAO[Article, String](collection = mongoCollection("article")) {}

  def findByCatId(id: ObjectId, take: Int = 10) = {
    val blogIds = BlogDao.findByCatId(id).map(_._id)
    find(
      MongoDBObject("blogId" -> MongoDBObject("$in" -> blogIds))
    ).sort(MongoDBObject("publishedDate" -> -1))
      .take(take)
      .toList
  }

  def topTen = find(MongoDBObject.empty).sort(MongoDBObject("publishedDate" -> -1)).take(10).toList

}
