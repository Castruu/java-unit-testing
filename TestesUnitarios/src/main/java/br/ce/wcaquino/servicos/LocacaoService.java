package br.ce.wcaquino.servicos;

import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.exceptions.MovieWithoutStockException;
import br.ce.wcaquino.utils.DataUtils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

public class LocacaoService {

	LocacaoDAO dao;
	SPCService spcService;
	EmailService emailService;

	public Locacao alugarFilme(Usuario usuario, Filme ...filmes) throws LocadoraException, MovieWithoutStockException {
		if(usuario == null) throw new LocadoraException("Usuário vazio");
		if(filmes == null || filmes.length == 0) throw new LocadoraException("É necessário alugar pelo menos um filme");

		for(Filme filme : filmes) {
			if(filme == null) throw new LocadoraException("Filme vazio");
			if(filme.getEstoque() == 0) throw new MovieWithoutStockException("Filme sem estoque");
		}

		if(spcService.isNegative(usuario)) {
			throw new LocadoraException("Usuário negativado");
		}

		List<Filme> filmeList = List.of(filmes);
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmeList);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		double totalValue = 0;
		for (int i = 0; i < filmeList.size(); i++) {
			totalValue += filmeList.get(i).getPrecoLocacao() * getModifier(i);
		}
		locacao.setValor(totalValue);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY))
			dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		dao.save(locacao);
		
		return locacao;
	}

	public void notifyLate() {
		List<Locacao> locacoes = dao.getPendingLocations();
		for(Locacao locacao : locacoes) {
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notifyLate(locacao.getUsuario());
			}
		}
	}

	public void setDao(LocacaoDAO dao) {
		this.dao = dao;
	}


	public void setSpcService(SPCService spcService) {
		this.spcService = spcService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	private double getModifier(int i) {
		if(i == 2) return 0.75;
		else if(i == 3) return 0.5;
		else if(i == 4) return 0.25;
		else if(i == 5) return 0;
		return 1;
	}


}