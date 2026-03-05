package org.example.level1;

import org.example.level0.MathFunction;

public class CosFunction implements MathFunction {
    private final MathFunction sinFunction;

    public CosFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double precision) {
        // cos(x) = sin(x + PI/2)
        return sinFunction.calculate(x + Math.PI / 2, precision);
    }
}