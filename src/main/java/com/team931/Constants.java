package com.team931;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;

public class Constants {
    // Drive Train Constants
    public static class DriveTrain {
        public static final double wheelDiameter = 0.1016; // TODO measure
        public static final double trackWidth = 0.61595;
        public static final double trackLength = 0.61595;
        public static final double gearRatio = 6.55;
        public static final double motorRpm = 6380.0 / gearRatio;
        public static final double encoderResolution = 2048.0;

        public static final double distancePerRotation = Math.PI * wheelDiameter;

        public static final double maxAngularVelocity = 1.4 * Math.PI; // TODO measure
        public static final double maxDriveVelocity = ((motorRpm / 60) * distancePerRotation) * 0.80 / 10;

        // Used to translate the encoder values to a true distance value
        public static final double driveEncoderTranslationCoefficient = distancePerRotation
                / encoderResolution;

        // Used to translate the encoder values to a true radian value
        public static final double turnEncoderTranslationCoefficient = 2.0 * Math.PI / encoderResolution;

        // Used to translate from the encoder velocity to m/s
        public static final double driveVelocityTranslationCoefficient = driveEncoderTranslationCoefficient * 10.0;

        public static final SwerveDriveKinematics driveTrainKinematics = new SwerveDriveKinematics(
                // Front left
                new Translation2d(Constants.DriveTrain.trackWidth / 2.0,
                        Constants.DriveTrain.trackLength / 2.0),
                // Front right
                new Translation2d(Constants.DriveTrain.trackWidth / 2.0,
                        -Constants.DriveTrain.trackLength / 2.0),
                // Back left
                new Translation2d(-Constants.DriveTrain.trackWidth / 2.0,
                        Constants.DriveTrain.trackLength / 2.0),
                // Back right
                new Translation2d(-Constants.DriveTrain.trackWidth / 2.0,
                        -Constants.DriveTrain.trackLength / 2.0));
    }

    // CAN Swerve Module Can IDs
    public static int flDriveId = 3;
    public static int flTurnId = 2;
    public static int frDriveId = 0;
    public static int frTurnId = 1;
    public static int blDriveId = 4;
    public static int blTurnId = 5;
    public static int brDriveId = 7;
    public static int brTurnId = 6;

    // DIO Swerve Module Encoder Ports
    public static int flAbsEncoder = 3;
    public static int frAbsEncoder = 2;
    public static int blAbsEncoder = 0;
    public static int brAbsEncoder = 1;
}
