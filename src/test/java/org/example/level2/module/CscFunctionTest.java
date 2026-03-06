package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.CscFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
class CscFunctionTest {

    @Mock private MathFunction sinMock;
    private CscFunction cscFunction;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        cscFunction = new CscFunction(sinMock);
    }

    @Test
    @DisplayName("Проверка на корректное вычисление функции csc")
    void shouldCalculateCorrectly() {
        double x = 1.0;
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.5);

        double actual = cscFunction.calculate(x, PRECISION);
        assertEquals(2.0, actual, PRECISION);
    }

    @Test
    @DisplayName("Проверка на выброс исключений функции csc")
    void shouldThrowExceptionWhenSinIsZero() {
        double x = 0.0; // Точка разрыва
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> {
            cscFunction.calculate(x, PRECISION);
        });
    }

    @ParameterizedTest
    @DisplayName("Параметризованный тест функции csc")
    @CsvSource({
            "1.5708, 1.0, 1.0",         // pi/2: sin = 1, csc = 1
            "-0.5236, -0.5, -2.0",      // -pi/6: sin = -0.5, csc = -2
            "0.5236, 0.5, 2.0"          // pi/6: sin = 0.5, csc = 2
    })
    void shouldCalculateCorrectlyForVariousX(double x, double sinVal, double expected) {
        when(sinMock.calculate(x, PRECISION)).thenReturn(sinVal);

        double actual = cscFunction.calculate(x, PRECISION);
        assertEquals(expected, actual, 0.0001);
    }
}