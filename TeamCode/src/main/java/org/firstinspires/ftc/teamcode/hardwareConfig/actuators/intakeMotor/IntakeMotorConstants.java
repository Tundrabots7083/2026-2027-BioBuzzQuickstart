package org.firstinspires.ftc.teamcode.hardwareConfig.actuators.intakeMotor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//if a line is fully commented out, it's not necessary. double-check that fact before deleting it. - Caroline

public class IntakeMotorConstants {
    public String motorName = "IntakeMotor";

    //public String motorConfigurationType = "clone";
    public double ticksPerRev = 103.8; // check against gobuilda motor; needs 1620RPM
    public double achievableMaxRPMFraction = 1.0;
    public double gearing = 1; //check against gear ratio between motor and intake shaft, make sure direct, otherwise change from 1.
    public DcMotor.RunMode mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
    public DcMotor.RunMode resetMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
    public DcMotorSimple.Direction motorDirection = DcMotorSimple.Direction.FORWARD; //flip to REVERSE if running intake pushes balls out.
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;

    public double idleRPM = 200; //NEEDS TUNING - speed when not collecting balls.
    public double intakeRPM = 600; //NEEDS TUNING - speed while collecting balls.
    public double rejectRPM = -200; //NEEDS TUNING

    //public double startPosition = 0;
    //public double targetPosition = 0;
    public double tolerableError = 20; //NEEDS TUNING - how close in RPM counts as 'at speed', for isOnTarget() conditional.

    public double velocityKp = 0.0002; //NEEDS TUNING

    public double velocityKf = 0.0006; //NEEDS TUNING


    //JAM DETECTION - NEW, CHECK OVER
    public double stuckCurrentAmps = 2.5; //NEEDS TUNING - current (amps) that counts as a jam.
    public long stuckTimeMs = 200; //NEEDS TUNING - how long the current must stay that high before its counted as a jam.
    public long unstickDurationMs = 380; //NEEDS TUNING - when stuck, the controller reverses the motor this long to clear it.
    public double unstickReversePower = -0.5; //NEEDS TUNING - power used for the reverse pulse
    public boolean autoUnstick = true; //true is reverse to clear a jam, false is only report it.


}