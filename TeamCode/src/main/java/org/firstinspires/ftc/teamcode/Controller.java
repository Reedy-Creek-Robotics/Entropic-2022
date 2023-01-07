package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.util.ScalingUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Wraps a gamepad to provide convenience functions.
 */
public class Controller {

    private static final double DEFAULT_DEBOUNCE_DELAY = 0.2;

    private static final double DEFAULT_DEAD_ZONE = 0.1;

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

        private static final Map<Button, Button> ALIASES = new HashMap<>();

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

    public enum AnalogControl {
        LEFT_STICK_X,
        LEFT_STICK_Y,
        RIGHT_STICK_X,
        RIGHT_STICK_Y,
        LEFT_TRIGGER,
        RIGHT_TRIGGER
    }

    public static class AnalogConfig {

        /**
         * The dead zone for the component as a fraction from 0 - 1.
         */
        public double deadZone = DEFAULT_DEAD_ZONE;

        /**
         * Indicates if the analog control is inverted.
         */
        public boolean invert = false;

        /**
         * The minimum value that should be reported.  Values will be scaled from this number to maxValue.
         */
        public double minValue = 0.0;

        /**
         * The maximum value that should be reported.  Values will be scaled from minValue to this number.
         */
        public double maxValue = 1.0;

        /**
         * For quadratic or parabolic scaling, the exponent which will control the scaling.
         */
        public double scalingExponent = 1.0;

        public AnalogConfig withDeadZone(double deadZone) {
            this.deadZone = deadZone;
            return this;
        }

        public AnalogConfig inverted() {
            this.invert = true;
            return this;
        }

        public AnalogConfig withMinValue(double minValue) {
            this.minValue = minValue;
            return this;
        }

        public AnalogConfig withMaxValue(double maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        public AnalogConfig withScalingExponent(double scalingExponent) {
            this.scalingExponent = scalingExponent;
            return this;
        }

    }

    /**
     * The underlying gamepad from which input is received.
     */
    private Gamepad gamepad;

    /**
     * The default debounce delay for button presses.
     */
    private double debounceDelay = DEFAULT_DEBOUNCE_DELAY;

    /**
     * Configuration for each analog control.
     */
    private Map<AnalogControl, AnalogConfig> analogConfigs = new HashMap<>();
    {
        // Default analog control characteristics
        analogConfigs.put(AnalogControl.LEFT_STICK_X, new AnalogConfig()
                        .inverted()
                        .withScalingExponent(2.0));
        analogConfigs.put(AnalogControl.LEFT_STICK_Y, new AnalogConfig()
                        .inverted()
                        .withScalingExponent(2.0));

        analogConfigs.put(AnalogControl.RIGHT_STICK_X, new AnalogConfig()
                        .inverted()
                        .withScalingExponent(2.0));
        analogConfigs.put(AnalogControl.RIGHT_STICK_Y, new AnalogConfig()
                        .inverted()
                        .withScalingExponent(2.0));

        analogConfigs.put(AnalogControl.LEFT_TRIGGER, new AnalogConfig());
        analogConfigs.put(AnalogControl.RIGHT_TRIGGER, new AnalogConfig());
    }

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

    public AnalogConfig analogConfig(AnalogControl analogControl) {
        return analogConfigs.get(analogControl);
    }

    public double leftStickX() {
        return analogValue(AnalogControl.LEFT_STICK_X);
    }

    public double leftStickY() {
        return analogValue(AnalogControl.LEFT_STICK_Y);
    }

    public double rightStickX() {
        return analogValue(AnalogControl.RIGHT_STICK_X);
    }

    public double rightStickY() {
        return analogValue(AnalogControl.RIGHT_STICK_Y);
    }

    public double leftTrigger() {
        return analogValue(AnalogControl.LEFT_TRIGGER);
    }

    public double rightTrigger() {
        return analogValue(AnalogControl.RIGHT_TRIGGER);
    }

    public double analogValue(AnalogControl analogControl) {
        AnalogConfig analogConfig = analogConfig(analogControl);
        double value = rawAnalogValue(analogControl);

        if (Math.abs(value) < analogConfig.deadZone) {
            return 0.0;
        } else {
            double sign = Math.signum(value);
            value = ScalingUtil.scaleParabolic(
                    Math.abs(value),
                    analogConfig.scalingExponent,
                    analogConfig.deadZone, 1,
                    analogConfig.minValue, analogConfig.maxValue
            );
            if (analogConfig.invert) {
                value = -value;
            }
            return sign * value;
        }
    }

    public boolean isPressed(AnalogControl analogControl) {
        AnalogConfig analogConfig = analogConfig(analogControl);
        double value = rawAnalogValue(analogControl);
        return Math.abs(value) > analogConfig.deadZone;
    }

    public boolean isPressed(AnalogControl... analogControls) {
        for (AnalogControl analogControl : analogControls) {
            if (isPressed(analogControl)) {
                return true;
            }
        }
        return false;
    }

    private double rawAnalogValue(AnalogControl analogControl) {
        switch (analogControl) {
            case LEFT_STICK_X:
                return gamepad.left_stick_x;
            case LEFT_STICK_Y:
                return gamepad.left_stick_y;
            case RIGHT_STICK_X:
                return gamepad.right_stick_x;
            case RIGHT_STICK_Y:
                return gamepad.right_stick_y;
            case LEFT_TRIGGER:
                return gamepad.left_trigger;
            case RIGHT_TRIGGER:
                return gamepad.right_trigger;
            default:
                throw new IllegalArgumentException("Unknown analog control: " + analogControl);
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

}
