package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.SecFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecFunctionTest {

    @Mock private MathFunction cosMock;
    private SecFunction secFunction;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        secFunction = new SecFunction(cosMock);
    }

    @Test
    void shouldCalculateCorrectly() {
        double x = 1.0; 

        when(cosMock.calculate(x, PRECISION)).thenReturn(0.25);
        double actual = secFunction.calculate(x, PRECISION);
        assertEquals(4.0, actual, PRECISION);
    }

    @Test
    void shouldThrowExceptionWhenCosIsZero() {
        double x = 3.14 / 2;
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> {
            secFunction.calculate(x, PRECISION);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "0.0, 1.0, 1.0",            // 0: cos = 1, sec = 1
            "1.0472, 0.5, 2.0",         // pi/3: cos = 0.5, sec = 2
            "2.0944, -0.5, -2.0"        // 2*pi/3: cos = -0.5, sec = -2
    })
    void shouldCalculateCorrectlyForVariousX(double x, double cosVal, double expected) {
        when(cosMock.calculate(x, PRECISION)).thenReturn(cosVal);

        double actual = secFunction.calculate(x, PRECISION);
        assertEquals(expected, actual, 0.0001);
    }
}