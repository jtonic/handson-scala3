package ro.jtonic.handson.scala3.bookstore

package model:

  type Age = Option[Int]

  case class Address(
    street: String,
    city: String,
    country: String
  )

  case class Publisher(
    name: String,
    address: Address
  )

  enum Gender:
    case Female
    case Male

  case class Author(
    name: String,
    gender: Gender,
    email: String,
    age: Age
  )

  enum BookType:
    case ScienceFiction
    case Drama
    case Comedy
    case Documentary
    case LoveStory
    case Technical

  case class Book(
    title: String,
    authors: List[Author],
    publisher: Publisher,
    price: Double,
    bookType: BookType,
    year: Int,
    pages: Int
  )

  case class BookCopies(
    book: Book,
    copies: Int
  )

  case class BookStore(
    books: List[BookCopies],
    address: Address
  )
