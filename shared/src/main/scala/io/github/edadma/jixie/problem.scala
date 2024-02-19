package io.github.edadma.jixie

def problem(msg: String): Nothing =
  Console.err.println(msg)
//  sys.exit(1)
  sys.error(msg)
