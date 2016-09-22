package com.github.dwickern.macros

import scala.annotation.tailrec
import scala.reflect.macros._

private[macros] object NameOfImpl {
  def nameOfImpl(c: Context)(x: c.Expr[Any]): c.Expr[String] = {
    import c.universe._

    @tailrec def extract(tree: c.Tree): c.Name = tree match {
      case Ident(n) => n
      case Select(_, n) => n
      case Function(_, body) => extract(body)
      case Block(_, expr) => extract(expr)
      case Apply(func, _) => extract(func)
      case TypeApply(func, _) => extract(func)
      case _ => c.abort(c.enclosingPosition, s"Unsupported expression: $x")
    }

    val name = extract(x.tree).decoded
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }

  def nameOfMemberImpl(c: Context)(f: c.Expr[Any]): c.Expr[String] = nameOfImpl(c)(f)
  def nameOfTypeImpl[T](c: Context)(implicit tag: c.WeakTypeTag[T]): c.Expr[String] = {
    import c.universe._
    val name = showRaw(tag.tpe.typeSymbol.name)
    reify {
      c.Expr[String] { Literal(Constant(name)) }.splice
    }
  }
}
