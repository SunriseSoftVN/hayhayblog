package dao

import com.novus.salat.dao.ModelCompanion
import scala._
import com.mongodb.casbah.commons.MongoDBObject
import dto.PageDto
import org.apache.commons.lang3.StringUtils
import org.joda.time.DateTime
import model.BlogStatus

/**
 * The Class BaseDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/5/13 10:25 PM
 *
 */
trait BaseDao[ObjectType <: AnyRef, ID <: Any] extends ModelCompanion[ObjectType, ID] {

  def all = findAll().toList

  /**
   * Blog need to update is a feed is not updating and has last updated is 30m ago.
   * @return
   */
  def needToUpdate = find(
    MongoDBObject(
      "status" -> MongoDBObject("$ne" -> BlogStatus.UPDATING),
      "indexUpdated" -> MongoDBObject("$lt" -> DateTime.now.minusMinutes(30))
    )
  ).toList

  def query(pageDto: PageDto[_ <: ObjectType]) = {
    val skip = (pageDto.currentPage - 1) * pageDto.itemDisplay

    val query = if (pageDto.fieldName.isDefined && pageDto.filter.isDefined) {
      MongoDBObject(pageDto.fieldName.get -> MongoDBObject("$regex" -> pageDto.filter.get, "$options" -> "i"))
    } else MongoDBObject.empty

    val sortBy = if (pageDto.orderedField.isDefined && StringUtils.isNotBlank(pageDto.orderedField.get)) {
      MongoDBObject(pageDto.orderedField.get -> pageDto.sortAsc)
    } else MongoDBObject.empty

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
