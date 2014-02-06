package model

import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils

/**
 * The Class Article.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 12:26 PM
 *
 */
case class Article(
                    //this is a url after normalization.
                    _id: String,
                    blogId: ObjectId,
                    title: String,
                    url: String,
                    description: String,
                    //This form rss.
                    descriptionHtml: Array[Byte] = Array.empty,
                    featureImage: Option[String],
                    author: Option[String],
                    tags: Option[String],
                    clicked: Int = 0,
                    publishedDate: DateTime = DateTime.now(),
                    crawledDate: DateTime = DateTime.now()
                    ) extends BaseModel(_id) {
  def getDescriptionHtml = if (!descriptionHtml.isEmpty) IOUtils.toString(descriptionHtml, "UTF-8") else StringUtils.EMPTY

  override def equals(obj: Any) = {
    obj.isInstanceOf[Article] && obj.asInstanceOf[Article]._id == _id
  }

  override def hashCode() = _id.hashCode

  def shortDescription = if (description.length > 100) description.substring(0, 100) else description

  def tagList = tags.getOrElse("").split(",")
}
