package org.firstinspires.ftc.teamcode.components;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Outtake extends BaseComponent {
// todo: tune these;

    boolean leftOpen = false;
    boolean rightOpen = false;

    public enum OuttakePositions{
        LEFT(0.0,0.7 ),
        RIGHT(0.8,0);

        double open;
        double close;

        OuttakePositions(double open, double close) {
            this.open = open;
            this.close = close;
        }
    }

    public Servo leftServo;
    public Servo rightServo;

    public Outtake(RobotContext context) {
        super(context);

        leftServo = hardwareMap.get(Servo.class, "LeftDoor");
        rightServo = hardwareMap.get(Servo.class, "RightDoor");
    }

    @Override
    public void init() {
        super.init();

        leftServo.setPosition(OuttakePositions.LEFT.close);
        rightServo.setPosition(OuttakePositions.RIGHT.close);
    }

    @Override
    public void update() {
        super.update();
    }


    public void closeAll(){
        rightServo.setPosition(OuttakePositions.RIGHT.close);
        leftServo.setPosition(OuttakePositions.LEFT.close);

    }


    public void toggleRight(){
        if(rightOpen){
            rightServo.setPosition(OuttakePositions.RIGHT.close);
        }else{
            rightServo.setPosition(OuttakePositions.RIGHT.open);
        }

        rightOpen = !rightOpen;
    }

    public void toggleLeft(){
        if(leftOpen){
            leftServo.setPosition(OuttakePositions.LEFT.close);
        }else{
            leftServo.setPosition(OuttakePositions.LEFT.open);
        }

        leftOpen = !leftOpen;
    }

    public void outtakeLeft(){
        executeCommand(new OuttakeLeft(2500));
    }

    public void outtakeRight(){
        executeCommand(new OuttakeRight(2500));
    }

    public void outtakeBoth(){ executeCommand(new OuttakeBoth(2500));}

    public class OuttakeBoth implements Command{
        private ElapsedTime timer = new ElapsedTime();

        private int timeLimit;
        public OuttakeBoth(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        @Override
        public void start() {
            rightServo.setPosition(OuttakePositions.RIGHT.open);
            leftServo.setPosition(OuttakePositions.LEFT.open);
            timer.reset();
        }

        @Override
        public void stop() {
            rightServo.setPosition(OuttakePositions.RIGHT.close);
            leftServo.setPosition(OuttakePositions.LEFT.close);
        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= timeLimit;
        }
    }

    public class OuttakeRight implements Command {

        private ElapsedTime timer = new ElapsedTime();

        private int timeLimit;
        public OuttakeRight(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        @Override
        public void start() {
            rightServo.setPosition(OuttakePositions.RIGHT.open);
            timer.reset();
        }

        @Override
        public void stop() {
            rightServo.setPosition(OuttakePositions.RIGHT.close);
        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= timeLimit;
        }
    }
    public class OuttakeLeft implements Command {

        private ElapsedTime timer = new ElapsedTime();

        private int timeLimit;
        public OuttakeLeft(int timeLimit) {
            this.timeLimit = timeLimit;
        }

        @Override
        public void start() {
            leftServo.setPosition(OuttakePositions.LEFT.open);
            timer.reset();
        }

        @Override
        public void stop() {
            leftServo.setPosition(OuttakePositions.LEFT.close);
        }

        @Override
        public boolean update() {
            return timer.milliseconds() >= timeLimit;
        }
    }

}
