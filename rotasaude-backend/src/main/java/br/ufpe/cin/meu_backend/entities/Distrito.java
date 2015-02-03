package br.ufpe.cin.meu_backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 
 * @author diego
 *
 * Importar de:
 * URL: http://dados.recife.pe.gov.br/storage/f/2015-01-26T030127/distrito2015.csv


 *
 */
@Entity
public class Distrito {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(unique=true)
	private Integer codigo;
	
	private String descricao;
	
	private Integer municipioCodigo;
	
	private Integer regiao;
	
	private String municipioDescricao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getMunicipioCodigo() {
		return municipioCodigo;
	}

	public void setMunicipioCodigo(Integer municipioCodigo) {
		this.municipioCodigo = municipioCodigo;
	}

	public Integer getRegiao() {
		return regiao;
	}

	public void setRegiao(Integer regiao) {
		this.regiao = regiao;
	}

	public String getMunicipioDescricao() {
		return municipioDescricao;
	}

	public void setMunicipioDescricao(String municipioDescricao) {
		this.municipioDescricao = municipioDescricao;
	}
	
}

