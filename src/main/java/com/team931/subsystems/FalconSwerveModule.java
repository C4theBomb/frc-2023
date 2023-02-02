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
    private Rotation2d encoderZero;

    private Rotation2d offset;

    /**
     * Initializes the swerve module with default settings and binds motors.
     *
     * @param driveId     CAN ID of the drive motor.
     * @param turnId      CAN ID of the turn motor.
     * @param encoderId   DIO ID of the absolute encoder.
     * @param encoderZero The encoder value that the
     */
    public FalconSwerveModule(int driveId, int turnId, int encoderId, float encoderZero) {
        this.driveMotor = new TalonFX(driveId);
        this.turnMotor = new TalonFX(turnId);
        this.absEncoder = new DutyCycleEncoder(encoderId);
        this.encoderZero = new Rotation2d(encoderZero);

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

    /**
     * Set drive motor neutral mode to coast.
     */
    public void setNeutralModeCoast() {
        driveMotor.setNeutralMode(NeutralMode.Coast);
    }

    /**
     * Set drive motor neutral mode to brake.
     */
    public void setNeutralModeBrake() {
        driveMotor.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Offsets the steering encoder so that the current position becomes the new
     * front.
     */
    // public void rezeroSteeringMotor() {
    // offset = Rotation2d.fromRadians(turnMotor.getSelectedSensorPosition())
    // .rotateBy(getAdjustedRotation().unaryMinus());
    // }

    /**
     * Get the current state of the swerve module.
     * 
     * @return An object that contains the velocity and rotation of the swerve
     *         module.
     */
    public SwerveModuleState getState() {
        return new SwerveModuleState(getDriveVelocity(), getModuleAngle());
    }

    /**
     * Get the current rotation of the swerve module.
     * 
     * @return An object that contains the encoder value of the absolute encoder.
     */
    public Rotation2d getAbsEncoderAngle() {
        return Rotation2d.fromDegrees(absEncoder.getAbsolutePosition());
    }

    /**
     * Get the current rotation of the swerve module adjusted by the offset.
     * 
     * @return A rotation object that contains the adjusted rotation of the swerve
     *         module.
     */
    public Rotation2d getAdjustedAbsEncoderAngle() {
        return getAbsEncoderAngle().rotateBy(encoderZero.unaryMinus());
    }

    /**
     * Get the total distance that the drive motor has driven
     * 
     * @return A double of how many centimeters the bot has driven
     */
    public double getDriveDistance() {
        return driveMotor.getSelectedSensorPosition() * Constants.DriveTrain.driveEncoderTranslationCoefficient;
    }

    /**
     * Get the current velocity of the swerve module
     * 
     * @return A double of the current velocity of the drive motor encoder
     */
    public double getDriveVelocity() {
        return driveMotor.getSelectedSensorVelocity() * Constants.DriveTrain.driveVelocityTranslationCoefficient;
    }

    /**
     * Get the current angle of the swerve module
     * 
     * @return A rotation object of the current angle of the drive motor encoder in
     *         radians
     */
    public Rotation2d getModuleAngle() {
        return Rotation2d.fromRadians(
                turnMotor.getSelectedSensorPosition() * Constants.DriveTrain.turnEncoderTranslationCoefficient
                        - offset.getRadians());
    }

    /**
     * Get the current position of the swerve module,
     * 
     * @return A positional object containing how much the drive train has moved,
     *         and its rotation
     */
    public SwerveModulePosition getPosition() {
        return new SwerveModulePosition(
                driveMotor.getSelectedSensorPosition(), getModuleAngle());
    }

    /**
     * Get the current position of the swerve module,
     * 
     * @param desiredState The goal state of the swerve module.
     */
    public void setDesiredState(SwerveModuleState desiredState) {
        // Optimize the reference state to avoid spinning further than 90 degrees
        SwerveModuleState state = SwerveModuleState.optimize(desiredState, getModuleAngle());

        driveMotor.set(TalonFXControlMode.Velocity,
                state.speedMetersPerSecond / Constants.DriveTrain.driveVelocityTranslationCoefficient);
        turnMotor.set(TalonFXControlMode.Position,
                (state.angle.getRadians() + offset.getRadians())
                        / Constants.DriveTrain.turnEncoderTranslationCoefficient);
    }

    /**
     * Completely stop all swerve module activity
     */
    public void stop() {
        driveMotor.set(TalonFXControlMode.PercentOutput, 0.0);
        turnMotor.set(TalonFXControlMode.PercentOutput, 0.0);
    }
}
