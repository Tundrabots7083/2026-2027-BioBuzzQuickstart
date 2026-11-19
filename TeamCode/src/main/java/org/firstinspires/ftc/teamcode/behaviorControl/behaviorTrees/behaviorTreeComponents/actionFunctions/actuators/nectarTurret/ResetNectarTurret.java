package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.actionFunctions.actuators.nectarTurret;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.nectarTurret.NectarTurretController;

public class ResetNectarTurret implements ActionFunction {

    Telemetry telemetry;
    NectarTurretController turretController;
    Status lastStatus;

    public ResetNectarTurret(Telemetry telemetry, NectarTurretController turretController) {
        this.telemetry = telemetry;
        this.turretController = turretController;
    }

    public Status perform(BlackBoard blackBoard) {
        if (lastStatus == Status.SUCCESS) {
            return lastStatus;
        }

        turretController.reset();
        lastStatus = Status.SUCCESS;
        return lastStatus;
    }
}