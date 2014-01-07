package model

import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * The Class User.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:29 PM
 *
 */
case class User(
                 _id: ObjectId = new ObjectId,
                 email: String,
                 password: String,
                 fullName: Option[String] = None,
                 role: String = NormalUser.value,
                 registerDate: DateTime = DateTime.now()
                 ) extends BaseModel(_id)
