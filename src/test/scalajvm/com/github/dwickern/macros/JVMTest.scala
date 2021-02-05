package com.github.dwickern.macros

import com.github.dwickern.macros.NameOf._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import javax.annotation.Resource

class JVMTest extends AnyFunSuite with Matchers {
  test("use in Java runtime annotations") {
    /**
      * Arguments to Java runtime annotations must be compile-time constants.
      *
      * Blackbox macros widen the return time from `"name"` to `("name": String)`,
      * so in order for this code to compile we must use whitebox macros.
      */
    @Resource(name = nameOfType[AnnotatedClass], description = qualifiedNameOfType[AnnotatedClass])
    class AnnotatedClass

    val annotation = classOf[AnnotatedClass].getDeclaredAnnotation(classOf[Resource])
    annotation.name should equal ("AnnotatedClass")
    annotation.description should equal ("com.github.dwickern.macros.JVMTest.AnnotatedClass")
  }
}
