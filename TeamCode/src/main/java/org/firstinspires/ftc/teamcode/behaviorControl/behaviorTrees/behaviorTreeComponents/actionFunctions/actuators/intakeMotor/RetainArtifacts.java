package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.intakeMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;
//One change, replaced retain velocity variable initialization with a reference to constants class.

public class RetainArtifacts implements ActionFunction {

    Telemetry telemetry;
    IntakeMotorController intakeController;
    Status lastStatus;
    Status status;

    public RetainArtifacts(Telemetry telemetry, IntakeMotorController intakeController) {
        this.telemetry = telemetry;
        this.intakeController = intakeController;
    }

    public Status perform(BlackBoard blackBoard) {
        if (lastStatus == Status.SUCCESS) {
            return lastStatus;
        }

        // Activate the intake mechanism
        intakeController.spinIdle(); // idleRPM, from IntakeMotorConstants (last year: 125)

        if (!intakeController.isOnTarget()) {
            status = Status.RUNNING;
        } else {
            status = Status.SUCCESS;
        }

        lastStatus = status;
        return status;
    }
}