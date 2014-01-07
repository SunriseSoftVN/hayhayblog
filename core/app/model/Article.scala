package model

import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * The Class Article.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 12:26 PM
 *
 */
case class Article(
                    _id: ObjectId = new ObjectId(),
                    blogId: ObjectId,
                    title: String,
                    url: String,
                    description: String,
                    text: String,
                    featureImage: Option[String],
                    author: Option[String],
                    tags: List[String],
                    publishDate: DateTime = DateTime.now(),
                    crawledDate: DateTime = DateTime.now()
                    ) extends BaseModel(_id)
