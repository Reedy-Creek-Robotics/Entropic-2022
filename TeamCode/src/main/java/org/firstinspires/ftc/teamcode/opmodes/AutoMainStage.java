package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
@Autonomous
public abstract class AutoMainStage extends AutoMain {

    @Override
    protected void runAuto() {
        /*TrajectorySequence trajectory1 = robot.getDriveTrain().roadrunner.trajectorySequenceBuilder(getStartPosition())
                .lineTo(new Vector2d(36,78))//deliver yellow
                .addDisplacementMarker(()->{

                })
                .splineToSplineHeading(new Pose2d(48,96),180)//backout of spikes
                .splineTo(new Vector2d(36,120),180)//deliver purple
                .addDisplacementMarker(()->{

                })
                .lineTo(new Vector2d(60,96))//line up to cross bridge
                .lineTo(new Vector2d(60,36))//cross bridge
                .lineTo()//diagonol to stack
                .addDisplacementMarker(()->{

                })
                .lineTo()//diagonal backout from stack
                .lineTo()//cross bridge
                .lineTo()//line up to backboard
                .addDisplacementMarker(()->{

                });*/
    }

}
