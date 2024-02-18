package io.github.edadma.jixie

import scala.annotation.tailrec

def eval(env: Scope, code: Any): Any =
  def eval(code: Any): Any =
    code match
      case Seq(name: String, )
      case s: Seq[?] => evalSequence(s)
      case v         => v

  @tailrec
  def evalSequence(code: Seq[Any], result: Any = ()): Any =
    code match
      case head :: tail => evalSequence(tail, eval(head))
      case Nil          => result

  eval(code)
