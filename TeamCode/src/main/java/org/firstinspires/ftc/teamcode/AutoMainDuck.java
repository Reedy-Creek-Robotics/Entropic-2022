package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inches;

public abstract class AutoMainDuck extends AutoMain {

    public static final int HOUGH_DETECT_TIME = 3;

    @Override
    public void runOpMode() throws InterruptedException {

        //account for reversed turning angles of each starting position
        if (getStartPosition() == StartPosition.RED_DUCK) {
            fieldSide = 1;
        } else if (getStartPosition() == StartPosition.BLUE_DUCK) {
            fieldSide = -1;
        }

        super.runOpMode();

        // Move to hub
        robot.getDriveTrain().moveForward(inches(-15), 1);
        robot.waitForCommandsToFinish(1.0);

        // Raise arm to proper position
        robot.getArm().moveToPosition(getArmPositionForBarCode(barCodePosition));
        robot.getCapper().goToPosition(Capper.Position.TRAVEL);
        robot.waitForCommandsToFinish();

        //rotate to face the hub
        robot.getDriveTrain().rotate(-57 * fieldSide, 0.5);
        robot.waitForCommandsToFinish();

        //move forward the correct distance to the hub
        if (robot.getArm().getPosition() == Arm.Position.LOW) {
            robot.getDriveTrain().moveForward(inches(-11), .7);
        } else if (robot.getArm().getPosition() == Arm.Position.MEDIUM) {
            robot.getDriveTrain().moveForward(inches(-10), .7);
        } else {
            robot.getDriveTrain().moveForward(inches(-14.5), 0.7);
        }
        robot.waitForCommandsToFinish();

        //deposit the cube in the hub
        robot.getIntake().getDoor().openDoor();
        robot.getIntake().intakeForward(1);
        robot.waitForCommandsToFinish();

        //close the door
        //Back up to avoid hitting allaince hub while turning
        //move the arm to the travel position
        robot.getIntake().getDoor().closeDoor();
        //account for arm position distance
        if (robot.getArm().getPosition() == Arm.Position.LOW) {
            robot.getDriveTrain().moveForward(inches(9.5), 0.7);
        } else if (robot.getArm().getPosition() == Arm.Position.MEDIUM) {
            robot.getDriveTrain().moveForward(inches(8), 0.7);
        } else {
            robot.getDriveTrain().moveForward(inches(12.5), 0.7);
        }
        robot.getArm().moveToPosition(Arm.Position.TRAVEL);
        robot.waitForCommandsToFinish();

        //rotation to line up robot back to the audience wall
        robot.getDriveTrain().rotate(147 * fieldSide, .4);
        robot.waitForCommandsToFinish();

        //account for camera position
        if(getStartPosition()==StartPosition.BLUE_DUCK){
            robot.getDriveTrain().strafe(inches(5),0.3, DriveTrain.StrafeDirection.RIGHT);
            robot.waitForCommandsToFinish();
        }

        //account for camera location
        if(getStartPosition() == StartPosition.RED_DUCK) {
            robot.getDriveTrain().strafe(inches(1),.3, DriveTrain.StrafeDirection.LEFT);
            robot.waitForCommandsToFinish();
        }

        /*
        //corrects for error in the rotation
        robot.getDriveTrain().alignToTileAngle(PARALLEL_TO_TILE_EDGE, .2, 1.5);
        robot.waitForCommandsToFinish();
         */

        //Move to the audience wall
        robot.getDriveTrain().moveForward(inches(-21), 1);
        robot.waitForCommandsToFinish();

        if(getStartPosition() == StartPosition.RED_DUCK) {
            robot.getDriveTrain().strafe(inches(5), .5, DriveTrain.StrafeDirection.LEFT);
            robot.waitForCommandsToFinish();
        }

        //corrects for error after being at the wall
        robot.getDriveTrain().alignToTileAngle(PARALLEL_TO_TILE_EDGE, .3,HOUGH_DETECT_TIME);
        robot.waitForCommandsToFinish();

        robot.getDriveTrain().moveDistanceFromTileEdge(inches(1),.3, HOUGH_DETECT_TIME);
        robot.waitForCommandsToFinish();

        //Strafe into the storage unit, will edventually strafe to duck spinners
        if (getStartPosition() == StartPosition.BLUE_DUCK) {
            robot.getDriveTrain().strafe(inches(20), .6, DriveTrain.StrafeDirection.LEFT);
        } else if (getStartPosition() == StartPosition.RED_DUCK) {
            robot.getDriveTrain().strafe(inches(17.5), .6, DriveTrain.StrafeDirection.RIGHT);
        }
        robot.waitForCommandsToFinish();

        //spins the duck wheels
        robot.getDuckSpinner().spinWheels(.4,4);
        if (getStartPosition() == StartPosition.BLUE_DUCK) {
            robot.getDriveTrain().strafeWithoutRamping(.5,.05, DriveTrain.StrafeDirection.LEFT);
        }else if (getStartPosition() == StartPosition.RED_DUCK) {
            robot.getDriveTrain().strafeWithoutRamping(.5,.05, DriveTrain.StrafeDirection.RIGHT);
        }
        robot.waitForCommandsToFinish();

        //Strafe back to the storage unit
        if (getStartPosition() == StartPosition.BLUE_DUCK) {
            //untested, used to be 20
            robot.getDriveTrain().strafe(inches(18), .5, DriveTrain.StrafeDirection.RIGHT);
        } else if (getStartPosition() == StartPosition.RED_DUCK) {
            //untested, used to be 20
            robot.getDriveTrain().strafe(inches(18), .5, DriveTrain.StrafeDirection.LEFT);
        }
        robot.waitForCommandsToFinish();

        robot.getArm().moveToPosition(Arm.Position.INTAKE);
        robot.getCapper().goToPosition(Capper.Position.START);
        robot.waitForCommandsToFinish();

    }

}
