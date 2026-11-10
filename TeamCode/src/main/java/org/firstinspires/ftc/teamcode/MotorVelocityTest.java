package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Motor Velocity", group = "Test")
public class MotorVelocityTest extends LinearOpMode {


    // REV HD Hex Motor, 6000 RPM, no gearbox
    static final double TICKS_PER_REV = 28.0;
    static final double MAX_RPM = 6000.0;

    // Starting speed and how fast the left stick changes it
    static final double DEFAULT_RPM = 3000.0;
    static final double RPM_PER_SECOND = 1000.0; // Full stick deflection changes speed by this much per second
    static final double STICK_DEADZONE = 0.1;    // Ignore small stick drift

    @Override
    public void runOpMode() {
        // "myMotor" must match the name in the configuration
        DcMotorEx motor = hardwareMap.get(DcMotorEx.class, "myMotor");
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        double targetRpm = DEFAULT_RPM;
        boolean running = false;
        boolean lastBumper = false;

        waitForStart();

        ElapsedTime timer = new ElapsedTime();

        while (opModeIsActive()) {
            double dt = timer.seconds();
            timer.reset();

            // Left stick: up raises the speed, down lowers it
            double stick = -gamepad1.left_stick_y; // Stick up is negative on the gamepad, so flip it
            if (Math.abs(stick) > STICK_DEADZONE) {
                targetRpm += stick * RPM_PER_SECOND * dt;
            }
            targetRpm = Math.max(0.0, Math.min(MAX_RPM, targetRpm));

            // Right bumper: toggle on/off on the press, not while held
            if (gamepad1.right_bumper && !lastBumper) {
                running = !running;
            }
            lastBumper = gamepad1.right_bumper;

            // setVelocity takes ticks per second, so convert from RPM
            double targetTicksPerSec = running ? targetRpm * TICKS_PER_REV / 60.0 : 0.0;
            motor.setVelocity(targetTicksPerSec);

            // Actual speed from the encoder, converted back to RPM.
            // Calculated here, before any telemetry uses it.
            double actualRpm = motor.getVelocity() * 60.0 / TICKS_PER_REV;


            telemetry.addData("Running", running);
            telemetry.addData("Set RPM", targetRpm);
            telemetry.addData("Actual RPM", actualRpm);
            telemetry.update();



        }

        motor.setVelocity(0); // Stop the motor when the OpMode ends
    }
}