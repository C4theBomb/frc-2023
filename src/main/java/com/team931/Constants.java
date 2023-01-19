package com.team931;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.TrapezoidProfile;

public class Constants {
    // Drive Train Constants
    public static class DriveTrain {
        public static final double driveTrainWheelDiameter = 0.10033 * 81.0 / 84.213; // TODO measure
        public static final double driveTrainTrackWidth = 0.61595; // TODO measure
        public static final double driveTrainTrackLength = 0.61595; // TODO measure
        public static final double gearRatio = 6.55;
        public static final double encoderResolution = 0; // TODO measure
        public static final double maxSpeed = 0; // TODO measure
        public static final double maxAngularSpeed = Math.PI; // TODO measure

        public static final SwerveDriveKinematics driveTrainKinematics = new SwerveDriveKinematics(
                // Front left
                new Translation2d(Constants.DriveTrain.driveTrainTrackWidth / 2.0,
                        Constants.DriveTrain.driveTrainTrackLength / 2.0),
                // Front right
                new Translation2d(Constants.DriveTrain.driveTrainTrackWidth / 2.0,
                        -Constants.DriveTrain.driveTrainTrackLength / 2.0),
                // Back left
                new Translation2d(-Constants.DriveTrain.driveTrainTrackWidth / 2.0,
                        Constants.DriveTrain.driveTrainTrackLength / 2.0),
                // Back right
                new Translation2d(-Constants.DriveTrain.driveTrainTrackWidth / 2.0,
                        -Constants.DriveTrain.driveTrainTrackLength / 2.0));

        // Gains are for example purposes only - must be determined for your own robot!
        public static final PIDController drivePIDController = new PIDController(1, 0, 0);

        // Gains are for example purposes only - must be determined for your own robot!
        public static final ProfiledPIDController turningPIDController = new ProfiledPIDController(
                1,
                0,
                0,
                new TrapezoidProfile.Constraints(
                        maxSpeed, maxAngularSpeed));

        // Gains are for example purposes only - must be determined for your own robot!
        public static final SimpleMotorFeedforward driveFeedforward = new SimpleMotorFeedforward(1, 3);
        public static final SimpleMotorFeedforward turnFeedforward = new SimpleMotorFeedforward(1, 0.5);
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
