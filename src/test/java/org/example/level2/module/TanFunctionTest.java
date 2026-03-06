package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.TanFunction;
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
class TanFunctionTest {

    @Mock private MathFunction sinMock;
    @Mock private MathFunction cosMock;
    private TanFunction tanFunction;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        tanFunction = new TanFunction(sinMock, cosMock);
    }

    @Test
    @DisplayName("Проверка на корректное вычисление функции tan")
    void shouldCalculateCorrectly() {
        double x = 1.0;
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.6);
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.3);

        double actual = tanFunction.calculate(x, PRECISION);
        assertEquals(2.0, actual, PRECISION);
    }

    @Test
    @DisplayName("Проверка на выброс исключений функции tan")
    void shouldThrowExceptionWhenCosIsZero() {
        double x = 3.14 / 2;

        when(cosMock.calculate(x, PRECISION)).thenReturn(0.0);
        assertThrows(ArithmeticException.class, () -> {
            tanFunction.calculate(x, PRECISION);
        });
    }

    @ParameterizedTest
    @DisplayName("Параметризованный тест функции tan")
    @CsvSource({
            "0.7854, 0.7071, 0.7071, 1.0",      // pi/4: sin = cos, tan = 1
            "-0.7854, -0.7071, 0.7071, -1.0",   // -pi/4: tan = -1
            "0.0, 0.0, 1.0, 0.0"                // 0: sin = 0, cos = 1, tan = 0
    })
    void shouldCalculateCorrectlyForVariousX(double x, double sinVal, double cosVal, double expected) {
        when(sinMock.calculate(x, PRECISION)).thenReturn(sinVal);
        when(cosMock.calculate(x, PRECISION)).thenReturn(cosVal);

        double actual = tanFunction.calculate(x, PRECISION);
        assertEquals(expected, actual, 0.001);
    }
}