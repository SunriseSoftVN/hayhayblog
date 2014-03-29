package model

import org.bson.types.ObjectId
import org.joda.time.DateTime

/**
 * The Class Image.
 *
 * @author Nguyen Duc Dung
 * @since 3/29/14 10:46 PM
 *
 */
case class Image(
                  _id: ObjectId = new ObjectId(),
                  url: String,
                  content: Array[Byte] = Array.empty,
                  lastUpdated: DateTime = DateTime.now()
                  ) extends BaseModel(_id)
