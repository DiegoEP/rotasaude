package br.ufpe.cin.meu_backend.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/***
 * 
 * @author diego
 *
 * Importar de:
 * http://dados.recife.pe.gov.br/dataset/servico-de-atendimento-movel-de-urgencia-samu-2015/resource/0cc26b70-b0ce-46b5-a53d-e76657962709
 *
 */
@Entity
public class UnidadeSaude {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	public enum UnidadeSaudeTipo { 
		CEO, HOSPITAL, POLICLINICA, SPA, UBS, MATERNIDADE
	}
	
	@Enumerated(EnumType.STRING)
	private UnidadeSaudeTipo tipo;

	private Integer rpa;
	
	private Integer microRegiao;
	
	private Integer cnes;
	
	private String unidade;
	
	private String endereco;
	
	private String bairro;
	
	@ManyToMany
	private List<UnidadeSaudeEspecialidade> especialidades = new ArrayList();
	
	private String fone;
	
	private String latitude;
	
	private String longitude;
	
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRpa() {
		return rpa;
	}

	public void setRpa(Integer rpa) {
		this.rpa = rpa;
	}

	public Integer getMicroRegiao() {
		return microRegiao;
	}

	public void setMicroRegiao(Integer microRegiao) {
		this.microRegiao = microRegiao;
	}

	public Integer getCnes() {
		return cnes;
	}

	public void setCnes(Integer cnes) {
		this.cnes = cnes;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public List<UnidadeSaudeEspecialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<UnidadeSaudeEspecialidade> especialidades) {
		this.especialidades = especialidades;
	}

	public String getFone() {
		return fone;
	}

	public void setFone(String fone) {
		this.fone = fone;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public UnidadeSaudeTipo getTipo() {
		return tipo;
	}

	public void setTipo(UnidadeSaudeTipo tipo) {
		this.tipo = tipo;
	}
	
	public void addEspecialidade(UnidadeSaudeEspecialidade e) {
		this.especialidades.add(e);
	}
	
	public void removeEspecialidade(UnidadeSaudeEspecialidade e) {
		this.especialidades.remove(e);
	}
	
}
