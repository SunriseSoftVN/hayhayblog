package filter

import play.api.Play
import play.api.Play.current
import com.googlecode.htmlcompressor.compressor.HtmlCompressor
import com.mohiva.play.htmlcompressor.HTMLCompressorFilter

/**
 * Defines a user-defined HTML compressor filter.
 *
 * @author Nguyen Duc Dung
 * @since 1/6/14 9:06 PM
 *
 */
object HTMLCompressorFilter {

  /**
   * Creates the HTML compressor filter.
   *
   * @return The HTML compressor filter.
   */
  def apply() = new HTMLCompressorFilter({
    val compressor = new HtmlCompressor()
    if (Play.isDev) {
      compressor.setPreserveLineBreaks(true)
    }

    compressor.setRemoveComments(true)
    compressor.setRemoveIntertagSpaces(true)
    compressor.setRemoveHttpProtocol(true)
    compressor.setRemoveHttpsProtocol(true)
    compressor
  })
}