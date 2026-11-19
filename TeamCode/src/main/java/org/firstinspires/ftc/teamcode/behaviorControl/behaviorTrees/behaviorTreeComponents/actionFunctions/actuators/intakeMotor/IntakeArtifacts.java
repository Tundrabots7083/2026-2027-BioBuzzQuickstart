package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.actionFunctions.actuators.intakeMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;

public class IntakeArtifacts implements ActionFunction {

    Telemetry telemetry;
    IntakeMotorController intakeController;
    Status status;
    Status lastStatus = Status.FAILURE;
    private double INTAKE_VELOCITY = 300;

    public IntakeArtifacts(Telemetry telemetry, IntakeMotorController intakeController) {
        this.telemetry = telemetry;
        this.intakeController = intakeController;
    }

    public Status perform(BlackBoard blackBoard) {
        if (lastStatus == Status.SUCCESS) {
            return lastStatus;
        }

        intakeController.spinToTargetVelocity(INTAKE_VELOCITY);

        status = Status.SUCCESS;
        lastStatus = status;
        return status;
    }
}
