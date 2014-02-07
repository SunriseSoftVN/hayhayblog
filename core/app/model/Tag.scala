package model

import org.bson.types.ObjectId

/**
 * The Class Tags.
 *
 * @author Nguyen Duc Dung
 * @since 2/7/14 5:28 PM
 *
 */
case class Tag(
                _id: ObjectId = new ObjectId(),
                name: String,
                count: Long = 0,
                read: Long = 0
                )
