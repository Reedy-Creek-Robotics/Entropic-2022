package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.TelemetryHolder;

/**
 * Wraps a gamepad to provide convenience functions.
 */
public class Controller {

    public enum Button {
        LEFT_STICK_BUTTON,
        RIGHT_STICK_BUTTON,
        DPAD_UP,
        DPAD_DOWN,
        DPAD_LEFT,
        DPAD_RIGHT,
        A,
        B,
        X,
        Y,
        GUIDE,
        OPTIONS,
        START,
        BACK,
        LEFT_BUMPER,
        RIGHT_BUMPER
    }

    private static final double DEFAULT_DEBOUNCE_DELAY = 0.2;

    private static final double DEFAULT_DEAD_ZONE = 0.1;

    /**
     * The underlying gamepad from which input is received.
     */
    private Gamepad gamepad;

    /**
     * The default debounce delay for button presses.
     */
    private double debounceDelay = DEFAULT_DEBOUNCE_DELAY;

    /**
     * The dead zone in the middle of each analog stick or trigger.
     */
    private double deadZone = DEFAULT_DEAD_ZONE;

    /**
     * Whether the left stick's range should be inverted.
     */
    private boolean invertLeftStickX = true;
    private boolean invertLeftStickY = true;

    /**
     * Whether the right stick's range should be inverted.
     */
    private boolean invertRightStickX = true;
    private boolean invertRightStickY = true;

    /**
     * The time since the last registered button press (used for debouncing).
     */
    private ElapsedTime lastButtonPress = new ElapsedTime();


    public Controller(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    public void setDebounceDelay(double debounceDelay) {
        this.debounceDelay = debounceDelay;
    }

    public void setDeadZone(double deadZone) {
        this.deadZone = deadZone;
    }

    public void setInvertLeftStick(boolean x, boolean y) {
        this.invertLeftStickX = x;
        this.invertLeftStickY = y;
    }

    public void setInvertRightStick(boolean x, boolean y) {
        this.invertRightStickX = x;
        this.invertRightStickY = y;
    }

    public double leftStickX() {
        return analogValue(gamepad.left_stick_x, invertLeftStickX);
    }

    public double leftStickY() {
        return analogValue(gamepad.left_stick_y, invertLeftStickY);
    }

    public double rightStickX() {
        return analogValue(gamepad.right_stick_x, invertRightStickX);
    }

    public double rightStickY() {
        return analogValue(gamepad.right_stick_y, invertRightStickY);
    }

    public double leftTrigger() {
        return analogValue(gamepad.left_trigger, false);
    }

    public double rightTrigger() {
        return analogValue(gamepad.right_trigger, false);
    }

    private double analogValue(double value, boolean invert) {
        if (Math.abs(value) < deadZone) {
            return 0.0;
        } else {
            return invert ? -value : value;
        }
    }

    public boolean isPressed(Button button) {
        if (lastButtonPress.seconds() < debounceDelay) {
            return false;
        } else {
            boolean buttonDown = isButtonDown(button);
            if (buttonDown) {
                lastButtonPress.reset();
            }
            return buttonDown;
        }
    }

    public boolean isButtonDown(Button button) {
        switch (button) {
            case LEFT_STICK_BUTTON:
                return gamepad.left_stick_button;
            case RIGHT_STICK_BUTTON:
                return gamepad.right_stick_button;
            case DPAD_UP:
                return gamepad.dpad_up;
            case DPAD_DOWN:
                return gamepad.dpad_down;
            case DPAD_LEFT:
                return gamepad.dpad_left;
            case DPAD_RIGHT:
                return gamepad.dpad_right;
            case A:
                return gamepad.a;
            case B:
                return gamepad.b;
            case X:
                return gamepad.x;
            case Y:
                return gamepad.y;
            case GUIDE:
                return gamepad.guide;
            case START:
                return gamepad.start;
            case OPTIONS:
                return gamepad.options;
            case BACK:
                return gamepad.back;
            case LEFT_BUMPER:
                return gamepad.left_bumper;
            case RIGHT_BUMPER:
                return gamepad.right_bumper;
            default:
                throw new IllegalArgumentException("Unknown button: " + button);
        }
    }

}
