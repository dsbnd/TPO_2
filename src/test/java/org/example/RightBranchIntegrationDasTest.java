package org.example;

import org.example.level0.LnFunction;
import org.example.level1.LogFunction;
import org.example.level3.RightBranchFunction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RightBranchIntegrationDasTest {

    private static final double PRECISION = 0.0001;

    @Mock private LnFunction mockLn;
    @Mock private LogFunction mockLog2;
    @Mock private LogFunction mockLog3;
    @Mock private LogFunction mockLog5;
    @Mock private LogFunction mockLog10;

    private static Map<Double, Double> lnValues = new HashMap<>();
    private static Map<Double, Double> log2Values = new HashMap<>();
    private static Map<Double, Double> log3Values = new HashMap<>();
    private static Map<Double, Double> log5Values = new HashMap<>();
    private static Map<Double, Double> log10Values = new HashMap<>();
    private static Map<Double, Double> systemExpectedValues = new HashMap<>();

    @BeforeAll
    static void loadAllCsvValues() throws IOException {
        loadCsvValues("src/test/resources/ln_values.csv", lnValues);
        loadCsvValues("src/test/resources/log2_values.csv", log2Values);
        loadCsvValues("src/test/resources/log3_values.csv", log3Values);
        loadCsvValues("src/test/resources/log5_values.csv", log5Values);
        loadCsvValues("src/test/resources/log10_values.csv", log10Values);
        loadCsvValues("src/test/resources/system_expected.csv", systemExpectedValues);
    }

    private static void loadCsvValues(String filePath, Map<Double, Double> map) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split("[,;]");
                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0].trim());
                        String yStr = parts[1].trim();

                        if (!"NaN".equals(yStr)) {
                            map.put(x, Double.parseDouble(yStr));
                        } else {
                            map.put(x, Double.NaN);
                        }
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
    }

    @Test
    void step1_RealLog2_MockOthers() {
        LogFunction realLog2 = new LogFunction(mockLn, 2.0);
        RightBranchFunction branch = new RightBranchFunction(mockLn, realLog2, mockLog3, mockLog5, mockLog10);
        runBranchTest(branch);
    }

    @Test
    void step2_RealLog2_RealLog3_MockOthers() {
        LogFunction realLog2 = new LogFunction(mockLn, 2.0);
        LogFunction realLog3 = new LogFunction(mockLn, 3.0);
        RightBranchFunction branch = new RightBranchFunction(mockLn, realLog2, realLog3, mockLog5, mockLog10);
        runBranchTest(branch);
    }

    @Test
    void step3_RealLog2_RealLog3_RealLog5_MockOthers() {
        LogFunction realLog2 = new LogFunction(mockLn, 2.0);
        LogFunction realLog3 = new LogFunction(mockLn, 3.0);
        LogFunction realLog5 = new LogFunction(mockLn, 5.0);
        RightBranchFunction branch = new RightBranchFunction(mockLn, realLog2, realLog3, realLog5, mockLog10);
        runBranchTest(branch);
    }

    @Test
    void step4_AllRealLogs_MockLnOnly() {
        LogFunction realLog2 = new LogFunction(mockLn, 2.0);
        LogFunction realLog3 = new LogFunction(mockLn, 3.0);
        LogFunction realLog5 = new LogFunction(mockLn, 5.0);
        LogFunction realLog10 = new LogFunction(mockLn, 10.0);
        RightBranchFunction branch = new RightBranchFunction(mockLn, realLog2, realLog3, realLog5, realLog10);
        runBranchTest(branch);
    }

    @Test
    void step5_AllRealObjects() {
        LnFunction realLn = new LnFunction();
        LogFunction realLog2 = new LogFunction(realLn, 2.0);
        LogFunction realLog3 = new LogFunction(realLn, 3.0);
        LogFunction realLog5 = new LogFunction(realLn, 5.0);
        LogFunction realLog10 = new LogFunction(realLn, 10.0);
        RightBranchFunction branch = new RightBranchFunction(realLn, realLog2, realLog3, realLog5, realLog10);
        runBranchTest(branch);
    }

    private void runBranchTest(RightBranchFunction branch) {
        for (Map.Entry<Double, Double> entry : systemExpectedValues.entrySet()) {
            double x = entry.getKey();

            if (x <= 0) continue;

            double expected = entry.getValue();
            boolean expectNaN = Double.isNaN(expected);

            setupMocksForRightBranch(x, expectNaN);

            if (expectNaN) {
                assertThrows(ArithmeticException.class, () -> branch.calculate(x, PRECISION));
            } else {
                double actual = branch.calculate(x, PRECISION);
                double allowedDelta = Math.max(PRECISION, Math.abs(expected * 0.05));
                assertEquals(expected, actual, allowedDelta, "Несовпадение при x = " + x);
            }
        }
    }

    private void setupMocksForRightBranch(double x, boolean expectNaN) {
        when(mockLn.calculate(eq(2.0), anyDouble())).thenReturn(lnValues.getOrDefault(2.0, Math.log(2.0)));
        when(mockLn.calculate(eq(3.0), anyDouble())).thenReturn(lnValues.getOrDefault(3.0, Math.log(3.0)));
        when(mockLn.calculate(eq(5.0), anyDouble())).thenReturn(lnValues.getOrDefault(5.0, Math.log(5.0)));
        when(mockLn.calculate(eq(10.0), anyDouble())).thenReturn(lnValues.getOrDefault(10.0, Math.log(10.0)));

        if (expectNaN) {
            when(mockLn.calculate(eq(x), anyDouble())).thenThrow(new ArithmeticException());
            when(mockLog2.calculate(eq(x), anyDouble())).thenThrow(new ArithmeticException());
            when(mockLog3.calculate(eq(x), anyDouble())).thenThrow(new ArithmeticException());
            when(mockLog5.calculate(eq(x), anyDouble())).thenThrow(new ArithmeticException());
            when(mockLog10.calculate(eq(x), anyDouble())).thenThrow(new ArithmeticException());
        } else {
            when(mockLn.calculate(eq(x), anyDouble())).thenReturn(lnValues.getOrDefault(x, Math.log(x)));
            when(mockLog2.calculate(eq(x), anyDouble())).thenReturn(log2Values.getOrDefault(x, Math.log(x) / Math.log(2.0)));
            when(mockLog3.calculate(eq(x), anyDouble())).thenReturn(log3Values.getOrDefault(x, Math.log(x) / Math.log(3.0)));
            when(mockLog5.calculate(eq(x), anyDouble())).thenReturn(log5Values.getOrDefault(x, Math.log(x) / Math.log(5.0)));
            when(mockLog10.calculate(eq(x), anyDouble())).thenReturn(log10Values.getOrDefault(x, Math.log(x) / Math.log(10.0)));
        }
    }
}