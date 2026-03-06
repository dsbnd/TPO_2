package org.example.level0;

public class SinFunction implements MathFunction {
    @Override
    public double calculate(double x, double precision) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x must be a number");
        }

        x = x % (2 * Math.PI);
        if (x < -Math.PI) x += 2 * Math.PI;
        else if (x > Math.PI) x -= 2 * Math.PI;

        double term = x;
        double sum = term;
        int n = 1;

        while (Math.abs(term) > precision) {
            term = -term * x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }
        return sum;
    }
}