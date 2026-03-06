package org.example;
import org.example.CsvWriter;
import org.example.SystemOfFunctions;
import org.example.level0.LnFunction;
import org.example.level0.MathFunction;
import org.example.level0.SinFunction;
import org.example.level1.CosFunction;
import org.example.level1.LogFunction;
import org.example.level2.CscFunction;
import org.example.level2.SecFunction;
import org.example.level2.TanFunction;
import org.example.level3.LeftBranchFunction;
import org.example.level3.RightBranchFunction;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD

            SinFunction sin = new SinFunction();
            LnFunction ln = new LnFunction();

            CosFunction cos = new CosFunction(sin);

            CscFunction csc = new CscFunction(sin);
            SecFunction sec = new SecFunction(cos);
            TanFunction tan = new TanFunction(sin, cos);

            LogFunction log2 = new LogFunction(ln, 2.0);
            LogFunction log3 = new LogFunction(ln, 3.0);
            LogFunction log5 = new LogFunction(ln, 5.0);
            LogFunction log10 = new LogFunction(ln, 10.0);

            LeftBranchFunction leftBranch = new LeftBranchFunction(sin, cos, csc, sec, tan);
            RightBranchFunction rightBranch = new RightBranchFunction(ln, log2, log3, log5, log10);

            SystemOfFunctions system = new SystemOfFunctions(leftBranch, rightBranch);

            new CsvWriter("src/test/resources/sin_values.csv", ",").write(sin, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/cos_values.csv", ",").write(cos, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/tan_values.csv", ",").write(tan, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/csc_values.csv", ",").write(csc, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/sec_values.csv", ",").write(sec, -5.0, 5.0, 0.5, 0.0001);

            new CsvWriter("src/test/resources/ln_values.csv", ",").write(ln, 0.1, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/log2_values.csv", ",").write(log2, 0.1, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/log3_values.csv", ",").write(log3, 0.1, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/log5_values.csv", ",").write(log5, 0.1, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/log10_values.csv", ",").write(log10, 0.1, 5.0, 0.5, 0.0001);

            new CsvWriter("src/test/resources/system_expected.csv", ",").write(system, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/test_points.csv", ",").write(system, -5.0, 5.0, 0.5, 0.0001);
            new CsvWriter("src/test/resources/system_detailed.csv", ",").write(system, -5.0, 5.0, 0.1, 0.0001);

        }
=======
        SinFunction sin = new SinFunction();
        LnFunction ln = new LnFunction();
        CosFunction cos = new CosFunction(sin);
        CscFunction csc = new CscFunction(sin);
        SecFunction sec = new SecFunction(cos);
        TanFunction tan = new TanFunction(sin, cos);
        LogFunction log2 = new LogFunction(ln, 2.0);
        LogFunction log3 = new LogFunction(ln, 3.0);
        LogFunction log5 = new LogFunction(ln, 5.0);
        LogFunction log10 = new LogFunction(ln, 10.0);
        LeftBranchFunction leftBranch = new LeftBranchFunction(sin, cos, csc, sec, tan);
        RightBranchFunction rightBranch = new RightBranchFunction(ln, log2, log3, log5, log10);
        SystemOfFunctions system = new SystemOfFunctions(leftBranch, rightBranch);
        System.out.println("Генерация CSV файлов...");
        new CsvWriter("src/test/resources/sin_values.csv", ",").write(sin, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/cos_values.csv", ",").write(cos, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/tan_values.csv", ",").write(tan, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/csc_values.csv", ",").write(csc, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/sec_values.csv", ",").write(sec, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/ln_values.csv", ",").write(ln, 0.1, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/log2_values.csv", ",").write(log2, 0.1, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/log3_values.csv", ",").write(log3, 0.1, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/log5_values.csv", ",").write(log5, 0.1, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/log10_values.csv", ",").write(log10, 0.1, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/system_expected.csv", ",").write(system, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/test_points.csv", ",").write(system, -5.0, 5.0, 0.5, 0.0001);
        new CsvWriter("src/test/resources/system_detailed.csv", ",").write(system, -5.0, 5.0, 0.1, 0.0001);
        System.out.println("Все CSV файлы созданы в папке src/test/resources/");
        System.out.println("\nСгенерировано строк в system_expected.csv:");
        System.out.println("  от -5.0 до 5.0 с шагом 0.5: " +
                ((5.0 - (-5.0)) / 0.5 + 1) + " строк");
>>>>>>> 2de6cfc (commets)
    }


