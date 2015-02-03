package br.ufpe.cin.meu_backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/***
 * 
 * @author diego
 *
 * Importar de:
 * http://dados.recife.pe.gov.br/dataset/servico-de-atendimento-movel-de-urgencia-samu-2015/resource/0cc26b70-b0ce-46b5-a53d-e76657962709
 *
 */
@Entity
@NamedQueries({
@NamedQuery(name="hospital.all", query="SELECT h FROM Hospital h"),
@NamedQuery(name="hospital.findByNome", query="SELECT h FROM Hospital h WHERE h.nome LIKE :nome")
})
public class Hospital {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(unique=true)
	private Integer codigo;
	
	private String nome;
	
	@ManyToOne
	private Bairro bairro;

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

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}
	
}
