package br.ce.wcaquino.servicos;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {
	
	public Locacao alugarFilme(Usuario usuario, Filme ...filmes) throws LocadoraException, MovieWithoutStockException {
		if(usuario == null) throw new LocadoraException("Usuário vazio");
		if(filmes == null || filmes.length == 0) throw new LocadoraException("É necessário alugar pelo menos um filme");

		for(Filme filme : filmes) {
			if(filme == null) throw new LocadoraException("Filme vazio");
			if(filme.getEstoque() == 0) throw new MovieWithoutStockException("Filme sem estoque");
		}

		List<Filme> filmeList = List.of(filmes);
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmeList);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(filmeList.stream().map(Filme::getPrecoLocacao).reduce(0.0, Double::sum));

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}


}