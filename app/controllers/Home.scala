package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{AuthElement, AsyncAuth}
import auth.AuthConfigImpl
import model.NormalUser
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

object Home extends Controller with AuthElement with AuthConfigImpl {

  def index = StackAction(AuthorityKey -> NormalUser) {
    implicit request =>
      Ok(views.html.index())
  }

}