package admin.controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.AuthElement
import auth.AuthConfigImpl
import model.Administrator
import play.api.Play

/**
 * The Class Home.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 12:34 PM
 *
 */
object HomeCtr extends Controller with AuthElement with AuthConfigImpl with AdminTemplate {

  def index = StackAction(AuthorityKey -> Administrator)(implicit request => {
    renderOk(admin.views.html.index())
  })

  def shutdown = StackAction(AuthorityKey -> Administrator)(implicit request => {
    Play.stop()
    System.exit(0)
    Ok
  })

}
