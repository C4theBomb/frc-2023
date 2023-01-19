// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team931;

import com.team931.subsystems.Drivetrain;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Robot extends TimedRobot {
    private final Joystick m_controller = new Joystick(0);
    private final Drivetrain joystick = new Drivetrain();

    // Slew rate limiters to make joystick inputs more gentle; 1/3 sec from 0 to 1.
    private final SlewRateLimiter xSpeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter ySpeedLimiter = new SlewRateLimiter(3);
    private final SlewRateLimiter rotLimiter = new SlewRateLimiter(3);

    @Override
    public void robotInit() {
    }

    @Override
    public void autonomousInit() {
    }

    @Override
    public void autonomousPeriodic() {
    }

    @Override
    public void teleopPeriodic() {
        driveWithJoystick(true);
    }

    @Override
    public void testInit() {
    }

    @Override
    public void testPeriodic() {
        driveWithJoystick(false);
        joystick.updateOdometry();
    }

    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
    }

    private void driveWithJoystick(boolean fieldRelative) {
        // Get the x speed. We are inverting this because Xbox controllers return
        // negative values when we push forward.
        final var xSpeed = -xSpeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getX(), 0.02))
                * Constants.DriveTrain.maxSpeed;

        // Get the y speed or sideways/strafe speed. We are inverting this because
        // we want a positive value when we pull to the left. Xbox controllers
        // return positive values when you pull to the right by default.
        final var ySpeed = -ySpeedLimiter.calculate(MathUtil.applyDeadband(m_controller.getY(), 0.02))
                * Constants.DriveTrain.maxSpeed;

        // Get the rate of angular rotation. We are inverting this because we want a
        // positive value when we pull to the left (remember, CCW is positive in
        // mathematics). Xbox controllers return positive values when you pull to
        // the right by default.
        final var rot = -rotLimiter.calculate(MathUtil.applyDeadband(m_controller.getZ(), 0.02))
                * Constants.DriveTrain.maxAngularSpeed;

        joystick.drive(xSpeed, ySpeed, rot, fieldRelative);
    }
}