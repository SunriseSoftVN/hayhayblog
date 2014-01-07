package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{LoginLogout, AuthElement}
import auth.AuthConfigImpl
import play.api.data.Form
import play.api.data.Forms._
import dao.UserDao
import scala.concurrent.ExecutionContext
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

  def login = TODO

  def auth = TODO

  def logout = Action.async(implicit request => {
    gotoLogoutSucceeded
  })

}
