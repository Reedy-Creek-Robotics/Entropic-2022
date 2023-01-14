package org.firstinspires.ftc.teamcode.game;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public class Field {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private Rectangle bounds;

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Calculates the new target position after moving one tile in the given direction.
     */
    public Position move(Position position, Direction direction) {
        Vector2 offset = offsetForDirection(direction);
        if (isOnTileEdge(position)) {
            // If starting on the tile edge, move only half a tile.
            offset = offset.multiply(0.5);
        }

        Position targetPosition = position.add(offset);

        if (isValidMove(position, targetPosition, direction)) {
            return targetPosition.alignToTileMiddle();
        } else {
            return position.alignToTileEdge();
        }
    }

    /**
     * Calculates the new target position after moving a half tile in the given direction.
     */
    public Position moveHalf(Position position, Direction direction) {
        Vector2 offset = offsetForDirection(direction).multiply(0.5);
        Position targetPosition = position.add(offset);

        if (isValidMove(position, targetPosition, direction)) {
            return targetPosition.alignToTileEdge();
        } else {
            return position.alignToTileEdge();
        }
    }

    public boolean isValidMove(Position position, Position targetPosition, Direction direction) {
        return avoidsWalls(targetPosition) && avoidsObstacles(position, direction);
    }

    private boolean avoidsWalls(Position position) {
        if (bounds == null) return true;
        return position.getX() <= bounds.getRight() - 0.25 && position.getX() >= bounds.getLeft() + 0.25 &&
                position.getY() <= bounds.getTop() - 0.25 && position.getY() >= bounds.getBottom() + 0.25;
    }

    private boolean avoidsObstacles(Position position, Direction direction) {
        double positionInTile;
        if ((direction == Direction.NORTH || direction == Direction.SOUTH)) {
            positionInTile = position.getX() - (int) position.getX();
        } else {
            positionInTile = position.getY() - (int) position.getY();
        }
        return inRange(Math.abs(positionInTile), 0.25, 0.75);
    }

    private boolean isOnTileEdge(Position position) {
        double x = Math.abs(position.getX() - (int) position.getX());
        double y = Math.abs(position.getY() - (int) position.getY());

        return !inRange(x, 0.25, 0.75) ||
                !inRange(y, 0.25, 0.75);
    }

    private boolean inRange(double value, double rangeStart, double rangeEnd) {
        return value >= rangeStart && value <= rangeEnd;
    }

    private Vector2 offsetForDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                return new Vector2(0.0, 1.0);
            case SOUTH:
                return new Vector2(0.0, -1.0);
            case EAST:
                return new Vector2(1.0, 0.0);
            case WEST:
                return new Vector2(-1.0, 0.0);
            default:
                throw new IllegalArgumentException();
        }
    }

}
