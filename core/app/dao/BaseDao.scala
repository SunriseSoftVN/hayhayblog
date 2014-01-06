package dao

import com.novus.salat.dao.{ModelCompanion, SalatDAO}
import scala._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._

/**
 * The Class BaseDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 10:25 PM
 *
 */
trait BaseDao[ObjectType <: AnyRef, ID <: Any] extends ModelCompanion[ObjectType, ID] {

  def query(fieldName: String, fieldValue: Any, sortFiled: String, sortAsc: Int = 1, page: Int = 1, itemDisplay: Int = 10) = {
    val skip = (page - 1) * itemDisplay
    find(MongoDBObject(fieldName -> MongoDBObject("$regex" -> fieldValue, "$options" -> "i")))
      .skip(skip)
      .limit(itemDisplay)
      .sort(MongoDBObject(sortFiled -> sortAsc))
      .toList
  }

  def totalPage(fieldName: String, fieldValue: Any, itemDisplay: Int = 10): Long = {
    val totalRow = count(MongoDBObject(fieldName -> MongoDBObject("$regex" -> fieldValue, "$options" -> "i")))
    var page = totalRow / itemDisplay
    if (totalRow - page * itemDisplay > 0) {
      page += 1
    }
    page
  }

  def findList(ids: List[ID]) = find(MongoDBObject("_id" -> MongoDBObject("$in" -> ids))).toList
}
