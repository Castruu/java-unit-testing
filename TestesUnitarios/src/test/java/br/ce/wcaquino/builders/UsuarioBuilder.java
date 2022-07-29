package br.ce.wcaquino.builders;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {

    private Usuario usuario;

    private UsuarioBuilder() {}

    public static UsuarioBuilder createUser() {
        UsuarioBuilder builder = new UsuarioBuilder();
        builder.usuario = new Usuario();
        builder.usuario.setNome("User");
        return builder;
    }

    public UsuarioBuilder withName(String name) {
        usuario.setNome(name);
        return this;
    }

    public Usuario now() {
        return usuario;
    }

}
