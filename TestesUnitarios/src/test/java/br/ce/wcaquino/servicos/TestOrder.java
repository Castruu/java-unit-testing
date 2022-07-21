package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;


// O ideal é deixar os testes independentes
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestOrder {

    public static int count = 0;

    @Test
    public void start() {
        count = 1;
    }

    @Test
    public void verify() {
        Assert.assertEquals(1, count);
    }

//    @Test
//    public void allTests() {
//        start();
//        verify();
//    }

}
