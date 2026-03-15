package org.example;

import org.example.level0.LnFunction;
import org.example.level1.LogFunction;
import org.example.level3.RightBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RightBranchIntegrationTest {

    private RightBranchFunction branch;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        // Собираем правую ветку исключительно из настоящих объектов
        LnFunction realLn = new LnFunction();
        LogFunction realLog2 = new LogFunction(realLn, 2.0);
        LogFunction realLog3 = new LogFunction(realLn, 3.0);
        LogFunction realLog5 = new LogFunction(realLn, 5.0);
        LogFunction realLog10 = new LogFunction(realLn, 10.0);
        
        branch = new RightBranchFunction(realLn, realLog2, realLog3, realLog5, realLog10);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/all_results.csv", numLinesToSkip = 1, delimiter = ';')
    void testRightBranchWithRealObjects(double x, double expectedResult) {
        if (x <= 0) return; // Правая ветка работает только при X > 0

        if (Double.isNaN(expectedResult)) {
            assertThrows(ArithmeticException.class, () -> branch.calculate(x, PRECISION));
        } else {
            double actual = branch.calculate(x, PRECISION);
            double allowedDelta = Math.max(PRECISION, Math.abs(expectedResult * 0.05));
            assertEquals(expectedResult, actual, allowedDelta, "Несовпадение при x = " + x);
        }
    }
}