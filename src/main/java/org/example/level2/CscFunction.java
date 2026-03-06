package org.example.level2;

import org.example.level0.MathFunction;

public class CscFunction implements MathFunction {
    private final MathFunction sinFunction;

    public CscFunction(MathFunction sinFunction) {
        this.sinFunction = sinFunction;
    }

    @Override
    public double calculate(double x, double precision) {
        double sinVal = sinFunction.calculate(x, precision);
        if (Math.abs(sinVal) < precision) {
            throw new ArithmeticException("Деление на ноль в функции CscFunction");
        }
        return 1.0 / sinVal;
    }
}