package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.IntakeSweeper;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeSweeper.IntakeSweeperController;
import org.firstinspires.ftc.teamcode.hardwareControl.sensors.gamepad.GamepadDelta;

/**
 * ============================================================
 *  TEST STEP: SWYFT wheel buttons (gamepad 1)
 * ============================================================
 *   A -> SpinIntakeSweeper
 *   B -> StopIntakeSweeper
 *
 * Same idea as TestIntakeButtons: a button that was JUST pressed starts a
 * fresh copy of its action function (they latch, so each press needs a new one).
 * Both of these finish in one tick, so there's nothing to keep running.
 *
 * Always answers SUCCESS.
 */
public class TestIntakeSweeperButtons implements ActionFunction {

    private final Telemetry telemetry;
    private final IntakeSweeperController intakeSweeperController;

    private String lastPressed = "none";   // for telemetry

    public TestIntakeSweeperButtons(Telemetry telemetry, IntakeSweeperController intakeSweeperController) {
        this.telemetry = telemetry;
        this.intakeSweeperController = intakeSweeperController;
    }

    @Override
    public Status perform(BlackBoard blackBoard) {
        // What changed on gamepad 1 since last tick. It's null on the very first tick.
        GamepadDelta delta = (GamepadDelta) blackBoard.getValue("gamepad1Delta");

        if (delta != null) {
            if (delta.aPressed) {
                new SpinIntakeSweeper(telemetry, intakeSweeperController).perform(blackBoard);
                lastPressed = "A: SpinIntakeSweeper";
            }
            if (delta.bPressed) {
                new StopIntakeSweeper(telemetry, intakeSweeperController).perform(blackBoard);
                lastPressed = "B: StopIntakeSweeper";
            }
        }

        telemetry.addData("Buttons", "A spin | B stop");
        telemetry.addData("Last pressed", lastPressed);
        telemetry.addData("Spinning", intakeSweeperController.isSpinning());

        return Status.SUCCESS;
    }
}