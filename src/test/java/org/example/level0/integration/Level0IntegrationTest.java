package org.example.level0.integration;

import org.example.level0.LnFunction;
import org.example.level0.SinFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class Level0IntegrationTest {

    private static final double PRECISION = 1e-6;
    private static final double DELTA = 1e-5;
    private static final double PI_2=3.1415926535897932384626433/2;
    private static final double PI = 3.1415926535897932384626433;
    private static final double E =2.71828182845904523536028747;
    @Spy
    private SinFunction spySin;

    @Spy
    private LnFunction spyLn;

    @Mock
    private SinFunction mockSin;

    @Mock
    private LnFunction mockLn;

    @Test
    @DisplayName("Проверка вызова sin с Spy")
    void shouldCallSinMethod() {
        spySin.calculate( PI_2, PRECISION);

        verify(spySin, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spySin, times(1)).calculate(eq( PI_2), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка вызова ln с Spy")
    void shouldCallLnMethod() {
        spyLn.calculate(2.0, PRECISION);

        verify(spyLn, atLeastOnce()).calculate(anyDouble(), anyDouble());
        verify(spyLn, times(1)).calculate(eq(2.0), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка многократных вызовов sin")
    void shouldCallSinMultipleTimes() {
        spySin.calculate(0.0, PRECISION);
        spySin.calculate( PI_2, PRECISION);
        spySin.calculate( PI, PRECISION);

        verify(spySin, times(3)).calculate(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("Проверка вызова ln с невалидным аргументом")
    void shouldThrowExceptionForInvalidLnArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> spyLn.calculate(0.0, PRECISION));

        verify(spyLn, times(1)).calculate(eq(0.0), eq(PRECISION));
    }

    @Test
    @DisplayName("Проверка sin с Mock и заданным поведением")
    void shouldCalculateSinWithMock() {

        when(mockSin.calculate(eq( PI_2), anyDouble())).thenReturn(1.0);
        when(mockSin.calculate(eq(0.0), anyDouble())).thenReturn(0.0);
        when(mockSin.calculate(eq( PI), anyDouble())).thenReturn(0.0);

        assertEquals(1.0, mockSin.calculate( PI_2, PRECISION), DELTA);
        assertEquals(0.0, mockSin.calculate(0.0, PRECISION), DELTA);
        assertEquals(0.0, mockSin.calculate( PI, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq( PI_2), anyDouble());
        verify(mockSin, times(1)).calculate(eq(0.0), anyDouble());
        verify(mockSin, times(1)).calculate(eq( PI), anyDouble());
    }

    @Test
    @DisplayName("Проверка ln с Mock и заданным поведением")
    void shouldCalculateLnWithMock() {
        when(mockLn.calculate(eq(1.0), anyDouble())).thenReturn(0.0);
        when(mockLn.calculate(eq(E), anyDouble())).thenReturn(1.0);
        when(mockLn.calculate(eq(2.0), anyDouble())).thenReturn(0.693147);

        assertEquals(0.0, mockLn.calculate(1.0, PRECISION), DELTA);
        assertEquals(1.0, mockLn.calculate(E, PRECISION), DELTA);
        assertEquals(0.693147, mockLn.calculate(2.0, PRECISION), DELTA);

        verify(mockLn, times(1)).calculate(eq(1.0), anyDouble());
        verify(mockLn, times(1)).calculate(eq(E), anyDouble());
        verify(mockLn, times(1)).calculate(eq(2.0), anyDouble());
    }

    @Test
    @DisplayName("Проверка выброса исключения в моке")
    void shouldThrowExceptionInMock() {

        when(mockLn.calculate(eq(0.0), anyDouble()))
                .thenThrow(new IllegalArgumentException("x must be > 0"));

        assertThrows(IllegalArgumentException.class,
                () -> mockLn.calculate(0.0, PRECISION));

        verify(mockLn, times(1)).calculate(eq(0.0), anyDouble());
    }


    @ParameterizedTest(name = "sin({0}) = {1}")
    @DisplayName("Проверка sin с Mock для всех значений из CSV")
    @CsvFileSource(resources = "/level0/sin.csv", numLinesToSkip = 1, delimiter = ',')
    void shouldCalculateSinWithMockForAllValues(double x, double expected) {
        when(mockSin.calculate(eq(x), anyDouble())).thenReturn(expected);

        assertEquals(expected, mockSin.calculate(x, PRECISION), DELTA);

        verify(mockSin, times(1)).calculate(eq(x), anyDouble());
    }

    @ParameterizedTest(name = "ln({0}) = {1}")
    @DisplayName("Проверка ln с Mock для всех значений из CSV")
    @CsvFileSource(resources = "/level0/ln.csv", numLinesToSkip = 1, delimiter = ',')
    void shouldCalculateLnWithMockForAllValues(double x, double expected) {

        double mockValue = expected;
        when(mockLn.calculate(eq(x), anyDouble())).thenReturn(mockValue);
        assertEquals(expected, mockLn.calculate(x, PRECISION), DELTA);
        verify(mockLn, times(1)).calculate(eq(x), anyDouble());
    }

    @Test
    @DisplayName("Проверка, что sin и ln не влияют друг на друга")
    void shouldNotInteract() {

        spySin.calculate( PI_2, PRECISION);
        spyLn.calculate(2.0, PRECISION);

        verify(spySin, times(1)).calculate(anyDouble(), anyDouble());
        verify(spyLn, times(1)).calculate(anyDouble(), anyDouble());

        verify(spySin, never()).calculate(eq(2.0), anyDouble());
        verify(spyLn, never()).calculate(eq( PI_2), anyDouble());
    }

    @Test
    @DisplayName("Проверка порядка вызовов")
    void shouldVerifyCallOrder() {

        spySin.calculate(0.0, PRECISION);
        spySin.calculate( PI_2, PRECISION);
        spyLn.calculate(1.0, PRECISION);
        spyLn.calculate(2.0, PRECISION);

        InOrder inOrder = inOrder(spySin, spyLn);

        inOrder.verify(spySin).calculate(eq(0.0), anyDouble());
        inOrder.verify(spySin).calculate(eq( PI_2), anyDouble());
        inOrder.verify(spyLn).calculate(eq(1.0), anyDouble());
        inOrder.verify(spyLn).calculate(eq(2.0), anyDouble());
    }
}