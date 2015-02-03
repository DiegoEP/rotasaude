package br.ufpe.cin.meu_backend.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * 
 * @author diego
 *
 *
 * Importar de:
 * http://dados.recife.pe.gov.br/dataset/servico-de-atendimento-movel-de-urgencia-samu-2015/resource/89cd16d3-4799-42c1-9e87-e68325228274
 *
 */
@Entity
@NamedQueries({
@NamedQuery(name="especialidade.all", query="SELECT e FROM Especialidade e"),
@NamedQuery(name="especialidade.findByCodigo", query="SELECT e FROM Especialidade e WHERE e.codigo = :codigo"),
@NamedQuery(name="especialidade.findByBairro", query="SELECT e FROM Especialidade e JOIN e.hospitais h JOIN h.bairro b WHERE b.codigo = :bairro")
})
public class Especialidade {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@ManyToMany(fetch=FetchType.LAZY)
	private List<Hospital> hospitais = new ArrayList<Hospital>();
	
	@Column(unique=true)
	private Integer codigo;
	
	private String descricao;

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

	public List<Hospital> getHospitais() {
		return hospitais;
	}

	public void setHospitais(List<Hospital> hospitais) {
		this.hospitais = hospitais;
	}
	
	public void addHospital(Hospital hospital) {
		this.hospitais.add(hospital);
	}
	
	public void removeHospital(Hospital hospital) {
		this.hospitais.remove(hospital);
	}
	
}
