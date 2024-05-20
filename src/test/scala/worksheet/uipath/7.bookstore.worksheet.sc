import ro.jtonic.handson.scala3.bookstore.model._

// authors
val johnDoe = new Author("John Doe", Gender.Male, "john.doe@gmail.com", Some(30))
val janeDoe = new Author("Jane Doe", Gender.Female, "jane.doe@gmail.com", Some(25))
val jillDoe = new Author("Jill Noname", Gender.Male, "jill.noname@gmail.com", Some(35))
val paulFeval = new Author("Paul Feval", Gender.Male, "none@gmail.com", None)
val alexandreDumas = new Author("Alexandre Dumas", Gender.Male, "none@gmail.com", None)
val frankHerbert = new Author("Frank Herbert", Gender.Male, "none@email.com", None)

// publishers
val packt = Publisher("Packt Publishing", Address("Baker Street", "London", "UK"))
val oreilly = Publisher("O'Reilly", Address("Fleet Street", "London", "UK"))
val manning = Publisher("Manning", Address("High Street", "New York", "USA"))


// books
val scala3forDummies = Book("Scala 3 for dummies", List(johnDoe, janeDoe), packt, 25.0, BookType.Technical, 2021, 300)
val scala3InAction = Book("Scala 3 in action", List(johnDoe), packt, 30.0, BookType.Technical, 2021, 400)
val java21InAction = Book("Java 21 in action", List(jillDoe), packt, 35.0, BookType.Technical, 2021, 500)
val loveStory = Book("Love story", List(janeDoe), oreilly, 20.0, BookType.LoveStory, 1968, 200)
val threeOfTheMusqueteers = Book("The three of the musqueteers", List(janeDoe), manning, 30.0, BookType.Drama, 1844, 700)
val after20Years = Book("After 20 years", List(alexandreDumas), manning, 25.0, BookType.Drama, 1845, 750)
val theBlackTulip = Book("The black tulip", List(alexandreDumas), manning, 25.0, BookType.Drama, 1850, 700)
val fausta = Book("Fausta", List(paulFeval), manning, 35.0, BookType.Drama, 1846, 800)
val dune = Book("Dune", List(johnDoe), manning, 40.0, BookType.ScienceFiction, 1965, 600)

// bookstores
val bookStoreNY = BookStore(
    List(
      BookCopies(scala3InAction, 1),
      BookCopies(java21InAction, 10),
      BookCopies(loveStory, 3),
      BookCopies(dune, 20),
      BookCopies(threeOfTheMusqueteers, 10),
    ),
    Address("High Street", "New York", "USA")
)
val bookStoreLondon = BookStore(
  List(
    BookCopies(scala3forDummies, 20),
    BookCopies(scala3InAction, 2),
    BookCopies(java21InAction, 10),
    BookCopies(loveStory, 30),
    BookCopies(dune, 10),
    BookCopies(fausta, 5)
  ),
  Address("Baker Street", "London", "UK")
)

// How many books are in all bookstores?
val allBooksCopies = bookStoreLondon.books ++ bookStoreNY.books
allBooksCopies.map(_.copies).sum

// Total number of Dunes book in all bookstores
allBooksCopies.filter(_.book.title == "Dune").map(_.copies).sum

// Total number of books written by female author in all bookstores
allBooksCopies.flatMap(_.book.authors).filter(_.gender == Gender.Female).distinct

// all books written
allBooksCopies.map(_.book.title).distinct
