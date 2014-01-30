package dao

import com.novus.salat.dao.{ModelCompanion, SalatDAO}
import scala._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import dto.PageDto

/**
 * The Class BaseDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 10:25 PM
 *
 */
trait BaseDao[ObjectType <: AnyRef, ID <: Any] extends ModelCompanion[ObjectType, ID] {

  def query(pageDto: PageDto[_ <: ObjectType]) = {
    val skip = (pageDto.currentPage - 1) * pageDto.itemDisplay

    val query = if (pageDto.fieldName.isDefined && pageDto.filter.isDefined) {
      MongoDBObject(pageDto.fieldName.get -> MongoDBObject("$regex" -> pageDto.filter.get, "$options" -> "i"))
    } else MongoDBObject.empty

    val sortBy = pageDto.orderedField.map(field => MongoDBObject(field -> pageDto.sortAsc)).getOrElse(MongoDBObject.empty)

    val totalRow = count(query)

    var totalPage = totalRow / pageDto.itemDisplay
    if (totalRow - totalPage * pageDto.itemDisplay > 0) {
      totalPage += 1
    }

    val items = find(query)
      .skip(skip)
      .limit(pageDto.itemDisplay)
      .sort(sortBy)
      .toList

    pageDto.copy(items = items, totalPage = totalPage.toInt)
  }

  def findList(ids: List[ID]) = find(MongoDBObject("_id" -> MongoDBObject("$in" -> ids))).toList
}
