package org.firstinspires.ftc.teamcode.opModes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;



@Autonomous(name = "Auto Test", group = "Auto")
public class AutoTestOpMode extends LinearOpMode {

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
        }
    }

    private void initialize(LinearOpMode opMode) {
        telemetry.addLine("Initialias");
        telemetry.update();
    }
}
