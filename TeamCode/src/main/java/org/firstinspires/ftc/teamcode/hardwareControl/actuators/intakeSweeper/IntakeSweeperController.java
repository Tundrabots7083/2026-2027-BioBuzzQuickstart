package org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeSweeper;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardwareConfig.actuators.intakeSweeper.IntakeSweeperConstants;

public class IntakeSweeperController {
    private CRServo intakeSweeperServo;
    private IntakeSweeperConstants intakeSweeperConstants;
    private Telemetry telemetry;

    private boolean initialized = false;
    private boolean spinning = false;

    private static final IntakeSweeperController INSTANCE = new IntakeSweeperController();

    //private constructor to prevent instantiation
    private IntakeSweeperController() {

    }

    public static IntakeSweeperController getInstance() {
        return INSTANCE;
    }

    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        if (initialized) {
            return;
        }
        this.telemetry = telemetry;
        this.intakeSweeperConstants = new IntakeSweeperConstants();

        intakeSweeperServo = hardwareMap.get(CRServo.class, intakeSweeperConstants.name);
        intakeSweeperServo.setPower(intakeSweeperConstants.stopPower);

        spinning = false;
        initialized = true;
    }

// Stops the wheel, forgets the hardware, so next OpMode has to initialize again.
    public void reset() {
        if (!initialized) {
            return;
        }
        intakeSweeperServo.setPower(intakeSweeperConstants.stopPower);
        spinning = false;
        initialized = false;
    }

    public void spin(){
        intakeSweeperServo.setPower(intakeSweeperConstants.spinPower);
        spinning = true;
    }

    public void stop(){
        intakeSweeperServo.setPower(intakeSweeperConstants.stopPower);
        spinning = false;
    }

    public boolean isSpinning(){
        return spinning;
    }

    public void update(){

    }


}









//Below is last year's code in full, commented out.
//New code uses a continuous servo with simple functions.
//Wildly different than the code below, which was a turret controller; a positional servo.
//So, feel free to delete whenever. I wanted to keep it before this class was double-checked.

/*
@Configurable
public class IntakeSweeperController {

    private ServoImplEx intakeSweeperServo;

    private IntakeSweeperConstants intakeSweeperConstants;

    private double INTAKE_SWEEPER_TARGET_POSITION;
    //private double lastTargetPosition = 0.0;
    private double MAX_DEGREES;
    private double MIN_DEGREES;
    private double INTAKE_SWEEPER_X_OFFSET;
    private double INTAKE_SWEEPER_Y_OFFSET;
    private double RESET_POSITION;

    private double servoPosition = 0.0;

    private boolean initialized = false;

    // Singleton instance
    private static final IntakeSweeperController INSTANCE = new IntakeSweeperController();
    private Telemetry telemetry;

    private IntakeSweeperController() {}

    public static IntakeSweeperController getInstance() {
        return INSTANCE;
    }


    public void initialize(HardwareMap hardwareMap, Telemetry telemetry) {
        if (initialized) return;

        setupConstants();
        this.telemetry = telemetry;
        initializeServo(hardwareMap);
        initialized = true;
    }

    private void setupConstants() {
        intakeSweeperConstants = new IntakeSweeperConstants();

        MAX_DEGREES = intakeSweeperConstants.maxDegrees;
        MIN_DEGREES = intakeSweeperConstants.minDegrees;

        INTAKE_SWEEPER_X_OFFSET = intakeSweeperConstants.turretXOffset;
        INTAKE_SWEEPER_Y_OFFSET = intakeSweeperConstants.turretYOffset;

        RESET_POSITION = intakeSweeperConstants.resetPosition;
    }

    private void initializeServo(HardwareMap hardwareMap) {
        intakeSweeperServo = hardwareMap.get(ServoImplEx.class, intakeSweeperConstants.name);

        intakeSweeperServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
    }

    public void setTurretTargetAngle(double intakeSweeperTargetAngle) {

        intakeSweeperTargetAngle = intakeSweeperTargetAngle + 93;

        intakeSweeperTargetAngle = intakeSweeperTargetAngle % 360;

        if (intakeSweeperTargetAngle < 0) {
            intakeSweeperTargetAngle += 360;
        }

        intakeSweeperTargetAngle = Range.clip(intakeSweeperTargetAngle, MIN_DEGREES, MAX_DEGREES);

        INTAKE_SWEEPER_TARGET_POSITION = intakeSweeperTargetAngle;

        servoPosition = intakeSweeperTargetAngle / 339.267;

        intakeSweeperServo.setPosition(servoPosition);
//        telemetry.addData("Servo Target", servoPosition);
        telemetry.addData("Intake Sweeper Target", this.INTAKE_SWEEPER_TARGET_POSITION);
    }

    public void moveToTargetPosition(Pose robotPose, Position targetPosition, double scalarAddition) {
        double turretTargetAngle = this.getTurretAngleDegrees(robotPose, targetPosition, INTAKE_SWEEPER_X_OFFSET, INTAKE_SWEEPER_Y_OFFSET);

        INTAKE_SWEEPER_TARGET_POSITION = -turretTargetAngle;
        setTurretTargetAngle(INTAKE_SWEEPER_TARGET_POSITION + scalarAddition);
    }

    public double getPosition() {
        return INTAKE_SWEEPER_TARGET_POSITION - 122;
    }

    public void update() {}

    public void reset() {
        if (!initialized) return;
        initialized = false;
        setTurretTargetAngle(RESET_POSITION);
    }


    private double getTurretAngleDegrees(
            Pose robotPose,
            Position targetPosition,
            double intakeSweeperXOffset,
            double intakeSweeperYOffset
    ) {
        // --- Target coordinates ---
        double targetX = targetPosition.x;
        double targetY = targetPosition.y;

        // --- Robot pose ---
        double robotX = robotPose.x();
        double robotY = robotPose.y();

        // Yaw in radians
        double robotYawRad = robotPose.heading();

        // --- Rotate inputSweeper offset into field frame ---
        double cos = Math.cos(robotYawRad);
        double sin = Math.sin(robotYawRad);

        double intakeSweeperX = robotX + intakeSweeperXOffset * cos - intakeSweeperYOffset * sin;
        double intakeSweeperY = robotY + intakeSweeperXOffset * sin + intakeSweeperYOffset * cos;

        // --- Vector from inputSweeper to target ---
        double vx = targetX - intakeSweeperX;
        double vy = targetY - intakeSweeperY;

        // --- Absolute angle to target in field frame ---
        double angleToTargetRad = Math.atan2(vy, vx);

        // --- Relative inputSweeper angle ---
        double intakeSweeperAngleRad = angleToTargetRad - robotYawRad;
        double intakeSweeperAngleDeg = Math.toDegrees(intakeSweeperAngleRad);

        return intakeSweeperAngleDeg;
    }
}
*/
