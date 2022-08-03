package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

public class CalculatorMockTest {

    @Mock
    private Calculator calculator;

    @Spy
    private Calculator calculatorSpy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldShowDifferenceBetweenMockAndSpy() {
        Mockito.when(calculator.sum(1, 2)).thenReturn(8);
        //Mockito.when(calculatorSpy.sum(1, 2)).thenReturn(8);
        Mockito.doReturn(5).when(calculatorSpy).sum(1, 2);
       // Mockito.doNothing().when(calculatorSpy).print();

        System.out.println("Mock: " + calculator.sum(1, 2));
        System.out.println("Spy: " + calculatorSpy.sum(1, 2));

        System.out.println("Mock");
        System.out.println("Spy");

    }

    @Test
    public void teste() {
        Calculator calc = Mockito.mock(Calculator.class);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        Mockito.when(calc.sum(argumentCaptor.capture(), argumentCaptor.capture())).thenReturn(5);

        Assert.assertEquals(5, (calc.sum(2, 8)));
        System.out.println(argumentCaptor.getAllValues());
    }

}
