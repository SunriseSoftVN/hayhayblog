package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.Administrator

/**
 * The Class Users.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 2:04 PM
 *
 */
object Users extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.users.list())
  })

}
