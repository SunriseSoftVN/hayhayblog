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

  def findByCatId(catId: ObjectId, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay
    val blogIds = BlogDao.findByCatId(catId).map(_._id)

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

  def findByBlogId(blogId: ObjectId, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay

    val query = MongoDBObject("blogId" -> blogId)

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

  def findByTag(tag: String, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay

    val query = MongoDBObject("tags" -> MongoDBObject("$regex" -> tag))

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


  def latest(take: Int = 10) = find(MongoDBObject.empty)
    .sort(MongoDBObject("publishedDate" -> -1))
    .take(take)
    .toList

  def mostRead(take: Int = 6) = find(MongoDBObject.empty)
    .sort(MongoDBObject("clicked" -> -1))
    .take(take)
    .toList

  def mostReadByCatId(catId: ObjectId, take: Int) = {
    val blogIds = BlogDao.findByCatId(catId).map(_._id)
    val query = MongoDBObject("blogId" -> MongoDBObject("$in" -> blogIds))
    find(query).sort(MongoDBObject("clicked" -> -1)).take(take).toList
  }

  def mostReadByBlogId(blogId: ObjectId, take: Int) = {
    val query = MongoDBObject("blogId" -> blogId)
    find(query).sort(MongoDBObject("clicked" -> -1)).take(take).toList
  }

  def findByUrl(url: String) = findOne(MongoDBObject("url" -> url))


  def findByUniqueTitleAndBlogName(blogName: String, uniqueTitle: String) = findOne(
    MongoDBObject("uniqueTitle" -> uniqueTitle, "blogName" -> blogName)
  )

  def removeByBlogId(blogId: ObjectId) = remove(MongoDBObject("blogId" -> blogId))

}
