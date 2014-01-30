package dao

import org.bson.types.ObjectId
import com.novus.salat.dao.SalatDAO
import se.radley.plugin.salat._
import model.Article
import play.api.Play.current

/**
 * The Class ArticleDao.
 *
 * @author Nguyen Duc Dung
 * @since 1/31/14 4:03 AM
 *
 */
object ArticleDao extends BaseDao[Article, ObjectId] {
  override def dao = new SalatDAO[Article, ObjectId](collection = mongoCollection("article")) {}
}
