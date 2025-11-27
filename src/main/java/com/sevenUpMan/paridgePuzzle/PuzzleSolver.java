package com.sevenUpMan.paridgePuzzle;

/**
 * Solve the Partridge puzzle by placing puzzle pieces in a grid.
 * 
 * The name "Partridge Puzzle" comes from the 12 days of christmas song where
 * the number of gifts on each day forms a cube:
 * 
 * Discussed here:
 * https://pyrigan.com/2017/02/17/the-partridge-puzzle/
 * https://www.youtube.com/watch?v=eqyuQZHfNPQ
 * https://www.mathpuzzle.com/partridge.html
 * https://www.mscroggs.co.uk/squares/
 * 
 * There are: 18,656 Solutions for size 8
 * 
 * With FAKE_DATA true and MAX_PIECE_SIZE 2, there are 4 solutions.
 * With FAKE_DATA true and MAX_PIECE_SIZE 3, there are 32 solutions.
 * With FAKE_DATA true and MAX_PIECE_SIZE 4, there are 79 solutions.
 */
public class PuzzleSolver {
    private final int maxPuzzlePieceWidth;
    private int totalCells;
    private int gridWidth;
    private int solvedCount = 0;
    private long recursions = 0;
    private long startTime;

    private static final int TIMES_TO_RUN = 1;
    public static final boolean FAKE_DATA = false;
    public static final int MAX_PIECE_SIZE = 8;
    private static final String SOLUTIONS_TXT = "Solutions.txt";
    private static final String SOLUTIONS_HTML = "Solutions%05d.html";

    public static void main(String[] args) {
        PuzzleSolver solver = new PuzzleSolver(MAX_PIECE_SIZE);
        solver.solve();
    }

    public PuzzleSolver(int maxPuzzlePieceWidth) {
        this.maxPuzzlePieceWidth = maxPuzzlePieceWidth;

        if (FAKE_DATA) {
            System.out.println("Using FAKE DATA set");
        }
        System.out.printf("Max puzzle piece width: %d%n", maxPuzzlePieceWidth);

        // Work out size of puzzle grid. Could be a better formula for this?
        totalCells = 0;
        for (int i = 1; i <= maxPuzzlePieceWidth; i++) {
            totalCells += Math.pow(i, 3);
        }

        gridWidth = (int) Math.sqrt(totalCells);

        /*
         * I think gridWidth should be: (maxPuzzlePieceWidth + 1) * (maxPuzzlePieceWidth
         * / 2)
         */
    }

    /**
     * Solve the puzzle by placing puzzle pieces in the grid. We keep track of what
     * puzzle pieces we have left using PuzzlePieceCounters. We place pieces in the
     * grid using PuzzleGrid.
     */
    public void solve() {

        startTime = System.currentTimeMillis();

        for (int i = 0; i < TIMES_TO_RUN; i++) {
            solvedCount = 0;
            recursions = 0;

            PuzzleGrid grid = new PuzzleGrid(gridWidth, maxPuzzlePieceWidth);
            PuzzlePieceCounters counters = new PuzzlePieceCounters(maxPuzzlePieceWidth);

            // Start at the bottom left of the grid
            PuzzleGridLocation nextFreeLocation = new PuzzleGridLocation(0, 0);

            solveRecursive(grid, counters, nextFreeLocation);

            // Close any open file writer
            closeFiles();
        }

        long endTime = System.currentTimeMillis();

        // Display the output
        System.out.printf("Total time taken: %,d ms, with %,d recursions, giving %,d solutions%n",
                (endTime - startTime),
                recursions, solvedCount);
        System.out.printf("Individual time taken: %,d ms%n", (endTime - startTime) / TIMES_TO_RUN);

    }

    /**
     * Recursive solver function. It takes the next free location in the grid
     * then tries to place each available piece that fits there, recursing to try
     * and complete the grid. When the grid is complete, it stores the solution.
     * If no pieces fit, it backtracks.
     * 
     * @param grid
     * @param counters
     * @param currentLocation
     */
    private void solveRecursive(PuzzleGrid grid, PuzzlePieceCounters counters,
            PuzzleGridLocation currentLocation) {

        // Keep track of recursion count for metrics
        recursions++;

        // No need to check for space left as the nextLoc == null test below gets it
        // first

        int maxSquareSize = grid.getMaxSquareAtLocation(currentLocation);

        int smallestAvailablePieceSize = counters.getSmallestAvailablePieceSize();
        if(smallestAvailablePieceSize == -1 || smallestAvailablePieceSize > maxSquareSize) {
            // No pieces left that will fit here
            return;
        }

        // // Get the biggest piece that can fit here
        // int biggestPieceAvailable = counters.getBiggestPieceAvailable(maxSquareSize);
        // if (biggestPieceAvailable == -1) {
        //     return;
        // }

        // Try each available piece size
        for (int size = smallestAvailablePieceSize; size <= maxSquareSize; size++) {
            // Don't have this piece available
            if (!counters.hasPuzzlePieceOfSize(size)) {
                continue;
            }

            // Check if we can skip this placement due to symmetries
            if (canSkipPlacement(currentLocation, size, grid)) {
                continue;
            }

            // Place the piece
            storePiece(currentLocation, size, grid, counters);

            // Recalculate next free location, start after the placed piece
            PuzzleGridLocation nextLocation = grid.getNextFreeLocation(currentLocation.addXY(size, 0, gridWidth));

            // Recurse or check if complete
            if (nextLocation == null) {
                solutionFound(grid);
            } else {
                solveRecursive(grid, counters, nextLocation);
            }

            // BACKTRACK: undo the placement (always continue searching)
            removePiece(currentLocation, size, grid, counters);
        }

        // No solution in this branch
    }

