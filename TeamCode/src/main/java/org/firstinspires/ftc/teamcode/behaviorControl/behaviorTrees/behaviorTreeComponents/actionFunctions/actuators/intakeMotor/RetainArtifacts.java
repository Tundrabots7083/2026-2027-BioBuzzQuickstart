package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.actionFunctions.actuators.intakeMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;

public class RetainArtifacts implements ActionFunction {

    Telemetry telemetry;
    IntakeMotorController intakeController;
    Status lastStatus;
    Status status;
    private final double RETAIN_VELOCITY = 125;

    public RetainArtifacts(Telemetry telemetry, IntakeMotorController intakeController) {
        this.telemetry = telemetry;
        this.intakeController = intakeController;
    }

    public Status perform(BlackBoard blackBoard) {
        if (lastStatus == Status.SUCCESS) {
            return lastStatus;
        }

        // Activate the intake mechanism
        intakeController.spinToTargetVelocity(RETAIN_VELOCITY);

        if (!intakeController.isOnTarget()) {
            status = Status.RUNNING;
        } else {
            status = Status.SUCCESS;
        }

        lastStatus = status;
        return status;
    }
}
