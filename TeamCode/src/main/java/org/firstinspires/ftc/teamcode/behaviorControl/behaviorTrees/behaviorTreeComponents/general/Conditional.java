package org.firstinspires.ftc.teamcode.behaviorControl.behaviorTrees.behaviorTreeComponents.general;

public class Conditional extends Node {
    private Condition condition;

    public Conditional(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Status execute(BlackBoard blackBoard) {
        boolean result = condition.check(blackBoard);
        if (result) {
            return Status.SUCCESS;
        } else {
            return Status.FAILURE;
        }

        // return condition.check() ? Status.SUCCESS : Status.FAILURE;
    }
}
