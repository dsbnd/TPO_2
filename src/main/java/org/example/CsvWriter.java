package org.example;

import org.example.level0.MathFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {
    private final String filePath;
    private final String delimiter;

    public CsvWriter(String filePath, String delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    public void write(MathFunction function, double xStart, double xEnd, double step, double precision) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("X" + delimiter + "Result");

            for (double x = xStart; x <= xEnd + step / 10; x += step) {
                String resultStr;
                try {
                    double result = function.calculate(x, precision);
                    resultStr = String.valueOf(result);
                } catch (ArithmeticException | IllegalArgumentException e) {
                    resultStr = "NaN"; //*деление на 0
                }

                writer.println(x + delimiter + resultStr);
            }
            System.out.println("Данные в файле");
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}