package com.team931.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.math.geometry.Rotation2d;

import com.team931.Constants;

public class FalconSwerveModule {
    private final TalonFX driveMotor;
    private final TalonFX turnMotor;

    public FalconSwerveModule(int driveId, int turnId) {
        driveMotor = new TalonFX(driveId);
        turnMotor = new TalonFX(turnId);
    }
}
