package org.example;

import org.example.level0.MathFunction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

public class CsvWriter {
    private final String filePath;
    private final char delimiter;

    public CsvWriter(String filePath, char delimiter) {
        this.filePath = filePath;
        this.delimiter = delimiter;
    }

    /**
     * Записывает результаты вычисления функции в CSV файл.
     *
     * @param function  любой модуль, реализующий MathFunction
     * @param xStart    начальное значение X
     * @param xEnd      конечное значение X
     * @param step      шаг наращивания X
     * @param precision заданная погрешность для вычислений
     */
    public void write(MathFunction function, double xStart, double xEnd, double step, double precision) {
        // Используем try-with-resources для автоматического закрытия файла
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            
            // Пишем заголовок
            writer.println("X" + delimiter + "Result");

            // Проходим по диапазону с заданным шагом
            // Добавляем небольшую погрешность (step / 10) к xEnd, чтобы не пропустить последнюю точку из-за особенностей double
            for (double x = xStart; x <= xEnd + step / 10; x += step) {
                String resultStr;
                try {
                    double result = function.calculate(x, precision);
                    resultStr = String.valueOf(result);
                } catch (ArithmeticException | IllegalArgumentException e) {
                    // Если попали в ОДЗ (например, деление на ноль)
                    resultStr = "NaN"; 
                }

                // Используем Locale.US, чтобы разделителем дробной части была точка, а не запятая (чтобы не путать с разделителем CSV)
                writer.printf(Locale.US, "%.5f%c%s%n", x, delimiter, resultStr);
            }
            System.out.println("Данные успешно записаны в файл: " + filePath);

        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}