package io.github.edadma.jixie

import pprint.pprintln

@main def run(): Unit =
  val interp = new Interpreter
  val result = interp.run(Seq(123, 456))

  pprintln(result)
