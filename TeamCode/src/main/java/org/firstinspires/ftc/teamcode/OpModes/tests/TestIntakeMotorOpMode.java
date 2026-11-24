package org.firstinspires.ftc.teamcode.OpModes.tests;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTree.IntakeMotorTestBehaviorTree;
import org.firstinspires.ftc.teamcode.hardwareConfig.actuators.intakeMotor.IntakeMotorConstants;

/**
 * ============================================================
 *  TEST: INTAKE MOTOR (buttons, behavior tree)
 * ============================================================
 * Gamepad 1:  A intake speed | B idle speed | X reject (backwards) | Y stop
 *
 *   INIT  -> build the test tree (this sets up the intake motor)
 *   Start -> tick the tree every loop until Stop is pressed
 *   Stop  -> stop the motor
 *
 * The OpMode is the "outside": it owns the loop and the INIT/Start/Stop buttons.
 * The tree is the "inside": it decides what the robot does on each tick.
 *
 * On the Driver Station: TeleOp list, "Test Intake Motor".
 * Keep hands clear of the intake.
 */
@TeleOp(name = "Test Intake Motor", group = "test")
public class TestIntakeMotorOpMode extends LinearOpMode {
    IntakeMotorTestBehaviorTree behaviorTree = null;

    @Override
    public void runOpMode() {
        // ---------- INIT (after the drivers press INIT) ----------

        // Bulk reads: the tree clears the cache once per tick
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        // Building the tree sets up the intake motor
        behaviorTree = new IntakeMotorTestBehaviorTree(this, telemetry);

        // Show the speeds, and warn about any that haven't been set yet
        IntakeMotorConstants constants = new IntakeMotorConstants();
        telemetry.addData("intakeRPM (A)", constants.intakeRPM);
        telemetry.addData("idleRPM (B)", constants.idleRPM);
        telemetry.addData("rejectRPM (X)", constants.rejectRPM);
        if (constants.intakeRPM == 0) telemetry.addLine("WARNING: intakeRPM is 0, so A won't move the motor.");
        if (constants.idleRPM == 0) telemetry.addLine("WARNING: idleRPM is 0, so B will just stop the motor.");
        telemetry.addLine("Ready. Press Start, then use A / B / X / Y on gamepad 1.");
        telemetry.update();

        waitForStart();

        // ---------- RUN (after the drivers press Start) ----------
        // One pass of this loop = one tick of the tree. Every step answers
        // SUCCESS, so there's nothing to check: just tick until Stop.
        while (opModeIsActive()) {
            behaviorTree.tick();
            telemetry.update();   // send this tick's lines to the Driver Station
        }

        // ---------- END (Stop pressed) ----------
        behaviorTree.stopIntake();
    }
}