    /**
     * Check if we can skip placing a piece of given size at the given location
     * due to symmetries in the puzzle. The simplest is you cannot place a 1x1
     * pirce on the edge because nothing can go around it. The others are varients
     * on that logic.
     * 
     * @param currentLocation
     * @param size
     * @param grid
     * @return
     */
    private boolean canSkipPlacement(PuzzleGridLocation currentLocation, int size, PuzzleGrid grid) {

        // We cannot optomise FAKE_DATA because the rules don't hold
        if (FAKE_DATA) {
            return false;
        }

        // If size is 1 the piece cannot be placed on the edge of the box or one row in
        // from edge
        if (size == 1 && (currentLocation.getX() <= 1 || currentLocation.getY() <= 1
                || currentLocation.getX() >= gridWidth - 2 || currentLocation.getY() >= gridWidth - 2)) {
            return true;
        }

        // If the size is 3 or above it cannot be placed 1 row from the edge
        if ((size >= 3) && (currentLocation.getX() == 1 || currentLocation.getY() == 1
                || currentLocation.getX() == gridWidth - 1 || currentLocation.getY() == gridWidth - 1)) {
            return true;
        }

        // If the size is 5 or above it cannot be placed 2 rows from the edge
        if ((size >= 5) && (currentLocation.getX() == 2 || currentLocation.getY() == 2
                || currentLocation.getX() == gridWidth - 2 || currentLocation.getY() == gridWidth - 2)) {
            return true;
        }

        // If size=2 and we are on an edge, it must be next to another 2x2 also on the
        // same edge

        return false;

    }

    /**
     * Place a piece of given size at the given location in the grid, updating the
     * counters.
     * 
     * @param location
     * @param size
     * @param grid
     * @param counters
     */
    private void storePiece(PuzzleGridLocation location, int size, PuzzleGrid grid, PuzzlePieceCounters counters) {
        counters.takePuzzlePieceOfSize(size);
        grid.setSquare(location, size);
    }

    /**
     * Remove a piece of given size at the given location in the grid, updating
     * the counters.
     * 
     * @param location
     * @param size
     * @param grid
     * @param counters
     */
    private void removePiece(PuzzleGridLocation location, int size, PuzzleGrid grid, PuzzlePieceCounters counters) {
        grid.removeSquare(location, size);
        counters.returnPuzzlePieceOfSize(size);
    }

    private String currentHtmlFileName = null;

    /**
     * Deal with a found solution: increment count, display metrics,
     * write to files.  This is a mess, but it works and is not performance critical
     * or why anyone else will read this code.
     * 
     * @param grid
     */
    private void solutionFound(PuzzleGrid grid) {
        solvedCount++;
        diaplayMetrics();
        Tools.writeFile(SOLUTIONS_TXT, String.format("Solution number %d%n%s%n%n", solvedCount, grid.toString()));

        /* Create a series of HTML files each with up to 1000 solutions (the first
         * file has solutions 0-999, the second 1000-1999, etc).
         * Each files starts with head.html and ends with footer.html
         */

        if (currentHtmlFileName == null || solvedCount % 1000 == 0) {
            // If we already have a file, finish it off
            if (currentHtmlFileName != null) {
                String footer = Tools.readFile("footer.html");
                Tools.writeFile(currentHtmlFileName, footer);
                Tools.closeFile(currentHtmlFileName);
            }
            currentHtmlFileName = String.format(SOLUTIONS_HTML, solvedCount == 1 ? 0 : solvedCount);
            String header = Tools.readFile("head.html");
            Tools.writeFile(currentHtmlFileName, header);

        }

        Tools.writeFile(currentHtmlFileName,
                String.format("<h1>Solution %d</h1>%n%s%n", solvedCount, grid.toHtmlString()));
    }

    private void closeFiles() {
        // Complete the last html file
        if (currentHtmlFileName != null) {
            String footer = Tools.readFile("footer.html");
            Tools.writeFile(currentHtmlFileName, footer);
        }
        Tools.closeAllFiles();
    }

    private long displayAtRecursionCount = 10000000000L; // 10 billion

    /**
     * Display metrics at intervals. 10bn recustions takes 10-15 minutes.
     */
    private void diaplayMetrics() {
        if (recursions >= displayAtRecursionCount) {
            long currentTime = System.currentTimeMillis();
            long timeTaken = currentTime - startTime;
            double recursionsPerMs = (double) recursions / (double) timeTaken;
            System.out.printf("Recursions: %,d, Time taken: %,d ms, Recursions/ms: %,.2f%n",
                    recursions, timeTaken, recursionsPerMs);
            displayAtRecursionCount += 10000000000L;
        }
    }
}