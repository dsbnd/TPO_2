package org.example.level0;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class SinFunctionTest {

    private SinFunction sinFunction;
    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

    @BeforeEach
    void setUp() {
        sinFunction = new SinFunction();
    }

    @Test
    void testSinZero() {
        assertEquals(0.0, sinFunction.calculate(0.0, PRECISION), PRECISION);
    }

    @Test
    void testSinPeriodicity() {
        double sin1 = sinFunction.calculate(Math.PI / 6, PRECISION);
        double sin2 = sinFunction.calculate(Math.PI / 6 + 2 * Math.PI, PRECISION);
        assertEquals(sin1, sin2, PRECISION);
    }

    @ParameterizedTest(name = "sin({0}) = {1}")
    @CsvFileSource(resources = "/level0/sin.csv", numLinesToSkip = 1, delimiter = ',')
    void testSinWithCsv(double x, double expected) {
        assertEquals(expected, sinFunction.calculate(x, PRECISION), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testSinInvalidInput(double x) {
        assertThrows(IllegalArgumentException.class,
                () -> sinFunction.calculate(x, PRECISION));
    }
}