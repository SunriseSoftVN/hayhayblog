package controllers

import play.api.mvc.Controller
import jp.t2v.lab.play2.auth.OptionalAuthElement
import auth.AuthConfigImpl

/**
 * The Class StaticCtr.
 *
 * @author Nguyen Duc Dung
 * @since 3/30/14 9:02 AM
 *
 */
object StaticCtr extends Controller with OptionalAuthElement with AuthConfigImpl with MainTemplate {

  def about = StackAction { implicit request =>
    renderOk(views.html.statics.about())
  }

  def lienket = StackAction { implicit request =>
    renderOk(views.html.statics.lienket())
  }

}
