package org.example;

import org.example.level0.MathFunction;

public class SystemOfFunctions implements MathFunction {
    private final MathFunction leftBranch;
    private final MathFunction rightBranch;

    public SystemOfFunctions(MathFunction leftBranch, MathFunction rightBranch) {
        this.leftBranch = leftBranch;
        this.rightBranch = rightBranch;
    }

    @Override
    public double calculate(double x, double precision) {
        if (x <= 0) {
            return leftBranch.calculate(x, precision);
        } else {
            return rightBranch.calculate(x, precision);
        }
    }
}