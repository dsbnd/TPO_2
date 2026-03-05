package org.example.level3.module;

import org.example.level0.MathFunction;
import org.example.level3.RightBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RightBranchFunctionTest {

    @Mock private MathFunction lnMock;
    @Mock private MathFunction log2Mock;
    @Mock private MathFunction log3Mock;
    @Mock private MathFunction log5Mock;
    @Mock private MathFunction log10Mock;

    private RightBranchFunction rightBranch;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        rightBranch = new RightBranchFunction(lnMock, log2Mock, log3Mock, log5Mock, log10Mock);
    }

    @Test
    void shouldCalculateCorrectlyForXEquals2() {
        double x = 2.0;

        when(lnMock.calculate(x, PRECISION)).thenReturn(0.693147);
        when(log10Mock.calculate(x, PRECISION)).thenReturn(0.301030);
        when(log2Mock.calculate(x, PRECISION)).thenReturn(1.0);
        when(log3Mock.calculate(x, PRECISION)).thenReturn(0.630930);
        when(log5Mock.calculate(x, PRECISION)).thenReturn(0.430677);

        double actualResult = rightBranch.calculate(x, PRECISION);
        double expectedResult = 0.048633;

        assertEquals(expectedResult, actualResult, 0.0001);
    }

    @Test
    void shouldThrowExceptionWhenLog5IsZero() {
        double x = 1.0;

        when(lnMock.calculate(x, PRECISION)).thenReturn(0.0);
        when(log10Mock.calculate(x, PRECISION)).thenReturn(0.0);
        when(log2Mock.calculate(x, PRECISION)).thenReturn(0.0);
        when(log3Mock.calculate(x, PRECISION)).thenReturn(0.0);
        when(log5Mock.calculate(x, PRECISION)).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> {
            rightBranch.calculate(x, PRECISION);
        });
    }

    @Test
    void shouldCalculateCorrectlyForXInZeroToOneInterval() {
        double x = 0.5;

        when(lnMock.calculate(x, PRECISION)).thenReturn(-0.693147);
        when(log10Mock.calculate(x, PRECISION)).thenReturn(-0.301030);
        when(log2Mock.calculate(x, PRECISION)).thenReturn(-1.0);
        when(log3Mock.calculate(x, PRECISION)).thenReturn(-0.630930);
        when(log5Mock.calculate(x, PRECISION)).thenReturn(-0.430677);

        double actualResult = rightBranch.calculate(x, PRECISION);
        double expectedResult = -0.048633;

        assertEquals(expectedResult, actualResult, 0.0001);
    }
}