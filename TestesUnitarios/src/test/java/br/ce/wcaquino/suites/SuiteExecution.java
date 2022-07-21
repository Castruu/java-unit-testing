package br.ce.wcaquino.suites;

import br.ce.wcaquino.servicos.CalcValueLocacaoTest;
import br.ce.wcaquino.servicos.Calculator;
import br.ce.wcaquino.servicos.CalculatorTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.time.Instant;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        LocacaoServiceTest.class,
        CalculatorTest.class,
        CalcValueLocacaoTest.class
})
public class SuiteExecution {
    //Remova se puder

    // Permitido utilizar @AfterClass e @BeforeClass
}
