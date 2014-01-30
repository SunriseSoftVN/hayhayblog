package dao

import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import se.radley.plugin.salat._
import model.Blog
import play.api.Play.current

/**
 * The Class BlogDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 3:18 AM
 *
 */
object BlogDao extends BaseDao[Blog, ObjectId] {
  override def dao = new SalatDAO[Blog, ObjectId](collection = mongoCollection("blog")) {}
}
