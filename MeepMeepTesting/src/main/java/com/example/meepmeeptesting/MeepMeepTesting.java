package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeRedDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        int randomization = 2;


        RoadRunnerBotEntity blueStage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(30, 30, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 60, Math.toRadians(-90)))
                                //to tile center
                                //to spike
                                .forward(24)
                                .turn(Math.toRadians(60))
                                .forward(5)
                                .addDisplacementMarker(()->{

                                })
                                //to stage
                                .back(5)
                                .lineToLinearHeading(new Pose2d(24,54,Math.toRadians(180)))

                                .lineTo(new Vector2d(48,36))
                                //line up to deliver
                                //come back
                                //park
                                .addDisplacementMarker(()->{

                                })
                                .lineTo(new Vector2d(48,60))
                                .build()
                );

        RoadRunnerBotEntity blueStack = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(40, 40, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, 60, Math.toRadians(-90)))
                                .forward(24)
                                .turn(Math.toRadians(-60))
                                .forward(3)
                                .addDisplacementMarker(()->{

                                })
                                .back(3)
                                .lineToLinearHeading(new Pose2d(-36,42,Math.toRadians(-90)))
//                                .lineToLinearHeading(new Pose2d(-56,48,Math.toRadians(180)))
                                .lineTo(new Vector2d(-36,12))
                                .lineTo(new Vector2d(24,12))
                                .lineToLinearHeading(new Pose2d(48,36,Math.toRadians(180)))
                                //.lineTo(new Vector2d(48,12))
                                .build()
                );

        RoadRunnerBotEntity redStack = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, -60, Math.toRadians(-90)))
                                .lineTo(new Vector2d(0,0))
                                .build()
                );

        RoadRunnerBotEntity redStage = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, -60, Math.toRadians(90)))
                                .lineTo(new Vector2d(12, -36))
                                .build()
                );

        RoadRunnerBotEntity test = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeRedDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, -60, Math.toRadians(90)))
                                .forward(24)
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueStage)
                .addEntity(blueStack)
                //.addEntity(redStack)
                //.addEntity(redStage)
                //.addEntity(test)
                .start();
    }
}