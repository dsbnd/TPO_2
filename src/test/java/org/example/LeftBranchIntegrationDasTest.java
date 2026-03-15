package org.example;


import org.example.level0.SinFunction;

import org.example.level1.CosFunction;

import org.example.level2.CscFunction;

import org.example.level2.SecFunction;

import org.example.level2.TanFunction;

import org.example.level3.LeftBranchFunction;

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

import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)

@MockitoSettings(strictness = Strictness.LENIENT)

class LeftBranchIntegrationDasTest {


    private static final double PRECISION = 0.0001;


    // Наши "суфлеры"

    @Mock private SinFunction mockSin;

    @Mock private CscFunction mockCsc;

    @Mock private SecFunction mockSec;

    @Mock private TanFunction mockTan;


    private static Map<Double, Double> sinValues = new HashMap<>();

    private static Map<Double, Double> cosValues = new HashMap<>();

    private static Map<Double, Double> tanValues = new HashMap<>();

    private static Map<Double, Double> cscValues = new HashMap<>();

    private static Map<Double, Double> secValues = new HashMap<>();

    private static Map<Double, Double> systemExpectedValues = new HashMap<>();


    @BeforeAll

    static void loadAllCsvValues() throws IOException {

        loadCsvValues("src/test/resources/sin_values.csv", sinValues);

        loadCsvValues("src/test/resources/cos_values.csv", cosValues);

        loadCsvValues("src/test/resources/tan_values.csv", tanValues);

        loadCsvValues("src/test/resources/csc_values.csv", cscValues);

        loadCsvValues("src/test/resources/sec_values.csv", secValues);

        loadCsvValues("src/test/resources/all_results.csv", systemExpectedValues);

    }


    private static void loadCsvValues(String filePath, Map<Double, Double> map) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            reader.readLine();

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                // Регулярка "[,;]" позволяет читать файлы и с запятыми, и с точками с запятой

                String[] parts = line.split("[,;]");

                if (parts.length >= 2) {

                    try {

                        double x = Double.parseDouble(parts[0].trim());

                        String yStr = parts[1].trim();

                        if (!"NaN".equals(yStr)) {

                            map.put(x, Double.parseDouble(yStr));

                        } else {

                            map.put(x, Double.NaN); // Сохраняем NaN для проверки ошибок

                        }

                    } catch (NumberFormatException ignored) {}

                }

            }

        }

    }


    // =========================================================================

    // 5 ШАГОВ ВОСХОДЯЩЕЙ ИНТЕГРАЦИИ (BOTTOM-UP)

    // =========================================================================


    @Test

    void step1_RealCos_MockOthers() {

        CosFunction realCos = new CosFunction(mockSin);

        LeftBranchFunction branch = new LeftBranchFunction(mockSin, realCos, mockCsc, mockSec, mockTan);

        runBranchTest(branch);

    }


    @Test

    void step2_RealCos_RealSec_MockOthers() {

        CosFunction realCos = new CosFunction(mockSin);

        SecFunction realSec = new SecFunction(realCos);

        LeftBranchFunction branch = new LeftBranchFunction(mockSin, realCos, mockCsc, realSec, mockTan);

        runBranchTest(branch);

    }


    @Test

    void step3_RealCos_RealSec_RealCsc_MockOthers() {

        CosFunction realCos = new CosFunction(mockSin);

        SecFunction realSec = new SecFunction(realCos);

        CscFunction realCsc = new CscFunction(mockSin);

        LeftBranchFunction branch = new LeftBranchFunction(mockSin, realCos, realCsc, realSec, mockTan);

        runBranchTest(branch);

    }


    @Test

    void step4_RealCos_RealSec_RealCsc_RealTan_MockSinOnly() {

        CosFunction realCos = new CosFunction(mockSin);

        SecFunction realSec = new SecFunction(realCos);

        CscFunction realCsc = new CscFunction(mockSin);

        TanFunction realTan = new TanFunction(mockSin, realCos);

        LeftBranchFunction branch = new LeftBranchFunction(mockSin, realCos, realCsc, realSec, realTan);

        runBranchTest(branch);

    }


    @Test

    void step5_AllRealObjects() {

        SinFunction realSin = new SinFunction();

        CosFunction realCos = new CosFunction(realSin);

        SecFunction realSec = new SecFunction(realCos);

        CscFunction realCsc = new CscFunction(realSin);

        TanFunction realTan = new TanFunction(realSin, realCos);

        LeftBranchFunction branch = new LeftBranchFunction(realSin, realCos, realCsc, realSec, realTan);

        runBranchTest(branch);

    }


    // =========================================================================

    // ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ (ЧТОБЫ НЕ ДУБЛИРОВАТЬ КОД)

    // =========================================================================


    private void runBranchTest(LeftBranchFunction branch) {

        for (Map.Entry<Double, Double> entry : systemExpectedValues.entrySet()) {

            double x = entry.getKey();

            if (x > 0) continue; // Тестируем только левую ветку


            double expected = entry.getValue();

            setupMocksForLeftBranch(x);


            if (Double.isNaN(expected)) {

                assertThrows(ArithmeticException.class, () -> branch.calculate(x, PRECISION));

            } else {

                double actual = branch.calculate(x, PRECISION);

                double allowedDelta = Math.max(PRECISION, Math.abs(expected * 0.05));

                assertEquals(expected, actual, allowedDelta, "Несовпадение при x = " + x);

            }

        }

    }


    private void setupMocksForLeftBranch(double x) {

        double sinVal = sinValues.getOrDefault(x, 0.0);

        double sinValPlus = cosValues.getOrDefault(x, 0.0);

        

        when(mockSin.calculate(eq(x), eq(PRECISION))).thenReturn(sinVal);

        when(mockSin.calculate(eq(x + Math.PI / 2), eq(PRECISION))).thenReturn(sinValPlus);


        if (Double.isNaN(systemExpectedValues.getOrDefault(x, 0.0))) {

            when(mockCsc.calculate(eq(x), eq(PRECISION))).thenThrow(new ArithmeticException());

            when(mockSec.calculate(eq(x), eq(PRECISION))).thenThrow(new ArithmeticException());

            when(mockTan.calculate(eq(x), eq(PRECISION))).thenThrow(new ArithmeticException());

        } else {

            when(mockCsc.calculate(eq(x), eq(PRECISION))).thenReturn(cscValues.getOrDefault(x, 0.0));

            when(mockSec.calculate(eq(x), eq(PRECISION))).thenReturn(secValues.getOrDefault(x, 0.0));

            when(mockTan.calculate(eq(x), eq(PRECISION))).thenReturn(tanValues.getOrDefault(x, 0.0));

        }

    }

}