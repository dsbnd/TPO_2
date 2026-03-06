package org.example.level0.module;

import org.example.level0.LnFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class LnFunctionTest {

    private LnFunction lnFunction;
    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double E =2.71828182845904523536028747;

    @BeforeEach
    void setUp() {
        lnFunction = new LnFunction();
    }

    @Test
    void testLnOne() {
        assertEquals(0.0, lnFunction.calculate(1.0, PRECISION), PRECISION);
    }

    @Test
    void testLnE() {
        assertEquals(1.0, lnFunction.calculate(E, PRECISION), PRECISION);
    }

    @ParameterizedTest(name = "ln({0}) = {1}")
    @CsvFileSource(resources = "/level0/ln.csv", numLinesToSkip = 1, delimiter = ',')
    void testLnWithCsv(double x, double expected) {
        assertEquals(expected, lnFunction.calculate(x, PRECISION), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -5.0, Double.NaN, Double.NEGATIVE_INFINITY})
    void testLnInvalidInput(double x) {
        assertThrows(IllegalArgumentException.class,
                () -> lnFunction.calculate(x, PRECISION));
    }
}