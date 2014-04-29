package utils

/**
 * The Class StringUtils.
 *
 * @author Nguyen Duc Dung
 * @since 4/15/14 4:44 PM
 *
 */
object StringUtils {
  /**
   * Convert vietnamese unicode string to ascii string.
   *
   * @param st vietnamese unicode string.
   * @return ascii string.
   */
  def convertNonAscii(st: String): String = {
    val _st = st.replaceAll("[đ]", "d").replaceAll("[Đ]", "D")
    org.apache.commons.lang3.StringUtils.stripAccents(_st)
  }

}
