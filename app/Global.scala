import actor.{Start, FeedSpout}
import akka.actor.Props
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.typesafe.config.ConfigFactory
import dao.UserDao
import filter.HTMLCompressorFilter
import java.io.File
import model.{Administrator, User}
import play.api._
import play.api.libs.concurrent.Akka
import play.api.mvc.WithFilters
import scala.concurrent.duration._
import play.api.Play.current
import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global

/**
 * The Class Global.
 *
 * @author Nguyen Duc Dung
 * @since 1/6/14 9:51 AM
 *
 */
object Global extends WithFilters(HTMLCompressorFilter()) {

  val devConfFilePath = "conf/dev.conf"
  val prodConfFilePath = "prod.conf"
  lazy val feedSpout = Akka.system.actorOf(Props(new FeedSpout(nrOfActor = 10)), name = "feedSpout")

  override def onStart(app: Application) {
    Logger.info("Starting...")
    RegisterJodaTimeConversionHelpers()
    if (UserDao.findByEmail("dungvn3000@gmail.com").isEmpty) {
      UserDao.save(User(
        email = "dungvn3000@gmail.com",
        password = "a17b76ab0f69faf9ba22449b6a5abb88",
        role = Administrator.value
      ))
    }
    //crawling blog every 15 minutes
    if(Play.isProd) {
      Akka.system.scheduler.schedule(10.seconds, 15.minutes, feedSpout, Start)
    }
  }

  override def onLoadConfig(config: Configuration, path: File, classLoader: ClassLoader, mode: Mode.Mode) = if (mode == Mode.Prod) {
    val prodConfig = ConfigFactory.parseResources(classLoader, prodConfFilePath)
    config ++ Configuration(prodConfig)
  } else {
    val devConfig = ConfigFactory.parseFileAnySyntax(new File(path, devConfFilePath))
    config ++ Configuration(devConfig)
  }

  override def onStop(app: Application) = {
    Akka.system.shutdown()
    Akka.system.awaitTermination()
    Logger.info("Shutdown...")
  }
}
