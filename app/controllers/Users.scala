package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{LoginLogout, AuthElement}
import auth.AuthConfigImpl
import play.api.data.Form
import play.api.data.Forms._
import dao.UserDao
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

/**
 * The Class Users.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 2:39 PM
 *
 */
object Users extends Controller with AuthElement with AuthConfigImpl with LoginLogout {

  lazy val loginForm = Form {
    tuple(
      "email" -> email,
      "password" -> nonEmptyText
    ) verifying("login.failed", fields => fields match {
      case (emailAddress, password) => UserDao.login(emailAddress, password)
    })
  }

  lazy val registerForm = Form {
    tuple(
      "email" -> email,
      "fullname" -> optional(text),
      "password" -> text(minLength = 6),
      "rePassword" -> text(minLength = 6)
    ) verifying("password.error.match", fields => fields match {
      case (emailAddress, fullname, password, rePassword) => password == rePassword
    })
  }

  lazy val recoverForm = Form {
    "email" -> email
  }

  def recover = Action {
    Ok(views.html.users.recoverpassword(recoverForm))
  }

  def recoverPost = TODO

  def register = Action {
    Ok(views.html.users.register(registerForm))
  }

  def registerPost = TODO

  def login = Action {
    Ok(views.html.users.login(loginForm))
  }

  def auth = Action.async(implicit request => {
    loginForm.bindFromRequest.fold(
      error => Future.successful(BadRequest(views.html.users.login(error))),
      form => {
        gotoLoginSucceeded(form._1)
      }
    )
  })

  def logout = Action.async(implicit request => {
    gotoLogoutSucceeded
  })

}
