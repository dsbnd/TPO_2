import org.example.level0.MathFunction;
import org.example.level3.LeftBranchFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeftBranchFunctionTest {

    @Mock private MathFunction sinMock;
    @Mock private MathFunction cosMock;
    @Mock private MathFunction cscMock;
    @Mock private MathFunction secMock;
    @Mock private MathFunction tanMock;

    private LeftBranchFunction leftBranch;
    private static final double PRECISION = 0.0001;

    @BeforeEach
    void setUp() {
        // Собираем ветку, подсовывая ей моки тригонометрии
        leftBranch = new LeftBranchFunction(sinMock, cosMock, cscMock, secMock, tanMock);
    }

    @Test
    void shouldCalculateCorrectlyForMinusPiOver4() {
        double x = -0.7853; // -пи/4

        // 1. ОБУЧАЕМ МОКИ РЕАЛЬНОЙ МАТЕМАТИКЕ (данные из Wolfram)
        when(sinMock.calculate(x, PRECISION)).thenReturn(-0.707037);
        when(cosMock.calculate(x, PRECISION)).thenReturn(0.707176);
        when(cscMock.calculate(x, PRECISION)).thenReturn(-1.41435);
        when(secMock.calculate(x, PRECISION)).thenReturn(1.41407);
        when(tanMock.calculate(x, PRECISION)).thenReturn(-0.999804);

        // 2. ВЫЗЫВАЕМ НАШУ ЖУТКУЮ ФОРМУЛУ
        double actualResult = leftBranch.calculate(x, PRECISION);

        // 3. ПРОВЕРЯЕМ ИТОГОВЫЙ РЕЗУЛЬТАТ (тоже из Wolfram)
        double expectedResult = 5.7592407;
        
        // Сравниваем с дельтой (погрешностью), так как работаем с double
        assertEquals(expectedResult, actualResult, 0.00001); 
    }
}