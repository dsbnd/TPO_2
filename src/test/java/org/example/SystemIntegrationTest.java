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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SystemIntegrationTest {

    private static final double PRECISION = 0.0001;
    private static final double DELTA = 1e-4;
    private static final double PI = 3.1415926535897932384626433;

    @Mock private LeftBranchFunction mockLeftBranch;
    @Mock private RightBranchFunction mockRightBranch;


    private SinFunction realSin;
    private LnFunction realLn;
    private CosFunction realCos;
    private CscFunction realCsc;
    private SecFunction realSec;
    private TanFunction realTan;
    private LogFunction realLog2;
    private LogFunction realLog3;
    private LogFunction realLog5;
    private LogFunction realLog10;

    private LeftBranchFunction realLeftBranch;
    private RightBranchFunction realRightBranch;

    private static Map<Double, Double> expectedValues = new HashMap<>();
    private static Map<Double, Double> wolframValues = new HashMap<>();

    @BeforeAll
    static void loadExpectedValues() throws IOException {
        loadCsvValues("src/test/resources/system_expected.csv", expectedValues);
        loadCsvValues("src/test/resources/wolfram_res.csv", wolframValues);
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


                        if ("NaN".equals(yStr) || "Infinity".equals(yStr) || "-Infinity".equals(yStr)) {
                            map.put(x, Double.NaN);
                        } else if (!yStr.isEmpty()) {
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

        realSin = new SinFunction();
        realLn = new LnFunction();
        realCos = new CosFunction(realSin);
        realCsc = new CscFunction(realSin);
        realSec = new SecFunction(realCos);
        realTan = new TanFunction(realSin, realCos);
        realLog2 = new LogFunction(realLn, 2.0);
        realLog3 = new LogFunction(realLn, 3.0);
        realLog5 = new LogFunction(realLn, 5.0);
        realLog10 = new LogFunction(realLn, 10.0);

        realLeftBranch = new LeftBranchFunction(realSin, realCos, realCsc, realSec, realTan);
        realRightBranch = new RightBranchFunction(realLn, realLog2, realLog3, realLog5, realLog10);
    }


    private void setupLeftBranchMockWithCsv() {
        try {
            Map<Double, Double> leftValues = new HashMap<>();
            loadCsvValues("src/test/resources/system_expected.csv", leftValues);

            for (Map.Entry<Double, Double> entry : leftValues.entrySet()) {
                double x = entry.getKey();
                if (x <= 0) {
                    double val = entry.getValue();
                    if (!Double.isNaN(val)) {

                        lenient().doReturn(val).when(mockLeftBranch).calculate(eq(x), anyDouble());
                    } else {

                        lenient().doThrow(new ArithmeticException("Точка разрыва"))
                                .when(mockLeftBranch).calculate(eq(x), anyDouble());
                    }
                }
            }
        } catch (IOException e) {
            fail("Не удалось загрузить CSV для левой ветки");
        }
    }


    private void setupRightBranchMockWithCsv() {
        try {
            Map<Double, Double> rightValues = new HashMap<>();
            loadCsvValues("src/test/resources/system_expected.csv", rightValues);

            for (Map.Entry<Double, Double> entry : rightValues.entrySet()) {
                double x = entry.getKey();
                if (x > 0) {
                    double val = entry.getValue();
                    if (!Double.isNaN(val)) {

                        lenient().doReturn(val).when(mockRightBranch).calculate(eq(x), anyDouble());
                    } else {

                        lenient().doThrow(new ArithmeticException("Точка разрыва"))
                                .when(mockRightBranch).calculate(eq(x), anyDouble());
                    }
                }
            }
        } catch (IOException e) {
            fail("Не удалось загрузить CSV для правой ветки");
        }
    }

    @Test
    @DisplayName("Тест 1: Обе ветки замоканы")
    void testBothBranchesMocked() {
        setupLeftBranchMockWithCsv();
        setupRightBranchMockWithCsv();

        SystemOfFunctions system = new SystemOfFunctions(mockLeftBranch, mockRightBranch);

        for (Map.Entry<Double, Double> entry : expectedValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();

            if (!Double.isNaN(expected)) {
                double result = system.calculate(x, PRECISION);
                assertEquals(expected, result, DELTA, "Несовпадение при x = " + x);
            } else {
                try {
                    system.calculate(x, PRECISION);
                    fail("Ожидалось исключение при x = " + x);
                } catch (Exception e) {
                    assertTrue(e instanceof ArithmeticException,
                            "Ожидался ArithmeticException, но получен: " + e.getClass().getSimpleName());
                }
            }
        }
    }

    @Test
    @DisplayName("Тест 2: Левая ветка реальная, правая замокана")
    void testRealLeftBranch_MockRightBranch() {
        setupRightBranchMockWithCsv();

        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, mockRightBranch);

        for (Map.Entry<Double, Double> entry : expectedValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();

            try {
                double result = system.calculate(x, PRECISION);
                if (!Double.isNaN(expected)) {
                    assertEquals(expected, result, DELTA, "Несовпадение при x = " + x);
                }
            } catch (Exception e) {
                if (x <= 0) {

                    if (Double.isNaN(expected)) {

                        System.out.println("x = " + x + " - получено ожидаемое исключение: " + e.getMessage());
                    } else {
                        fail("Неожиданное исключение при x = " + x + ": " + e.getMessage());
                    }
                } else if (!Double.isNaN(expected)) {
                    fail("Неожиданное исключение при x = " + x + ": " + e.getMessage());
                }
            }
        }
    }


    @Test
    @DisplayName("Тест 3: Левая ветка замокана, правая реальная")
    void testMockLeftBranch_RealRightBranch() {
        setupLeftBranchMockWithCsv();

        SystemOfFunctions system = new SystemOfFunctions(mockLeftBranch, realRightBranch);

        for (Map.Entry<Double, Double> entry : expectedValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();

            try {
                double result = system.calculate(x, PRECISION);
                if (!Double.isNaN(expected)) {
                    assertEquals(expected, result, DELTA, "Несовпадение при x = " + x);
                }
            } catch (Exception e) {
                if (x > 0) {

                    if (Double.isNaN(expected)) {

                        System.out.println("x = " + x + " - получено ожидаемое исключение: " + e.getMessage());
                    } else {
                        fail("Неожиданное исключение при x = " + x + ": " + e.getMessage());
                    }
                } else if (!Double.isNaN(expected)) {
                    fail("Неожиданное исключение при x = " + x + ": " + e.getMessage());
                }
            }
        }
    }


    @Test
    @DisplayName("Тест 4: Обе ветки реальные - финальная интеграция")
    void testBothBranchesReal() {
        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, realRightBranch);

        int totalPoints = 0;
        int passedPoints = 0;

        for (Map.Entry<Double, Double> entry : expectedValues.entrySet()) {
            double x = entry.getKey();
            double expected = entry.getValue();
            totalPoints++;

            try {
                double result = system.calculate(x, PRECISION);

                if (Double.isNaN(expected)) {
                    fail("Ожидалось исключение при x = " + x + ", но получен результат: " + result);
                } else {
                    assertEquals(expected, result, DELTA, "Несовпадение при x = " + x);
                    passedPoints++;
                    System.out.println("x = " + x + " -> " + result + " (expected: " + expected + ")");
                }
            } catch (Exception e) {
                if (!Double.isNaN(expected)) {
                    System.err.println("x = " + x + " - неожиданное исключение: " + e.getMessage());
                } else {
                    passedPoints++;
                    System.out.println("x = " + x + " - получено ожидаемое исключение: " + e.getClass().getSimpleName());
                }
            }
        }

        assertEquals(totalPoints, passedPoints, "Не все точки прошли успешно");
    }


    @Test
    @DisplayName("Тест 5: Проверка границы между ветками (x = 0)")
    void testBoundaryBetweenBranches() {
        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, realRightBranch);

        double x = 0.0;
        assertThrows(ArithmeticException.class,
                () -> system.calculate(x, PRECISION),
                "При x = 0 должно быть исключение (деление на ноль в csc)");

        double smallPositive = 1e-10;
        try {
            double result = system.calculate(smallPositive, PRECISION);
            System.out.println("x = " + smallPositive + " (правая ветка) -> " + result);
            assertFalse(Double.isNaN(result));
        } catch (Exception e) {
            System.out.println("x = " + smallPositive + " - исключение: " + e.getMessage());
        }

        double smallNegative = -1e-10;
        try {
            double result = system.calculate(smallNegative, PRECISION);
            System.out.println("x = " + smallNegative + " (левая ветка) -> " + result);
            assertFalse(Double.isNaN(result));
        } catch (Exception e) {
            System.out.println("x = " + smallNegative + " - исключение: " + e.getMessage());
        }
    }


    @Test
    @DisplayName("Тест 6: Проверка конкретных значений")
    void testSpecificValuesFromCsv() {
        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, realRightBranch);

        double[] testPoints = {-2.0, -1.5, -1.0, -0.5, 0.5, 1.5, 2.0, 2.5, 3.0};

        for (double x : testPoints) {
            if (expectedValues.containsKey(x)) {
                double expected = expectedValues.get(x);
                try {
                    double result = system.calculate(x, PRECISION);
                    assertEquals(expected, result, DELTA, "Несовпадение при x = " + x);
                    System.out.println("x = " + x + " -> " + result + " (expected: " + expected + ")");
                } catch (Exception e) {
                    if (!Double.isNaN(expected)) {
                        fail("Неожиданное исключение при x = " + x + ": " + e.getMessage());
                    } else {
                        System.out.println("x = " + x + " - получено ожидаемое исключение");
                    }
                }
            }
        }
    }


    @Test
    @DisplayName("Тест 7: Сравнение всех реальных объектов с Wolfram")
    void testCompareWithWolframCsv() {
        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, realRightBranch);

        int totalPoints = 0;
        int passedPoints = 0;
        double maxDeviation = 0.0;
        double sumDeviation = 0.0;
        int discontinuityPoints = 0;


        for (Map.Entry<Double, Double> entry : wolframValues.entrySet()) {
            double x = entry.getKey();
            double wolframValue = entry.getValue();
            totalPoints++;

            try {
                double result = system.calculate(x, PRECISION);

                if (Double.isNaN(wolframValue)) {

                    discontinuityPoints++;
                } else {

                    double deviation = Math.abs(result - wolframValue);
                    sumDeviation += deviation;
                    maxDeviation = Math.max(maxDeviation, deviation);

                    if (deviation <= DELTA) {
                        passedPoints++;
                    } else {
                        System.err.println("x = " + x + " - большое отклонение: " +
                                result + " vs " + wolframValue + " (откл: " + deviation + ")");
                    }
                }
            } catch (Exception e) {
                if (Double.isNaN(wolframValue)) {

                    passedPoints++;
                    discontinuityPoints++;
                    System.out.println("x = " + x + " - получено исключение (Wolfram: NaN)");
                } else {

                    System.err.println("x = " + x + " - неожиданное исключение: " + e.getMessage());
                }
            }
        }


        double avgDeviation = passedPoints > 0 ? sumDeviation / passedPoints : 0;
        double successRate = totalPoints - discontinuityPoints > 0
                ? (passedPoints * 100.0) / (totalPoints - discontinuityPoints)
                : 0;



        assertTrue(successRate > 95.0,
                "Слишком низкая точность: " + String.format("%.2f", successRate) + "%");
    }


    @ParameterizedTest(name = "x = {0}, Wolfram = {1}")
    @DisplayName("Тест 8: Параметризованное сравнение с Wolfram Alpha")
    @CsvFileSource(resources = "/wolfram_res.csv", numLinesToSkip = 1, delimiter = ',')
    void testParameterizedWithWolframCsv(double x, String wolframStr) {
        SystemOfFunctions system = new SystemOfFunctions(realLeftBranch, realRightBranch);


        boolean isDiscontinuity = false;

        if ("NaN".equals(wolframStr) || "Infinity".equals(wolframStr) || "-Infinity".equals(wolframStr)) {
            isDiscontinuity = true;
        }

        if (isDiscontinuity) {

            assertThrows(Exception.class,
                    () -> system.calculate(x, PRECISION),
                    "В точке x = " + x + " должно быть исключение (Wolfram: NaN)");
            System.out.println("x = " + x + " - получено исключение (Wolfram: NaN)");
        } else {

            double wolframValue = Double.parseDouble(wolframStr);
            double result = system.calculate(x, PRECISION);
            assertEquals(wolframValue, result, DELTA,
                    "Несовпадение с Wolfram при x = " + x);
            System.out.println("x = " + x + " -> " + result + " (Wolfram: " + wolframValue + ")");
        }
    }
}