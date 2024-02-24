package io.github.edadma.jixie

import scala.annotation.tailrec
import math.*

import scala.collection.immutable.Seq

import math.Ordered.orderingToOrdered

abstract class Type
case object STRING extends Type
case object NUMBER extends Type
case object ANY extends Type

abstract class Define { val name: String }
case class Function(name: String, func: IndexedSeq[Any] => Any) extends Define
case class NumericalFunction(name: String, func: Seq[Double] => Any) extends Define
case class NumberFunction(name: String, func: Double => Any) extends Define
case class Builtin(name: String, func: (Scope, IndexedSeq[Any]) => Any) extends Define
case class Variable(name: String, var value: Any) extends Define

class Interpreter:
  val global: Scope = new Scope

  List(
    NumericalFunction("+", _.sum),
    NumericalFunction("-", args => args.head - args.drop(1).sum),
    NumberFunction("sqrt", sqrt),
    Function("display", args => println(args mkString ", ")),
    Function("=", args => args.isEmpty || args.tail.forall(_ == args.head)),
    Function("!=", args => args.isEmpty || args.tail.exists(_ != args.head)),
    Function(
      "<",
      args =>
        args.isEmpty || args.length == 1 || args.sliding(2).forall { case Seq(a, b) =>
          a.asInstanceOf[Comparable[Any]] < b.asInstanceOf[Comparable[Any]]
        },
    ),
    Function(
      ">",
      args =>
        args.isEmpty || args.length == 1 || args.sliding(2).forall { case Seq(a, b) =>
          a.asInstanceOf[Comparable[Any]] > b.asInstanceOf[Comparable[Any]]
        },
    ),
    Function(
      "<=",
      args =>
        args.isEmpty || args.length == 1 || args.sliding(2).forall { case Seq(a, b) =>
          a.asInstanceOf[Comparable[Any]] <= b.asInstanceOf[Comparable[Any]]
        },
    ),
    Function(
      ">=",
      args =>
        args.isEmpty || args.length == 1 || args.sliding(2).forall { case Seq(a, b) =>
          a.asInstanceOf[Comparable[Any]] >= b.asInstanceOf[Comparable[Any]]
        },
    ),
    Builtin("begin", (sc, args) => evalBegin(sc, args)),
    Builtin(
      "define",
      (sc, args) => sc define Variable(args(0).toString, eval(sc, args(1))),
    ),
    Builtin("quote", (_, args) => args(0)),
    Builtin(
      "and",
      (sc, args) => args forall (a => evalBoolean(sc, a)),
    ),
    Builtin(
      "or",
      (sc, args) => args exists (a => evalBoolean(sc, a)),
    ),
    Builtin("not", (sc, args) => evalBoolean(sc, args(0))),
  ) foreach (df => global define df)

  def run(code: Seq[Any]): Any = evalBegin(global, code)

  def evalBoolean(sc: Scope, code: Any): Boolean = eval(sc, code).asInstanceOf[Boolean]

  def eval(sc: Scope, code: Any): Any =
    code match
      case Seq(name: String, args*) if sc contains name =>
        sc(name) match
          case Function(_, func) => func(evalSeq(sc, args).toIndexedSeq)
          case NumericalFunction(_, func) =>
            func(evalSeq(sc, args).asInstanceOf[Seq[Number]].map(_.doubleValue))
          case NumberFunction(_, func) => func(eval(sc, args.head).asInstanceOf[Number].doubleValue)
          case Builtin(_, func)        => func(sc, args.toIndexedSeq)
      case s: Seq[?]                  => evalSeq(sc, s)
      case v: String if sc contains v => sc(v).asInstanceOf[Variable].value
      case v                          => v

  final def evalBegin(sc: Scope, code: Seq[Any]): Any =
    val it = code.iterator
    var result: Any = null

    while it.hasNext do result = eval(sc, it.next)

    result

  def evalSeq(sc: Scope, list: Seq[Any]): Seq[Any] = list map (a => eval(sc, a))
