package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.IntakeSweeper;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeSweeper.IntakeSweeperController;

public class SpinIntakeSweeper implements ActionFunction {
    private final Telemetry telemetry;
    private final IntakeSweeperController intakeSweeperController;
    private Status lastStatus = Status.FAILURE;   // FAILURE = "haven't done it yet"

    public SpinIntakeSweeper(Telemetry telemetry, IntakeSweeperController intakeSweeperController) {
        this.telemetry = telemetry;
        this.intakeSweeperController = intakeSweeperController;
    }

    @Override
    public Status perform(BlackBoard blackBoard) {
        if (lastStatus == Status.SUCCESS) {
            return lastStatus;                    // already done: skip
        }
        intakeSweeperController.spin();
        lastStatus = Status.SUCCESS;
        return lastStatus;
    }
}