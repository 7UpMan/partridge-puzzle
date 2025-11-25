# Runtimes for various configurations

## Without any optimisation
Total time taken: 11,998,007 ms, with 61,636,000,054 recursions, giving 18,656 solutions  
Individual time taken: 11,998,007.000000 ms - 3:20


## With detection for bounded boxes and ignoring spaces without a single piece fit for 1-7
Total time taken: 14,441,521 ms, with 61,573,685,349 recursions, giving 18,656 solutions  
Individual time taken: 14,441,521 ms  
Fully bound count: 20,370,266, Call count: 1,444,143,205


## Changed getNextFreeLocation(), stopped some pieces from being placed around the edges
Max puzzle piece width: 8  
Total time taken: 11,359,259 ms, with 56,123,795,603 recursions, giving 18,656 solutions  
Individual time taken: 11,359,259 ms  
Fully bound count: 0, Call count: 0


## Made changes around big boxes near edges and 2x2 blocks have to be together.
**Got wrong answer.**  
Max puzzle piece width: 8  
Total time taken: 4,414,763 ms, with 34,213,320,266 recursions, giving 14,496 solutions  
Individual time taken: 4,414,763 ms  
Fully bound count: 0, Call count: 0


## As previously, but with the assumption that 2x2 doesn't need to be next to each other
Total time taken: 6,942,494 ms, with 54,099,448,673 recursions, giving 18,656 solutions  
Individual time taken: 6,942,494 ms  
Fully bound count: 0, Call count: 0


## Optimised the getMaxSquareAtLocation() to only look horizontally
Max puzzle piece width: 8  
Total time taken: 4,471,712 ms, with 54,099,448,673 recursions, giving 18,656 solutions  
Individual time taken: 4,471,712 ms  
Fully bound count: 0, Call count: 0


## Added extra test to try and reduce recursions
**Made situation worse**  
Max puzzle piece width: 8  
Total time taken: 5,092,934 ms, with 54,099,448,673 recursions, giving 18,656 solutions  
Individual time taken: 5,092,934 ms  
Fully bound count: 0, Call count: 0


## Took out the tests above, changed the getMaxSquareAtLocation() algorithm and added tracking
Max puzzle piece width: 8  
Recursions: 10,013,400,472, Time taken: 914,428 ms, Recursions/ms: 10,950.45  
Recursions: 20,013,871,572, Time taken: 1,823,629 ms, Recursions/ms: 10,974.75  
Recursions: 30,014,501,310, Time taken: 2,736,157 ms, Recursions/ms: 10,969.58  
Recursions: 40,076,984,821, Time taken: 3,651,840 ms, Recursions/ms: 10,974.46  
Recursions: 50,010,754,611, Time taken: 4,545,416 ms, Recursions/ms: 11,002.46  
Total time taken: 4,912,647 ms, with 54,099,448,673 recursions, giving 18,656 solutions  
Individual time taken: 4,912,647 ms


## Removed redundant methods, and stopped methods calling other methods of the same name
So this:
```java
    public int getLocation(int x, int y) {
        return grid[x][y];
    }

    public int getLocation(PuzzleGridLocation location) {
        return getLocation(location.getX(),location.getY());
    }
```
Got replaced with :
```java
    public int getLocation(int x, int y) {
        return grid[x][y];
    }

    public int getLocation(PuzzleGridLocation location) {
        return grid[location.getX()][location.getY()];
    }
```
Whilst probably only a few cycles.  When you are doing something 50bn+ times, it
all adds up.  There were probably 10 such simple changes.

Max puzzle piece width: 8  
Recursions: 10,013,400,472, Time taken: 893,187 ms, Recursions/ms: 11,210.87  
Recursions: 20,013,871,572, Time taken: 1,776,679 ms, Recursions/ms: 11,264.77  
Recursions: 30,014,501,310, Time taken: 2,663,142 ms, Recursions/ms: 11,270.33  
Recursions: 40,076,984,821, Time taken: 3,547,147 ms, Recursions/ms: 11,298.37  
Recursions: 50,010,754,611, Time taken: 4,408,989 ms, Recursions/ms: 11,342.91  
Total time taken: 4,764,118 ms, with 54,099,448,673 recursions, giving 18,656 solutions  
Individual time taken: 4,764,118 ms


# Old Logic routes
I tried a different logic.  Rather than holding an array that represented squares and
spaces.  I held a `List<>` of the pieces that were placed.  When I wanted to add another
I ran through the `List<>` checking for overlaps.

**This turned out to be slower (although more elegant) than using the array.**

## With no optimisations and using a stack to hold items
Max puzzle piece width: 8  
Total time taken: 19,297,392 ms, with 61,636,000,054 recursions, giving 18,656 solutions  
Individual time taken: 19,297,392 ms  
Fully bound count: 0, Call count: 0


## Stack holding items, limited optimisations
Max puzzle piece width: 8  
Total time taken: 18,800,760 ms, with 56,123,795,603 recursions, giving 18,656 solutions  
Individual time taken: 18,800,760 ms  
Fully bound count: 0, Call count: 0
