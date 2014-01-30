package utils

import play.api.Play
import org.apache.commons.mail.{HtmlEmail, DefaultAuthenticator}
import play.api.i18n.Messages
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * The Class Emailer.
 *
 * @author Nguyen Duc Dung
 * @since 9/6/13, 7:36 AM
 *
 */
object Emailer {

  lazy val emailUsername = Play.current.configuration.getString("email.username").get
  lazy val emailPassword = Play.current.configuration.getString("email.password").get
  lazy val emailSmtp = Play.current.configuration.getString("email.smtp").get
  lazy val emailPort = Play.current.configuration.getInt("email.port").get

  def sendAsync(contentHtml: String, title: String, toEmail: String) = Future {
    val sender = new HtmlEmail()
    sender.setHostName(emailSmtp)
    sender.setSmtpPort(emailPort)
    sender.setAuthenticator(new DefaultAuthenticator(emailUsername, emailPassword))
    sender.setSSLOnConnect(true)
    sender.setFrom(emailUsername, Messages("application.name"))
    sender.addTo(toEmail)
    sender.setSubject(title)
    sender.setHtmlMsg(contentHtml)
    sender.setCharset("UTF8")
    sender.send()
  }


}
