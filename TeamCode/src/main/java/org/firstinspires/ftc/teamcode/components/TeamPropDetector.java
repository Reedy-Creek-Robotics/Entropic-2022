package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.teamcode.components.ColorDetector.ColorDetection;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Rectangle;

import java.util.List;

public class TeamPropDetector extends BaseComponent {

    public enum TeamPropPosition {
        LEFT(new Rectangle(new Position(100, 100), 75, 75)),
        MIDDLE(new Rectangle(new Position(300, 100), 75, 75)),
        RIGHT(new Rectangle(new Position(500, 100), 75, 75));

        public Rectangle rectangle;

        TeamPropPosition(Rectangle rectangle) {
            this.rectangle = rectangle;
        }
    }

    /**
     * The color detector to use to find the team prop.
     */
    private ColorDetector colorDetector;

    public TeamPropDetector(RobotContext context, WebCam webCam) {
        super(context);

        this.colorDetector = new ColorDetector(context, webCam);
        addSubComponents(colorDetector);
    }

    public void activate() {
        colorDetector.activate();
    }

    public boolean isActive() {
        return colorDetector.isActive();
    }

    public void deactivate() {
        colorDetector.deactivate();
    }

    public TeamPropPosition waitForDetection(double timeOutInSeconds) {
        if (!isActive()) activate();

        // todo: implement!
        return null;
    }

    public TeamPropPosition getDetectedPosition() {
        // Get the latest detections from the color detector
        List<ColorDetection> detections = this.colorDetector.getDetections();

        return null;
    }

}
