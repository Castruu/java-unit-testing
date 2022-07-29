package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

import static br.ce.wcaquino.matchers.LocationMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import br.ce.wcaquino.utils.DataUtils;
import buildermaster.BuilderMaster;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocacaoServiceTest {

    // O teste reinicializa as variáveis da classe
    public LocacaoService locacaoService;
    public EmailService emailService;
    public SPCService spcService;
    public LocacaoDAO locacaoDAO;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before //-> Executa antes de todos os testes
    public void setup() {
        locacaoService = new LocacaoService();
        locacaoDAO = Mockito.mock(LocacaoDAO.class);
        locacaoService.setDao(locacaoDAO);
        spcService = Mockito.mock(SPCService.class);
        locacaoService.setSpcService(spcService);
        emailService = Mockito.mock(EmailService.class);
        locacaoService.setEmailService(emailService);
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
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().withValue(5.0).now();

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        assertEquals(5.0, locacao.getValor(), 0.01);
        ///assertThat(locacao.getValor(), is(equalTo(5.0))); // Em caso de erro, o teste pararia aqui!
        // error.checkThat(locacao.getValor(), is(equalTo(6.0)));  Com o error collector, mesmo com erro o teste continua
        // assertThat(locacao.getValor(), is(not(6.0)));

    }


    @Test
    public void sameLocationDateReturnsTrue() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();
        Filme filme2 = FilmeBuilder.createMovie().now();

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme, filme2);

        //Verificação

        assertThat(locacao.getDataLocacao(), isToday());
        //assertTrue(isMesmaData(locacao.getDataLocacao(), new Date()));
        //assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
        // error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));


    }

    @Test
    public void sameReturnDateReturnTrue() throws Exception {
        Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));

        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação


        assertThat(locacao.getDataRetorno(), isTodayWithDaysDifference(1));
        //assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        //assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
        //error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));  Com o error collector, mesmo com erro o teste continua
    }

    @Test
    //   @Ignore
    public void locationOnSaturdayShouldReturnOnMonday() throws Exception {
        Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação
//        boolean isMonday = verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
//        assertTrue(isMonday);
        assertThat(locacao.getDataRetorno(), isA(Calendar.SUNDAY));
//      assertThat(locacao.getDataRetorno(), isAMonday());
    }


    //Forma Elegante - Sempre utilizar uma exceção personalizada (apenas a exceção importa)
    @Test(expected = MovieWithoutStockException.class)
    public void throwsExceptionWhenMovieHasNoStocks() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();
        Filme filmeWithoutStock = FilmeBuilder.createMovieWithoutStock().now();

        //Ação
        locacaoService.alugarFilme(usuario, filme, filmeWithoutStock);
        // try/catch with Assert.fail();


    }

    //Forma robusta -> Há mais controle sobre a exceção / Útil para verificar a mensagem também
    @Test
    public void throwsExceptionWhenUserIsNull() throws MovieWithoutStockException {
        //Cenário
        Filme filme = FilmeBuilder.createMovie().now();

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
        Usuario usuario = UsuarioBuilder.createUser().now();


        // Verificação dentro do cenário
        expectedException.expectMessage("É necessário alugar pelo menos um filme");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario, null);
    }

    @Test
    public void throwsExceptionWhenOneMovieIsNull() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

        // Verificação dentro do cenário
        expectedException.expectMessage("Filme vazio");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario, null, filme);
    }

    @Test
    public void throwsExceptionWhenMoviesAreEmpty() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();

        // Verificação dentro do cenário
        expectedException.expectMessage("É necessário alugar pelo menos um filme");
        expectedException.expect(LocadoraException.class);

        //Ação
        locacaoService.alugarFilme(usuario);
    }

    @Test
    public void throwsExceptionWhenUserIsNegative() throws MovieWithoutStockException {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

        Mockito.when(spcService.isNegative(Mockito.any(Usuario.class ))).thenReturn(true);

        //Ação
        try {
            locacaoService.alugarFilme(usuario, filme);
            //Verificação

            Assert.fail("Should throw exception");
        } catch (LocadoraException e) {
            Assert.assertEquals("Usuário negativado", e.getMessage());
        }

        Mockito.verify(spcService).isNegative(usuario);
    }

    @Test
    public void shouldSendEmailToPendingLocations() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Usuario usuario2 = UsuarioBuilder.createUser().withName("Usuario Em Dia").now();
        Usuario usuario3 = UsuarioBuilder.createUser().withName("Usuario Atrasado").now();
        List<Locacao> pendingLocation = Arrays.asList(
                LocacaoBuilder.createLocation()
                        .pending()
                        .withUser(usuario).now(),
                LocacaoBuilder.createLocation()
                        .withUser(usuario2).now(),
                LocacaoBuilder.createLocation()
                        .pending()
                        .withUser(usuario3).now(),
                LocacaoBuilder.createLocation()
                        .pending()
                        .withUser(usuario3).now()
        );
        Mockito.when(locacaoDAO.getPendingLocations()).thenReturn(pendingLocation);

        //Ação
        locacaoService.notifyLate();

        //Verificação
        Mockito.verify(emailService, Mockito.times(3)).notifyLate(Mockito.any(Usuario.class));

        Mockito.verify(emailService).notifyLate(usuario);
        Mockito.verify(emailService, Mockito.atLeastOnce()).notifyLate(usuario3);
        Mockito.verify(emailService, Mockito.never()).notifyLate(usuario2);

        Mockito.verifyNoMoreInteractions(emailService);

    }


}
