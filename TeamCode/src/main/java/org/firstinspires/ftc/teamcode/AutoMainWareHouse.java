package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.util.DistanceUtil.inches;

public abstract class AutoMainWareHouse extends AutoMain{
    @Override
    public void runOpMode() throws InterruptedException {

        if (getStartPosition() == StartPosition.RED_WAREHOUSE) {
            fieldSide = 1;
        } else if (getStartPosition() == StartPosition.BLUE_WAREHOUSE) {
            fieldSide = -1;
        }

        //raise servo to correct level, try to detect barcode
        super.runOpMode();

        //move to avoid the wall when rotating
        if(getStartPosition() == StartPosition.RED_WAREHOUSE) {
            robot.getDriveTrain().strafe(inches(3), .5, DriveTrain.StrafeDirection.RIGHT);
        } else if (getStartPosition() == StartPosition.BLUE_WAREHOUSE) {
            robot.getDriveTrain().strafe(inches(3), .5, DriveTrain.StrafeDirection.LEFT);
        }
        robot.waitForCommandsToFinish();

        //go forward and raise arm to correct position
        robot.getDriveTrain().moveForward(inches(-15),1);
        robot.waitForCommandsToFinish(1);
        robot.getArm().moveToPosition(getArmPositionForBarCode(barCodePosition));
        robot.waitForCommandsToFinish();

        //face the hub
        robot.getDriveTrain().rotate(47 * fieldSide,.3);
        robot.waitForCommandsToFinish();

        //go to the hub
        if (robot.getArm().getPosition() == Arm.Position.LOW) {
            robot.getDriveTrain().moveForward(inches(-5.5), .5);
        } else if (robot.getArm().getPosition() == Arm.Position.MEDIUM) {
            robot.getDriveTrain().moveForward(inches(-6.5), .5);
        } else {
            robot.getDriveTrain().moveForward(inches(-9.5), 0.5);
        }
        robot.waitForCommandsToFinish();

        //deposit the cube in the hub
        robot.getIntake().getDoor().openDoor();
        robot.getIntake().intakeForward(1);
        robot.waitForCommandsToFinish();

        //backs the robot up from the hub
        robot.getDriveTrain().moveForward(inches(3),.5);
        robot.waitForCommandsToFinish();

        //close door to not take out preloaded box
        robot.getIntake().getDoor().closeDoor();
        robot.waitForCommandsToFinish();

        //move the arm to travel
        //line up with wall
        if(getStartPosition()==StartPosition.BLUE_WAREHOUSE){
            robot.getDriveTrain().rotate(-137 * fieldSide,0.3);
            robot.getArm().moveToPosition(Arm.Position.TRAVEL);
            robot.waitForCommandsToFinish();

            robot.getDriveTrain().strafe(inches(1),0.3, DriveTrain.StrafeDirection.LEFT);
            robot.waitForCommandsToFinish();

            //correct the angle
            robot.getDriveTrain().alignToTileAngle(PARALLEL_TO_TILE_EDGE,.3);
            robot.waitForCommandsToFinish();

            //move out of the path of the TSE
            if(barCodePosition == BarCodePositionDetector.BarCodePosition.LEFT) {
                robot.getDriveTrain().moveDistanceFromTileEdge(inches(-7.75), .3);
            }else {
                robot.getDriveTrain().moveDistanceFromTileEdge(inches(-7),.3);
            }
            robot.waitForCommandsToFinish();


            //move to the warehouse
            robot.getDriveTrain().moveForward(-5,1);
            robot.waitForCommandsToFinish();
        }else if(getStartPosition()== StartPosition.RED_WAREHOUSE){
            robot.getDriveTrain().rotate(43 * fieldSide,0.6);
            robot.getArm().moveToPosition(Arm.Position.TRAVEL);
            robot.waitForCommandsToFinish();

            //correct the angle
            robot.getDriveTrain().alignToTileAngle(PARALLEL_TO_TILE_EDGE,.3);
            robot.waitForCommandsToFinish();

            //move out of the path of the TSE
            robot.getDriveTrain().moveDistanceFromTileEdge(inches(-6),.3);
            robot.waitForCommandsToFinish();

            //move to the warehouse
            robot.getDriveTrain().moveForward(5,1);
            robot.waitForCommandsToFinish();
        }



        robot.getArm().moveToPosition(Arm.Position.INTAKE);
        robot.waitForCommandsToFinish();

    }
}
