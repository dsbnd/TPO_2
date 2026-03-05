package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.CscFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CscFunctionTest {

    @Mock private MathFunction sinMock;
    private CscFunction cscFunction;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        cscFunction = new CscFunction(sinMock);
    }

    @Test
    void shouldCalculateCorrectly() {
        double x = 1.0; // Значение x тут не так важно, важен мок
        // Если sin(x) = 0.5, то csc(x) = 1 / 0.5 = 2.0
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.5);

        double actual = cscFunction.calculate(x, PRECISION);
        assertEquals(2.0, actual, PRECISION);
    }

    @Test
    void shouldThrowExceptionWhenSinIsZero() {
        double x = 0.0; // Точка разрыва (sin(0) = 0)
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> {
            cscFunction.calculate(x, PRECISION);
        });
    }
}