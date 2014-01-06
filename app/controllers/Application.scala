package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{AuthElement, AsyncAuth}
import auth.AuthConfigImpl
import model.NormalUser
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

object Application extends Controller with AuthElement with AuthConfigImpl {

  def index = StackAction(AuthorityKey -> NormalUser) {
    implicit request =>
      Ok(views.html.index())
  }

  def test = Action.async {
    Future {
      Thread.sleep(2000)
      Ok(views.html.index())
    }
  }

}