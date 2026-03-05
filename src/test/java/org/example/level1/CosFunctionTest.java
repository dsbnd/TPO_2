package org.example.level1;

import org.example.level0.MathFunction;
import org.example.level0.SinFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CosFunctionTest {

    private CosFunction cosFunction;
    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

    @BeforeEach
    void setUp() {
        // Используем реальный SinFunction вместо мока
        MathFunction sinFunction = new SinFunction();
        cosFunction = new CosFunction(sinFunction);
    }

    @Test
    void testCosZero() {
        assertEquals(1.0, cosFunction.calculate(0.0, PRECISION), PRECISION);
    }

    @Test
    void testCosPeriodicity() {
        double cos1 = cosFunction.calculate(2 * Math.PI, PRECISION);
        double cos2 = cosFunction.calculate(4 * Math.PI, PRECISION);
        assertEquals(cos1, cos2, PRECISION);
    }

    @ParameterizedTest(name = "cos({0}) = {1}")
    @CsvFileSource(resources = "/level1/cos.csv", numLinesToSkip = 1, delimiter = ',')
    void testCosWithCsv(double x, double expected) {
        assertEquals(expected, cosFunction.calculate(x, PRECISION), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testCosInvalidInput(double x) {
        assertThrows(IllegalArgumentException.class,
                () -> cosFunction.calculate(x, PRECISION));
    }
}