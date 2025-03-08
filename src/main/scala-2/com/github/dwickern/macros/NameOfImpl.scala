package com.github.dwickern.macros

import scala.language.experimental.macros
import scala.reflect.macros.whitebox
import scala.annotation.tailrec

object NameOfImpl {
  def nameOf(c: whitebox.Context)(expr: c.Expr[Any]): c.Expr[String] = {
    import c.universe._

    @tailrec def extract(tree: c.Tree): String = tree match {
      case Ident(n) => n.decodedName.toString
      case Select(_, n) => n.decodedName.toString
      case Function(_, body) => extract(body)
      case Block(_, expr) => extract(expr)
      case Apply(func, _) => extract(func)
      case TypeApply(func, _) => extract(func)
      case _ =>
        c.abort(c.enclosingPosition, s"Unsupported expression: ${expr.tree}")
    }

    /**
      * Compile-time constants have already been replaced before this macro runs.
      * For example, when calling `nameOf(Byte.MaxValue)`, the macro will see `nameOf(127)`.
      * We use the compiler APIs to solve this problem.
      */
    def nameOfConstant(): String = {
      val cc = c.asInstanceOf[reflect.macros.runtime.Context]
      import cc.universe._
      val macroName = cc.macroApplication.symbol.asTerm.name

      @tailrec def extractConstant(tree: cc.Tree): cc.Name = tree match {
        case Apply(RefTree(_, `macroName`), List(RefTree(_, name))) => name
        case Apply(func, _) => extractConstant(func)
        case Select(qualifier, _) => extractConstant(qualifier)
        case _ =>
          c.abort(c.enclosingPosition, s"Unsupported constant expression: ${expr.tree}")
      }

      extractConstant(cc.callsiteTyper.context.tree).decodedName.toString
    }

    val name = expr.tree match {
      case Literal(Constant(_)) => nameOfConstant()
      case _ => extract(expr.tree)
    }
    c.Expr[String](q"$name")
  }

  def qualifiedNameOf(c: whitebox.Context)(expr: c.Expr[Any]): c.Expr[String] = {
    import c.universe._

    def extractNames(tree: c.Tree): List[c.Name] = {
      tree.children.headOption match {
        case Some(child) =>
          extractNames(child) :+ tree.symbol.name
        case None =>
          List(tree.symbol.name)
      }
    }

    @tailrec def extract(tree: c.Tree): List[c.Name] = tree match {
      case Ident(n) => List(n)
      case Select(tree, n) => extractNames(tree) :+ n
      case Function(_, body) => extract(body)
      case Block(_, expr) => extract(expr)
      case Apply(func, _) => extract(func)
      case TypeApply(func, _) => extract(func)
      case _ => c.abort(c.enclosingPosition, s"Unsupported expression: $expr")
    }

    val name = extract(expr.tree)
      // drop sth like x$1
      .drop(1)
      .mkString(".")
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }

  def nameOfType[T](c: whitebox.Context)(implicit tag: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val name = showRaw(tag.tpe.typeSymbol.name)
    c.Expr[String](q"$name")
  }

  def qualifiedNameOfType[T](c: whitebox.Context)(implicit tag: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val name = showRaw(tag.tpe.typeSymbol.fullName)
    c.Expr[String](q"$name")
  }
}
