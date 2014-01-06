package model

import org.bson.types.ObjectId

/**
 * The Class BaseModel.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:30 PM
 *
 */
abstract class BaseModel(_id: ObjectId) {

  def id = _id.toString

}