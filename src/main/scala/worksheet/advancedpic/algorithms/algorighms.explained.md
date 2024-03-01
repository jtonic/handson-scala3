# Algorithms

## Insertion sort algorithm

- input:

> 5  3   12  1  2

- Recursion over the two functions: `sort(s)` and `insert(i)`

```text
     5   (3   (12    (1   (2    Nil ))))
                           2 :: Nil (s)
                      1 :: 2 :: Nil (s)

               12 ::  1 :: 2 :: Nil (s)
                1 :: 12 :: 2 :: Nil (i)
                1 ::  2 :: 12:: Nil (i)

          3 ::  1 ::  2 :: 12:: Nil (s)
          1 ::  3 ::  2 :: 12:: Nil (i)
          1 ::  2 ::  3 :: 12:: Nil (i)

     5 :: 1 ::  2 ::  3 :: 12:: Nil (s)
     1 :: 5 ::  2 ::  3 :: 12:: Nil (s)
     1 :: 2 ::  5 ::  3 :: 12:: Nil (s)
     1 :: 2 ::  3 ::  5 :: 12:: Nil (s)  <- FINISH
```

- __Complexity__

  - `Time`

     O(n) = n^2 (worse or average case)

     O(n) = n   (best case - already sorted)

  - `Space`

     O(n) = n   (best case - already sorted)

algorithms
