package org.example.level1;

import org.example.level0.MathFunction;

public class LogFunction implements MathFunction {
    private final MathFunction lnFunction;
    private final double base;

    public LogFunction(MathFunction lnFunction, double base) {
        if (base <= 0 || base == 1 || Double.isNaN(base) || Double.isInfinite(base)) {
            throw new IllegalArgumentException("Основание должно быть > 0 and != 1");
        }
        this.lnFunction = lnFunction;
        this.base = base;
    }

    @Override
    public double calculate(double x, double precision) {
        validateX(x);
        return calculateInternal(x, precision);
    }

    private void validateX(double x) {
        if (x <= 0 || Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x должно быть > 0");
        }
    }

    protected double calculateInternal(double x, double precision) {
        return lnFunction.calculate(x, precision) / lnFunction.calculate(base, precision);
    }
}