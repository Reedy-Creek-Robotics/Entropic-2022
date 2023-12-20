package org.firstinspires.ftc.teamcode.components;

import org.firstinspires.ftc.teamcode.geometry.Heading;
import org.firstinspires.ftc.teamcode.geometry.Position;
import org.firstinspires.ftc.teamcode.geometry.Vector2;

import java.util.Arrays;
import java.util.List;

public class PositionAggregator extends BaseComponent implements RobotPositionProvider {

    private List<RobotPositionProvider> providers;
    private List<DeltaPositionProvider> deltaProviders;

    public PositionAggregator(
            RobotContext context,
            List<RobotPositionProvider> providers,
            List<DeltaPositionProvider> deltaProviders
    ) {
        super(context);
        this.providers = providers;
        this.deltaProviders = deltaProviders;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public Heading getHeading() {
        return null;
    }

    @Override
    public Vector2 getVelocity() {
        return null;
    }

}
