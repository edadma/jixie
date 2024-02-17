package io.github.edadma.jixie

import scala.annotation.tailrec

var formEscape = Map.empty

def eval(env: Env, code: List[Any]): Any =
  @tailrec
  def eval(code: List[Any], result: Any = ()): Any =
    code match
      case head :: tail =>
        eval(
          tail,
          head match
            case List(formEscape, name: String) =>
            case v                              => v,
        )
      case Nil => result

  eval(code)
