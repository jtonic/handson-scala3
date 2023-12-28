//-------------------------------------------
// set
// ------------------------------------------

val fruits = Set("apple", "banana", "orange", "kiwi")
fruits("apple")
fruits("pear")

val fruits2 = Set("apple", "banana", "pineapple", "pear", "grape", "watermelon")

fruits & fruits2

fruits | fruits2

fruits &~ fruits2
fruits2 &~ fruits

fruits &~ fruits2 | fruits2 &~ fruits

import scala.collection.mutable

val mutableFruits = mutable.Set("apple", "banana", "orange", "kiwi")
mutableFruits += "pear"
mutableFruits -= "apple"
mutableFruits ++= List("pineapple", "pear", "grape", "watermelon")

// ------------------------------------------
// map
// ------------------------------------------

val fruitsMap = Map("apple" -> 1, "banana" -> 2, "orange" -> 3, "kiwi" -> 4)

fruitsMap("apple")
fruitsMap.get("apple")
fruitsMap.get("plum")

fruitsMap + ("plum" -> 5)
fruitsMap - "apple"
fruitsMap -- List("orange", "banana")
fruitsMap ++ List("grape" -> 6, "watermelon" -> 7)
fruitsMap.updatedWith("apple")(_.map(_ * 100))
fruitsMap
fruitsMap.isDefinedAt("apple")
fruitsMap.isDefinedAt("plum")

fruitsMap.view.filterKeys(_ != "apple").toMap
fruitsMap.view.mapValues(_ * 10).toMap

val fruitsMutMap = fruitsMap to mutable.Map
fruitsMutMap("apple") = 200
fruitsMutMap("grapes") = 20
fruitsMutMap -= "kiwi"
fruitsMutMap ++= List("plum" -> 5, "watermelon" -> 7)
fruitsMutMap.put("peach", 10)
fruitsMutMap
