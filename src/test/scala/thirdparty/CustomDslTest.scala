package thirdparty

import com.github.dwickern.macros.NameOfImpl
import org.scalatest.{FunSuite, Matchers}

object CustomDsl {
  import language.experimental.macros
  def ref(expr: Any): String = macro NameOfImpl.nameOf
}

class CustomDslTest extends FunSuite with Matchers {
  test("create alias from third-party code") {
    def someIdentifier = ???
    CustomDsl.ref(someIdentifier) should equal ("someIdentifier")
  }
}
