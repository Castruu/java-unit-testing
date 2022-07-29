package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

    private Filme filme;

    private FilmeBuilder() {}

    public static FilmeBuilder createMovie() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setEstoque(2);
        builder.filme.setNome("Filme 1");
        builder.filme.setPrecoLocacao(10.0);
        return builder;
    }

    public static FilmeBuilder createMovieWithoutStock() {
        FilmeBuilder builder = new FilmeBuilder();
        builder.filme = new Filme();
        builder.filme.setEstoque(0);
        builder.filme.setNome("Filme 1");
        builder.filme.setPrecoLocacao(10.0);
        return builder;
    }

    public FilmeBuilder withoutStock() {
        filme.setEstoque(0);
        return this;
    }

    public FilmeBuilder withValue(Double value) {
        filme.setPrecoLocacao(value);
        return this;
    }

    public Filme now() {
        return filme;
    }

}
