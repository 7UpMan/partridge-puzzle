# Partridge Puzzle or Sum of Cubes Tiling Puzzle

A program to solve the "Partridge Puzzle", named after the 12 days of Christmas.  In
this specific problem you have 1 square of 1 unit x 1 unit, 2 squares of 2 units x
2 units and so on up to N squares of N units x N units.

There are no solutions for N <= 7.  There are 18,656 solutions for N = 8.  This gives
a total of 1,296 square units giving a grid that is 36 x 36.  Each solution to
the puzzle exists within that data set 8 times.  Each "base" solution can have 4 rotations
and 4 reflections in total.

The program brute forces the solutions using some loops and a lot of recursion.

I have tried to add some strategies to "prune" the recursion and reduce the runtime.

Each side of the square that is created has the length ((N+1)*(N/2))

# External References

Here are some additional resources that reference the same problem:
[https://pyrigan.com/2017/02/17/the-partridge-puzzle/](https://pyrigan.com/2017/02/17/the-partridge-puzzle/)

[https://www.youtube.com/watch?v=eqyuQZHfNPQ](https://www.youtube.com/watch?v=eqyuQZHfNPQ)

[https://www.mathpuzzle.com/partridge.html](https://www.mathpuzzle.com/partridge.html)

[https://www.mscroggs.co.uk/squares/](https://www.mscroggs.co.uk/squares/)


# The Code
Start with PuzzleSolver - this has the `main()` class.
This file has the following flags:
`TIMES_TO_RUN` - it will create a loop and try the solve several times, used to 
work out the quickest way to do something.

`FAKE_DATA` - this creates some fake data for size = 2, 3 or 4.  These are not normally solvable sizes
but the data is created to help testing.  These break the optimisations so they are turned off
if `FAKE_DATA` is turned on.

`MAX_PIECE_SIZE` - the maximum piece size.  The puzzle dimensions are calculated based on this.

In general, you should find the code well commented.  The "interesting bits" i.e. optimisations are
placed in the `canSkipPlacement()` method inside `PuzzleSolver.java'.  The rest of it are the 
mechanics to make everything work.

# Timings and metrics
The code is single threaded and takes about just over 1 hour to complete 54,099,448,673 recursions
which gives the correct answer of 18,656 solutions for N=8.  There are some files created in
the `output` folder.  The txt file is a textual representation of the solution.  There are
several HTML files that are generated.  These HTML files give a nice graphical view of the
solutions.  It creates several HTML files otherwise, a single file is too big.
