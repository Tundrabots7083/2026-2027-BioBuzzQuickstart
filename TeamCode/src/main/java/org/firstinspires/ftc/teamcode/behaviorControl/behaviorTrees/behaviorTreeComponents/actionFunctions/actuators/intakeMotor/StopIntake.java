package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.intakeMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;

public class StopIntake implements ActionFunction {

    Telemetry telemetry;
    IntakeMotorController intakeController;
    Status lastStatus = Status.FAILURE;
    Status status;


    public StopIntake(Telemetry telemetry, IntakeMotorController intakeController) {
        this.telemetry = telemetry;
        this.intakeController = intakeController;
    }

    public Status perform(BlackBoard blackBoard) {

        if (lastStatus == Status.SUCCESS) {
            return lastStatus;
        }

        // Stop the intake mechanism
        intakeController.stop();


        status = Status.SUCCESS;

        lastStatus = status;
        return status;
    }
}