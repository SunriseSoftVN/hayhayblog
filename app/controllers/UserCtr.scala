package controllers

import play.api.mvc._
import jp.t2v.lab.play2.auth.{OptionalAuthElement, LoginLogout}
import auth.AuthConfigImpl
import play.api.data.Form
import play.api.data.Forms._
import dao.UserDao
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import model.User
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.lang3.StringUtils

/**
 * The Class UserCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/7/14 2:39 PM
 *
 */
object UserCtr extends Controller with OptionalAuthElement with AuthConfigImpl with LoginLogout with MainTemplate {

  val loginForm = Form {
    tuple(
      "email" -> email,
      "password" -> nonEmptyText
    ) verifying("login.failed", fields => fields match {
      case (emailAddress, password) => UserDao.login(emailAddress, password)
    })
  }

  val registerForm = Form {
    tuple(
      "email" -> email,
      "fullname" -> optional(text(minLength = 6)),
      "password" -> text(minLength = 6),
      "rePassword" -> text(minLength = 6)
    ) verifying("password.error.match", fields => fields match {
      case (emailAddress, fullname, password, rePassword) => password == rePassword
    })
  }

  val profileForm = Form {
    tuple(
      "email" -> email,
      "fullname" -> optional(text(minLength = 6)),
      "password" -> optional(text(minLength = 6)),
      "rePassword" -> optional(text(minLength = 6))
    )
  }

  val recoverForm = Form {
    "email" -> email
  }

  def recover = Action {
    Ok(views.html.user.recoverpassword(recoverForm))
  }

  def recoverPost = TODO

  def register = StackAction(implicit request => {
    renderOk(views.html.user.register(registerForm))
  })

  def registerPost = AsyncStack(implicit request => {
    registerForm.bindFromRequest.fold(
      error => Future.successful(renderBadRequest(views.html.user.register(error))),
      tuple => {
        val (email, fullname, password, rePassword) = tuple
        val user = User(
          email = email,
          fullname = fullname,
          password = DigestUtils.md5Hex(password)
        )
        UserDao.insert(user)
        gotoLoginSucceeded(user.email)
      }

    )
  })

  def login = StackAction(implicit request => {
    renderOk(views.html.user.login(loginForm))
  })

  def auth = AsyncStack(implicit request => {
    loginForm.bindFromRequest.fold(
      error => Future.successful(renderBadRequest(views.html.user.login(error))),
      tuple => gotoLoginSucceeded(tuple._1)
    )
  })

  def logout = AsyncStack(implicit request => {
    gotoLogoutSucceeded
  })

  def profile = StackAction(implicit request => {
    loggedIn.map(user => renderOk(
      views.html.user.profile(profileForm.fill(user.email, user.fullname, None, None)))
    ).getOrElse(NotFound)
  })

  def profilePost = StackAction(implicit request => {
    val bindedForm = profileForm.bindFromRequest
    bindedForm.fold(
      error => renderBadRequest(views.html.user.profile(error)),
      tuple => {
        val (email, fullname, password, rePassword) = tuple
        if (
          password.isDefined
            && rePassword.isDefined
            && StringUtils.isNotBlank(password.get)
            && StringUtils.isNotBlank(rePassword.get)
            && password != rePassword) {
          renderBadRequest(views.html.user.profile(bindedForm.withGlobalError("password.error.match")))
        } else {
          if (password.isDefined && rePassword.isDefined &&
            StringUtils.isNotBlank(password.get) && password.get == rePassword.get) {
            loggedIn.map(user => {
              UserDao.save(
                user.copy(
                  fullname = fullname,
                  password = DigestUtils.md5Hex(password.get)
                )
              )
            })
          } else {
            loggedIn.map(user => {
              UserDao.save(user.copy(fullname = fullname))
            })
          }
          Redirect("/")
        }
      }
    )
  })
}
