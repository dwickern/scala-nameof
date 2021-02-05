package com.github.dwickern.macros

import com.github.dwickern.macros.NameOf._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import javax.annotation.Resource

/**
  * Arguments to Java runtime annotations must be compile-time constants.
  *
  * Blackbox macros widen the return time from `"name"` to `("name": String)`,
  * so in order for this code to compile we must use whitebox macros.
  */
class AnnotationTest extends AnyFunSuite with Matchers {
  test("nameOf") {
    def test = ???

    @Resource(name = nameOf(test))
    class AnnotatedClass

    val annotation = classOf[AnnotatedClass].getDeclaredAnnotation(classOf[Resource])
    annotation.name should === ("test")
  }

  test("nameOf instance") {
    class SomeClass(val foobar: String)

    @Resource(name = nameOf[AnnotatedClass](_.classMember))
    class AnnotatedClass {
      def classMember = ???
    }

    val annotation = classOf[AnnotatedClass].getDeclaredAnnotation(classOf[Resource])
    annotation.name should === ("classMember")
  }

  test("nameOfType") {
    @Resource(name = nameOfType[AnnotatedClass])
    class AnnotatedClass

    val annotation = classOf[AnnotatedClass].getDeclaredAnnotation(classOf[Resource])
    annotation.name should === ("AnnotatedClass")
  }

  test("qualifiedNameOfType") {
    @Resource(name = qualifiedNameOfType[AnnotatedClass])
    class AnnotatedClass

    val annotation = classOf[AnnotatedClass].getDeclaredAnnotation(classOf[Resource])
    annotation.name should === ("com.github.dwickern.macros.AnnotationTest.AnnotatedClass")
  }
}
