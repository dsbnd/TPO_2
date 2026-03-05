package org.example;

import org.example.level0.LnFunction;
import org.example.level0.SinFunction;
import org.example.level1.CosFunction;
import org.example.level1.LogFunction;
import org.example.level2.CscFunction;
import org.example.level2.SecFunction;
import org.example.level2.TanFunction;
import org.example.level3.LeftBranchFunction;
import org.example.level3.RightBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemIntegrationTest {

    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-4;

    @Spy
    private SinFunction spySin = new SinFunction();
    @Spy
    private LnFunction spyLn = new LnFunction();

    private CosFunction spyCos;
    private LogFunction spyLog2;
    private LogFunction spyLog3;
    private LogFunction spyLog5;
    private LogFunction spyLog10;

    private CscFunction spyCsc;
    private SecFunction spySec;
    private TanFunction spyTan;

    private LeftBranchFunction spyLeft;
    private RightBranchFunction spyRight;

    @Mock
    private LeftBranchFunction mockLeft;
    @Mock
    private RightBranchFunction mockRight;

    private SystemOfFunctions system;

    @BeforeEach
    void setUp() {
         
        spyCos = spy(new CosFunction(spySin));
        spyCsc = spy(new CscFunction(spySin));
        spySec = spy(new SecFunction(spyCos));
        spyTan = spy(new TanFunction(spySin, spyCos));

        spyLog2 = spy(new LogFunction(spyLn, 2.0));
        spyLog3 = spy(new LogFunction(spyLn, 3.0));
        spyLog5 = spy(new LogFunction(spyLn, 5.0));
        spyLog10 = spy(new LogFunction(spyLn, 10.0));

        spyLeft = spy(new LeftBranchFunction(spySin, spyCos, spyCsc, spySec, spyTan));
        spyRight = spy(new RightBranchFunction(spyLn, spyLog2, spyLog3, spyLog5, spyLog10));

        system = new SystemOfFunctions(spyLeft, spyRight);
    }

     

    @Test
    @DisplayName("Проверка, что для x < 0 вызывается LeftBranch и все тригонометрические функции")
    void shouldCallLeftBranchAndAllTrigForNegativeX() {
        double x = -1.0;
        system.calculate(x, PRECISION);

         
        verify(spyLeft, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyRight, never()).calculate(anyDouble(), anyDouble());

         
        verify(spySin, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyCos, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyCsc, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spySec, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyTan, atLeastOnce()).calculate(anyDouble(), anyDouble());

         
        verify(spySin, times(3)).calculate(eq(x), eq(PRECISION));  
        verify(spyCos, times(3)).calculate(eq(x), eq(PRECISION));  
        verify(spyCsc, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spySec, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyTan, times(1)).calculate(eq(x), eq(PRECISION));

         
        verify(spyLn, never()).calculate(anyDouble(), anyDouble());
        verify(spyLog2, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка, что для x > 0 вызывается RightBranch и все логарифмические функции")
    void shouldCallRightBranchAndAllLogForPositiveX() {
        double x = 2.0;
        system.calculate(x, PRECISION);

         
        verify(spyRight, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyLeft, never()).calculate(anyDouble(), anyDouble());

         
        verify(spyLn, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog2, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog3, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog5, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog10, atLeastOnce()).calculate(anyDouble(), anyDouble());

         
         
        verify(spyLn, times(9)).calculate(anyDouble(), anyDouble());  

        verify(spyLog2, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyLog3, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyLog5, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyLog10, times(1)).calculate(eq(x), eq(PRECISION));

         
        verify(spySin, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка, что для x = 0 выбрасывается исключение")
    void shouldThrowExceptionAtZero() {
        assertThrows(ArithmeticException.class,
                () -> system.calculate(0.0, PRECISION));

         
        verify(spyLeft, times(1)).calculate(eq(0.0), eq(PRECISION));  
        verify(spyRight, never()).calculate(anyDouble(), anyDouble());

         
        verify(spySin, atLeastOnce()).calculate(eq(0.0), eq(PRECISION));
        verify(spyCos, atLeastOnce()).calculate(eq(0.0), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка, что для x = 1 выбрасывается исключение")
    void shouldThrowExceptionAtOne() {
        assertThrows(ArithmeticException.class,
                () -> system.calculate(1.0, PRECISION));

         
        verify(spyRight, times(1)).calculate(eq(1.0), eq(PRECISION));  
        verify(spyLeft, never()).calculate(anyDouble(), anyDouble());

         
        verify(spyLn, atLeastOnce()).calculate(eq(1.0), eq(PRECISION));
    }

     

    @Test
    @DisplayName("Проверка System с моками для отрицательного x")
    void shouldCalculateWithMocksForNegativeX() {
        double x = -1.0;
        double expected = -4.0;  

        when(mockLeft.calculate(eq(x), anyDouble())).thenReturn(expected);

        SystemOfFunctions system = new SystemOfFunctions(mockLeft, mockRight);

        assertEquals(expected, system.calculate(x, PRECISION), DELTA);

        verify(mockLeft, times(1)).calculate(eq(x), anyDouble());
        verify(mockRight, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка System с моками для положительного x")
    void shouldCalculateWithMocksForPositiveX() {
        double x = 2.0;
        double expected = 2.0;  

        when(mockRight.calculate(eq(x), anyDouble())).thenReturn(expected);

        SystemOfFunctions system = new SystemOfFunctions(mockLeft, mockRight);

        assertEquals(expected, system.calculate(x, PRECISION), DELTA);

        verify(mockRight, times(1)).calculate(eq(x), anyDouble());
        verify(mockLeft, never()).calculate(anyDouble(), anyDouble());
    }



     

    @Test
    @DisplayName("Проверка полной цепочки вызовов для отрицательного x")
    void shouldVerifyFullChainForNegativeX() {
        double x = -1.0;
        system.calculate(x, PRECISION);

         
        InOrder inOrder = inOrder(spyLeft, spySin, spyCos, spyCsc, spySec, spyTan);

        inOrder.verify(spyLeft).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spySin).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyCos).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyCsc).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spySec).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyTan).calculate(eq(x), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка полной цепочки вызовов для положительного x")
    void shouldVerifyFullChainForPositiveX() {
        double x = 2.0;
        system.calculate(x, PRECISION);

         
        InOrder inOrder = inOrder(spyRight, spyLn, spyLog2, spyLog3, spyLog5, spyLog10);

        inOrder.verify(spyRight).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyLn).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyLog2).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyLog3).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyLog5).calculate(eq(x), eq(PRECISION));
        inOrder.verify(spyLog10).calculate(eq(x), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка, что System правильно делегирует вызовы")
    void shouldDelegateCallsCorrectly() {
         
        system.calculate(-2.0, PRECISION);
        verify(spyLeft, times(1)).calculate(eq(-2.0), eq(PRECISION));
        verify(spyRight, never()).calculate(anyDouble(), anyDouble());

         
        system.calculate(3.0, PRECISION);
        verify(spyRight, times(1)).calculate(eq(3.0), eq(PRECISION));
        verify(spyLeft, times(1)).calculate(eq(-2.0), eq(PRECISION));  
    }
}