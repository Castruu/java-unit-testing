package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.DivisionByZeroException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {

    private Calculator calc;

    @Before
    public void setup() {
        calc  = new Calculator();
    }

    @Test
    public void twoValuesSumShouldReturn() {
        //Cenário
        int a = 5;
        int b = 3;

        //Ação
        int result = calc.sum(a, b);

        //Verificação
        Assert.assertEquals(8, result);
    }

    @Test
    public void twoValuesSubtractShouldReturn() {
        //Cenário
        int a = 8;
        int b = 5;

        //Ação
        int result = calc.subtract(a, b);

        //Verificação
        Assert.assertEquals(3, result);
    }

    @Test
    public void twoValuesDivisionShouldReturn() {
        //Cenário
        int a = 10;
        int b = 5;

        //Ação
        int result = calc.divide(10, 5);

        //Verificação
        Assert.assertEquals(2, result);
    }

    @Test(expected = DivisionByZeroException.class)
    public void throwsExceptionWhenDividedByZero() {
        //Cenário
        int a = 10;
        int b = 0;

        //Ação
        calc.divide(a, b);

    }

}
