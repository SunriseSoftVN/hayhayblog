package model

import org.bson.types.ObjectId
import org.joda.time.DateTime
import dao.CategoryDao

/**
 * The Class Blog.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 12:24 PM
 *
 */
case class Blog(
                 _id: ObjectId = new ObjectId(),
                 name: String,
                 url: String,
                 uniqueName: String,
                 rssUrl: String,
                 status: String = BlogStatus.UPDATED,
                 description: Option[String] = None,
                 isEnable: Boolean = false,
                 categoryId: ObjectId,
                 read: Int = 0,
                 lastUpdated: DateTime = DateTime.now
                 ) extends BaseModel(_id) {
  def category = CategoryDao.findOneById(categoryId)
}

object BlogStatus {
  lazy val UPDATED = "updated"
  lazy val UPDATING = "updating"
  lazy val ERROR = "error"
  lazy val ACCEPTABLE_ERROR_COUNT = 10

  def asSelectValue = Seq(
    UPDATED -> "UPDATED",
    UPDATING -> "UPDATING",
    ERROR -> "ERROR"
  )
}