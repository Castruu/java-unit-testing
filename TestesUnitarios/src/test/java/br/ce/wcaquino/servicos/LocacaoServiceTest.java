package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.builders.LocacaoBuilder;
import br.ce.wcaquino.builders.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;

import static br.ce.wcaquino.matchers.LocationMatchers.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import br.ce.wcaquino.utils.DataUtils;
import org.junit.*;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Method;
import java.util.*;


//@RunWith(PowerMockRunner.class)
//@PrepareForTest({LocacaoService.class, DataUtils.class})
public class LocacaoServiceTest {

    // O teste reinicializa as variáveis da classe
    @InjectMocks @Spy
    public LocacaoService locacaoService;

    @Mock
    public EmailService emailService;
    @Mock
    public SPCService spcService;
    @Mock
    public LocacaoDAO locacaoDAO;

    @Rule
    public ErrorCollector error = new ErrorCollector();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    @Before //-> Executa antes de todos os testes
    public void setup() {
        MockitoAnnotations.initMocks(this);
       // locacaoService = PowerMockito.spy(locacaoService);
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
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, 28);
//        calendar.set(Calendar.MONTH, Calendar.APRIL);
//        calendar.set(Calendar.YEAR, 2017);
//        PowerMockito.mockStatic(Calendar.class);
//        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
        Mockito.doReturn(DataUtils.obterData(28, 4, 2017)).when(locacaoService).getDate();

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação

        //assertThat(locacao.getDataRetorno(), isTodayWithDaysDifference(1));
        assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
        //assertThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(true));
        //assertTrue(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)));
        //error.checkThat(isMesmaData(locacao.getDataRetorno(), obterDataComDiferencaDias(1)), is(false));  Com o error collector, mesmo com erro o teste continua
    }

    @Test
    //   @Ignore
    public void locationOnSaturdayShouldReturnOnMonday() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

//        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.DAY_OF_MONTH, 29);
//        calendar.set(Calendar.MONTH, Calendar.APRIL);
//        calendar.set(Calendar.YEAR, 2017);
//        PowerMockito.mockStatic(Calendar.class);
//        PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
        Mockito.doReturn(DataUtils.obterData(29, 4, 2017)).when(locacaoService).getDate();


        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filme);

        //Verificação
//        boolean isMonday = verificarDiaSemana(locacao.getDataRetorno(), Calendar.MONDAY);
//        assertTrue(isMonday);
       // assertThat(locacao.getDataRetorno(), isA(Calendar.SUNDAY));
        assertThat(locacao.getDataRetorno(), isAMonday());
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
    public void throwsExceptionWhenUserIsNegative() throws Exception {
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

    @Test
    public void shouldCoverErrorInSPC() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();

        Mockito.when(spcService.isNegative(Mockito.any(Usuario.class))).thenThrow(new Exception("Falha catastrófica"));

        expectedException.expect(LocadoraException.class);
        expectedException.expectMessage("Problemas com SPC, tente novamente");

        //Ação
        locacaoService.alugarFilme(usuario, filme);

        //Verificação
        Mockito.verify(spcService).isNegative(usuario);
    }
    
    @Test
    public void shouldLateLocation() {
        //Cenario
        Locacao locacao = LocacaoBuilder.createLocation().now();

        //Acao
        locacaoService.lateLocacao(locacao, 3);

        //Verificacao
        ArgumentCaptor<Locacao> argumentCaptor = ArgumentCaptor.forClass(Locacao.class);
        Mockito.verify(locacaoDAO).save(argumentCaptor.capture());
        Locacao returnedLocation = argumentCaptor.getValue();

        error.checkThat(returnedLocation.getValor(), is(30.0));
        error.checkThat(returnedLocation.getDataLocacao(), isToday());
        error.checkThat(returnedLocation.getDataRetorno(), isTodayWithDaysDifference(3));
    }

    @Test
    public void shouldCalcRentValue() throws Exception {
        //Cenário
        Usuario usuario = UsuarioBuilder.createUser().now();
        Filme filme = FilmeBuilder.createMovie().now();


        //Acao
        Class<LocacaoService> clazz = LocacaoService.class;
        Method method = clazz.getDeclaredMethod("getTotalValue", List.class);
        method.setAccessible(true);
        Double value = (Double) method.invoke(locacaoService, Collections.singletonList(filme));

        //Verificacao
        Assert.assertThat(value, is(10.0));
    }


}
