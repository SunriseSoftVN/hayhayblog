package controllers

import play.api.mvc.{SimpleResult, Controller}

/**
 * The Class ControllerHelper.
 *
 * @author Nguyen Duc Dung
 * @since 3/29/14 11:13 PM
 *
 */
trait ControllerHelper extends Controller {

  implicit class RichOption[E](op: Option[E]) {
    def mapRender(f: E => SimpleResult) = op.map(f).getOrElse(NotFound)
  }

}
