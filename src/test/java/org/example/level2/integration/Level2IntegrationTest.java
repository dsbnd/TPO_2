package org.example.level2.integration;

import org.example.level0.SinFunction;
import org.example.level1.CosFunction;
import org.example.level2.CscFunction;
import org.example.level2.SecFunction;
import org.example.level2.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.AdditionalMatchers.eq;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Level2IntegrationTest {

    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;

     
    @Spy
    private SinFunction spySin = new SinFunction();   

     
    private CosFunction spyCos;

     
    @Mock
    private SinFunction mockSin;

    @Mock
    private CosFunction mockCos;

    @BeforeEach
    void setUp() {
         
        spyCos = spy(new CosFunction(spySin));
    }

     

    @Test
    @DisplayName("Проверка, что Csc вызывает Sin")
    void shouldCallSinWhenCalculatingCsc() {
        CscFunction csc = new CscFunction(spySin);
        csc.calculate(Math.PI / 6, PRECISION);

        verify(spySin, times(1)).calculate(anyDouble(), anyDouble());
        verify(spySin, times(1)).calculate(eq(Math.PI / 6), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка Csc с Mock Sin")
    void shouldCalculateCscWithMockSin() {
        when(mockSin.calculate(eq(Math.PI / 6), anyDouble())).thenReturn(0.5);

        CscFunction csc = new CscFunction(mockSin);
        assertEquals(2.0, csc.calculate(Math.PI / 6, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq(Math.PI / 6), anyDouble());
    }



    @Test
    @DisplayName("Проверка обработки исключений в Csc")
    void shouldThrowExceptionWhenSinIsZero() {
        when(mockSin.calculate(eq(0.0), anyDouble())).thenReturn(0.0);

        CscFunction csc = new CscFunction(mockSin);
        assertThrows(ArithmeticException.class,
                () -> csc.calculate(0.0, PRECISION));
    }

     

    @Test
    @DisplayName("Проверка, что Sec вызывает Cos")
    void shouldCallCosWhenCalculatingSec() {
        SecFunction sec = new SecFunction(spyCos);
        sec.calculate(0.0, PRECISION);

        verify(spyCos, times(1)).calculate(anyDouble(), anyDouble());
        verify(spyCos, times(1)).calculate(eq(0.0), eq(PRECISION));

         
        verify(spySin, times(1)).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка Sec с Mock Cos")
    void shouldCalculateSecWithMockCos() {
        when(mockCos.calculate(eq(0.0), anyDouble())).thenReturn(1.0);

        SecFunction sec = new SecFunction(mockCos);
        assertEquals(1.0, sec.calculate(0.0, PRECISION), DELTA);

        verify(mockCos, times(1)).calculate(eq(0.0), anyDouble());
    }



    @Test
    @DisplayName("Проверка обработки исключений в Sec")
    void shouldThrowExceptionWhenTanIsZero() {
        when(mockCos.calculate(eq(Math.PI / 2), anyDouble())).thenReturn(0.0);

        SecFunction sec = new SecFunction(mockCos);
        assertThrows(ArithmeticException.class,
                () -> sec.calculate(Math.PI / 2, PRECISION));
    }

     

    @Test
    @DisplayName("Проверка, что Tan вызывает Sin и Cos")
    void shouldCallSinAndCosWhenCalculatingTan() {
        TanFunction tan = new TanFunction(spySin, spyCos);
        tan.calculate(Math.PI / 4, PRECISION);

         
         
         

        verify(spySin, times(2)).calculate(anyDouble(), anyDouble());   
        verify(spyCos, times(1)).calculate(anyDouble(), anyDouble());

        verify(spySin, times(1)).calculate(eq(Math.PI / 4), eq(PRECISION));   
        verify(spyCos, times(1)).calculate(eq(Math.PI / 4), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка Tan с Mock Sin и Cos")
    void shouldCalculateTanWithMocks() {
        when(mockSin.calculate(eq(Math.PI / 4), anyDouble())).thenReturn(0.70710678);
        when(mockCos.calculate(eq(Math.PI / 4), anyDouble())).thenReturn(0.70710678);

        TanFunction tan = new TanFunction(mockSin, mockCos);
        assertEquals(1.0, tan.calculate(Math.PI / 4, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq(Math.PI / 4), anyDouble());
        verify(mockCos, times(1)).calculate(eq(Math.PI / 4), anyDouble());
    }

    @Test
    @DisplayName("Проверка обработки исключений в Tan")
    void shouldThrowExceptionWhenCosIsZero() {
        when(mockCos.calculate(eq(Math.PI / 2), anyDouble())).thenReturn(0.0);

        TanFunction tan = new TanFunction(mockSin, mockCos);
        assertThrows(ArithmeticException.class,
                () -> tan.calculate(Math.PI / 2, PRECISION));
    }

     

    @Test
    @DisplayName("Проверка, что все функции уровня 2 вызывают свои зависимости")
    void shouldCallAllDependencies() {
        CscFunction csc = new CscFunction(spySin);
        SecFunction sec = new SecFunction(spyCos);
        TanFunction tan = new TanFunction(spySin, spyCos);

        csc.calculate(Math.PI / 6, PRECISION);   
        sec.calculate(0.0, PRECISION);           
        tan.calculate(Math.PI / 4, PRECISION);   

         
        verify(spySin, times(4)).calculate(anyDouble(), anyDouble());

         
        verify(spyCos, times(2)).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка полной цепочки вызовов для Sec")
    void shouldVerifyFullChainForSec() {
        SecFunction sec = new SecFunction(spyCos);
        sec.calculate(0.0, PRECISION);

         
        InOrder inOrder = inOrder(spyCos, spySin);

        inOrder.verify(spyCos).calculate(anyDouble(), anyDouble());
        inOrder.verify(spySin).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка полной цепочки вызовов для Tan")
    void shouldVerifyFullChainForTan() {
        double x = Math.PI / 4;
        TanFunction tan = new TanFunction(spySin, spyCos);
        tan.calculate(x, PRECISION);

         
        InOrder inOrder = inOrder(spySin, spyCos);

         
        inOrder.verify(spyCos).calculate(eq(x, 1e-9), eq(PRECISION, 1e-9));

         
         
        inOrder.verify(spySin).calculate(eq(x + Math.PI / 2, 1e-9), eq(PRECISION, 1e-9));

         
        inOrder.verify(spySin).calculate(eq(x, 1e-9), eq(PRECISION, 1e-9));
    }


}