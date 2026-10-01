package org.firstinspires.ftc.teamcode.hardwareConfig.actuators.test;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class ShoulderConstants {
    public String motorName = "Shoulder";
    public String motorConfigurationType = "clone";
    public double ticksPerRev =134.4;
    public double achievableMaxRPMFraction = 1.0;
    public double gearing = 1;
    public DcMotor.RunMode mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
    public DcMotor.RunMode resetMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
    public DcMotorSimple.Direction motorDirection = DcMotorSimple.Direction.FORWARD;
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
    public double startPosition = 0;
    public double targetPosition = 0;
    public double tolerableError = 20; //in degrees
}