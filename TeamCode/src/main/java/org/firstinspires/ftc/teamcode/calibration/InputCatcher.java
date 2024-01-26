package org.firstinspires.ftc.teamcode.calibration;

import org.firstinspires.ftc.teamcode.game.Controller;

public class InputCatcher {
    double caughtValue;

    Controller controller;

    Controller.Button button;

    public InputCatcher(Controller controller,Controller.Button button) {
        this.button = button;
        this.controller = controller;
    }

    public void watch(double watchValue){



    }

    public double getCaughtValue() {
        return caughtValue;
    }
}
