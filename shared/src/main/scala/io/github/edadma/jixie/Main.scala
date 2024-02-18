package io.github.edadma.jixie

import pprint.pprintln

@main def run(): Unit =
  val interp = new Interpreter
  val result = interp.run(Seq(Seq("+", 3, 4)))

  pprintln(result)

//  Seq(1, 2, 3) match
//    case Seq(a, b*) => pprintln((a, b))
//    case _          => println("no match")
