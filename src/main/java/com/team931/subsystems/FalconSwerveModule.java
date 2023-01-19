package com.team931.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.SensorVelocityMeasPeriod;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DutyCycleEncoder;

import com.team931.Constants;

public class FalconSwerveModule {
    private final TalonFX driveMotor;
    private final TalonFX turnMotor;
    private final DutyCycleEncoder absEncoder;

    private Rotation2d offset;

    /**
     * Initializes the swerve module with default settings and binds motors.
     *
     * @param driveId   CAN ID of the drive motor.
     * @param turnId    CAN ID of the turn motor.
     * @param encoderId DIO ID of the absolute encoder.
     */
    public FalconSwerveModule(int driveId, int turnId, int encoderId) {
        driveMotor = new TalonFX(driveId);
        turnMotor = new TalonFX(turnId);
        absEncoder = new DutyCycleEncoder(encoderId);

        driveMotor.enableVoltageCompensation(true);
        driveMotor.setNeutralMode(NeutralMode.Coast);
        driveMotor.setInverted(TalonFXInvertType.Clockwise);
        driveMotor.setSensorPhase(false);
        driveMotor.configVelocityMeasurementPeriod(SensorVelocityMeasPeriod.Period_5Ms, 10);
        driveMotor.configVelocityMeasurementWindow(32, 10);

        turnMotor.enableVoltageCompensation(true);
        turnMotor.setNeutralMode(NeutralMode.Coast);
        turnMotor.setInverted(TalonFXInvertType.Clockwise);
        turnMotor.setSensorPhase(false);
    }

    // Offsets the steering motor so that current position is now 0
    public void rezeroSteeringMotor() {
        offset = Rotation2d.fromRadians(turnMotor.getSelectedSensorPosition())
                .rotateBy(getAdjustedRotation().unaryMinus());
    }

    /**
     * Get the current state of the swerve module.
     * 
     * @return An object that contains the velocity and rotation of the swerve
     *         module.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(
                getDriveVelocity(), getAdjustedRotation());
    }

    /**
     * Get the current rotation of the swerve module
     * 
     * @return An object that contains true rotation of the swerve module.
     */
    public Rotation2d getRotation() {
        return Rotation2d.fromDegrees(absEncoder.getAbsolutePosition());
    }

    /**
     * Get the current rotation of the swerve module adjusted by the offset.
     * 
     * @return A rotation object that contains the adjusted rotation of the swerve
     *         module.
     */
    public Rotation2d getAdjustedRotation() {
        return getRotation().rotateBy(offset.unaryMinus());
    }

    /**
     * Get the current velocity of the swerve module
     * 
     * @return A double of the current velocity of the drive motor encoder
     */
    public double getDriveVelocity() {
        return driveMotor.getSelectedSensorVelocity();
    }

    /**
     * Get the current position of the swerve module,
     * 
     * @return A positional object containing how much the drive train has moved,
     *         and its rotation
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                driveMotor.getSelectedSensorPosition(), getAdjustedRotation());
    }

    /**
     * Get the current position of the swerve module,
     * 
     * @param desiredState The goal state of the swerve module.
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        // Optimize the reference state to avoid spinning further than 90 degrees
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, getAdjustedRotation());

        // Calculate the drive output from the drive PID controller.
        final double driveOutput = Constants.DriveTrain.drivePIDController.calculate(getDriveVelocity(),
                state.speedMetersPerSecond);

        final double driveFeedforward = Constants.DriveTrain.driveFeedforward.calculate(state.speedMetersPerSecond);

        // Calculate the turning motor output from the turning PID controller.
        final double turnOutput = Constants.DriveTrain.turningPIDController.calculate(
                getAdjustedRotation().getRadians(),
                state.angle.getRadians());

        final double turnFeedforward = Constants.DriveTrain.turnFeedforward
                .calculate(Constants.DriveTrain.turningPIDController.getSetpoint().velocity);

        driveMotor.set(TalonFXControlMode.Current, driveOutput + driveFeedforward);
        turnMotor.set(TalonFXControlMode.Current, turnOutput + turnFeedforward);
    }

    /**
     * Completely stop all swerve module activity
     */
    public void stop() {
        driveMotor.set(TalonFXControlMode.PercentOutput, 0.0);
        turnMotor.set(TalonFXControlMode.PercentOutput, 0.0);
    }
}
