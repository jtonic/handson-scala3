object col:

    trait MyQueue[A]:
        def head: A
        def tail: MyQueue[A]
        def enqueue(x: A): MyQueue[A]

    object MyQueue:

        def apply[A](xs: A*): MyQueue[A] = new MyQueueImpl[A](xs.toList, Nil)

        private class MyQueueImpl[A](private val leading: List[A], private val trailing: List[A]) extends col.MyQueue[A]:
            private def mirror =
                if leading.isEmpty then
                    new MyQueueImpl(trailing.reverse, Nil)
                else
                    this
            def head = mirror.leading.head
            def tail =
                val q = mirror
                new MyQueueImpl(q.leading.tail, q.trailing)
            def enqueue(x: A) = new MyQueueImpl(leading, x :: trailing)
            override def toString = s"Queue($leading, $trailing)"

import col._

val q: MyQueue[Int] = MyQueue(1, 2, 3, 4, 5, 6)
println(q)
println(q.head)
println(q.tail)
println(q.enqueue(7))

def myQueue[T](q: MyQueue[T]) = { }
def myQueue1(q: MyQueue[AnyRef]) = { }

myQueue(q)
// myQueue1(q)
