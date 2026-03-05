package org.example.level2;

import org.example.level0.MathFunction;

public class TanFunction implements MathFunction {
    private final MathFunction sinFunction;
    private final MathFunction cosFunction;

    public TanFunction(MathFunction sinFunction, MathFunction cosFunction) {
        this.sinFunction = sinFunction;
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double precision) {
        double cosVal = cosFunction.calculate(x, precision);
        if (Math.abs(cosVal) < precision) {
            throw new ArithmeticException("Division by zero in TanFunction");
        }
        return sinFunction.calculate(x, precision) / cosVal;
    }
}