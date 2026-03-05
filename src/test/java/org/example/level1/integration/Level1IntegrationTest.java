package org.example.level1.integration;

import org.example.level0.LnFunction;
import org.example.level0.SinFunction;
import org.example.level1.CosFunction;
import org.example.level1.LogFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Level1IntegrationTest {

    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

    @Spy
    private SinFunction spySin;

    @Spy
    private LnFunction spyLn;

    @Mock
    private SinFunction mockSin;

    @Mock
    private LnFunction mockLn;

    @Test
    @DisplayName("Проверка, что Cos вызывает Sin")
    void shouldCallSinWhenCalculatingCos() {
        CosFunction cos = new CosFunction(spySin);

        cos.calculate(Math.PI / 2, PRECISION);
         
        verify(spySin, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spySin, times(1)).calculate(eq(Math.PI / 2 + Math.PI / 2), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка многократных вызовов Cos")
    void shouldCallSinMultipleTimes() {
        CosFunction cos = new CosFunction(spySin);

        cos.calculate(0.0, PRECISION);
        cos.calculate(Math.PI / 2, PRECISION);
        cos.calculate(Math.PI, PRECISION);
         
        verify(spySin, times(3)).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка корректности передачи точности в Cos")
    void shouldPassPrecisionToSin() {
        CosFunction cos = new CosFunction(spySin);
        double testPrecision = 1e-4;

        cos.calculate(1.0, testPrecision);
         
        verify(spySin, times(1)).calculate(anyDouble(), eq(testPrecision));
    }


    @Test
    @DisplayName("Проверка, что Log2 вызывает Ln дважды")
    void shouldCallLnTwiceWhenCalculatingLog2() {
        LogFunction log2 = new LogFunction(spyLn, 2.0);

        log2.calculate(2.0, PRECISION);
         
        verify(spyLn, times(2)).calculate(anyDouble(), anyDouble());
        verify(spyLn, times(2)).calculate(eq(2.0), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка, что Log10 вызывает Ln дважды")
    void shouldCallLnTwiceWhenCalculatingLog10() {
        LogFunction log10 = new LogFunction(spyLn, 10.0);

        log10.calculate(100.0, PRECISION);

        verify(spyLn, times(2)).calculate(anyDouble(), anyDouble());
        verify(spyLn, times(1)).calculate(eq(100.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(10.0), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка разных оснований логарифма")
    void shouldHandleDifferentBases() {
        LogFunction log2 = new LogFunction(spyLn, 2.0);
        LogFunction log3 = new LogFunction(spyLn, 3.0);
        LogFunction log5 = new LogFunction(spyLn, 5.0);

        log2.calculate(8.0, PRECISION);
        log3.calculate(9.0, PRECISION);
        log5.calculate(25.0, PRECISION);

         
        verify(spyLn, times(1)).calculate(eq(8.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(2.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(9.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(3.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(25.0), eq(PRECISION));
        verify(spyLn, times(1)).calculate(eq(5.0), eq(PRECISION));
    }


    @Test
    @DisplayName("Проверка Cos с Mock Sin")
    void shouldCalculateCosWithMockSin() {
         
        when(mockSin.calculate(eq(Math.PI / 2), anyDouble())).thenReturn(1.0);
         
        when(mockSin.calculate(eq(Math.PI), anyDouble())).thenReturn(0.0);

        CosFunction cos = new CosFunction(mockSin);

        assertEquals(1.0, cos.calculate(0.0, PRECISION), DELTA);
        assertEquals(0.0, cos.calculate(Math.PI / 2, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq(Math.PI / 2), anyDouble());
        verify(mockSin, times(1)).calculate(eq(Math.PI), anyDouble());
    }

    @ParameterizedTest(name = "cos({0}) = {1} with mock")
    @DisplayName("Проверка Cos с Mock для всех значений из CSV")
    @CsvFileSource(resources = "/level1/cos.csv", numLinesToSkip = 1, delimiter = ',')
    void shouldCalculateCosWithMockForAllValues(double x, double expected) {
         
        double sinArg = x + Math.PI / 2;

         
        when(mockSin.calculate(eq(sinArg), anyDouble())).thenReturn(expected);

        CosFunction cos = new CosFunction(mockSin);
        assertEquals(expected, cos.calculate(x, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq(sinArg), anyDouble());
    }

    @Test
    @DisplayName("Проверка Log2 с Mock Ln")
    void shouldCalculateLog2WithMockLn() {
         
        when(mockLn.calculate(eq(8.0), anyDouble())).thenReturn(2.07944);  
        when(mockLn.calculate(eq(2.0), anyDouble())).thenReturn(0.693147);  

        LogFunction log2 = new LogFunction(mockLn, 2.0);
        assertEquals(3.0, log2.calculate(8.0, PRECISION), DELTA);

        verify(mockLn, times(1)).calculate(eq(8.0), anyDouble());
        verify(mockLn, times(1)).calculate(eq(2.0), anyDouble());
    }

    @ParameterizedTest(name = "log{1}({0}) = {2} with mock")
    @DisplayName("Проверка Log с Mock для всех значений из CSV")
    @CsvFileSource(resources = "/level1/log.csv", numLinesToSkip = 1, delimiter = ',')
    void shouldCalculateLogWithMockForAllValues(double x, double base, double expected) {
        double lnX = expected * Math.log(base);
        double lnBase = Math.log(base);

        lenient().when(mockLn.calculate(eq(x), anyDouble())).thenReturn(lnX);
        lenient().when(mockLn.calculate(eq(base), anyDouble())).thenReturn(lnBase);

        LogFunction log = new LogFunction(mockLn, base);
        assertEquals(expected, log.calculate(x, PRECISION), DELTA);

        if (Math.abs(x - base) < 1e-10) {   
             
            verify(mockLn, times(2)).calculate(eq(x), anyDouble());
        } else {
             
            verify(mockLn, times(1)).calculate(eq(x), anyDouble());
            verify(mockLn, times(1)).calculate(eq(base), anyDouble());
        }

         
        verify(mockLn, times(2)).calculate(anyDouble(), anyDouble());
    }
     

    @Test
    @DisplayName("Проверка, что Cos и Log не влияют друг на друга")
    void shouldNotInteract() {
        CosFunction cos = new CosFunction(spySin);
        LogFunction log2 = new LogFunction(spyLn, 2.0);

        cos.calculate(0.0, PRECISION);
        log2.calculate(2.0, PRECISION);

        verify(spySin, times(1)).calculate(anyDouble(), anyDouble());
        verify(spyLn, times(2)).calculate(anyDouble(), anyDouble());  

        verify(spySin, never()).calculate(eq(2.0), anyDouble());
        verify(spyLn, never()).calculate(eq(0.0), anyDouble());
    }

    @Test
    @DisplayName("Проверка создания нескольких логарифмов")
    void shouldCreateMultipleLogarithms() {
        LogFunction log2 = new LogFunction(spyLn, 2.0);
        LogFunction log3 = new LogFunction(spyLn, 3.0);
        LogFunction log5 = new LogFunction(spyLn, 5.0);
        LogFunction log10 = new LogFunction(spyLn, 10.0);

        log2.calculate(2.0, PRECISION);
        log3.calculate(3.0, PRECISION);
        log5.calculate(5.0, PRECISION);
        log10.calculate(10.0, PRECISION);

         
        verify(spyLn, times(8)).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка обработки исключений в цепочке вызовов")
    void shouldHandleExceptionsInChain() {
        when(mockLn.calculate(eq(2.0), anyDouble()))
                .thenThrow(new IllegalArgumentException("Ошибка в ln"));

        LogFunction log2 = new LogFunction(mockLn, 2.0);

        assertThrows(IllegalArgumentException.class,
                () -> log2.calculate(2.0, PRECISION));

        verify(mockLn, times(1)).calculate(eq(2.0), anyDouble());
    }
}