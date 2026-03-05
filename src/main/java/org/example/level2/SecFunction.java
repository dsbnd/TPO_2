package org.example.level2;

import org.example.level0.MathFunction;

public class SecFunction implements MathFunction {
    private final MathFunction cosFunction;

    public SecFunction(MathFunction cosFunction) {
        this.cosFunction = cosFunction;
    }

    @Override
    public double calculate(double x, double precision) {
        double cosVal = cosFunction.calculate(x, precision);
        if (Math.abs(cosVal) < precision) {
            throw new ArithmeticException("Division by zero in SecFunction");
        }
        return 1.0 / cosVal;
    }
}