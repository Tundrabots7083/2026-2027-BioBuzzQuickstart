package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTree;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.intakeMotor.RunIntake;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.intakeMotor.TestIntakeButtons;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.sensors.Gamepad.ComputeGamepad_1_Delta;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.sensors.Gamepad.ReadGamepadsSnapshot;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Action;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BehaviorTree;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Node;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Sequence;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeMotor.IntakeMotorController;

import java.util.Arrays;

/**
 * ============================================================
 *  INTAKE MOTOR TEST TREE (buttons)
 * ============================================================
 * Every tick, in order:
 *   1. ReadGamepadsSnapshot   - freeze both gamepads for this tick
 *   2. ComputeGamepad_1_Delta - work out which buttons were JUST pressed
 *   3. TestIntakeButtons      - A/B/X/Y start IntakeArtifacts / RetainArtifacts /
 *                               RejectArtifacts / StopIntake
 *   4. RunIntake              - hold whatever speed was asked for
 *
 * Every step answers SUCCESS, so the whole Sequence runs every tick and the
 * test keeps going until you press Stop.
 */
public class IntakeMotorTestBehaviorTree {
    private BehaviorTree tree;
    private Node root;
    private BlackBoard blackBoard;
    protected Telemetry telemetry;
    protected HardwareMap hardwareMap;
    protected LinearOpMode opMode;

    /// Intake
    protected IntakeMotorController intakeMotorController;
    ///

    // The OpMode builds the tree during INIT. The constructor sets up the hardware.
    public IntakeMotorTestBehaviorTree(LinearOpMode opMode, Telemetry telemetry) {
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = telemetry;
        this.opMode = opMode;

        Init();
    }

    private void Init() {
        // Fresh, empty shared memory for this run. reset() throws away the old
        // blackboard (from the last OpMode), so the getInstance() after it makes
        // a new, empty one.
        BlackBoard.getInstance(telemetry).reset();
        this.blackBoard = BlackBoard.getInstance(telemetry);

        /// Intake
        // Get the one controller, make it forget the last OpMode's hardware,
        // then give it this run's hardware. (Crashes here if "IntakeMotor"
        // is not in the hub config.)
        this.intakeMotorController = IntakeMotorController.getInstance();

        this.intakeMotorController.reset();
        this.intakeMotorController.initialize(hardwareMap, telemetry);
        /// End Intake

        telemetry.clearAll();

        // The steps, in order. The Sequence runs all of them every tick.
        this.root = new Sequence(
                Arrays.asList(
                        new Action(new ReadGamepadsSnapshot(telemetry, opMode), telemetry),                // freeze the gamepads
                        new Action(new ComputeGamepad_1_Delta(), telemetry),                               // which buttons were just pressed
                        new Action(new TestIntakeButtons(telemetry, intakeMotorController), telemetry),    // buttons -> action functions
                        new Action(new RunIntake(telemetry, intakeMotorController), telemetry)             // hold the speed
                ), telemetry);

        this.tree = new BehaviorTree(root, blackBoard);
    }

    // The OpMode calls this once per loop.
    public Status tick() {
        // Fresh hub readings for this tick (the OpMode turns on MANUAL bulk caching).
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.clearBulkCache();
        }

        // Run the behavior tree once
        Status result = tree.tick();

        // What to watch on the Driver Station (the buttons step adds its own lines)
        telemetry.addData("Target RPM", "%.0f", intakeMotorController.getTargetVelocity());
        telemetry.addData("Current RPM", "%.0f", intakeMotorController.getCurrentVelocity());
        telemetry.addData("On target", intakeMotorController.isOnTarget());
        telemetry.addData("Jam detected", intakeMotorController.isStuck());

        return result;
    }

    // Lets the OpMode stop the motor when the test ends.
    public void stopIntake() {
        intakeMotorController.stop();
    }
}
