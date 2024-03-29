package br.ce.wcaquino.servicos;

import br.ce.wcaquino.builders.FilmeBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.daos.LocacaoDAOFake;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CalcValueLocacaoTest {

    @InjectMocks
    private LocacaoService locacaoService;

    @Mock
    private LocacaoDAO locacaoDAO;

    @Mock
    private SPCService spcService;

    @Parameterized.Parameter(0)
    public Filme[] filmes;

    @Parameterized.Parameter(1)
    public Double valorLocacao;

    @Parameterized.Parameter(2)
    public String testScenario;

    @Before //-> Executa antes de todos os testes
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private static final Filme filme1 = FilmeBuilder.createMovie().now();
    private static final Filme filme2 = FilmeBuilder.createMovie().now();
    private static final Filme filme3 = FilmeBuilder.createMovie().now();
    private static final Filme filme4 = FilmeBuilder.createMovie().now();
    private static final Filme filme5 = FilmeBuilder.createMovie().now();
    private static final Filme filme6 = FilmeBuilder.createMovie().now();
    private static final Filme filme7 = FilmeBuilder.createMovie().now();

    @Parameterized.Parameters(name = "{2}")
    public static List<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {new Filme[]{filme1, filme2}, 20.0, "2 Filmes - Sem desconto"},
                {new Filme[]{filme1, filme2, filme3}, 27.5, "3 Filmes - 25%"},
                {new Filme[]{filme1, filme2, filme3, filme4}, 32.5, "4 Filmes - 50%"},
                {new Filme[]{filme1, filme2, filme3, filme4, filme5}, 35.0, "5 Filmes - 75%"},
                {new Filme[]{filme1, filme2, filme3, filme4, filme5, filme6}, 35.0, "6 Filmes - 100%"},
                {new Filme[]{filme1, filme2, filme3, filme4, filme5, filme6, filme7}, 45.0, "7 Filmes - Sem desconto"},
        });
    }


    @Test
    public void shouldCalculateDiscountOnMovies() throws MovieWithoutStockException, LocadoraException {
        //Cenário
        Usuario usuario = new Usuario("User");

        //Ação
        Locacao locacao = locacaoService.alugarFilme(usuario, filmes);

        //Verificação
        assertThat(locacao.getValor(), is(valorLocacao));
    }

}
