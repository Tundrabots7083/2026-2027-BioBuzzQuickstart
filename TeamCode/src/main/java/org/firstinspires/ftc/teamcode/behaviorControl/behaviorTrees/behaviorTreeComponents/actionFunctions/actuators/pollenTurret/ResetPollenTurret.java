package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.actionFunctions.actuators.pollenTurret;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.ActionFunction;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.pollenTurret.PollenTurretController;

public class ResetPollenTurret implements ActionFunction {

    Telemetry telemetry;
    PollenTurretController turretController;
    Status lastStatus;

    public ResetPollenTurret(Telemetry telemetry, PollenTurretController turretController) {
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