package org.firstinspires.ftc.teamcode.OpModes.tests;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTree.IntakeSweeperTestBehaviorTree;

//A spin, B stop

@TeleOp(name = "Test Intake Sweeper", group = "test")
public class TestIntakeSweeperOpMode extends LinearOpMode {
    IntakeSweeperTestBehaviorTree behaviorTree = null;

    @Override
    public void runOpMode() {
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }                                   // closes the for loop only

        behaviorTree = new IntakeSweeperTestBehaviorTree(this, telemetry);

        telemetry.addLine("Ready. Press Start, then A = spin, B = stop on gamepad 1.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            behaviorTree.tick();
            telemetry.update();
        }                                   // closes the while loop

        behaviorTree.stopSweeper();
    }                                       // closes runOpMode()
}                                           // closes the class

