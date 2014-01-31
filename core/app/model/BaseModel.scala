package model


/**
 * The Class BaseModel.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:30 PM
 *
 */
abstract class BaseModel(_id: Any) {

  def id = _id.toString

}