package org.example;

import org.example.level0.LnFunction;
import org.example.level0.SinFunction;
import org.example.level0.MathFunction;
import org.example.level1.CosFunction;
import org.example.level1.LogFunction;
import org.example.level2.CscFunction;
import org.example.level2.SecFunction;
import org.example.level2.TanFunction;
import org.example.level3.LeftBranchFunction;
import org.example.level3.RightBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysIntegrationTest {

    private static final double PRECISION = 0.0001;
    private static final double DELTA = 1e-3;

    @Mock private MathFunction mockSin;
    @Mock private MathFunction mockCos;
    @Mock private MathFunction mockCsc;
    @Mock private MathFunction mockSec;
    @Mock private MathFunction mockTan;
    @Mock private MathFunction mockLn;
    @Mock private MathFunction mockLog2;
    @Mock private MathFunction mockLog3;
    @Mock private MathFunction mockLog5;
    @Mock private MathFunction mockLog10;

    private LeftBranchFunction leftBranch;
    private RightBranchFunction rightBranch;
    private SystemOfFunctions system;

    @BeforeEach
    void setUp() {
        leftBranch = new LeftBranchFunction(mockSin, mockCos, mockCsc, mockSec, mockTan);
        rightBranch = new RightBranchFunction(mockLn, mockLog2, mockLog3, mockLog5, mockLog10);
        system = new SystemOfFunctions(leftBranch, rightBranch);
    }

    @Test
    void testLeftBranchWithMocks() {

        double x = -2.0;

        when(mockSin.calculate(eq(x), eq(PRECISION))).thenReturn(-0.909297);
        when(mockCos.calculate(eq(x), eq(PRECISION))).thenReturn(-0.416147);
        when(mockCsc.calculate(eq(x), eq(PRECISION))).thenReturn(-1.09975);
        when(mockSec.calculate(eq(x), eq(PRECISION))).thenReturn(-2.403);
        when(mockTan.calculate(eq(x), eq(PRECISION))).thenReturn(2.185);

        double expected = -5.606679233133033;
        double actual = system.calculate(x, PRECISION);

        assertEquals(expected, actual, DELTA);

        verify(mockSin, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockCos, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockCsc, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockSec, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockTan, times(1)).calculate(eq(x), eq(PRECISION));
    }

    @Test
    void testRightBranchWithMocks() {
        double x = 2.0;

        when(mockLn.calculate(eq(x), eq(PRECISION))).thenReturn(0.693147);
        when(mockLog2.calculate(eq(x), eq(PRECISION))).thenReturn(1.0);
        when(mockLog3.calculate(eq(x), eq(PRECISION))).thenReturn(0.63093);
        when(mockLog5.calculate(eq(x), eq(PRECISION))).thenReturn(0.430677);
        when(mockLog10.calculate(eq(x), eq(PRECISION))).thenReturn(0.30103);

        double expected = 0.048633545188499036;
        double actual = system.calculate(x, PRECISION);

        assertEquals(expected, actual, DELTA);

        verify(mockLn, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockLog2, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockLog3, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockLog5, times(1)).calculate(eq(x), eq(PRECISION));
        verify(mockLog10, times(1)).calculate(eq(x), eq(PRECISION));
    }

    @Test
    void testLeftBranchSpecialPointsWithMocks() {
        double x = Math.PI / 2;
        assertThrows(ArithmeticException.class, () -> system.calculate(x, PRECISION));
    }

    @Test
    void testRightBranchSpecialPointsWithMocks() {
        double x = 1.0;
        when(mockLn.calculate(eq(x), eq(PRECISION))).thenReturn(0.0);
        when(mockLog2.calculate(eq(x), eq(PRECISION))).thenReturn(0.0);
        when(mockLog3.calculate(eq(x), eq(PRECISION))).thenReturn(0.0);
        when(mockLog5.calculate(eq(x), eq(PRECISION))).thenReturn(0.0);
        when(mockLog10.calculate(eq(x), eq(PRECISION))).thenReturn(0.0);

        assertThrows(ArithmeticException.class, () -> system.calculate(x, PRECISION));
    }

    // Здесь мы используем реальные реализации всех функций
    // и сравниваем с эталонными значениями из Wolfram

    @Test
    void testLeftBranchWithRealImplementations() {
        SinFunction realSin = new SinFunction();
        CosFunction realCos = new CosFunction(realSin);
        CscFunction realCsc = new CscFunction(realSin);
        SecFunction realSec = new SecFunction(realCos);
        TanFunction realTan = new TanFunction(realSin, realCos);

        LeftBranchFunction realLeft = new LeftBranchFunction(realSin, realCos, realCsc, realSec, realTan);

        RightBranchFunction mockRight = mock(RightBranchFunction.class);
        SystemOfFunctions realSystem = new SystemOfFunctions(realLeft, mockRight);

        double[] testPoints = {-2.0, -1.5, -1.0, -0.5};
        double[] expectedValues = {-5.606679233133033, -198.70864114027503, 367326.50287362747, 9.0264040065010749E18};

        for (int i = 0; i < testPoints.length; i++) {
            double actual = realSystem.calculate(testPoints[i], PRECISION);
            assertEquals(expectedValues[i], actual, DELTA,
                    "Несовпадение при x = " + testPoints[i]);
        }

        verify(mockRight, never()).calculate(anyDouble(), anyDouble());
    }

    @Test
    void testRightBranchWithRealImplementations() {
        LnFunction realLn = new LnFunction();
        LogFunction realLog2 = new LogFunction(realLn, 2.0);
        LogFunction realLog3 = new LogFunction(realLn, 3.0);
        LogFunction realLog5 = new LogFunction(realLn, 5.0);
        LogFunction realLog10 = new LogFunction(realLn, 10.0);

        RightBranchFunction realRight = new RightBranchFunction(realLn, realLog2, realLog3, realLog5, realLog10);

        LeftBranchFunction mockLeft = mock(LeftBranchFunction.class);
        SystemOfFunctions realSystem = new SystemOfFunctions(mockLeft, realRight);

        double[] testPoints = {0.5, 1.5, 2.0, 2.5, 3.0};
        double[] expectedValues = {-0.048633304574179796, 0.009734633633955533,
                0.048633545188499036, 0.11234646416763512, 0.19363902385223547};

        for (int i = 0; i < testPoints.length; i++) {
            double actual = realSystem.calculate(testPoints[i], PRECISION);
            assertEquals(expectedValues[i], actual, DELTA,
                    "Несовпадение при x = " + testPoints[i]);
        }

        verify(mockLeft, never()).calculate(anyDouble(), anyDouble());
    }

    // Здесь мы используем CSV файлы с эталонными значениями

    @Test
    void testAgainstCsvValues() throws IOException {
        Map<Double, Double> csvValues = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(
                new FileReader("src/test/resources/wolfram_res.csv"))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                csvValues.put(x, y);
            }
        }

        SinFunction realSin = new SinFunction();
        LnFunction realLn = new LnFunction();

        CosFunction realCos = new CosFunction(realSin);
        CscFunction realCsc = new CscFunction(realSin);
        SecFunction realSec = new SecFunction(realCos);
        TanFunction realTan = new TanFunction(realSin, realCos);

        LogFunction realLog2 = new LogFunction(realLn, 2.0);
        LogFunction realLog3 = new LogFunction(realLn, 3.0);
        LogFunction realLog5 = new LogFunction(realLn, 5.0);
        LogFunction realLog10 = new LogFunction(realLn, 10.0);

        LeftBranchFunction realLeft = new LeftBranchFunction(realSin, realCos, realCsc, realSec, realTan);
        RightBranchFunction realRight = new RightBranchFunction(realLn, realLog2, realLog3, realLog5, realLog10);

        SystemOfFunctions realSystem = new SystemOfFunctions(realLeft, realRight);

        for (Map.Entry<Double, Double> entry : csvValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();

            if (Double.isNaN(expected)) {
                assertThrows(ArithmeticException.class,
                        () -> realSystem.calculate(x, PRECISION));
            } else {
                double actual = realSystem.calculate(x, PRECISION);
                assertEquals(expected, actual, DELTA,
                        "Несовпадение с CSV при x = " + x);
            }
        }
    }
}