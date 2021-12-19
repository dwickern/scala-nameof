package thirdparty

import com.github.dwickern.macros.NameOfImpl
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

object CustomDsl {
  inline def ref(inline expr: Any): String = ${NameOfImpl.nameOf('expr)}
}

class CustomDslTest extends AnyFunSuite with Matchers {
  test("create alias from third-party code") {
    def someIdentifier = ???
    CustomDsl.ref(someIdentifier) should equal ("someIdentifier")
  }

  test("compile-time constant") {
    CustomDsl.ref(Byte.MaxValue) should equal ("MaxValue")

    import CustomDsl._
    ref(Byte.MaxValue) should equal ("MaxValue")
  }
}
