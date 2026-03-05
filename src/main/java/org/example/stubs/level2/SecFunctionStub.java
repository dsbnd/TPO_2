package org.example.stubs.level2;

import org.example.level0.MathFunction;
import java.util.HashMap;
import java.util.Map;

public class SecFunctionStub implements MathFunction {
    private final Map<Double, Double> values = new HashMap<>();

    public SecFunctionStub() {
        values.put(-2.0, -2.40302);
        values.put(-1.5, 14.1368);
        values.put(-1.0, 1.85082);
        values.put(-0.5, 1.13949);
        values.put(0.0, 1.0);
    }

    @Override
    public double calculate(double x, double precision) {
        // Проверка на точки, где cos(x) = 0 (например, x = ±π/2 ≈ ±1.5708)
        if (Math.abs(Math.abs(x) - Math.PI/2) < 0.001) {
            throw new ArithmeticException("Division by zero in SecFunction");
        }
        Double value = values.get(x);
        if (value == null) {
            throw new IllegalArgumentException("No stub value for sec(" + x + ")");
        }
        return value;
    }
}