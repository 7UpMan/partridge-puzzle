package com.sevenUpMan.paridgePuzzle;

public class PuzzlePieceCounters {
    private final int puzzleSize;
    private int[] pieceCounts;

    public PuzzlePieceCounters(int puzzleSize) {
        this.puzzleSize = puzzleSize;
        this.pieceCounts = new int[puzzleSize];

        // Initialize counts to correct values
        for (int i = 0; i < puzzleSize; i++) {
            pieceCounts[i] = i + 1;
        }

        // Create some fake data for testing
        if (PuzzleSolver.FAKE_DATA) {
            if (puzzleSize == 4) {
                pieceCounts = new int[] { 0, 9, 0, 4 };
            }
            if (puzzleSize == 3) {
                pieceCounts = new int[] { 5, 1, 3 };
            }
            if (puzzleSize == 2) {
                pieceCounts = new int[] { 5, 1 };
            }
        }
    }

    /**
     * Get the largest puzzle piece size that is available, up to the given max
     * size.
     * If no pieces are available that fit, return -1.
     * 
     * @param maxPieceSize
     * @return
     */
    public int getBiggestPieceAvailable(int maxPieceSize) {
        for (int i = maxPieceSize - 1; i >= 0; i--) {
            if (pieceCounts[i] > 0) {
                return i + 1;
            }
        }

        return -1; // Nothing left that will fit
    }

    public void takePuzzlePieceOfSize(int size) {
        pieceCounts[size - 1]--;
    }

    public void returnPuzzlePieceOfSize(int size) {
        pieceCounts[size - 1]++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PuzzlePieceCounters: ");
        for (int i = 0; i < puzzleSize; i++) {
            sb.append(String.format("[Size %dx%d: Count %d] ", i + 1, i + 1, pieceCounts[i]));
        }
        return sb.toString();
    }

    /**
     * Check if a piece of the given size is available.
     */
    public boolean hasPuzzlePieceOfSize(int size) {
        return pieceCounts[size - 1] > 0;
    }

    /**
     * Get the count of pieces of a specific size.
     */
    public int getCountOfSize(int size) {
        return pieceCounts[size - 1];
    }
}