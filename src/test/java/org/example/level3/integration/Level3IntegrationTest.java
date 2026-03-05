package org.example.level3.integration;

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
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Level3IntegrationTest {

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

     
    @Mock
    private SinFunction mockSin;
    @Mock
    private LnFunction mockLn;
    @Mock
    private CosFunction mockCos;
    @Mock
    private CscFunction mockCsc;
    @Mock
    private SecFunction mockSec;
    @Mock
    private TanFunction mockTan;
    @Mock
    private LogFunction mockLog2;
    @Mock
    private LogFunction mockLog3;
    @Mock
    private LogFunction mockLog5;
    @Mock
    private LogFunction mockLog10;

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
    }

     

    @Test
    @DisplayName("Проверка, что LeftBranch вызывает все тригонометрические функции")
    void shouldCallAllTrigFunctionsInLeftBranch() {
        LeftBranchFunction leftBranch = new LeftBranchFunction(
                spySin, spyCos, spyCsc, spySec, spyTan
        );

        leftBranch.calculate(-1.0, PRECISION);

         
        verify(spySin, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyCos, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyCsc, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spySec, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyTan, atLeastOnce()).calculate(anyDouble(), anyDouble());

         
        verify(spyLn, never()).calculate(anyDouble(), anyDouble());
        verify(spyLog2, never()).calculate(anyDouble(), anyDouble());
        verify(spyLog3, never()).calculate(anyDouble(), anyDouble());
        verify(spyLog5, never()).calculate(anyDouble(), anyDouble());
        verify(spyLog10, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка LeftBranch с моками")
    void shouldCalculateLeftBranchWithMocks() {
        double x = -1.0;

         
        when(mockSin.calculate(eq(x), anyDouble())).thenReturn(-0.84147);
        when(mockCos.calculate(eq(x), anyDouble())).thenReturn(0.54030);
        when(mockCsc.calculate(eq(x), anyDouble())).thenReturn(-1.18840);
        when(mockSec.calculate(eq(x), anyDouble())).thenReturn(1.85082);
        when(mockTan.calculate(eq(x), anyDouble())).thenReturn(-1.55741);

        LeftBranchFunction leftBranch = new LeftBranchFunction(
                mockSin, mockCos, mockCsc, mockSec, mockTan
        );

        double result = leftBranch.calculate(x, PRECISION);
        assertFalse(Double.isNaN(result));
        assertFalse(Double.isInfinite(result));

        verify(mockSin, times(1)).calculate(eq(x), anyDouble());
        verify(mockCos, times(1)).calculate(eq(x), anyDouble());
        verify(mockCsc, times(1)).calculate(eq(x), anyDouble());
        verify(mockSec, times(1)).calculate(eq(x), anyDouble());
        verify(mockTan, times(1)).calculate(eq(x), anyDouble());
    }



    @Test
    @DisplayName("Проверка обработки исключений в LeftBranch")
    void shouldThrowExceptions() {
        LeftBranchFunction leftBranch = new LeftBranchFunction(
                spySin, spyCos, spyCsc, spySec, spyTan
        );

         
        assertThrows(ArithmeticException.class,
                () -> leftBranch.calculate(0.0, PRECISION));
        assertThrows(ArithmeticException.class,
                () -> leftBranch.calculate(-Math.PI, PRECISION));
        assertThrows(ArithmeticException.class,
                () -> leftBranch.calculate(-Math.PI / 2, PRECISION));
    }

     

    @Test
    @DisplayName("Проверка, что RightBranch вызывает все логарифмические функции")
    void shouldCallAllLogFunctionsInRightBranch() {
        RightBranchFunction rightBranch = new RightBranchFunction(
                spyLn, spyLog2, spyLog3, spyLog5, spyLog10
        );

        rightBranch.calculate(2.0, PRECISION);

         
        verify(spyLn, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog2, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog3, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog5, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLog10, atLeastOnce()).calculate(anyDouble(), anyDouble());

         
        verify(spySin, never()).calculate(anyDouble(), anyDouble());
        verify(spyCos, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка RightBranch с моками")
    void shouldCalculateRightBranchWithMocks() {
        double x = 2.0;

         
        when(mockLn.calculate(eq(x), anyDouble())).thenReturn(0.693147);
        when(mockLog2.calculate(eq(x), anyDouble())).thenReturn(1.0);
        when(mockLog3.calculate(eq(x), anyDouble())).thenReturn(0.63093);
        when(mockLog5.calculate(eq(x), anyDouble())).thenReturn(0.43068);
        when(mockLog10.calculate(eq(x), anyDouble())).thenReturn(0.30103);

        RightBranchFunction rightBranch = new RightBranchFunction(
                mockLn, mockLog2, mockLog3, mockLog5, mockLog10
        );

        double result = rightBranch.calculate(x, PRECISION);
        assertFalse(Double.isNaN(result));
        assertFalse(Double.isInfinite(result));

        verify(mockLn, times(1)).calculate(eq(x), anyDouble());
        verify(mockLog2, times(1)).calculate(eq(x), anyDouble());
        verify(mockLog3, times(1)).calculate(eq(x), anyDouble());
        verify(mockLog5, times(1)).calculate(eq(x), anyDouble());
        verify(mockLog10, times(1)).calculate(eq(x), anyDouble());
    }



    @Test
    @DisplayName("Проверка обработки исключений в RightBranch")
    void shouldThrowExceptionAtDiscontinuityPoints() {
        RightBranchFunction rightBranch = new RightBranchFunction(
                spyLn, spyLog2, spyLog3, spyLog5, spyLog10
        );

         
        assertThrows(IllegalArgumentException.class,
                () -> rightBranch.calculate(0.0, PRECISION));

         
        assertThrows(ArithmeticException.class,
                () -> rightBranch.calculate(1.0, PRECISION));

         
        assertThrows(IllegalArgumentException.class,
                () -> rightBranch.calculate(-1.0, PRECISION));
    }

     

    @Test
    @DisplayName("Проверка полной цепочки вызовов в LeftBranch")
    void shouldVerifyFullChainInLeftBranch() {
        LeftBranchFunction leftBranch = new LeftBranchFunction(
                spySin, spyCos, spyCsc, spySec, spyTan
        );

        double x = -1.0;
        leftBranch.calculate(x, PRECISION);

         
        verify(spySin, times(3)).calculate(eq(x), eq(PRECISION));   
        verify(spyCos, times(3)).calculate(eq(x), eq(PRECISION));   
        verify(spyCsc, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spySec, times(1)).calculate(eq(x), eq(PRECISION));
        verify(spyTan, times(1)).calculate(eq(x), eq(PRECISION));
    }
    @Test
    @DisplayName("Проверка полной цепочки вызовов в RightBranch")
    void shouldVerifyFullChainInRightBranch() {
        RightBranchFunction rightBranch = new RightBranchFunction(
                spyLn, spyLog2, spyLog3, spyLog5, spyLog10
        );

        rightBranch.calculate(2.0, PRECISION);

         
        verify(spyLn, times(9)).calculate(anyDouble(), anyDouble());

         
        verify(spyLn, times(6)).calculate(eq(2.0), eq(PRECISION));

         
        verify(spyLn, times(1)).calculate(eq(3.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(5.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(10.0), eq(PRECISION));

         
        verify(spyLog2, times(1)).calculate(eq(2.0), eq(PRECISION));
        verify(spyLog3, times(1)).calculate(eq(2.0), eq(PRECISION));
        verify(spyLog5, times(1)).calculate(eq(2.0), eq(PRECISION));
        verify(spyLog10, times(1)).calculate(eq(2.0), eq(PRECISION));
    }

}