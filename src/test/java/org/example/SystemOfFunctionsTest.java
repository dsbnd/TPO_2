package org.example;

import org.example.level0.MathFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemOfFunctionsTest {

    @Mock
    private MathFunction leftBranchMock;

    @Mock
    private MathFunction rightBranchMock;

    private SystemOfFunctions systemOfFunctions;

    @BeforeEach
    void setUp() {
        systemOfFunctions = new SystemOfFunctions(leftBranchMock, rightBranchMock);
    }

    private static final double PRECISION = 0.0001;

    @Test
    void shouldCallLeftBranchWhenXIsNegative() {
        double x = -5.0;
        double expectedResult = -10.86056;

        when(leftBranchMock.calculate(x, PRECISION)).thenReturn(expectedResult);


        double actualResult = systemOfFunctions.calculate(x, PRECISION);
        assertEquals(expectedResult, actualResult, PRECISION);

        // Проверяем, что метод calculate у левой ветви был вызван ровно 1 раз
        verify(leftBranchMock, times(1)).calculate(x, PRECISION);
        // Проверяем, что правая ветвь вообще не вызывалась
        verify(rightBranchMock, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    void shouldThrowExceptionWhenXIsZero() {
        double x = 0.0;

        when(leftBranchMock.calculate(x, PRECISION))
                .thenThrow(new ArithmeticException("Division by zero in CscFunction"));

        assertThrows(ArithmeticException.class, () -> {
            systemOfFunctions.calculate(x, PRECISION);
        });

        // Проверяем, что роутинг все равно сработал правильно:
        // Система попыталась обратиться к левой ветви (которая и упала)
        verify(leftBranchMock, times(1)).calculate(x, PRECISION);
        // А правую ветвь даже не трогала
        verify(rightBranchMock, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    void shouldCallRightBranchWhenXIsPositive() {
        double x = 2.0;
        double expectedResult = 0.0151;

        when(rightBranchMock.calculate(x, PRECISION)).thenReturn(expectedResult);

        double actualResult = systemOfFunctions.calculate(x, PRECISION);

        assertEquals(expectedResult, actualResult, PRECISION);
        verify(rightBranchMock, times(1)).calculate(x, PRECISION);
        verify(leftBranchMock, never()).calculate(anyDouble(), anyDouble());
    }
}