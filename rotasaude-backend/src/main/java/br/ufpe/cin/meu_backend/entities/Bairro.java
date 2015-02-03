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
 * http://dados.recife.pe.gov.br/storage/f/2015-01-26T030111/bairro2015.csv
 *
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name="bairro.all", query="SELECT b FROM Bairro b"),
	@NamedQuery(name="bairro.findByNome", query="SELECT b FROM Bairro b WHERE b.descricao LIKE :nome"),
	@NamedQuery(name="bairro.findByCodigo", query="SELECT b FROM Bairro b WHERE b.codigo = :codigo")
})
public class Bairro {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(unique=true)
	private Integer codigo;
	
	private String descricao; 
	
	@ManyToOne
	private Distrito distrito;

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

	public Distrito getDistrito() {
		return distrito;
	}

	public void setDistrito(Distrito distrito) {
		this.distrito = distrito;
	}
	
}
