package org.example.stubs.level2;

import org.example.level0.MathFunction;
import java.util.HashMap;
import java.util.Map;

public class TanFunctionStub implements MathFunction {
    private final Map<Double, Double> values = new HashMap<>();

    public TanFunctionStub() {
        values.put(-2.0, 2.18504);
        values.put(-1.5, -14.1014);
        values.put(-1.0, -1.55741);
        values.put(-0.5, -0.546302);
        values.put(0.0, 0.0);
    }

    @Override
    public double calculate(double x, double precision) {
        // Проверка на точки, где cos(x) = 0 (асимптоты тангенса)
        if (Math.abs(Math.abs(x) - Math.PI/2) < 0.001) {
            throw new ArithmeticException("Division by zero in TanFunction");
        }
        Double value = values.get(x);
        if (value == null) {
            throw new IllegalArgumentException("No stub value for tan(" + x + ")");
        }
        return value;
    }
}