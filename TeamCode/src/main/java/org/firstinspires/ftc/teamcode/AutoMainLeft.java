package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.GROUND_LEVEL;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.INTAKE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.MEDIUM_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.SMALL_POLE;
import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.TRAVEL;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.LEFT_SIDE;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainLeft extends AutoMain {

    @Override
    protected Position getStartPosition() {
        return new Position(
                1 + inchesToTiles(robotDescriptor.robotDimensionsInInches.width / 2),
                inchesToTiles(robotDescriptor.robotDimensionsInInches.height / 2)
        );
    }

    @Override
    public void runAutoPath() {

        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, .5), BASE_SPEED);
        //robot.getDriveTrain().waitForTileEdgeDetection(1.0);

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2), new Heading(0), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(LinearSlide.SlideHeight.MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        // Wait until we get an observation to make sure we are where we expect

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(1.575, 2), BASE_SPEED);
        robot.waitForCommandsToFinish();

        //todo: add together deliver offset and outtake
        robot.getSlide().moveDeliverOffset();
        robot.waitForCommandsToFinish();

        robot.getIntake().outtake(1);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.75), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish(0.5);
        robot.getSlide().moveToHeight(TRAVEL);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);

        getNewCone();
        deliverToPole(Pole.HIGH);

        //getNewCone();
        //deliverToPole(Pole.HIGH);

        robot.getDriveTrain().moveToTargetPosition(new Position(robot.getDriveTrain().getPosition().getX(),2.5),BASE_SPEED);
        park();

        //getNewCone();
        //deliverToPole(Pole.HIGH);
    }

    //Used after recentered
    protected void getNewCone() {
        //get to stack
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(15), 2.5), BASE_SPEED);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        //intake
        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(1.5);
        robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(10), 2.5), .3);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToHeight(SMALL_POLE);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

    protected void deliverToPole(Pole pole) {
        if (pole == Pole.HIGH) {
            robot.getSlide().moveToHeight(LinearSlide.SlideHeight.TOP_POLE);
            robot.getTurret().moveToOrientation(LEFT_SIDE);
            robot.getDriveTrain().moveToTargetPosition(new Position(2, 2.5), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(2,2.60), BASE_SPEED);
            robot.waitForCommandsToFinish();


            //todo: uncomment
            //robot.getSlide().moveDeliverOffset();
            //robot.waitForCommandsToFinish();

            robot.getIntake().outtake(1);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(0), BASE_SPEED);
            robot.waitForCommandsToFinish(.5);
            robot.getSlide().moveToHeight(TRAVEL);

        } else if (pole == Pole.MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(-45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(1 + inchesToTiles(19), 2 + inchesToTiles(4)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(1.5, 2.5), new Heading(0), BASE_SPEED);
            robot.waitForCommandsToFinish(.5);
            robot.getSlide().moveToHeight(TRAVEL);

        } else if (pole == Pole.LOW) {
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(-45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19), 2 + inchesToTiles(4)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(0), BASE_SPEED);
        } else if (pole == Pole.GROUND) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(45), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(inchesToTiles(19), 2 + inchesToTiles(18.5)), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(0.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(.5, 2.5), new Heading(0), BASE_SPEED);
        }

        robot.waitForCommandsToFinish();
        robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);
    }

    private void park() {
        // April tag position is
        robot.getDriveTrain().moveToTargetPosition(new Position(.5 + (getAprilTagPosition() - 1), 2.5), BASE_SPEED);
        robot.getSlide().moveToHeight(INTAKE);
        robot.waitForCommandsToFinish();
    }

}
