package org.example.stubs.level2;

import org.example.level0.MathFunction;
import java.util.HashMap;
import java.util.Map;

public class CscFunctionStub implements MathFunction {
    private final Map<Double, Double> values = new HashMap<>();

    public CscFunctionStub() {
        values.put(-2.0, -1.09975);
        values.put(-1.5, -1.00251);
        values.put(-1.0, -1.1884);
        values.put(-0.5, -2.08583);
        // x = 0.0 вызывает исключение (деление на ноль)
    }

    @Override
    public double calculate(double x, double precision) {
        if (Math.abs(x) < 0.001) { // x = 0
            throw new ArithmeticException("Division by zero in CscFunction");
        }
        Double value = values.get(x);
        if (value == null) {
            throw new IllegalArgumentException("No stub value for csc(" + x + ")");
        }
        return value;
    }
}