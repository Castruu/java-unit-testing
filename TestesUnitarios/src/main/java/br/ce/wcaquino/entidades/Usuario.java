package br.ce.wcaquino.entidades;

import java.util.Objects;

public class Usuario {

	private String nome;
	
	public Usuario() {}
	
	public Usuario(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Usuario)) return false;
		Usuario usuario = (Usuario) o;
		return nome.equals(usuario.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public String toString() {
		return "Usuario{" +
				"nome='" + nome + '\'' +
				'}';
	}
}