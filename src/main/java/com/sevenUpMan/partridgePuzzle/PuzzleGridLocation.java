package com.sevenUpMan.partridgePuzzle;

/**
 * Represents a location in the puzzle grid.
 */
public record PuzzleGridLocation(int x, int y) {

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Add the given dx and dy to this location, returning a new location. If the
     * new x goes beyond the grid dimension, wrap to the next row.
     * @param dx
     * @param dy
     * @param gridDimension
     * @return
     */
    public PuzzleGridLocation addXY(int dx, int dy, int gridDimension) {
        if (x + dx >= gridDimension ) {
            return new PuzzleGridLocation(0, y + dy + 1);
        }
        return new PuzzleGridLocation(x + dx, y + dy);
    }

}
