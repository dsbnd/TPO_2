package org.example.level3.module;

import org.example.level0.MathFunction;
import org.example.level3.LeftBranchFunction;
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
        double x = -3.14 / 2; //точка, в кот косинус равен нулю

        when(secMock.calculate(x, PRECISION)).thenThrow(new ArithmeticException("Division by zero in SecFunction"));
        assertThrows(ArithmeticException.class, () -> {
            leftBranch.calculate(x, PRECISION);
        });
    }

    @Test
    void shouldThrowExceptionWhenCscThrowsException() {
        double x = 0.0;
        when(cscMock.calculate(x, PRECISION)).thenThrow(new ArithmeticException("Division by zero in CscFunction"));

        assertThrows(ArithmeticException.class, () -> {
            leftBranch.calculate(x, PRECISION);
        });
    }

    @ParameterizedTest
    @CsvSource({
            //-пи/12 (-0.2618)
            "-0.2618, -0.258819, 0.965926, -3.863703, 1.035276, -0.267949, 5.7165944405101744e26",

            //-пи/6 (-0.5235)
            "-0.5235, -0.5, 0.866025, -2.0, 1.1547, -0.57735, 2.19386826e18"
    })
    void shouldCalculateCorrectlyForVariousX(
            double x,
            double sinVal, double cosVal, double cscVal, double secVal, double tanVal,
            double expectedResult) {

        when(sinMock.calculate(x, PRECISION)).thenReturn(sinVal);
        when(cosMock.calculate(x, PRECISION)).thenReturn(cosVal);
        when(cscMock.calculate(x, PRECISION)).thenReturn(cscVal);
        when(secMock.calculate(x, PRECISION)).thenReturn(secVal);
        when(tanMock.calculate(x, PRECISION)).thenReturn(tanVal);

        double actualResult = leftBranch.calculate(x, PRECISION);

        assertEquals(expectedResult, actualResult, 1e10);
    }
}