package org.example.level0;

public class LnFunction implements MathFunction {
    @Override
    public double calculate(double x, double precision) {
        if (x <= 0) {
            throw new IllegalArgumentException("x must be > 0");
        }
        
        // Для ускорения сходимости ряда (ряд Меркатора)
        double z = (x - 1) / (x + 1);
        double term = z;
        double sum = term;
        int n = 1;

        while (Math.abs(term) > precision) {
            term = term * z * z * (2 * n - 1) / (2 * n + 1);
            sum += term;
            n++;
        }
        return 2 * sum;
    }
}