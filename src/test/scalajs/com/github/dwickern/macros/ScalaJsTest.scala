package com.github.dwickern.macros

import com.github.dwickern.macros.NameOf._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Fields {
  def widgetCount: Int = ???
}

/**
  * Arguments to Scala.js export annotations must be compile-time constants.
  *
  * This is the Scala.js counterpart to `AnnotationTest`: Scala.js has no runtime
  * annotations to read back, so the assertion is that this export compiles at all,
  * which it does not if the macro widens `"widgetCount"` to `String`.
  */
@JSExportTopLevel(nameOf(Fields.widgetCount))
object ExportedWidgetCount

class ScalaJsTest extends AnyFunSuite with Matchers {
  test("nameOf") {
    nameOf(Fields.widgetCount) should equal ("widgetCount")
    qualifiedNameOfType[Fields.type] should equal ("com.github.dwickern.macros.Fields")
  }

  test("javascript types") {
    nameOfType[js.Date] should equal ("Date")
    qualifiedNameOfType[js.Date] should equal ("scala.scalajs.js.Date")
    nameOfType[js.Dynamic] should equal ("Dynamic")
  }

  test("javascript facade members") {
    nameOf[js.Date](_.getTime()) should equal ("getTime")
    qualifiedNameOf[js.Date](_.getTime().toInt) should equal ("getTime.toInt")
  }
}
