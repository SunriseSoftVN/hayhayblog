package model

import org.bson.types.ObjectId

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
                 rssUrl: String,
                 isEnable: Boolean = false,
                 categoryId: ObjectId
                 ) extends BaseModel(_id)
