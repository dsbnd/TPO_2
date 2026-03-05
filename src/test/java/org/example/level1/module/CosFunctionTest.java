package org.example.level1.module;

import org.example.level0.MathFunction;
import org.example.level0.SinFunction;
import org.example.level1.CosFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  
class CosFunctionTest {
    private CosFunction cosFunction;
    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

    @Mock
    private MathFunction sinMock;

    @BeforeEach
    void setUp() {
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

    @Test
    void testCosWithMock() {
        CosFunction cosWithMock = new CosFunction(sinMock);

        double x = 1.0;
        double expectedSinArg = x + Math.PI / 2;
         
        double sinValue = 0.5403023058681398;
        double expectedCos = 0.5403023058681398;

        when(sinMock.calculate(eq(expectedSinArg), anyDouble())).thenReturn(sinValue);

        assertEquals(expectedCos, cosWithMock.calculate(x, PRECISION), DELTA);

        verify(sinMock, times(1)).calculate(eq(expectedSinArg), anyDouble());
    }
}