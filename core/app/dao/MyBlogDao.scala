package dao

import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import se.radley.plugin.salat._
import model.MyBlog
import play.api.Play.current

/**
 * The Class BlogDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 3:18 AM
 *
 */
object MyBlogDao extends BaseDao[MyBlog, ObjectId] {
  override def dao = new SalatDAO[MyBlog, ObjectId](collection = mongoCollection("my_blog")) {}
}