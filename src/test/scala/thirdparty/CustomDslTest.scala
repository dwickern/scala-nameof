package thirdparty

import com.github.dwickern.macros.NameOfImpl
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object CustomDsl {
  import language.experimental.macros
  def ref(expr: Any): String = macro NameOfImpl.nameOf
}

class CustomDslTest extends AnyFunSuite with Matchers {
  test("create alias from third-party code") {
    def someIdentifier = ???
    CustomDsl.ref(someIdentifier) should equal ("someIdentifier")
  }
}
