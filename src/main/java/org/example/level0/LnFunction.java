package org.example.level0;

    /**
     Функция 0 уровня натурального логарифма
     */

public class LnFunction implements MathFunction {

    @Override
    public double calculate(double x, double precision) {
        if (Double.isNaN(x) || Double.isInfinite(x)) {
            throw new IllegalArgumentException("x должен быть конечным числом");
        }
        if (x <= 0) {
            throw new IllegalArgumentException("x должен быть > 0");
        }

        if (Math.abs(x - 1) <= 0.1) {
            return calculateNearOne(x, precision);
        }

        else {
            return calculateScaled(x, precision);
        }
    }
        /**
            Вычисление вблизи 1, сходимость быстрая
         */
    private double calculateNearOne(double x, double precision) {
        double y = x - 1;
        double term = y;
        double sum = term;
        int n = 2;

        while (Math.abs(term) > precision) {
            term = -term * y * (n - 1) / n;
            sum += term;
            n++;
        }
        return sum;
    }
        /**
         Вычисление масштабируемого значения, чтобы достигалась погрешность и сходимость
         */
    private double calculateScaled(double x, double precision) {
        int k = 0;
        double scaledX = x;

        while (scaledX > 1.5) {
            scaledX /= Math.E;
            k++;
        }
        while (scaledX < 0.5) {
            scaledX *= Math.E;
            k--;
        }

        double z = (scaledX - 1) / (scaledX + 1);
        double term = z;
        double sum = term;
        int n = 1;

        while (Math.abs(term) > precision / 10) {
            term = term * z * z * (2 * n - 1) / (2 * n + 1);
            sum += term;
            n++;
        }

        // ln(x) = ln(scaledX) + k
        return 2 * sum + k;
    }
}