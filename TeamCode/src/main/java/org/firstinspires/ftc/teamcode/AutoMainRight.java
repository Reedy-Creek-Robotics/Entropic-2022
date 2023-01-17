package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.components.LinearSlide.SlideHeight.*;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.BACK;
import static org.firstinspires.ftc.teamcode.components.Turret.Orientation.FRONT;
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
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2), new Heading(180), BASE_SPEED);
        robot.getTurret().moveToOrientation(FRONT);
        robot.getSlide().moveToHeight(MEDIUM_POLE);
        robot.waitForCommandsToFinish();

        //drop off pole
        robot.getDriveTrain().moveToTargetPosition(new Position(4.425, 2), BASE_SPEED);
        robot.waitForCommandsToFinish();
        robot.getIntake().outtake(2);
        robot.waitForCommandsToFinish();

        //todo: add together deliver offset and outtake
        robot.getSlide().moveDeliverOffset();
        robot.waitForCommandsToFinish();

        robot.getIntake().outtake(1);
        robot.waitForCommandsToFinish();

        //recenter
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.75), BASE_SPEED);
        robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish(0.5);
        robot.getSlide().moveToHeight(TRAVEL);
        robot.waitForCommandsToFinish();

        if(usingHough) {
            robot.getDriveTrain().waitForTileEdgeDetection(0.5, 1.0);
        }

        getNewCone();
        deliverToPole(Pole.HIGH);

        robot.getDriveTrain().moveToTargetPosition(new Position(robot.getDriveTrain().getPosition().getX(),2.5),BASE_SPEED);
        park();

        /*
        //test
        getNewCone();
        deliverToPole(HIGH);

        getNewCone();
        deliverToPole(MEDIUM);

        getNewCone();
        deliverToPole(LOW);

        getNewCone();
        deliverToPole(GROUND);
        */

        //get a new cone(loop*5)
        //getNewCone();
        //deliver to pole(loop*5)
        //deliverToPole(Pole.HIGH);
    }

    //Used after recentered
    protected void getNewCone() {
        robot.getDriveTrain().moveToTargetPosition(new Position(5.7, 2.5), BASE_SPEED);
        robot.getTurret().moveToOrientation(BACK);
        robot.waitForCommandsToFinish();

        robot.getSlide().moveToIntake(coneCount);
        coneCount--;
        robot.getIntake().intake(.5);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), BASE_SPEED);
        robot.waitForCommandsToFinish();
    }

    protected void deliverToPole(Pole pole) {
        if (pole == Pole.HIGH) {
            robot.getSlide().moveToHeight(TOP_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(135), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3, 2.7), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == Pole.MEDIUM) {
            robot.getSlide().moveToHeight(MEDIUM_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(225), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.3, 2.3), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(4.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == Pole.LOW) {
            robot.getSlide().moveToHeight(SMALL_POLE);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(225), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3, 2.3), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(180), BASE_SPEED);
        } else if (pole == Pole.GROUND) {
            robot.getSlide().moveToHeight(GROUND_LEVEL);
            robot.getTurret().moveToOrientation(FRONT);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(135), BASE_SPEED);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.3, 2.7), BASE_SPEED);
            robot.waitForCommandsToFinish();

            robot.getIntake().outtake(.5);
            robot.waitForCommandsToFinish();

            robot.getSlide().moveToHeight(TRAVEL);
            robot.getDriveTrain().moveToTargetPosition(new Position(5.5, 2.5), new Heading(180), BASE_SPEED);
        }
        robot.waitForCommandsToFinish();
    }

    private void park() {
        robot.getDriveTrain().moveToTargetPosition(new Position(3.5 + (getAprilTagPosition() - 1), 1.5), BASE_SPEED);
        robot.getSlide().moveToHeight(INTAKE);
        robot.waitForCommandsToFinish();
    }

}
