# Partride Puzzle or Sum of Cubes Tiling Puzzle

A program to solve the "Partridge Puzzle", named after the 12 days of Christmas.  In
this specific problem you have 1 square of 1 unit x 1 unit, 2 squares of 2 units x
2 units and so on up to N squares of N units c N units.

There are no solutions for N <= 7.  There are 16,656 solutions for N = 8.  This gives
a total of 1,296 square units giving a grid that is 36 x 36.  Each solution to
the puzzle exists within that data set 8 times.  Each "base" solution can have 4 rotations
and 4 reflections in total.

The program brute forces the solutions using some loops and a lot of recursion.

I have tried to add some strategies to "prune" the recursion and reduce the runtime.

Each side of the square that is created has the length ((N+1)*(N/2))