package model

import org.bson.types.ObjectId

/**
 * The Class Category.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 12:23 PM
 *
 */
case class Category(
                     _id: ObjectId = new ObjectId(),
                     name: String,
                     shortName: String
                     ) extends BaseModel(_id)
