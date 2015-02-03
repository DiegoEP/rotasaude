package br.ufpe.cin.meu_backend.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ufpe.cin.meu_backend.entities.UnidadeSaude;
import br.ufpe.cin.meu_backend.entities.UnidadeSaudeEspecialidade;

@RestController
@RequestMapping("services")
public class Fachada {
	
	@PersistenceContext
	private EntityManager em;
	
	/*
	@RequestMapping(value="especialidades", method=RequestMethod.GET)
	public List<Especialidade> listaEspecialidades(@RequestParam(value="bairro", required=false) Integer bairroCodigo) {
		List<Especialidade> especialidades = null;
		if (bairroCodigo == null) {
			especialidades = em.createNamedQuery("especialidade.all").getResultList();
		} else {
			especialidades = em.createNamedQuery("especialidade.findByBairro").setParameter("bairro", bairroCodigo).getResultList();
		}
		return especialidades;
	}
	
	@RequestMapping(value="hospitais", method=RequestMethod.GET)
	public List<Hospital> listaHospitais() {
		List<Hospital> hospitais = null;
		hospitais = em.createNamedQuery("hospital.all").getResultList();
		return hospitais;
	}


	@RequestMapping(value="hospitaisProximos", method=RequestMethod.GET)
	public List<Hospital> listaHospitaisProximos(@RequestParam(value="especialidade", required=false) Integer especialidade, 
												@RequestParam(value="bairro") Integer bairroCodigo) {
		List<Hospital> hospitais = null;
		try {
			Query query = null;
			try {
			query = em.createQuery("SELECT h FROM Especialidade e JOIN e.hospitais h WHERE e.codigo = :especialidade AND h.bairro.codigo = :bairro");
			query.setParameter("especialidade", especialidade);
			query.setParameter("bairro", bairroCodigo);
			} catch(Exception e){
				e.printStackTrace();
			}
			
			hospitais = query.getResultList();
			
		} catch (Exception e) {}
		
		return hospitais;
	}
	*/
	
	@RequestMapping(value="unidadesSaude", method=RequestMethod.GET)
	public List<UnidadeSaude> listarUnidades(@RequestParam(value="especialidade",required=false) Integer especialidade) {
		List<UnidadeSaude> unidades = null;
		if (especialidade == null) {
			try {
				Query query = null;
				try {
					query = em.createQuery("SELECT u FROM UnidadeSaude u");
				} catch (Exception e) {
					e.printStackTrace();
				}

				unidades = query.getResultList();

			} catch (Exception e) {
			}
		} else if (especialidade != null){
			try {
				Query query = null;
				try {
					query = em.createQuery("SELECT u FROM UnidadeSaude u JOIN u.especialidades e WHERE e.id = :especialidade");
					query.setParameter("especialidade", especialidade);
				} catch (Exception e) {
					e.printStackTrace();
				}

				unidades = query.getResultList();

			} catch (Exception e) {
			}
		}
		
		return unidades;
	}
	
	@RequestMapping(value="especialidades", method=RequestMethod.GET)
	public List<UnidadeSaudeEspecialidade> listarEspecialidades() {
		
		List<UnidadeSaudeEspecialidade> unidades = em.createNamedQuery("unidadeespecialidade.all").getResultList();
		return unidades;
		
	}
	
}
