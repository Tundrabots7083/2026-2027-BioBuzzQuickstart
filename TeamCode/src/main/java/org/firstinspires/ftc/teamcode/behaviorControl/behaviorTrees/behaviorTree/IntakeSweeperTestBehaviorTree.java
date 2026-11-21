package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTree;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.actuators.IntakeSweeper.TestIntakeSweeperButtons;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.sensors.Gamepad.ComputeGamepad_1_Delta;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.ActionFunctions.sensors.Gamepad.ReadGamepadsSnapshot;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Action;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BehaviorTree;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.BlackBoard;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Node;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Sequence;
import org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general.Status;
import org.firstinspires.ftc.teamcode.hardwareControl.actuators.intakeSweeper.IntakeSweeperController;

import java.util.Arrays;

/**
 * ============================================================
 *  SWYFT WHEEL (INTAKE SWEEPER) TEST TREE (buttons)
 * ============================================================
 * Every tick, in order:
 *   1. ReadGamepadsSnapshot     - freeze both gamepads for this tick
 *   2. ComputeGamepad_1_Delta   - work out which buttons were JUST pressed
 *   3. TestIntakeSweeperButtons - A spins the wheel, B stops it
 *
 * No "Run" step: a continuous servo keeps the power it was given, so nothing
 * needs updating every tick. Runs until you press Stop.
 */
public class IntakeSweeperTestBehaviorTree {
    private BehaviorTree tree;
    private Node root;
    private BlackBoard blackBoard;
    protected Telemetry telemetry;
    protected HardwareMap hardwareMap;
    protected LinearOpMode opMode;

    /// Intake sweeper
    protected IntakeSweeperController intakeSweeperController;
    ///

    // The OpMode builds the tree during INIT. The constructor sets up the hardware.
    public IntakeSweeperTestBehaviorTree(LinearOpMode opMode, Telemetry telemetry) {
        this.hardwareMap = opMode.hardwareMap;
        this.telemetry = telemetry;
        this.opMode = opMode;

        Init();
    }

    private void Init() {
        // Fresh, empty shared memory for this run (reset first, then get a new one)
        BlackBoard.getInstance(telemetry).reset();
        this.blackBoard = BlackBoard.getInstance(telemetry);

        /// Intake sweeper
        // Crashes here if "IntakeSweeper" isn't in the hub config as a
        // Continuous Rotation Servo.
        this.intakeSweeperController = IntakeSweeperController.getInstance();

        this.intakeSweeperController.reset();
        this.intakeSweeperController.initialize(hardwareMap, telemetry);
        /// End Intake sweeper

        telemetry.clearAll();

        // The steps, in order. The Sequence runs all of them every tick.
        this.root = new Sequence(
                Arrays.asList(
                        new Action(new ReadGamepadsSnapshot(telemetry, opMode), telemetry),                          // freeze the gamepads
                        new Action(new ComputeGamepad_1_Delta(), telemetry),                                         // which buttons were just pressed
                        new Action(new TestIntakeSweeperButtons(telemetry, intakeSweeperController), telemetry)      // buttons -> spin / stop
                ), telemetry);

        this.tree = new BehaviorTree(root, blackBoard);
    }

    // The OpMode calls this once per loop.
    public Status tick() {
        // Fresh hub readings for this tick (the OpMode turns on MANUAL bulk caching).
        for (LynxModule hub : hardwareMap.getAll(LynxModule.class)) {
            hub.clearBulkCache();
        }

        // Run the behavior tree once (the buttons step adds the telemetry lines)
        return tree.tick();
    }

    // Lets the OpMode stop the wheel when the test ends.
    public void stopSweeper() {
        intakeSweeperController.stop();
    }
}