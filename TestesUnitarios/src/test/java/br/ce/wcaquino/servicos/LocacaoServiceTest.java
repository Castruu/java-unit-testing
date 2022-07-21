package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

import static br.ce.wcaquino.utils.DataUtils.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import java.util.Calendar;
import java.util.Date;

public class LocacaoServiceTest {

    // O teste reinicializa as variáveis da classe
    public LocacaoService locacaoService;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before //-> Executa antes de todos os testes
    public void setup() {
        locacaoService = new LocacaoService();

    }

//    @After //-> Executa após todos os testes
//    public void tearDown() {
//    }
//
//    @BeforeClass //-> Antes da classe ser inicializada
//    public static void setupClass() {
//    }
//
//    @AfterClass //-> Antes da classe ser inicializaada
//    public static void tearDownClass() {
//    }



    @Test
    public void sameLocationValueReturnsTrue() throws Exception {
        //Cenário
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
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);
        Filme filme2 = new Filme("Filme 2", 5, 7.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme, filme2);

        //Verificação

        assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        // assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        // error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));


    }

    @Test
    public void sameReturnDateReturnTrue() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
        // assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        // error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));  Com o error collector, mesmo com erro o teste continua
    }

    @Test
 //   @Ignore
    public void locationOnSaturdayShouldReturnOnMonday() throws Exception {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação
        boolean isMonday = verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
        assertTrue(isMonday);
        // assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        // error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));  Com o error collector, mesmo com erro o teste continua
    }


    //Forma Elegante - Sempre utilizar uma exceção personalizada (apenas a exceção importa)
    @Test(expected = MovieWithoutStockException.class)
    public void throwsExceptionWhenMovieHasNoStocks() throws Exception {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Filme 1", 2, 5.0);
        Filme filmeWithoutStock = new Filme("Filme 1", 0, 5.0);

        //Ação
        locacaoService.alugarFilme(usuario, filme, filmeWithoutStock);
        // try/catch with Assert.fail();


    }

    //Forma robusta -> Há mais controle sobre a exceção / Útil para verificar a mensagem também
    @Test
    public void throwsExceptionWhenUserIsNull() throws MovieWithoutStockException {
        //Cenário
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
    public void throwsExceptionWhenMoviesAreNull() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");


        // Verificação dentro do cenário
        expectedException.expectMessage("É necessário alugar pelo menos um filme");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario, null);
    }

    @Test
    public void throwsExceptionWhenOneMovieIsNull() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme = new Filme("Teste", 2, 10.0);

        // Verificação dentro do cenário
        expectedException.expectMessage("Filme vazio");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario, null, filme);
    }

    @Test
    public void throwsExceptionWhenMoviesAreEmpty() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");

        // Verificação dentro do cenário
        expectedException.expectMessage("É necessário alugar pelo menos um filme");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario);
    }

    @Test
    public void thirdMovieShouldGet25Discount() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme1 = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0); // 7.5

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme1, filme2, filme3);

        //Verificação
        assertEquals(27.5, locacao.getValor(), 0.01);
    }

    @Test
    public void fourthMovieShouldGet50Discount() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme1 = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0); // 7.5
        Filme filme4 = new Filme("Filme 4", 2, 10.0); // 5

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme1, filme2, filme3, filme4);

        //Verificação
        assertEquals(32.5, locacao.getValor(), 0.01);
    }

    @Test
    public void fifthMovieShouldGet75Discount() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme1 = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0); // 7.5
        Filme filme4 = new Filme("Filme 4", 2, 10.0); // 5
        Filme filme5 = new Filme("Filme 5", 2, 10.0); // 2.5

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme1, filme2, filme3, filme4, filme5);

        //Verificação
        assertEquals(35.0, locacao.getValor(), 0.01);
    }

    @Test
    public void sixthMovieShouldGet100Discount() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");
        Filme filme1 = new Filme("Filme 1", 2, 10.0);
        Filme filme2 = new Filme("Filme 2", 2, 10.0);
        Filme filme3 = new Filme("Filme 3", 2, 10.0); // 7.5
        Filme filme4 = new Filme("Filme 4", 2, 10.0); // 5
        Filme filme5 = new Filme("Filme 5", 2, 10.0); // 2.5
        Filme filme6 = new Filme("Filme 6", 2, 10.0);

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme1, filme2, filme3, filme4, filme5, filme6);

        //Verificação
        assertEquals(35.0, locacao.getValor(), 0.01);
    }



}
