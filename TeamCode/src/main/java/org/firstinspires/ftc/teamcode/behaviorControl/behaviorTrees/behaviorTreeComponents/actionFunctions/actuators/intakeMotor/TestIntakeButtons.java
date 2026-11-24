package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.intakeMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;
import org.firstinspires.ftc.teamcode.hardwareControl.sensors.gamepad.GamepadDelta;

/**
 * ============================================================
 *  TEST STEP: intake buttons (gamepad 1)
 * ============================================================
 * Each button starts one of the intake's action functions:
 *   A -> IntakeArtifacts  (intake speed)
 *   B -> RetainArtifacts  (idle speed)
 *   X -> RejectArtifacts  (backwards)
 *   Y -> StopIntake
 *
 * Every tick: if a button was JUST pressed, make a fresh copy of its action
 * function. Then keep running that one until it answers SUCCESS.
 *
 * Why a NEW copy on every press: action functions latch. Once one has
 * answered SUCCESS it never does anything again, so pressing A a second
 * time needs a second IntakeArtifacts.
 *
 * This step always answers SUCCESS, so the test tree never stops here.
 */
public class TestIntakeButtons implements ActionFunction {

    private final Telemetry telemetry;
    private final IntakeMotorController intakeMotorController;

    private ActionFunction current = null;   // the step the last button started (null = none yet)
    private String currentName = "none";     // its name, for telemetry
    private Status currentStatus = null;     // its latest answer

    public TestIntakeButtons(Telemetry telemetry, IntakeMotorController intakeMotorController) {
        this.telemetry = telemetry;
        this.intakeMotorController = intakeMotorController;
    }

    @Override
    public Status perform(BlackBoard blackBoard) {
        // What changed on gamepad 1 since last tick. It's null on the very first tick.
        GamepadDelta delta = (GamepadDelta) blackBoard.getValue("gamepad1Delta");

        // A button that was JUST pressed starts a fresh copy of its action function
        if (delta != null) {
            if (delta.aPressed) start(new IntakeArtifacts(telemetry, intakeMotorController), "A: IntakeArtifacts");
            if (delta.bPressed) start(new RetainArtifacts(telemetry, intakeMotorController), "B: RetainArtifacts");
            if (delta.xPressed) start(new RejectArtifacts(telemetry, intakeMotorController), "X: RejectArtifacts");
            if (delta.yPressed) start(new StopIntake(telemetry, intakeMotorController), "Y: StopIntake");
        }

        // Keep running the current one until it says SUCCESS
        // (RetainArtifacts and RejectArtifacts answer RUNNING until they reach their speed)
        if (current != null && currentStatus != Status.SUCCESS) {
            currentStatus = current.perform(blackBoard);
        }

        telemetry.addData("Buttons", "A intake | B idle | X reject | Y stop");
        telemetry.addData("Last pressed", currentName);
        telemetry.addData("Its status", currentStatus == null ? "-" : currentStatus);

        return Status.SUCCESS;
    }

    // Remember which action function is now in charge
    private void start(ActionFunction step, String name) {
        current = step;
        currentName = name;
        currentStatus = null;
    }
}
