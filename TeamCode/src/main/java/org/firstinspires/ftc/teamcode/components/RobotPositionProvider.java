package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

public interface RobotPositionProvider {

    Position getPosition();

    Heading getHeading();

    Vector2 getVelocity();
}
