package org.example;

import org.example.level0.MathFunction;
import org.example.level0.SinFunction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication; // Нужно добавить этот импорт


@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

//        MathFunction sin = new SinFunction();
//        CsvWriter writer = new CsvWriter("sin_results.csv", ',');
//        writer.write(sin, -3.0, 3.0, 0.5, 0.0001);
    }

}