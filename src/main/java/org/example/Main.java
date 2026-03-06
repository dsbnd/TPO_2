package org.example;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // Нужно добавить этот импорт


@SpringBootApplication
public class Main {
    public static void main(String[] args) {

//        SpringApplication.run(Main.class, args);

        MathFunction sin1 = new SinFunction();
        CsvWriter writer = new CsvWriter("sin_results.csv", " -> ");
        writer.write(sin1, -3.0, 3.0, 0.5, 0.0001);


        CsvWriter writer2 = new CsvWriter("all_results.csv", ";");

        MathFunction sin2 = new SinFunction();
        MathFunction cos = new CosFunction(sin2);
        MathFunction csc = new CscFunction(sin2);
        MathFunction sec = new SecFunction(cos);
        MathFunction tan = new TanFunction(sin2, cos);
        MathFunction leftBranch = new LeftBranchFunction(sin2, cos, csc, sec, tan);

        MathFunction ln = new LnFunction();
        MathFunction log2 = new LogFunction(ln, 2.0);
        MathFunction log3 = new LogFunction(ln, 3.0);
        MathFunction log5 = new LogFunction(ln, 5.0);
        MathFunction log10 = new LogFunction(ln, 10.0);
        MathFunction rightBranch = new RightBranchFunction(ln, log2, log3, log5, log10);

        SystemOfFunctions system = new SystemOfFunctions(leftBranch, rightBranch);
        writer2.write(system, -10.0, 10.0, 0.5, 0.0001);
    }

}