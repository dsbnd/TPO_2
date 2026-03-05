package org.example.level2.module;

import org.example.level0.MathFunction;
import org.example.level2.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    void shouldCalculateCorrectly() {
        double x = 1.0; 
        // sin(x) = 0.6, cos(x) = 0.3. Тогда tan(x) = 0.6 / 0.3 = 2.0
        when(sinMock.calculate(x, PRECISION)).thenReturn(0.6);
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.3);

        double actual = tanFunction.calculate(x, PRECISION);
        assertEquals(2.0, actual, PRECISION);
    }

    @Test
    void shouldThrowExceptionWhenCosIsZero() {
        double x = Math.PI / 2; 
        // Нам неважно, чему равен sin, если cos = 0, должно быть исключение
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.0);
        
        // Mockito lenient() используется, чтобы он не ругался, если мы не вызовем sinMock
        // но для простоты мы просто не будем его настраивать, так как код упадет до его вызова

        assertThrows(ArithmeticException.class, () -> {
            tanFunction.calculate(x, PRECISION);
        });
    }
}