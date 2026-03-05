package org.example.level1;

import org.example.level0.MathFunction;

public class LogFunction implements MathFunction {
    private final MathFunction lnFunction;
    private final double base;

    public LogFunction(MathFunction lnFunction, double base) {
        this.lnFunction = lnFunction;
        this.base = base;
    }

    @Override
    public double calculate(double x, double precision) {
        if (base <= 0 || base == 1) {
            throw new IllegalArgumentException("Base must be > 0 and != 1");
        }
        // log_a(x) = ln(x) / ln(a)
        return lnFunction.calculate(x, precision) / lnFunction.calculate(base, precision);
    }
}