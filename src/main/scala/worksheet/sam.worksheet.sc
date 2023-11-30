trait Increaser:
  def increase(x: Int): Int

extension (x: Int)
  def increase(increaser: Increaser): Int = increaser.increase(x)
  def increment(): Int = increase(_ + 1)

3.increase(_ + 2)
3.increment()
