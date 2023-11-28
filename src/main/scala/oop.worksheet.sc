/*
############################################################
## Standalone Objects and  Companion Objects
############################################################
*/

case class Student private (val name: String)

object Student {

  def apply(name: String) =
    if name.isBlank() then None
    else Some(new Student(name))
}


def getName(student: Option[Student]) = student match
  case None => "no name"
  case Some(value) => value

val tony = Student("Tony")
val irina = Student("")
getName(tony)
getName(irina)
