package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.SecFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        // Если cos(x) = 0.25, то sec(x) = 1 / 0.25 = 4.0
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.25);

        double actual = secFunction.calculate(x, PRECISION);
        assertEquals(4.0, actual, PRECISION);
    }

    @Test
    void shouldThrowExceptionWhenCosIsZero() {
        double x = Math.PI / 2; // Точка разрыва (cos(PI/2) = 0)
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> {
            secFunction.calculate(x, PRECISION);
        });
    }
}