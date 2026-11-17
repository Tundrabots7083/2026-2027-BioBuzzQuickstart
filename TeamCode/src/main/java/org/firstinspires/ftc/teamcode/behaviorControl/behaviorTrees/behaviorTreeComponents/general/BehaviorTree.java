package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general;


public class BehaviorTree {
    private final Node root;
    private final BlackBoard blackBoard;

    public BehaviorTree(Node root, BlackBoard blackBoard) {
        this.root = root;
        this.blackBoard = blackBoard;
    }

    public Status tick() {
        return root.execute(blackBoard);
    }
}
