package org.firstinspires.ftc.teamcode.game;

import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;

public class Field {

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }

    private Rectangle bounds = new Rectangle(
            6.0, 6.0
    );

    /**
     * Calculates the new target position after moving one tile in the given direction.
     */
    public Position move(Position position, Direction direction) {
        return new Position(0.0, 0.0);
    }

    /**
     * Calculates the new target position after moving a half tile in the given direction.
     */
    public Position moveHalf(Position position, Direction direction) {
       return new Position(0.0, 0.0);
    }

}
