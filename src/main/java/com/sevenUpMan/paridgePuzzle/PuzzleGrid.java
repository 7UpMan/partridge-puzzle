package com.sevenUpMan.paridgePuzzle;

import java.util.Arrays;

public class PuzzleGrid {
    private int gridWidth;
    private int maxPuzzlePieceWidth;
    private int[][] grid;

    public PuzzleGrid(int gridWidth, int maxPuzzlePieceWidth) {
        this.gridWidth = gridWidth;
        this.maxPuzzlePieceWidth = maxPuzzlePieceWidth;
        this.grid = new int[gridWidth][gridWidth];
        // Fill the grid with zeros
        for (int[] row : grid)
            Arrays.fill(row, 0);
    }

    /**
     * Find the next free location in the grid starting from the given location. We
     * fill the grid left to right, top to bottom.
     * 
     * @param startLocation The location to start searching from.
     * @return The next free location, or null if no free location is found.
     */
    public PuzzleGridLocation getNextFreeLocation(PuzzleGridLocation startLocation) {
        // If Y is off the top we have run out of space to look
        if (startLocation.getY() >= gridWidth) {
            return null;
        }

        int x = startLocation.getX();
        int y = startLocation.getY();
        int currentLocationValue = grid[x][y];
        while(currentLocationValue != 0) {
            // Move to next location
            x += currentLocationValue;

            if (x >= gridWidth) {
                x = 0;
                y ++;

                // If y is too big we are done
                if (y >= gridWidth) {
                    return null;
                }
            }

            currentLocationValue = grid[x][y];
        }

        // Found
        return new PuzzleGridLocation(x, y);
    }

    public boolean isLocationFree(PuzzleGridLocation location) {
        return grid[location.getX()][location.getY()] == 0;
    }

    public boolean isLocationFree(int x, int y) {
        return grid[x][y] == 0;
    }


    /**
     * Set a box in the grid at given location.  We just plot left edge. Because we
     * build from the left bottom, this is all we need, and is *much* quicker.
     * @param location
     * @param size
     * @return The value assigned to the box.
     */
    public void setSquare(PuzzleGridLocation location, int size) {
        for (int i = 0; i < size; i++) {
            grid[location.getX()][location.getY() + i] = size;
        }
    }

    /**
     * Remove a square from the grid at given location. We just clear tje left edge.
     * @param x
     * @param y
     * @param size
     */
    public void removeSquare(PuzzleGridLocation location, int size) {
        for (int i = 0; i < size; i++) {
            grid[location.getX()][location.getY() + i] = 0;
        }
    }



    /**
     * Returns the maximum space available at a given location. The location
     * is assumed to be at the bottom left corner of the shape to be placed. The
     * entire space must be empty, and the shape will be a square.
     * Note that a size of 0 menas there is no space available. A size of 1 means
     * there is space for a 1x1 square, etc.
     * 
     * @param location
     * @return
     */
    public int getMaxSquareAtLocation(PuzzleGridLocation location) {

        // If we are not given an empty location, return -1. This also means size >= 1
        if (!isLocationFree(location)) {
            return -1;
        }

        // Size counts from 1. Arrays are zero based.

        /* We only need to check the horizontal direction, as the vertical direction
         * will be empty above this space because we build from the bottom left and
         * all pieces are squares.  No overhangs are possible
         */
        int maxSizePossible = Math.min(maxPuzzlePieceWidth, Math.min(gridWidth - location.getX() , gridWidth - location.getY()));

        // We don't need to check a size of 1 because the first check covers that
        
        // Look for a blockage, and stop if we find it
        for(int size = 1; size <= maxSizePossible; size++) {
            // Check the new row and column are free
            if(!isLocationFree(location.getX() + size - 1, location.getY())) {
                return size - 1;
            }
        }

        // Nothing found, so max size is possible
        return maxSizePossible;
    }

    public int getLocation(int x, int y) {
        return grid[x][y];
    }

    public int getLocation(PuzzleGridLocation location) {
        return grid[location.getX()][location.getY()];
    }

    private static final String SEQUENCE = ".123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0";
    private static final char[] CHAR_SEQUENCE = SEQUENCE.toCharArray();

    /**
     * String representation of the grid. Note that the grid is printed
     * upside down (y=0 is at the bottom).
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rows = gridWidth - 1; rows >= 0; rows--) {
            for (int cols = 0; cols < gridWidth; cols++) {
                sb.append(CHAR_SEQUENCE[grid[cols][rows]]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Generate the same output at getHtmlString() but without using the placements
     * data structure because we are going to remove it.
     * @return
     */
    public String toHtmlString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"grid\">");

        // Create a copy of the grid
        int[][] gridCopy = new int[gridWidth][gridWidth];
        for (int i = 0; i < gridWidth; i++) {
            System.arraycopy(grid[i], 0, gridCopy[i], 0, gridWidth);
        }

        int x=0;
        int y=0;

        while(y < gridWidth) {
            int size = gridCopy[x][y];
            if(size != 0) {
                sb.append(String.format(
                            "<div class=\"square s%d c%d x%d y%d\"></div>%n",
                            size,
                            size,
                            x + 1,
                            y + 1));

                // Clear the square from the copy
                for(int i=0; i<size; i++) {
                    gridCopy[x][y + i] = 0;
                }
                // Move x on
                x += size;
            } else {
                x++;
            }

            if(x >= gridWidth) {
                x = 0;
                y++;
            }
        }
       
        sb.append("</div>");
        return sb.toString();
    }
}
