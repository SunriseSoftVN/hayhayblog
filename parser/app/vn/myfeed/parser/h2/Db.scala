package vn.myfeed.parser.h2

import scala.slick.driver.H2Driver.simple._
/**
 * The Class Db.
 *
 * @author Nguyen Duc Dung
 * @since 7/1/13 7:15 AM
 *
 */
object Db {

  def database = Database.forURL("jdbc:h2:parser/test/resources/article", driver = "org.h2.Driver")

}
