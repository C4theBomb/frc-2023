// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
    private final Joystick m_controller = new Joystick(0);
    private final Drivetrain m_swerve = new Drivetrain();

    // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
    private final SlewRateLimiter m_xspeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter m_yspeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter m_rotLimiter = new SlewRateLimiter(3);

    TalonFX frDrive = new TalonFX(0);
    TalonFX flDrive = new TalonFX(3);
    TalonFX brDrive = new TalonFX(7);
    TalonFX blDrive = new TalonFX(4);
    TalonFX frTurn = new TalonFX(1);
    TalonFX flTurn = new TalonFX(2);
    TalonFX brTurn = new TalonFX(6);
    TalonFX blTurn = new TalonFX(5);

    @Override
    public void autonomousPeriodic() {
        driveWithJoystick(false);
        m_swerve.updateOdometry();
    }

    @Override
    public void teleopPeriodic() {
        driveWithJoystick(true);
    }

    private void driveWithJoystick(boolean fieldRelative) {
        // Get the x speed. We are inverting this because Xbox controllers return
        // negative values when we push forward.
        final var xSpeed = -m_xspeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getX(), 0.02))
                * Drivetrain.kMaxSpeed;

        // Get the y speed or sideways/strafe speed. We are inverting this because
        // we want a positive value when we pull to the left. Xbox controllers
        // return positive values when you pull to the right by default.
        final var ySpeed = -m_yspeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getY(), 0.02))
                * Drivetrain.kMaxSpeed;

        // Get the rate of angular rotation. We are inverting this because we want a
        // positive value when we pull to the left (remember, CCW is positive in
        // mathematics). Xbox controllers return positive values when you pull to
        // the right by default.
        final var rot = -m_rotLimiter.calculate(MathUtil.applyDeadband(m_controller.getZ(), 0.02))
                * Drivetrain.kMaxAngularSpeed;

        frDrive.set(TalonFXControlMode.PercentOutput, xSpeed);
        flDrive.set(TalonFXControlMode.PercentOutput, xSpeed);
        brDrive.set(TalonFXControlMode.PercentOutput, xSpeed);
        blDrive.set(TalonFXControlMode.PercentOutput, xSpeed);
        frTurn.set(TalonFXControlMode.PercentOutput, rot);
        flTurn.set(TalonFXControlMode.PercentOutput, rot);
        brTurn.set(TalonFXControlMode.PercentOutput, rot);
        blTurn.set(TalonFXControlMode.PercentOutput, rot);

        // m_swerve.drive(xSpeed, ySpeed, rot, fieldRelative);
    }
}
