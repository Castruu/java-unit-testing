package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Usuario;

public interface EmailService {

    void notifyLate(Usuario usuario);

}
