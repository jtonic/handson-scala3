package ro.jtonic.handson.scala3.ws


class Point(val x: Int, val y: Int):
  override def toString: String = s"Point($x, $y)"
  override def equals(that: Any): Boolean =
    that match
      case that: Point => this.x == that.x && this.y == that.y
      case _ => false
  override def hashCode(): Int = 31 * (31 + x) + y

object TicTacToe:
  opaque type X = Point
  opaque type O = Point
  private def validPoint(x: Int, y: Int): Point =
    require(x >= 0 && x < 3 && y >= 0 && y < 3)
    new Point(x, y)
  object X:
    def apply(x: Int, y: Int): X = validPoint(x, y)
  object O:
    def apply(x: Int, y: Int): O = validPoint(x, y)

import TicTacToe.*

val checkEqualityForOpaqueTypes =
  X(0, 1) == X(0, 1)
  X(0, 1) == O(0, 1)
