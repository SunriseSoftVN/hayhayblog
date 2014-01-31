package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl

/**
 * The Class AboutCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 10:00 PM
 *
 */
object AboutCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def index = StackAction(implicit request => renderOk(views.html.about()))

}
