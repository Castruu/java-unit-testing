package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculatorMockTest {

    @Test
    public void teste() {
        Calculator calc = Mockito.mock(Calculator.class);
        Mockito.when(calc.sum(Mockito.eq(1), Mockito.anyInt())).thenReturn(5);

        System.out.println(calc.sum(2, 8));
    }

}
