package org.example.stubs.level1;

import org.example.level0.MathFunction;
import java.util.HashMap;
import java.util.Map;

public class LogFunctionStub implements MathFunction {
    private final Map<String, Double> values = new HashMap<>();
    private final double base;

    public LogFunctionStub(double base) {
        this.base = base;

        // log2
        if (Math.abs(base - 2.0) < 0.001) {
            values.put("0.5", -1.0);
            values.put("1.0", 0.0);
            values.put("2.0", 1.0);
            values.put("3.0", 1.58496);
            values.put("5.0", 2.32193);
            values.put("10.0", 3.32193);
        }
        // log3
        else if (Math.abs(base - 3.0) < 0.001) {
            values.put("0.5", -0.63093);
            values.put("1.0", 0.0);
            values.put("2.0", 0.63093);
            values.put("3.0", 1.0);
            values.put("5.0", 1.46497);
            values.put("10.0", 2.0959);
        }
        // log5
        else if (Math.abs(base - 5.0) < 0.001) {
            values.put("0.5", -0.430677);
            values.put("1.0", 0.0);
            values.put("2.0", 0.430677);
            values.put("3.0", 0.682606);
            values.put("5.0", 1.0);
            values.put("10.0", 1.43068);
        }
        // log10
        else if (Math.abs(base - 10.0) < 0.001) {
            values.put("0.5", -0.30103);
            values.put("1.0", 0.0);
            values.put("2.0", 0.30103);
            values.put("3.0", 0.477121);
            values.put("5.0", 0.69897);
            values.put("10.0", 1.0);
        }
    }

    @Override
    public double calculate(double x, double precision) {
        if (x <= 0) {
            throw new IllegalArgumentException("x must be > 0");
        }
        String key = String.valueOf(x);
        Double value = values.get(key);
        if (value == null) {
            throw new IllegalArgumentException("No stub value for log_" + base + "(" + x + ")");
        }
        return value;
    }
}