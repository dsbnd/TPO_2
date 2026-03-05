package org.example.level0;

public class SinFunction implements MathFunction {
    @Override
    public double calculate(double x, double precision) {
        // Приводим x к периоду [-PI, PI] для точности вычислений
        x = x % (2 * Math.PI);
        if (x < -Math.PI) x += 2 * Math.PI;
        else if (x > Math.PI) x -= 2 * Math.PI;

        double term = x;
        double sum = term;
        int n = 1;

        // Считаем, пока очередной член ряда не станет меньше нашей погрешности
        while (Math.abs(term) > precision) {
            term = -term * x * x / ((2 * n) * (2 * n + 1));
            sum += term;
            n++;
        }
        return sum;
    }
}