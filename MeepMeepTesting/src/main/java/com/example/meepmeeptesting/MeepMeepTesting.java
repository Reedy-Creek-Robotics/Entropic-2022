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
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(12, 60, Math.toRadians(180)))
                                /*.forward(24)
                                .turn(Math.toRadians(0))
                                .forward(3)
                                .addDisplacementMarker(()->{

                                })
                                .lineTo(new Vector2d(24,57))
                                .lineToSplineHeading(new Pose2d(48,54,Math.toRadians(180)))*/
                                .lineTo(new Vector2d(48,48))
                                .lineTo(new Vector2d(48,36))
                                .lineTo(new Vector2d(48,60))
                                .build()
                );

        RoadRunnerBotEntity blueStack = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setDimensions(18,18)
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(-36, 60, Math.toRadians(180)))
                                .lineTo(new Vector2d(-36,12))
                                .lineTo(new Vector2d(48,12))
                                .lineTo(new Vector2d(48,36))
                                .lineTo(new Vector2d(48,12))
                                /*.forward(24)
                                .turn(Math.toRadians(75))
                                .forward(3)
                                .addDisplacementMarker(()->{

                                })
                                .splineToLinearHeading(new Pose2d(-58,48,Math.toRadians(0)),Math.toRadians(180))
                                .lineTo(new Vector2d(-58,12))
                                .lineTo(new Vector2d(24,12))*/
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
                                .addDisplacementMarker(()-> {
                                    //score purple
                                })
                                .lineToSplineHeading(new Pose2d(48, -36, Math.toRadians(180)))
                                .addDisplacementMarker(()-> {
                                    //score yellow
                                })
                                .lineTo(new Vector2d(48,-56))
                                //.splineToConstantHeading(new Vector2d(0, -60), Math.toRadians(180))
                                //.lineTo(new Vector2d(-24, -60))
                                //.lineToSplineHeading(new Pose2d(-52, -48, Math.toRadians(225)))
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
                                .turn(Math.toRadians(-75))
                                .forward(3)
                                .addDisplacementMarker(()->{

                                })
                                .build()
                );

        meepMeep.setBackground(MeepMeep.Background.FIELD_CENTERSTAGE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(blueStage)
                .addEntity(blueStack)
                //.addEntity(redStack)
                //.addEntity(redStage)
                .addEntity(test)
                .start();
    }
}