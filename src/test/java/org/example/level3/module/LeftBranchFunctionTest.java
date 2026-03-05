package org.example.level3.module;

import org.example.level0.MathFunction;
import org.example.level3.LeftBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeftBranchFunctionTest {

    @Mock private MathFunction sinMock;
    @Mock private MathFunction cosMock;
    @Mock private MathFunction cscMock;
    @Mock private MathFunction secMock;
    @Mock private MathFunction tanMock;

    private LeftBranchFunction leftBranch;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        // Собираем ветку, подсовывая ей моки тригонометрии
        leftBranch = new LeftBranchFunction(sinMock, cosMock, cscMock, secMock, tanMock);
    }

    @Test
    void shouldCalculateCorrectlyForMinusPiOver4() {
        double x = -0.7853; // -пи/4

        when(sinMock.calculate(x, PRECISION)).thenReturn(-0.707037);
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.707176);
        when(cscMock.calculate(x, PRECISION)).thenReturn(-1.41435);
        when(secMock.calculate(x, PRECISION)).thenReturn(1.41407);
        when(tanMock.calculate(x, PRECISION)).thenReturn(-0.999804);

        double actualResult = leftBranch.calculate(x, PRECISION);
        double expectedResult = 5.75928e11;
        assertEquals(expectedResult, actualResult, 1e7);
    }

    @Test
    void shouldThrowExceptionWhenSecThrowsException() {
        double x = -3.14 / 2; // Точка, где косинус равен нулю

        // Имитируем, что при попытке посчитать секанс вылетает ошибка деления на ноль
        when(secMock.calculate(x, PRECISION)).thenThrow(new ArithmeticException("Division by zero in SecFunction"));

        // Можем настроить и другие моки, но это необязательно,
        // так как при вызове sec.calculate() внутри левой ветки выполнение прервется

        // Проверяем, что левая ветка тоже упала с ArithmeticException
        assertThrows(ArithmeticException.class, () -> {
            leftBranch.calculate(x, PRECISION);
        });
    }
}