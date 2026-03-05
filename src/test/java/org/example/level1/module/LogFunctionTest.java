package org.example.level1.module;

import org.example.level0.LnFunction;
import org.example.level0.MathFunction;
import org.example.level1.LogFunction;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogFunctionTest {

    private MathFunction lnFunction;
    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

     
    @Mock
    private MathFunction lnMock;

    @BeforeEach
    void setUp() {
         
        lnFunction = new LnFunction();
    }

    @Test
    void testLogBaseValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> new LogFunction(lnFunction, 1.0));
        assertThrows(IllegalArgumentException.class,
                () -> new LogFunction(lnFunction, 0.0));
        assertThrows(IllegalArgumentException.class,
                () -> new LogFunction(lnFunction, -1.0));
    }

    @ParameterizedTest(name = "log{1}({0}) = {2}")
    @CsvFileSource(resources = "/level1/log.csv", numLinesToSkip = 1, delimiter = ',')
    void testLogWithCsv(double x, double base, double expected) {
        LogFunction logFunction = new LogFunction(lnFunction, base);
        assertEquals(expected, logFunction.calculate(x, PRECISION), DELTA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -1.0, -5.0})
    void testLogInvalidInput(double x) {
        LogFunction logFunction = new LogFunction(lnFunction, 2.0);
        assertThrows(IllegalArgumentException.class,
                () -> logFunction.calculate(x, PRECISION));
    }

    @Test
    void testLogWithMock() {
        LogFunction logWithMock = new LogFunction(lnMock, 2.0);

        double x = 4.0;
        when(lnMock.calculate(eq(x), anyDouble())).thenReturn(1.38629);
        when(lnMock.calculate(eq(2.0), anyDouble())).thenReturn(0.693147);

        assertEquals(2.0, logWithMock.calculate(x, PRECISION), DELTA);
    }
}