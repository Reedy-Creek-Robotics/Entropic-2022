package org.firstinspires.ftc.teamcode.game;

import static org.firstinspires.ftc.teamcode.game.Field.Direction.EAST;
import static org.firstinspires.ftc.teamcode.game.Field.Direction.NORTH;
import static org.firstinspires.ftc.teamcode.game.Field.Direction.SOUTH;
import static org.firstinspires.ftc.teamcode.game.Field.Direction.WEST;
import static org.firstinspires.ftc.teamcode.util.AssertUtil.assertPosition;

import org.firstinspires.ftc.teamcode.game.Field.Direction;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;
import org.firstinspires.ftc.teamcode.geometry.Vector2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class FieldTest {

    private Field field = new Field();

    @Test
    public void move() {
        tryMove(pos(1.5, 1.5), EAST, pos(2.5, 1.5));
        tryMove(pos(1.5, 1.5), NORTH, pos(1.5, 2.5));
        tryMove(pos(1.5, 1.5), WEST, pos(0.5, 1.5));
        tryMove(pos(1.5, 1.5), SOUTH, pos(1.5, 0.5));
    }

    @Test
    public void move_negativeCoordinates() {
        tryMove(pos(-1.5, -1.5), EAST, pos(-0.5, -1.5));
        tryMove(pos(-1.5, -1.5), NORTH, pos(-1.5, -0.5));
        tryMove(pos(-1.5, -1.5), WEST, pos(-2.5, -1.5));
        tryMove(pos(-1.5, -1.5), SOUTH, pos(-1.5, -2.5));
    }

    @Test
    public void move_startAtMidLine() {
        tryMove(pos(3.0, 1.5), EAST, pos(3.5, 1.5));
        tryMove(pos(3.0, 1.5), WEST, pos(2.5, 1.5));
        tryMove(pos(1.5, 3.0), NORTH, pos(1.5, 3.5));
        tryMove(pos(1.5, 3.0), SOUTH, pos(1.5, 2.5));
    }

    @Test
    public void move_avoidWalls() {
        field.setBounds(new Rectangle(6.0, 6.0));

        tryMove(pos(5.5, 1.5), EAST, pos(5.5, 1.5));
        tryMove(pos(1.5, 5.5), NORTH, pos(1.5, 5.5));
        tryMove(pos(0.5, 1.5), WEST, pos(0.5, 1.5));
        tryMove(pos(1.5, 0.5), SOUTH, pos(1.5, 0.5));
    }

    @Test
    public void move_avoidObstacles() {
        tryMove(pos(3.0, 1.5), NORTH, pos(3.0, 1.5));
        tryMove(pos(3.0, 1.5), SOUTH, pos(3.0, 1.5));
        tryMove(pos(1.5, 3.0), EAST, pos(1.5, 3.0));
        tryMove(pos(1.5, 3.0), WEST, pos(1.5, 3.0));
    }

    @Test
    public void moveHalf() {
        tryMoveHalf(pos(1.5, 1.5), EAST, pos(2.0, 1.5));
        tryMoveHalf(pos(1.5, 1.5), NORTH, pos(1.5, 2.0));
        tryMoveHalf(pos(1.5, 1.5), WEST, pos(1.0, 1.5));
        tryMoveHalf(pos(1.5, 1.5), SOUTH, pos(1.5, 1.0));
    }

    @Test
    public void moveHalf_startAtMidLine() {
        tryMoveHalf(pos(3.0, 1.5), EAST, pos(3.5, 1.5));
        tryMoveHalf(pos(3.0, 1.5), WEST, pos(2.5, 1.5));
        tryMoveHalf(pos(1.5, 3.0), NORTH, pos(1.5, 3.5));
        tryMoveHalf(pos(1.5, 3.0), SOUTH, pos(1.5, 2.5));
    }

    @Test
    public void moveHalf_avoidWalls() {
        field.setBounds(new Rectangle(6.0, 6.0));

        tryMoveHalf(pos(5.5, 1.5), EAST, pos(5.5, 1.5));
        tryMoveHalf(pos(1.5, 5.5), NORTH, pos(1.5, 5.5));
        tryMoveHalf(pos(0.5, 1.5), WEST, pos(0.5, 1.5));
        tryMoveHalf(pos(1.5, 0.5), SOUTH, pos(1.5, 0.5));
    }

    @Test
    public void moveHalf_avoidObstacles() {
        tryMoveHalf(pos(3.0, 1.5), NORTH, pos(3.0, 1.5));
        tryMoveHalf(pos(3.0, 1.5), SOUTH, pos(3.0, 1.5));
        tryMoveHalf(pos(1.5, 3.0), EAST, pos(1.5, 3.0));
        tryMoveHalf(pos(1.5, 3.0), WEST, pos(1.5, 3.0));
    }

    private void tryMove(Position start, Direction direction, Position expected) {
        tryMove(start, direction, expected, false);
    }

    private void tryMoveHalf(Position start, Direction direction, Position expected) {
        tryMove(start, direction, expected, true);
    }

    private void tryMove(Position start, Direction direction, Position expected, boolean half) {
        // Try the movement with various offsets from the starting point.
        List<Vector2> startOffsets = Arrays.asList(
                new Vector2(0.0, 0.0),
                new Vector2(0.2, 0.2),
                new Vector2(0.2, -0.2),
                new Vector2(-0.2, 0.2),
                new Vector2(-0.2, -0.2)
        );

        for (Vector2 startOffset : startOffsets) {
            Position startWithOffset = start.add(startOffset);

            Position actual = half ?
                    field.moveHalf(startWithOffset, direction) :
                    field.move(startWithOffset, direction);

            assertPosition(
                    "start " + start +
                            " with offset " + startOffset +
                            " direction " + direction + ", position",
                    expected, actual
            );
        }
    }

    private Position pos(double x, double y) {
        return new Position(x, y);
    }

}
