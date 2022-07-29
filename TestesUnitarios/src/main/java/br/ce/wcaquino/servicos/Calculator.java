package br.ce.wcaquino.servicos;

import br.ce.wcaquino.exceptions.DivisionByZeroException;

public class Calculator {

    public int sum(int a, int b) {
        return a + b;
    }

    public int subtract(int a, int b) {
        return a - b;
    }

    public int divide(int a, int b) {
        if(b == 0) throw new DivisionByZeroException();
        return a/b;
    }

}
