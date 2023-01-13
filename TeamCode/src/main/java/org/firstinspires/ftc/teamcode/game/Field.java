package org.firstinspires.ftc.teamcode.game;

import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.FIELD_X;
import static org.firstinspires.ftc.teamcode.components.DriveTrain.Direction.FIELD_Y;

import org.firstinspires.ftc.teamcode.components.DriveTrain;
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

    /*
    private Rectangle bounds = new Rectangle(
            6.0, 6.0
    );
    */

    // todo: for now, run without wall detection
    private Rectangle bounds = new Rectangle(
            100.0, 100.0, -100.0, -100.0
    );

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Calculates the new target position after moving one tile in the given direction.
     */
    public Position move(Position position, Direction direction) {
        Position targetPosition;
        if (direction == Direction.NORTH) {
            targetPosition = position.add(new Vector2(0, 1));
        } else if (direction == Direction.SOUTH) {
            targetPosition = position.add(new Vector2(0, -1));
        } else if (direction == Direction.EAST) {
            targetPosition = position.add(new Vector2(1, 0));
        } else if (direction == Direction.WEST) {
            targetPosition = position.add(new Vector2(-1, 0));
        } else {
            throw new IllegalArgumentException();
        }

        return validPosition(position, targetPosition, direction) ?
                targetPosition.alignToTileMiddle() :
                position.alignToTileEdge();
    }


    /**
     * Calculates the new target position after moving a half tile in the given direction.
     */
    public Position moveHalf(Position position, Direction direction) {


        //Add the distance moved
        Position targetPosition;
        if (direction == Direction.NORTH) {
            targetPosition = position.add(new Vector2(0, .5));
        } else if (direction == Direction.SOUTH) {
            targetPosition = position.add(new Vector2(0, -.5));
        } else if (direction == Direction.EAST) {
            targetPosition = position.add(new Vector2(.5, 0));
        } else if (direction == Direction.WEST) {
            targetPosition = position.add(new Vector2(-.5, 0));
        } else {
            throw new IllegalArgumentException();
        }

        targetPosition = validPosition(position, targetPosition, direction) ? targetPosition : position;

        return targetPosition.alignToTileEdge();
    }

    public boolean validPosition(Position position, Position targetPosition, Direction direction) {
        return avoidWalls(targetPosition) && avoidObstacles(position, direction);
    }

    private boolean avoidWalls(Position position) {
        return position.getX() <= bounds.getRight() - 0.2 && position.getX() >= bounds.getLeft() + 0.2 &&
               position.getY() <= bounds.getTop() - 0.2 && position.getY() >= bounds.getBottom() + 0.2;
    }

    private boolean avoidObstacles(Position position, Direction direction) {
        double decimalVal;
        if ((direction == Direction.NORTH || direction == Direction.SOUTH)) {
            decimalVal = position.getX() - (int) position.getX();
        } else {
            decimalVal = position.getY() - (int) position.getY();
        }
        return inThreshold(decimalVal, .3, .7);
    }

    private boolean inThreshold(double value, double rangeStart, double rangeEnd) {
        return value >= rangeStart && value <= rangeEnd;
    }

}
