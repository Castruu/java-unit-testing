package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Usuario;
import java.util.Arrays;
import java.lang.Double;
import java.util.Collections;
import java.util.Date;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.utils.DataUtils;


public class LocacaoBuilder {
    private Locacao elemento;
    private LocacaoBuilder(){}

    public static LocacaoBuilder createLocation() {
        LocacaoBuilder builder = new LocacaoBuilder();
        startDefaultValues(builder);
        return builder;
    }

    public static void startDefaultValues(LocacaoBuilder builder) {
        builder.elemento = new Locacao();
        Locacao elemento = builder.elemento;


        elemento.setUsuario(UsuarioBuilder.createUser().now());
        elemento.setFilmes(Collections.singletonList(FilmeBuilder.createMovie().now()));
        elemento.setDataLocacao(new Date());
        elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(1));
        elemento.setValor(10.0);
    }

    public LocacaoBuilder withUser(Usuario param) {
        elemento.setUsuario(param);
        return this;
    }

    public LocacaoBuilder withMovieList(Filme... params) {
        elemento.setFilmes(Arrays.asList(params));
        return this;
    }

    public LocacaoBuilder withLocationDate(Date param) {
        elemento.setDataLocacao(param);
        return this;
    }

    public LocacaoBuilder withReturnDate(Date param) {
        elemento.setDataRetorno(param);
        return this;
    }

    public LocacaoBuilder pending() {
        elemento.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
        elemento.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
        return this;
    }

    public LocacaoBuilder withValue(Double param) {
        elemento.setValor(param);
        return this;
    }

    public Locacao now() {
        return elemento;
    }
}