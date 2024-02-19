package io.github.edadma.jixie

import scala.annotation.tailrec
import scala.collection.immutable.Seq

abstract class Type
case object STRING extends Type
case object NUMBER extends Type
case object ANY extends Type

abstract class Define { val name: String }
case class Builtin(name: String, func: (Scope, IndexedSeq[Any]) => Any) extends Define
case class Variable(name: String, var value: Any) extends Define

class Interpreter:
  val global: Scope = new Scope

  List(
    Builtin("+", (_, args) => args.asInstanceOf[Seq[Number]].map(_.doubleValue).sum),
    Builtin("begin", (sc, args) => evalBegin(sc, args)),
    Builtin(
      "define",
      (sc, args) => sc define Variable(args(0).toString, eval(sc, args(1))),
    ),
    Builtin("quote", (sc, args) => eval(sc, args(0))),
    Builtin("display", (sc, args) => println(evalSeq(sc, args) mkString ", ")),
  ) foreach (df => global define df)

  def run(code: Seq[Any]): Any = evalBegin(global, code)

  def eval(sc: Scope, code: Any): Any =
    code match
      case Seq(name: String, args*) if sc contains name =>
        sc(name) match
          case Builtin(_, func) => func(sc, evalSeq(sc, args).toIndexedSeq)
          case _                => ???
      case s: Seq[?]                  => evalSeq(sc, s)
      case v: String if sc contains v => sc(v).asInstanceOf[Variable].value
      case v                          => v

  final def evalBegin(sc: Scope, code: Seq[Any]): Any =
    val it = code.iterator
    var result: Any = null

    while it.hasNext do result = eval(sc, it.next)

    result

  def evalSeq(sc: Scope, list: Seq[Any]): Seq[Any] = list map (a => eval(sc, a))
