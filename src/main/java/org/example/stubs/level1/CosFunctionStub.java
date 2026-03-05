package org.example.stubs.level1;

import org.example.level0.MathFunction;
import java.util.HashMap;
import java.util.Map;

public class CosFunctionStub implements MathFunction {
    private final Map<Double, Double> values = new HashMap<>();

    public CosFunctionStub() {
        values.put(-2.0, -0.416147);
        values.put(-1.5, 0.0707372);
        values.put(-1.0, 0.540302);
        values.put(-0.5, 0.877583);
        values.put(0.0, 1.0);
        values.put(0.5, 0.877583);
        values.put(1.0, 0.540302);
        values.put(1.5, 0.0707372);
        values.put(2.0, -0.416147);
    }

    @Override
    public double calculate(double x, double precision) {
        Double value = values.get(x);
        if (value == null) {
            throw new IllegalArgumentException("No stub value for x = " + x);
        }
        return value;
    }
}