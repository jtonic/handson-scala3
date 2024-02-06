import scala.util.Try
import scala.util.Success
import scala.util.Failure

trait Monad[F[_]] {
  def map[A, B](fa: F[A])(f: A => B): F[B]
  def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
  def pure[A](a: A): F[A]
}

given Monad[List] with {
  def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
  def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)
  def pure[A](a: A): List[A] = List(a)
}

given Monad[Option] with {
  def map[A, B](fa: Option[A])(f: A => B): Option[B] = fa.map(f)
  def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
  def pure[A](a: A): Option[A] = Option(a)
}

given Monad[Try] with {
  def map[A, B](fa: Try[A])(f: A => B): Try[B] = fa.map(f)
  def flatMap[A, B](fa: Try[A])(f: A => Try[B]): Try[B] = fa.flatMap(f)
  def pure[A](a: A): Try[A] = Success(a)
}

extension [F[_]: Monad, A](fa: F[A]) {
  def peek(f: A => Unit): F[A] = summon[Monad[F]].map(fa)(a => { f(a); a })
}

val list = List(1, 2, 3)
list.peek(println).filter(_ > 2).sum

val tryValue = Try(42 / 2)
tryValue.peek(println).map(_ + 1).foreach(println)

val option: Option[Int] = Some(42)
option.peek(println)
