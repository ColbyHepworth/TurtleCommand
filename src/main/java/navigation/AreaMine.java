package navigation;

import world.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Generates a list of positions in a rectangular area
 * The positions are sorted by the longest axis first
 * The positions are then ordered in a snake path
 * The positions are then reversed if the last position is closer to the current position
 */
public class AreaMine {

    private final int START_X;
    private final int START_Y;
    private final int START_Z;
    private final int END_X;
    private final int END_Y;
    private final int END_Z;

    private final int D_X;
    private final int D_Y;
    private final int D_Z;

    private static final int POSITIVE_STEP = 1; // used to increment a value
    private static final int NEGATIVE_STEP = -1; // used to decrement a value
    private static final int ALTERNATING_INDEX = 2; // used to alternate the order of the positions in the area

    List<Position> positions = new ArrayList<>();

    public AreaMine(Position start, Position end, Position currentPosition) {

        this.START_X = start.getX();
        this.START_Y = start.getY();
        this.START_Z = start.getZ();
        this.END_X = end.getX();
        this.END_Y = end.getY();
        this.END_Z = end.getZ();

        this.D_X = Math.abs(END_X - START_X);
        this.D_Y = Math.abs(END_Y - START_Y);
        this.D_Z = Math.abs(END_Z - START_Z);

        generateArea(); // generate all positions in the area
        sortPositions(); // sort positions in the area by the longest axis first
        generateSnakePathOrder(); // alternate the order of the positions in the area
        reversePositionsIfNeeded(currentPosition); // executes if the last position is closer to the current position
    }

    public List<Position> getPositions() {
        return positions;
    }

    /**
     * Sorts the positions in the area by the longest axis first
     *
     */
    private void generateArea() {

        int xStep = this.START_X <= this.END_X ? POSITIVE_STEP : NEGATIVE_STEP;
        int yStep = this.START_Y <= this.END_Y ? POSITIVE_STEP : NEGATIVE_STEP;
        int zStep = this.START_Z <= this.END_Z ? POSITIVE_STEP : NEGATIVE_STEP;

        for (int x = this.START_X; x != (this.END_X + xStep); x += xStep) {
            for (int y = this.START_Y; y != (this.END_Y + yStep); y += yStep) {
                for (int z = this.START_Z; z != (this.END_Z + zStep); z += zStep) {
                    this.positions.add(new Position(x, y, z));
                }
            }
        }
    }

    /**
     * Gets the subsets of positions in the area and alternates between reversing them
     */
    public void generateSnakePathOrder() {
        List<List<Position>> innerPositionSubsets = createPositionSubsets(positions, true);
        this.positions = alternatePositionSubsets(innerPositionSubsets);
        List<List<Position>> outerPositionSubsets = createPositionSubsets(positions, false);
        this.positions = alternatePositionSubsets(outerPositionSubsets);
    }

    /**
     * Reverses the positions in the area if the last position is closer to the current position
     * @param positions the positions in the area to create subsets of
     * @param innerSubset whether the subset is inner to handle pointer logic
     */
    private List<List<Position>> createPositionSubsets(List<Position> positions, boolean innerSubset) {
        List<List<Position>> positionSubsets = new ArrayList<>();

        Position previousPosition = positions.get(0);

        boolean xChange = false;
        boolean yChange = false;
        boolean zChange = false;

        int startSubSet = 0;
        for (int i = 1; i < positions.size() - 1; i++) {
            Position currentPosition = positions.get(i);
            if (currentPosition.getX() != previousPosition.getX()) {
                xChange = true;
            }
            if (currentPosition.getY() != previousPosition.getY()) {
                yChange = true;
            }
            if (currentPosition.getZ() != previousPosition.getZ()) {
                zChange = true;
            }
            if (((xChange && yChange) || (xChange && zChange) || (yChange && zChange)) && innerSubset) {
                positionSubsets.add(positions.subList(startSubSet, i));
                startSubSet = i;
                xChange = false;
                yChange = false;
                zChange = false;
            }
            if (xChange && yChange && zChange) {
                positionSubsets.add(positions.subList(startSubSet, i));
                startSubSet = i;
                xChange = false;
                yChange = false;
                zChange = false;
            }
            previousPosition = currentPosition;
        }
        positionSubsets.add(positions.subList(startSubSet, positions.size()));
        return positionSubsets;
    }

    /**
     * Takes in a list of position subsets and alternates between reversing them
     * @param positionSubsets the list of position subsets to alternate
     */
    private List<Position> alternatePositionSubsets(List<List<Position>> positionSubsets) {
        List<Position> newPositions = new ArrayList<>();

        for (int i = 0; i < positionSubsets.size(); i++) {
            if (i % ALTERNATING_INDEX == 0) {
                newPositions.addAll(positionSubsets.get(i));
            } else {
                for (int j = positionSubsets.get(i).size() - 1; j >= 0; j--) {
                    newPositions.add(positionSubsets.get(i).get(j));
                }
            }
        }
        return newPositions;
    }

    /**
     * Sorts the initial positions by the longest axis first
     */
    private void sortPositions() {

        Comparator<Position> byX = Comparator.comparingInt(Position::getX);
        Comparator<Position> byY = Comparator.comparingInt(Position::getY);
        Comparator<Position> byZ = Comparator.comparingInt(Position::getZ);

        if (this.D_X >= this.D_Y && this.D_X >= this.D_Z) {
            if (this.D_Y >= this.D_Z) {
                this.positions.sort(byZ.thenComparing(byY).thenComparing(byX));
            } else {
                positions.sort(byY.thenComparing(byZ).thenComparing(byX));
            }
            return;
        }

        if (this.D_Y >= this.D_X && this.D_Y >= this.D_Z) {
            if (this.D_X >= this.D_Z) {
                positions.sort(byZ.thenComparing(byX).thenComparing(byY));
            } else {
                positions.sort(byX.thenComparing(byZ).thenComparing(byY));
            }
            return;
        }

        if (this.D_X >= this.D_Y) {
            positions.sort(byY.thenComparing(byX).thenComparing(byZ));
        } else {
            positions.sort(byX.thenComparing(byY).thenComparing(byZ));
        }
    }

    /**
     * Reverses the positions in the area if the last position is closer to the current position
     * @param currentPosition the current position of the entity
     */
    private void reversePositionsIfNeeded(Position currentPosition) {
        if (currentPosition.distanceTo(this.positions.get(0)) > currentPosition.distanceTo(this.positions.get(this.positions.size() - 1))) {
            Collections.reverse(this.positions);
        }
    }
}