package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.ScalingUtil;

import java.util.Map;

import java.util.HashMap;

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
        CROSS,
        CIRCLE,
        TRIANGLE,
        SQUARE,
        GUIDE,
        OPTIONS,
        START,
        BACK,
        LEFT_BUMPER,
        RIGHT_BUMPER;

        private static Map<Button, Button> ALIASES = new HashMap<>();
        static {
            ALIASES.put(A, CROSS);
            ALIASES.put(B, CIRCLE);
            ALIASES.put(X, SQUARE);
            ALIASES.put(Y, TRIANGLE);

            for (Map.Entry<Button, Button> entry : new HashMap<>(ALIASES).entrySet()) {
                ALIASES.put(entry.getValue(), entry.getKey());
            }
        }

        private Button getAlias() {
            return ALIASES.get(this);
        }
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
     * Scaling exponents for analog sticks and triggers.
     */
    private double leftStickExponent = 2.0;
    private double rightStickExponent = 2.0;
    private double leftTriggerExponent = 1.0;
    private double rightTriggerExponent = 1.0;

    /**
     * The time since the last registered button press (used for debouncing).
     */
    private Map<Button, ElapsedTime> lastButtonPress = new HashMap<>();

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
        return analogValue(gamepad.left_stick_x, invertLeftStickX, leftStickExponent);
    }

    public double leftStickY() {
        return analogValue(gamepad.left_stick_y, invertLeftStickY, leftStickExponent);
    }

    public double rightStickX() {
        return analogValue(gamepad.right_stick_x, invertRightStickX, rightStickExponent);
    }

    public double rightStickY() {
        return analogValue(gamepad.right_stick_y, invertRightStickY, rightStickExponent);
    }

    public double leftTrigger() {
        return analogValue(gamepad.left_trigger, false, leftTriggerExponent);
    }

    public double rightTrigger() {
        return analogValue(gamepad.right_trigger, false, rightTriggerExponent);
    }

    private double analogValue(double value, boolean invert, double exponent) {
        if (Math.abs(value) < deadZone) {
            return 0.0;
        } else {
            double sign = Math.signum(value);
            value = ScalingUtil.scaleParabolic(Math.abs(value), exponent, deadZone, 1, 0, 1.0);
            return sign * (invert ? -value : value);
        }
    }

    public boolean isPressed(Button button) {
        ElapsedTime lastButtonPress = lastButtonPress(button);
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
        return isButtonDownInternal(button) ||
                isButtonDownInternal(button.getAlias());
    }

    private boolean isButtonDownInternal(Button button) {
        if (button == null) return false;

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
            case CROSS:
                return gamepad.cross;
            case CIRCLE:
                return gamepad.circle;
            case TRIANGLE:
                return gamepad.triangle;
            case SQUARE:
                return gamepad.square;
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

    private ElapsedTime lastButtonPress(Button button) {
        ElapsedTime time = lastButtonPress.get(button);
        if (time == null) {
            time = new ElapsedTime(0L);
            lastButtonPress.put(button, time);
        }
        return time;
    }

    public static boolean nonZero(double... values) {
        for (double value : values) {
            if (value != 0.0) {
                return true;
            }
        }
        return false;
    }

}
