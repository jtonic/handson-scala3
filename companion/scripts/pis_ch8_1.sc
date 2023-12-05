object col:

    class Queue[A](private val leading: List[A], private val trailing: List[A]):
        private def mirror =
            if leading.isEmpty then
                new Queue(trailing.reverse, Nil)
            else
                this
        def head = mirror.leading.head
        def tail =
            val q = mirror
            new Queue(q.leading.tail, q.trailing)
        def enqueue(x: A) = new Queue(leading, x :: trailing)
        override def toString = s"Queue($leading, $trailing)"

import col.Queue

val q = Queue(List(1, 2, 3), List(4, 5, 6))
println(q)
println(q.head)
println(q.tail)
println(q.enqueue(7))
