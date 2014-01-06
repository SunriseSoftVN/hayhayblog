import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.typesafe.config.ConfigFactory
import dao.UserDao
import java.io.File
import model.{Administrator, User}
import org.apache.commons.codec.digest.DigestUtils
import play.api._
import play.libs.Akka

/**
 * The Class Global.
 *
 * @author Nguyen Duc Dung
 * @since 1/6/14 9:51 AM
 *
 */
object Global extends GlobalSettings {

  lazy val devConfFilePath = "conf/dev.conf"
  lazy val prodConfFilePath = "prod.conf"


  override def onStart(app: Application) {
    Logger.info("Starting...")
    RegisterJodaTimeConversionHelpers()
    if (UserDao.findByEmail("dungvn3000@gmail.com").isEmpty) {
      UserDao.save(User(
        email = "dungvn3000@gmail.com",
        password = DigestUtils.md5Hex("Nevergiveup2512"),
        role = Administrator.value
      ))
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
    Logger.info("Shutdown...")
  }
}
