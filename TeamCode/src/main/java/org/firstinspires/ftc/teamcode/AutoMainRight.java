package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.RIGHT_SIDE;
import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inchesToTiles;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.components.LinearSlide;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;

@Autonomous
public class AutoMainRight extends AutoMain {

    @Override
    protected Position getStartPosition() {
        return new Position(
                4 + inchesToTiles(robotDescriptor.robotDimensionsInInches.width / 2),
                inchesToTiles(robotDescriptor.robotDimensionsInInches.height / 2)
        );
    }

    @Override
    public void runAutoPath() {

        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, .5), BASE_SPEED);

        //move to medium pole and prepare to drop off
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.05), new Heading(180), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(4.37, 2.05), BASE_SPEED);
        robot.waitForCommandsToFinish();

        //todo: add together deliver offset and outtake
        robot.getSlide().moveDeliverOffset();
        robot.waitForCommandsToFinish();

        robot.getIntake().outtake(1);
        robot.waitForCommandsToFinish();

        //recenter
        //robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.75), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2+ inchesToTiles(12.25)), BASE_SPEED);
        robot.waitForCommandsToFinish(POLE_WAIT_TIME);
        robot.getSlide().moveToHeight(TRAVEL);
        robot.waitForCommandsToFinish();

        if(usingHough) {
            robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);
        }

        getNewCone();
        deliverToPole(Pole.HIGH);

        park();
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToHeading(new Heading(270),BASE_SPEED);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(6-inchesToTiles(16.5), 2+ inchesToTiles(12.25)), BASE_SPEED);
        robot.getTurret().moveToOrientation(RIGHT_SIDE);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToHeading(new Heading(270),.3,exactRampingDescriptor);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(2);
        robot.getDriveTrain().moveToTargetPosition(new Position(6-inchesToTiles(9.75), robot.getDriveTrain().getPosition().getY()), .10);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToHeight(SMALL_POLE);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(5.5, robot.getDriveTrain().getPosition().getY()), BASE_SPEED);
        robot.waitForCommandsToFinish();

        if(usingHough) {
            robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);
        }
    }

    protected void deliverToPole(Pole pole) {
        if (pole == Pole.HIGH) {
            //line up
            robot.getSlide().moveToHeight(TOP_POLE);
            robot.getTurret().moveToOrientation(BACK);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.1, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            //move to pole
            robot.getDriveTrain().moveToTargetPosition(new Position(4.1, 2.65), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveDeliverOffset();
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(1.5);
            robot.waitForCommandsToFinish();

            //reline up
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), BASE_SPEED);
            robot.waitForCommandsToFinish(POLE_WAIT_TIME);
            robot.getSlide().moveToHeight(SMALL_POLE);

        } else if (pole == Pole.MEDIUM) {
            // Todo: Calibrate
            //line up
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.1, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            //move to pole
            robot.getDriveTrain().moveToTargetPosition(new Position(4.1, 2.45), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveDeliverOffset();
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(1.5);
            robot.waitForCommandsToFinish();

            //reline up
            robot.getDriveTrain().moveToTargetPosition(new Position(4.1, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish(POLE_WAIT_TIME);
            robot.getSlide().moveToHeight(TRAVEL);
        } else if (pole == Pole.LOW) {
            // Todo: Calibrate
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.45), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveDeliverOffset();
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(1.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish(POLE_WAIT_TIME);
            robot.getSlide().moveToHeight(TRAVEL);

        } else if (pole == Pole.GROUND) {
            // Todo: Calibrate
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.65), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(1.5);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().moveToTargetPosition(new Position(5.0, 2.5), new Heading(270), BASE_SPEED);
            robot.waitForCommandsToFinish(POLE_WAIT_TIME);
            robot.getSlide().moveToHeight(TRAVEL);
        }
        robot.waitForCommandsToFinish();
        if(usingHough) {
            robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);
        }
    }

    private void park() {
        robot.getDriveTrain().moveToTargetPosition(new Position(3.5 + (getAprilTagPosition() - 1), 2.5), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.idle(.5);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToHeight(INTAKE);
        robot.waitForCommandsToFinish();
    }

}
