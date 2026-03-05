package org.example.level3;

import org.example.level0.MathFunction;

public class LeftBranchFunction implements MathFunction {
    private final MathFunction sin;
    private final MathFunction cos;
    private final MathFunction csc;
    private final MathFunction sec;
    private final MathFunction tan;

    public LeftBranchFunction(MathFunction sin, MathFunction cos, MathFunction csc, MathFunction sec, MathFunction tan) {
        this.sin = sin;
        this.cos = cos;
        this.csc = csc;
        this.sec = sec;
        this.tan = tan;
    }

    @Override
    public double calculate(double x, double precision) {
        double sinX = sin.calculate(x, precision);
        double cosX = cos.calculate(x, precision);
        double cscX = csc.calculate(x, precision);
        double secX = sec.calculate(x, precision);
        double tanX = tan.calculate(x, precision);

        // (((cos(x) - csc(x)) + (cos(x) + sin(x)))^3)^2)^3)^2
        double innerPart = (cosX - cscX) + (cosX + sinX);
        double powPart = Math.pow(Math.pow(Math.pow(Math.pow(innerPart, 3), 2), 3), 2);
        
        // (((sec(x) * (tan(x) * sin(x))) - cos(x)) - cos(x))
        double rightPart = (secX * (tanX * sinX)) - cosX - cosX;

        return powPart - rightPart;
    }
}