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

  def findByCatId(id: ObjectId, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay
    val blogIds = BlogDao.findByCatId(id).map(_._id)

    val query = MongoDBObject("blogId" -> MongoDBObject("$in" -> blogIds))


    val totalRow = count(query)

    var totalPage = totalRow / itemDisplay
    if (totalRow - totalPage * itemDisplay > 0) {
      totalPage += 1
    }

    val articles = find(query)
      .skip(skip)
      .limit(itemDisplay)
      .sort(MongoDBObject("publishedDate" -> -1))
      .toList

    (articles, totalPage.toInt)
  }

  def topTenMostRead = find(MongoDBObject.empty).sort(MongoDBObject("clicked" -> -1)).take(10).toList

  def findByUrl(url: String) = findOne(MongoDBObject("url" -> url))

  def updateClick(url: String) {
    findByUrl(url).map(article => {
      save(article.copy(clicked = article.clicked + 1))
    })
  }
}
