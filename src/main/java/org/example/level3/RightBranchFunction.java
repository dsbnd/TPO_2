package org.example.level3;

import org.example.level0.MathFunction;

public class RightBranchFunction implements MathFunction {
    private final MathFunction ln;
    private final MathFunction log2;
    private final MathFunction log3;
    private final MathFunction log5;
    private final MathFunction log10;

    public RightBranchFunction(MathFunction ln, MathFunction log2, MathFunction log3, MathFunction log5, MathFunction log10) {
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
        this.log10 = log10;
    }

    @Override
    public double calculate(double x, double precision) {
        double lnX = ln.calculate(x, precision);
        double log2X = log2.calculate(x, precision);
        double log3X = log3.calculate(x, precision);
        double log5X = log5.calculate(x, precision);
        double log10X = log10.calculate(x, precision);

        if (Math.abs(log5X) < precision) {
            throw new ArithmeticException("Деление на ноль: log5(x) это 0");
        }

        // (((ln(x) - log_10(x)) + log_2(x)) * ln(x)) * log_3(x)
        double numerator = (((lnX - log10X) + log2X) * lnX) * log3X;

        // (log_2(x)^3) / (log_5(x)^3)
        double denominator = Math.pow(log2X, 3) / Math.pow(log5X, 3);
        if (Math.abs(denominator) < precision) {
            throw new ArithmeticException("Деление на ноль: main denominator is 0");
        }
        return numerator / denominator;
    }
}