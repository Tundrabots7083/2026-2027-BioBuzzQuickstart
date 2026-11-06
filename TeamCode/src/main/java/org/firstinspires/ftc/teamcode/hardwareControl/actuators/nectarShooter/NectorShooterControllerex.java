package org.firstinspires.ftc.teamcode.hardwareControl.actuators.nectarShooter;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.hardwareConfig.actuators.nectarShooter.NectarShooterConstants;
import org.firstinspires.ftc.teamcode.hardwareConfig.actuators.nectarShooter.NectarShooterTBHControllerConstants;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.common.TBHController;

/**
 * One-motor version of NectarShooterController: last season's code with the
 * rear motor removed. Uses the FRONT motor settings from NectarShooterConstants
 * and NectarShooterTBHControllerConstants (frontMotorName, FRONT_Kp, ...).
 * Added alongside the original; the original NectarShooterController is unchanged.
 */
public class NectarShooterController1MOTOR {

    private DcMotorEx shooterMotor;

    NectarShooterConstants shooterConstants;
    NectarShooterTBHControllerConstants tbhConstants;

    private double START_VELOCITY;
    private double TOLERABLE_ERROR;
    private double targetVelocity;


    private double Kp;
    private double Kf_a;
    private double Kf_b;
    private double Kf_c;

    /// motion control
    private TBHController tbhController;

    private double currentVelocity;

    private double lastPower = 0.0;

    ///

    private boolean initialized = false;

    // Private static instance (eager initialization)
    private static final NectarShooterController1MOTOR INSTANCE = new NectarShooterController1MOTOR();
    private Telemetry telemetry;

    // Private constructor to prevent instantiation
    private NectarShooterController1MOTOR() {

    }

    // Public method to access the singleton instance
    public static NectarShooterController1MOTOR getInstance() {
        return INSTANCE;
    }


    // Initialization method — must be called once at the beginning
    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        if (initialized) {
            return;
        }
        this.telemetry = telemetry;
        setupConstants();

        initializeMotor(hardwareMap);
        initializeLocalVariablesWithConstants();
        initializeTBHController();

        initialized = true;
    }

    private void setupConstants() {
        shooterConstants = new NectarShooterConstants();
    }

    private void initializeMotor(HardwareMap hardwareMap){
        shooterMotor = hardwareMap.get(DcMotorEx.class, shooterConstants.frontMotorName);
        MotorConfigurationType motorConfigurationType = shooterMotor.getMotorType().clone();
        motorConfigurationType.setTicksPerRev(shooterConstants.ticksPerRev);
        motorConfigurationType.setGearing(shooterConstants.gearing);
        motorConfigurationType.setAchieveableMaxRPMFraction(shooterConstants.achievableMaxRPMFraction);

        shooterMotor.setMotorType(motorConfigurationType);
        shooterMotor.setMode(shooterConstants.resetMode);
        shooterMotor.setMode(shooterConstants.mode);
        shooterMotor.setDirection(shooterConstants.frontMotorDirection);
        shooterMotor.setPower(0.0);
    }

    private void initializeLocalVariablesWithConstants(){
        tbhConstants = new NectarShooterTBHControllerConstants();

        START_VELOCITY = shooterConstants.startPosition;
        TOLERABLE_ERROR = shooterConstants.tolerableError;

        Kp = tbhConstants.FRONT_Kp;

        Kf_a = tbhConstants.FRONT_Kf_a;
        Kf_b = tbhConstants.FRONT_Kf_b;
        Kf_c = tbhConstants.FRONT_Kf_c;
    }

    private void initializeTBHController(){

        tbhController = new TBHController(Kp, Kf_a, Kf_b, Kf_c, telemetry);
    }


    public void reset() {
        if(initialized) {
            if (!isOnTarget()) {
                this.spinToTargetVelocity(START_VELOCITY);
            } else {
                shooterMotor.setMode(shooterConstants.resetMode);
                shooterMotor.setMode(shooterConstants.mode);
                shooterMotor.setDirection(shooterConstants.frontMotorDirection);
                shooterMotor.setZeroPowerBehavior(shooterConstants.frontMotorZeroPowerBehavior);


                targetVelocity = 0.0;
            }
            initialized = false;
        }
    }


    /**
     * Gets the velocity of the shooter motor
     * @return returns rotations per minute
     */
    public double getCurrentVelocity() {
        return shooterMotor.getVelocity(AngleUnit.DEGREES) / 6;
    }

    private void setPower(double power) {
        shooterMotor.setPower(power);
    }

    public void stop() {
        spinToTargetVelocity(0.0);
    }

    public void spinToTargetVelocity(double newTargetVelocity){


        if (newTargetVelocity == 0.0 && targetVelocity != 0.0) {
            shooterMotor.setPower(0.0);
        }

        targetVelocity = newTargetVelocity;

        if (targetVelocity != 0.0) {
            update();
        }
    }

    public boolean isOnTarget(){
        currentVelocity = getCurrentVelocity();

        return (Math.abs(targetVelocity - currentVelocity) <= TOLERABLE_ERROR);
    }

    public void update(){
        if (targetVelocity == 0.0) {
            lastPower = 0.0;
            return;
        }

        currentVelocity = getCurrentVelocity();

        // Calculate motor tbhPower using TBH
        double tbhPower = tbhController.calculate(targetVelocity, currentVelocity);

        telemetry.addData("[ShooterController] CurrentVelocity", getCurrentVelocity());
        telemetry.addData("[ShooterController] TargetVelocity", targetVelocity);


        //Apply power to the motor if this is the first loop
        //Or if it's substantially different than what the motor is currently running at.
        //Otherwise save time by ignoring small changes in power.
        if ((Math.abs(tbhPower - lastPower) >= 0.008)) {
            // Apply tbhPower to motor
            shooterMotor.setPower(tbhPower);
            lastPower = tbhPower;
        }
        telemetry.addData("[ShooterController] SentPower:", tbhPower);

    }
}
