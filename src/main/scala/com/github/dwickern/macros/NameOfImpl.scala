package com.github.dwickern.macros

import scala.language.experimental.macros
import scala.reflect.macros.blackbox
import scala.annotation.tailrec

object NameOfImpl {
  def nameOf(c: blackbox.Context)(expr: c.Expr[Any]): c.Expr[String] = {
    import c.universe._

    @tailrec def extract(tree: c.Tree): c.Name = tree match {
      case Ident(n) => n
      case Select(_, n) => n
      case Function(_, body) => extract(body)
      case Block(_, expr) => extract(expr)
      case Apply(func, _) => extract(func)
      case TypeApply(func, _) => extract(func)
      case _ => c.abort(c.enclosingPosition, s"Unsupported expression: $expr")
    }

    val name = extract(expr.tree).decodedName.toString
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }

  def qualifiedNameOf(c: blackbox.Context)(expr: c.Expr[Any]): c.Expr[String] = {
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

  def nameOfType[T](c: blackbox.Context)(implicit tag: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val name = showRaw(tag.tpe.typeSymbol.name)
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }

  def qualifiedNameOfType[T](c: blackbox.Context)(implicit tag: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val name = showRaw(tag.tpe.typeSymbol.fullName)
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }
}
