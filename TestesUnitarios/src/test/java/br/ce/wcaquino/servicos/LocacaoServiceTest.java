package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Date;

public class LocacaoServiceTest {

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Test
    public void sameLocationValueReturnsTrue() throws Exception {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        assertEquals(5.0, locacao.getValor(), 0.01);
        // assertThat(locacao.getValor(), is(equalTo(5.0))); // Em caso de erro, o teste pararia aqui!
        // error.checkThat(locacao.getValor(), is(equalTo(6.0)));  Com o error collector, mesmo com erro o teste continua
        // assertThat(locacao.getValor(), is(not(6.0)));

    }


    @Test
    public void sameLocationDateReturnsTrue() throws Exception {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        // assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        // error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));


    }

    @Test
    public void sameDateReturnTrue() throws Exception {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
        // assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        // error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));  Com o error collector, mesmo com erro o teste continua


    }


    //Forma Elegante - Sempre utilizar uma exceção personalizada (apenas a exceção importa)
    @Test(expected = MovieWithoutStockException.class)
    public void throwsExceptionWhenMovieHasNoStocks() throws Exception {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 0, 5.0);

        //Ação
        locacaoService.alugarFilme(usuario, filme);
            // try/catch with Assert.fail();


    }

    //Forma robusta -> Há mais controle sobre a exceção / Útil para verificar a mensagem também
    @Test
    public void throwsExceptionWhenUserIsNull() throws MovieWithoutStockException {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Filme filme = new Filme("Filme 1", 5, 5.0);

        //Ação
        try {
            locacaoService.alugarFilme(null, filme);
            Assert.fail("Should throw exception");
        } catch (LocadoraException e) {
            //Verificação
            assertThat(e.getMessage(), is("Usuário vazio"));
        }
    }

    //Forma nova -> Útil para verificar a mensagem também
    @Test
    public void throwsExceptionWhenMovieIsNull() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        LocacaoService locacaoService = new LocacaoService();
        Usuario usuario = new Usuario("User");

        // Verificação dentro do cenário
        expectedException.expectMessage("Filme vazio");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario, null);
    }



}
