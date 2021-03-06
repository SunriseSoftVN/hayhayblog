package formater

import org.joda.time.DateTime
import org.ocpsoft.prettytime.PrettyTime
import java.util.Locale


/**
 * The Class DateTimeFormat.
 *
 * @author Nguyen Duc Dung
 * @since 1/23/14 10:44 AM
 *
 */
object DateTimeFormat {

  val prettyTime = new PrettyTime(new Locale("vi"))

  def fmt = org.joda.time.format.DateTimeFormat.forPattern("dd/MM/yyyy")

  def fmt2 = org.joda.time.format.DateTimeFormat.forPattern("dd-MM-yyyy")

  def prettyDate(datetime: DateTime) = prettyTime.format(datetime.toDate)

}
