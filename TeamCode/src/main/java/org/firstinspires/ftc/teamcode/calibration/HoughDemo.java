package org.firstinspires.ftc.teamcode.calibration;

import static org.firstinspires.ftc.teamcode.Controller.*;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BaseDrivingTeleOp;
import org.firstinspires.ftc.teamcode.components.Robot;
import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.util.DistanceUtil;

@TeleOp
public class HoughDemo extends BaseDrivingTeleOp {
    @Override
    protected Robot.Camera getStreamingCamera() {
        return Robot.Camera.SIDE;
    }

    @Override
    public void loop() {

        if (controller.isPressed(Button.A)) {
            double x = DistanceUtil.inchesToTiles(robot.getRobotContext().robotDescriptor.robotDimensionsInInches.width / 2);
            robot.getDriveTrain().moveToTargetPosition(new Position(x, 0.5), new Heading(90), 0.4);
        }

        applyBasicDriving();
        robot.updateStatus();
    }
}