package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;

public class Outtake extends BaseComponent {

    // todo: tune these;
    private static final double LEFT_CLOSE_POSITION = 0.5;
    private static final double LEFT_OPEN_POSITION = 0.6;
    private static final double RIGHT_CLOSE_POSITION = 0.5;
    private static final double RIGHT_OPEN_POSITION = 0.6;

    private Servo leftServo;
    private Servo rightServo;

    private boolean leftOpen = false;
    private boolean rightOpen = false;

    public Outtake(RobotContext context) {
        super(context);

        leftServo = hardwareMap.get(Servo.class, "LeftDoor");
        rightServo = hardwareMap.get(Servo.class, "RightDoor");
    }

    public void toggleRight() {
        if (rightOpen) {
            closeRight();
        } else {
            openRight();
        }
    }

    public void openRight() {
        rightServo.setPosition(RIGHT_OPEN_POSITION);
        rightOpen = true;
    }

    public void closeRight() {
        rightServo.setPosition(RIGHT_CLOSE_POSITION);
        rightOpen = false;
    }

    public void toggleLeft() {
        if (leftOpen) {
            closeLeft();
        } else {
            openLeft();
        }
    }

    public void openLeft() {
        leftServo.setPosition(LEFT_OPEN_POSITION);
        leftOpen = true;
    }

    public void closeLeft() {
        leftServo.setPosition(LEFT_CLOSE_POSITION);
        leftOpen = false;
    }

    public void close() {
        closeLeft();
        closeRight();
    }

}
