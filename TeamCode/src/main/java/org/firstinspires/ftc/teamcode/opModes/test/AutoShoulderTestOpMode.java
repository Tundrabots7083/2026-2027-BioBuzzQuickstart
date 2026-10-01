package org.firstinspires.ftc.teamcode.opModes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;


@Autonomous(name = "Auto Shoulder Test", group = "Auto")
public class AutoShoulderTestOpMode extends LinearOpMode {
    private DcMotorEx testMotor2;


    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("This is a test op mode");
        telemetry.update();
        this.initialize(this);


        telemetry.addLine("Wait for start");
        telemetry.update();

        waitForStart();

        telemetry.addLine("Started...");
        telemetry.update();

        while (opModeIsActive()) {
            telemetry.addLine("running the op mode...");
            telemetry.update();
            testMotor2.setPower(-0.5);
        }
    }

    private void initialize(LinearOpMode opMode) {
        telemetry.addLine("Initialias");
        telemetry.update();

        testMotor2 = hardwareMap.get(DcMotorEx.class, "Shoulder");

    }
}
