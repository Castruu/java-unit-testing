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

		boolean isNegative;
		try {
			isNegative = spcService.isNegative(usuario);
		} catch (Exception e) {
			throw new LocadoraException("Problemas com SPC, tente novamente");
		}

		if(isNegative) {
			throw new LocadoraException("Usuário negativado");
		}

		List<Filme> filmeList = List.of(filmes);
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmeList);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(getDate());
		double totalValue = getTotalValue(filmeList);

		locacao.setValor(totalValue);

		//Entrega no dia seguinte
		Date dataEntrega = getDate();
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

	public void lateLocacao(Locacao locacao, int days) {
		Locacao newLocation = new Locacao();
		newLocation.setUsuario(locacao.getUsuario());
		newLocation.setFilmes(locacao.getFilmes());
		newLocation.setDataLocacao(new Date());
		newLocation.setDataRetorno(DataUtils.obterDataComDiferencaDias(days));
		newLocation.setValor(locacao.getValor() * days);
		dao.save(newLocation);
	}

	private double getTotalValue(List<Filme> filmeList) {
		System.out.println("Calculando...");
		double sum = 0;
		for (int i = 0; i < filmeList.size(); i++) {
			sum += filmeList.get(i).getPrecoLocacao() * getModifier(i);
		}
		return sum;
	}
	private double getModifier(int i) {
		if(i == 2) return 0.75;
		else if(i == 3) return 0.5;
		else if(i == 4) return 0.25;
		else if(i == 5) return 0;
		return 1;
	}

	protected Date getDate() {
		return new Date();
	}


}