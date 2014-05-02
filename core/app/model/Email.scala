package model

import org.bson.types.ObjectId

/**
 * The Class Email.
 *
 * @author Nguyen Duc Dung
 * @since 5/2/14 9:08 AM
 *
 */
case class Email(
                  _id: ObjectId = new ObjectId,
                  email: String
                  ) extends BaseModel(_id)
