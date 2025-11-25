package com.sevenUpMan.paridgePuzzle.old;

import java.util.ArrayDeque;
import java.util.Deque;

import com.sevenUpMan.paridgePuzzle.PuzzleGrid;
import com.sevenUpMan.paridgePuzzle.PuzzleGridLocation;

public class PuzzlePieceManager {
    private Deque<PuzzlePiecePlacement> placements = new ArrayDeque<>();
    private int gridSize = 0;
    private int spaceLeft;

    public PuzzlePieceManager(int gridSize) {
        this.gridSize = gridSize;
        this.spaceLeft = gridSize * gridSize;
    }

    /**
     * Place a piece of the given size at the given location.  Before this is done
     * all the other pieces are checked to ensure no overlap.  If there is no
     * overlap, the piece is placed and true is returned.  If there is overlap,
     * false is returned and no piece is placed.
     * @param newPieceSize
     * @param newLocation
     * @return
     */
    public boolean placePiece(int newPieceSize, PuzzleGridLocation newLocation) {
        if(!isPlacementPossible(newPieceSize, newLocation)) {
            return false;
        }

        // No overlap, place the piece
        spaceLeft -= newPieceSize * newPieceSize;
        placements.push(new PuzzlePiecePlacement(newPieceSize, newLocation));
        return true;
    }

    public boolean isPlacementPossible(int newPieceSize, PuzzleGridLocation newLocation) {
        for (PuzzlePiecePlacement placement : placements) {
            int existingPieceSize = placement.pieceSize;
            PuzzleGridLocation existingPieceLocation = placement.location;

            // Check the esges are not outside the grid
            if (newLocation.getX() + newPieceSize > gridSize ||
                newLocation.getY() + newPieceSize > gridSize) {
                return false;
            }

            // Check for overlap
            if (newLocation.getX() < existingPieceLocation.getX() + existingPieceSize &&
                newLocation.getX() + newPieceSize > existingPieceLocation.getX() &&
                newLocation.getY() < existingPieceLocation.getY() + existingPieceSize &&
                newLocation.getY() + newPieceSize > existingPieceLocation.getY()) {
                // Overlap detected
                return false;
            }
        }

        return true;
    }

    PuzzleGridLocation getNextAvailableLocation(PuzzleGridLocation startLocation) {
        for(int y = startLocation.getY(); y < gridSize; y++) {
            for(int x = (y == startLocation.getY() ? startLocation.getX() : 0); x < gridSize; x++) {
                PuzzlePiecePlacement overlap = getPiceOverlap(new PuzzleGridLocation(x, y));
                if (overlap != null) {
                    // Skip to the end of this piece
                    x = overlap.location.getX() + overlap.pieceSize - 1;
                    continue;
                } else {
                    return new PuzzleGridLocation(x, y);
                }
            }
        }

        return null; // No available location
    }

    public PuzzlePiecePlacement getPiceOverlap(PuzzleGridLocation location) {
        for (PuzzlePiecePlacement placement : placements) {
            int pieceSize = placement.pieceSize;
            PuzzleGridLocation pieceLocation = placement.location;

            if (location.getX() >= pieceLocation.getX() &&
                location.getX() < pieceLocation.getX() + pieceSize &&
                location.getY() >= pieceLocation.getY() &&
                location.getY() < pieceLocation.getY() + pieceSize) {
                return placement;
            }
        }

        return null; // No overlap
    }

    public void removeLastPiece() {
        PuzzlePiecePlacement removed = placements.pop();
        spaceLeft += removed.pieceSize * removed.pieceSize;
    }

    public void fillGrid(PuzzleGrid grid) {
        for (PuzzlePiecePlacement placement : placements) {
            grid.setSquare(placement.location, placement.pieceSize);
        }
    }

    public int getSpacesLeft() {
        return spaceLeft;
    }

    private record PuzzlePiecePlacement(int pieceSize, PuzzleGridLocation location) {
    }
}
