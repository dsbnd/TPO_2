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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class IntegrationTest {

    private static final double PRECISION = 0.0001;
    private static final double DELTA = 1e-4;

    @Mock private SinFunction mockSin;
    @Mock private LnFunction mockLn;

    private CosFunction cosWithMock;
    private CscFunction cscWithMock;
    private SecFunction secWithMock;
    private TanFunction tanWithMock;

    private LogFunction log2WithMock;
    private LogFunction log3WithMock;
    private LogFunction log5WithMock;
    private LogFunction log10WithMock;

    private LeftBranchFunction leftBranch;
    private RightBranchFunction rightBranch;

    private SystemOfFunctions system;

    private static Map<Double, Double> sinValues = new HashMap<>();
    private static Map<Double, Double> cosValues = new HashMap<>();
    private static Map<Double, Double> tanValues = new HashMap<>();
    private static Map<Double, Double> cscValues = new HashMap<>();
    private static Map<Double, Double> secValues = new HashMap<>();
    private static Map<Double, Double> lnValues = new HashMap<>();
    private static Map<Double, Double> log2Values = new HashMap<>();
    private static Map<Double, Double> log3Values = new HashMap<>();
    private static Map<Double, Double> log5Values = new HashMap<>();
    private static Map<Double, Double> log10Values = new HashMap<>();
    private static Map<Double, Double> systemExpectedValues = new HashMap<>();

    @BeforeAll
    static void loadAllCsvValues() throws IOException {
        loadCsvValues("src/test/resources/sin_values.csv", sinValues);
        loadCsvValues("src/test/resources/cos_values.csv", cosValues);
        loadCsvValues("src/test/resources/tan_values.csv", tanValues);
        loadCsvValues("src/test/resources/csc_values.csv", cscValues);
        loadCsvValues("src/test/resources/sec_values.csv", secValues);
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

                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    try {
                        double x = Double.parseDouble(parts[0].trim());
                        String yStr = parts[1].trim();

                        if (!"NaN".equals(yStr)) {
                            map.put(x, Double.parseDouble(yStr));
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка парсинга в файле " + filePath + ": " + line);
                    }
                }
            }
        }
    }

    @BeforeEach
    void setUp() {
        cosWithMock = new CosFunction(mockSin);
        cscWithMock = new CscFunction(mockSin);
        secWithMock = new SecFunction(cosWithMock);
        tanWithMock = new TanFunction(mockSin, cosWithMock);

        log2WithMock = new LogFunction(mockLn, 2.0);
        log3WithMock = new LogFunction(mockLn, 3.0);
        log5WithMock = new LogFunction(mockLn, 5.0);
        log10WithMock = new LogFunction(mockLn, 10.0);

        leftBranch = new LeftBranchFunction(
                mockSin, cosWithMock, cscWithMock, secWithMock, tanWithMock
        );

        rightBranch = new RightBranchFunction(
                mockLn, log2WithMock, log3WithMock, log5WithMock, log10WithMock
        );

        system = new SystemOfFunctions(leftBranch, rightBranch);
    }

    @Test
    void testAllPointsWithMocks() {
        for (Map.Entry<Double, Double> entry : systemExpectedValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();

            if (x < 0) {
                double sinVal = sinValues.get(x);
                double xPlusPiOver2 = x + Math.PI / 2;
                double sinValPlus = cosValues.get(x);

                if (sinVal == 0 && (Math.abs(x) % Math.PI == 0)) {
                    when(mockSin.calculate(x, PRECISION)).thenReturn(sinVal);
                    when(mockSin.calculate(xPlusPiOver2, PRECISION)).thenReturn(sinValPlus);

                    assertThrows(ArithmeticException.class, () -> system.calculate(x, PRECISION));
                } else {
                    when(mockSin.calculate(x,PRECISION)).thenReturn(sinVal);
                    when(mockSin.calculate(xPlusPiOver2, PRECISION)).thenReturn(sinValPlus);

                    double actual = system.calculate(x, PRECISION);
                    assertEquals(expected, actual, DELTA, "Несовпадение при x = " + x);
                }

            } else if (x > 0) {
                double lnVal = lnValues.get(x);
                double ln2 = lnValues.get(2.0);
                double ln3 = lnValues.get(3.0);
                double ln5 = lnValues.get(5.0);
                double ln10 = lnValues.get(10.0);

                if (x == 1.0) {
                    when(mockLn.calculate(x, PRECISION)).thenReturn(0.0);
                    when(mockLn.calculate(2.0, PRECISION)).thenReturn(ln2);
                    when(mockLn.calculate(3.0, PRECISION)).thenReturn(ln3);
                    when(mockLn.calculate(5.0, PRECISION)).thenReturn(ln5);
                    when(mockLn.calculate(10.0,PRECISION)).thenReturn(ln10);

                    assertThrows(ArithmeticException.class, () -> system.calculate(x, PRECISION));
                } else {
                    when(mockLn.calculate(x, PRECISION)).thenReturn(lnVal);
                    when(mockLn.calculate(2.0, PRECISION)).thenReturn(ln2);
                    when(mockLn.calculate(3.0, PRECISION)).thenReturn(ln3);
                    when(mockLn.calculate(5.0, PRECISION)).thenReturn(ln5);
                    when(mockLn.calculate(10.0, PRECISION)).thenReturn(ln10);

                    double actual = system.calculate(x, PRECISION);
                    assertEquals(expected, actual, 0.05, "Несовпадение при x = " + x);
                }
            }
        }
    }
}