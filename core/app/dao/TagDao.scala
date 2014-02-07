package dao

import com.novus.salat.dao.SalatDAO
import model.Tag
import org.bson.types.ObjectId
import se.radley.plugin.salat._
import play.api.Play.current

/**
 * The Class UserDao.
 *
 * @author Nguyen Duc Dung
 * @since 10/5/13 6:35 PM
 *
 */
object TagDao extends BaseDao[Tag, ObjectId] {

  def dao = new SalatDAO[Tag, ObjectId](collection = mongoCollection("tag")) {}

}