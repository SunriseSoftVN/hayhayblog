package controllers

import play.api.mvc._
import jp.t2v.lab.play2.stackc.StackableController
import jp.t2v.lab.play2.auth.LoginLogout
import auth.AuthConfigImpl
import play.api.Play
import play.api.Play.current
import com.google.api.client.googleapis.auth.oauth2.{GoogleAuthorizationCodeTokenRequest, GoogleBrowserClientRequestUrl, GoogleTokenResponse, GoogleCredential}
import scala.collection.JavaConverters._
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import scala.concurrent.{Promise, ExecutionContext, Future}
import model.User
import org.apache.commons.lang3.{RandomStringUtils, StringUtils}
import com.google.api.services.oauth2.Oauth2
import ExecutionContext.Implicits.global
import dao.UserDao

/**
 * The Class GoogleCtr.
 *
 * @author Nguyen Duc Dung
 * @since 1/12/14 9:37 PM
 *
 */
object GoogleCtr extends Controller with StackableController with LoginLogout with AuthConfigImpl {

  lazy val app_id = "649323438474.apps.googleusercontent.com"
  lazy val app_secret = "AIzaSyDuhd8bMDFQB-jjBRlrHjnTY8zxQxuAgfM"
  val redirect_url = Play.configuration.getString("application.google.redirect.url").getOrElse(throw new Exception("Please add application.google.redirect.url to application.conf"))

  def login = Action {
    val url = new GoogleBrowserClientRequestUrl(app_id, redirect_url,
      List(
        "https://www.googleapis.com/auth/userinfo.email",
        "https://www.googleapis.com/auth/userinfo.profile"
      ).asJava
    ).setState("/profile").setResponseTypes(List("code").asJava).build()
    Redirect(url)
  }

  def auth = AsyncStack(implicit request => request.getQueryString("code").map(code => {
    val promise = Promise[SimpleResult]()
    Future {
      val transport = new NetHttpTransport()
      val jsonFactory = new JacksonFactory()

      val tokenResponse = new GoogleAuthorizationCodeTokenRequest(transport, jsonFactory, app_id, app_secret, code, redirect_url).execute()
      val token = tokenResponse.toString

      val credential = new GoogleCredential.Builder()
        .setJsonFactory(jsonFactory)
        .setTransport(transport)
        .setClientSecrets(app_id, app_secret).build()
        .setFromTokenResponse(jsonFactory.fromString(token, classOf[GoogleTokenResponse]))

      val userInfoService = new Oauth2.Builder(transport, jsonFactory, credential).build()
      val userInfo = userInfoService.userinfo.get.execute()

      if (userInfo != null && StringUtils.isNotBlank(userInfo.getEmail)) {
        val user = UserDao.findByEmail(userInfo.getEmail).getOrElse {
          val newPassword = RandomStringUtils.randomAlphanumeric(6)
          val fullname = if (StringUtils.isNotBlank(userInfo.getName)) Some(userInfo.getName) else None
          val language = if (StringUtils.isNotBlank(userInfo.getLocale)) userInfo.getLocale else "en"
          val avatarUrl = if (StringUtils.isNotBlank(userInfo.getPicture)) Some(userInfo.getPicture) else None
          val newUser = User(
            email = userInfo.getEmail,
            fullname = fullname,
            password = newPassword
          )
          UserDao.insert(newUser)
          newUser
        }
        promise.completeWith(gotoLoginSucceeded(user.email))
      } else {
        promise.success(BadRequest)
      }
    }
    promise.future
  }).getOrElse(Future.successful(BadRequest)))
}
