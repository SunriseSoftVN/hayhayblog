package model

import org.bson.types.ObjectId

/**
 * The Class MyBlog.
 *
 * @author Nguyen Duc Dung
 * @since 3/30/14 4:38 PM
 *
 */
case class MyBlog(
                   _id: ObjectId = new ObjectId,
                   blogId: ObjectId,
                   userId: ObjectId
                   ) extends BaseModel(_id)
