enum Direction(val degree: Int):
  case North extends Direction(0)
  case East extends Direction(90)
  case South extends Direction(180)
  case West extends Direction(270)

object Direction:
  def towardsTo(degree: Int): Direction =
    degree match
      case 0 => North
      case 90 => East
      case 180 => South
      case 270 => West
      case _ => throw RuntimeException("Cannot figure out the Direction")

import Direction.*

def invert(direction: Direction): Direction =
  direction match
    case North => South
    case South => North
    case East => West
    case West => East

val north = North
north
val south = invert(north)

north.ordinal
north.degree
south.degree

Direction.values
Direction.values.foreach(println)

Direction.valueOf("North")

Direction.towardsTo(180)
