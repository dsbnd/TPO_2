package org.example;

import org.example.level0.LnFunction;

import org.example.level0.MathFunction;

import org.example.level0.SinFunction;

import org.example.level1.CosFunction;

import org.example.level1.LogFunction;

import org.example.level2.CscFunction;

import org.example.level2.SecFunction;

import org.example.level2.TanFunction;

import org.example.level3.LeftBranchFunction;

import org.example.level3.RightBranchFunction;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;

import org.junit.jupiter.params.provider.CsvFileSource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class SystemIntegrationTest2 {


    private SystemOfFunctions system;

    private static final double PRECISION = 0.0001;


    @BeforeEach

    void setUp() {

        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction csc = new CscFunction(sin);
        MathFunction sec = new SecFunction(cos);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction leftBranch = new LeftBranchFunction(sin, cos, csc, sec, tan);


        MathFunction ln = new LnFunction();
        MathFunction log2 = new LogFunction(ln, 2.0);
        MathFunction log3 = new LogFunction(ln, 3.0);
        MathFunction log5 = new LogFunction(ln, 5.0);
        MathFunction log10 = new LogFunction(ln, 10.0);
        MathFunction rightBranch = new RightBranchFunction(ln, log2, log3, log5, log10);

        system = new SystemOfFunctions(leftBranch, rightBranch);

    }


    @ParameterizedTest
    @CsvFileSource(resources = "/all_results.csv", numLinesToSkip = 1)
    void shouldCalculateEntireSystemCorrectly(double x, double expectedResult) {

        if (Double.isNaN(expectedResult)) {
            assertThrows(ArithmeticException.class, () -> {
                system.calculate(x, PRECISION);
            });
        } else {
            double actualResult = system.calculate(x, PRECISION);
            assertEquals(expectedResult, actualResult, PRECISION);
        }

    }

}