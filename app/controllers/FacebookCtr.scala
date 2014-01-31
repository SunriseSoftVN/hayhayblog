package controllers

import play.api.mvc._
import play.api.Play
import play.api.Play.current
import play.api.libs.ws.WS
import scala.util.matching.Regex
import scala.concurrent.{Promise, Future, ExecutionContext}
import ExecutionContext.Implicits.global
import com.restfb.DefaultFacebookClient
import org.apache.commons.lang3.{RandomStringUtils, StringUtils}
import model.User
import auth.AuthConfigImpl
import jp.t2v.lab.play2.auth.LoginLogout
import jp.t2v.lab.play2.stackc.{StackableController, RequestWithAttributes}
import dao.UserDao

/**
 * The Class FacebookCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/12/14 6:23 PM
 *
 */
object FacebookCtr extends Controller with StackableController with LoginLogout with AuthConfigImpl {

  lazy val app_id = "710328198998370"
  lazy val app_secret = "85066e96643fab34bb32b28f8d8dd249"
  lazy val redirect_url = Play.configuration.getString("application.facebook.redirect.url").getOrElse(throw new Exception("Please add application.facebook.redirect.url to application.conf"))
  lazy val profile_picture_url = "http://graph.facebook.com/${username}/picture?type=large"
  lazy val regex = new Regex("access_token=(.*)&expires=(.*)")

  def login = Action(implicit request => {
    val url = s"https://www.facebook.com/dialog/oauth?client_id=$app_id&redirect_uri=$redirect_url&scope=email"
    Redirect(url)
  })

  def auth = AsyncStack(implicit request => {
    request.getQueryString("code").map(code => {
      val accessTokenUrl = s"https://graph.facebook.com/oauth/access_token?client_id=$app_id&client_secret=$app_secret&code=$code&redirect_uri=$redirect_url"
      WS.url(accessTokenUrl).get().flatMap(response => response.body match {
        case regex(accessToken, expires) => authorize(accessToken)
        case _ => Future.successful(BadRequest)
      })
    }).getOrElse(Future.successful(BadRequest))
  })

  private def authorize(accessToken: String)(implicit request: RequestWithAttributes[_]): Future[SimpleResult] = {
    val promise = Promise[SimpleResult]()
    Future {
      val facebookClient = new DefaultFacebookClient(accessToken)
      val fbUser = facebookClient.fetchObject("me", classOf[com.restfb.types.User])
      //Make sure email is not null
      if (fbUser != null && StringUtils.isNotBlank(fbUser.getEmail)) {
        val user = UserDao.findByEmail(fbUser.getEmail).getOrElse {
          val newPassword = RandomStringUtils.randomAlphanumeric(6)
          val fullname = if (StringUtils.isNotBlank(fbUser.getName)) Some(fbUser.getName) else None
          val newUser = User(
            email = fbUser.getEmail,
            fullname = fullname,
            password = newPassword
          )
          UserDao.insert(newUser)
          newUser
        }
        promise.completeWith(gotoLoginSucceeded(user.email))
      } else {
        promise.success(Redirect("/user/register"))
      }
    }
    promise.future
  }

}
