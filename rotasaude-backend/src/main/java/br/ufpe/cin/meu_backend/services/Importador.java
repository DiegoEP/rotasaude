package br.ufpe.cin.meu_backend.services;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import br.ufpe.cin.meu_backend.entities.Bairro;
import br.ufpe.cin.meu_backend.entities.Distrito;
import br.ufpe.cin.meu_backend.entities.Especialidade;
import br.ufpe.cin.meu_backend.entities.Hospital;
import br.ufpe.cin.meu_backend.entities.UnidadeSaude;
import br.ufpe.cin.meu_backend.entities.UnidadeSaude.UnidadeSaudeTipo;
import br.ufpe.cin.meu_backend.entities.UnidadeSaudeEspecialidade;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Importador {
	
	private EntityManager em = Persistence.createEntityManagerFactory("rotasaude-pu").createEntityManager();

	public static void main(String[] args) throws UnsupportedEncodingException,
			IOException {

		Importador i = new Importador();
		i.importarDistritos();
		i.importarBairros();
		i.importarUnidadesSaude();
//		i.importarHospitais();
//		i.importarEspecialidades();
		i.importarHospitalGeometria();

	}

	public void importarDistritos() throws IOException {

		Query query = em.createQuery("select count(d) FROM Distrito d");
		long total = (Long) query.getSingleResult();

		if (total == 0) {

			final URL url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2015-01-26T030127/distrito2015.csv");
			final Reader reader = new InputStreamReader(url.openStream());
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					Distrito distrito = new Distrito();
					distrito.setCodigo(Integer.parseInt(record
							.get("distritosanitario_codigo")));
					distrito.setDescricao(record
							.get("distritosanitario_descricao"));
					distrito.setMunicipioCodigo((Integer.parseInt(record
							.get("municipio_codigo"))));
					distrito.setMunicipioDescricao(record
							.get("municipio_descricao"));
					distrito.setRegiao((Integer.parseInt(record.get("regiao"))));
					em.getTransaction().begin();
					em.persist(distrito);
					em.getTransaction().commit();
				}
			} finally {
				reader.close();
			}
		}

	}
	
	public void importarBairros() throws IOException {

		Query query = em.createQuery("select count(b) FROM Bairro b");
		long total = (Long) query.getSingleResult();

		if (total == 0) {

			final URL url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2015-01-26T030111/bairro2015.csv");
			final Reader reader = new InputStreamReader(url.openStream());
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					Bairro bairro = new Bairro();
					bairro.setCodigo(Integer.parseInt(record
							.get("bairrosaude_codigo")));
					bairro.setDescricao(record
							.get("bairrosaude_descricao"));
					
					Query query2 = em.createQuery("SELECT d FROM Distrito d WHERE d.codigo = :distrito");
					query2.setParameter("distrito",Integer.parseInt(record.get("distritosanitario_codigo")));
					Distrito distrito = (Distrito)query2.getSingleResult();
					bairro.setDistrito(distrito);

					em.getTransaction().begin();
					em.persist(bairro);
					em.getTransaction().commit();
				}
			} finally {
				reader.close();
			}
		}

	}
	
	public void importarUnidadesSaude() throws IOException {
		
		Query query = em.createQuery("select count(h) FROM UnidadeSaude h");
		long total = (Long) query.getSingleResult();

		if (total == 0) {

			/*** Importando Centros de Especialidades Odontológicas (CEO) ***/
			URL url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2013-10-14T12%3A26%3A30.837Z/ceo.csv");
			Reader reader = new InputStreamReader(url.openStream());
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.CEO);
					unidade.setRpa(Integer.parseInt(record.get("rpa")));
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("fone"));
					unidade.setLatitude((record.get("latitude")));
					unidade.setLongitude((record.get("longitude")));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					UnidadeSaudeEspecialidade e = null;
					try {
						e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", "ODONTOLOGIA").getSingleResult();
					} catch (Exception ex) {}
					
					if (e == null) {
						e = new UnidadeSaudeEspecialidade();
						e.setNome("ODONTOLOGIA");
						em.getTransaction().begin();
						em.persist(e);
						em.getTransaction().commit();
					}

					if (!unidade.getEspecialidades().contains(e)) {
						unidade.addEspecialidade(e);
					}

					unidade.setMicroRegiao(Integer.parseInt(record.get("micro_regiao")));
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();
				}
			} finally {
				reader.close();
			}
			
			/*** Importando Hospitais ***/
			url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2013-10-14T14%3A21%3A46.633Z/hospitais.csv");
			reader = new InputStreamReader(url.openStream());
			records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.HOSPITAL);
					unidade.setRpa(Integer.parseInt(record.get("rpa")));
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("telefone"));
					unidade.setLatitude((record.get("latitude")));
					unidade.setLongitude((record.get("longitude")));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					String espec = record.get("especialidades");
					String[] especs = espec.split(",");
					String[] especs2 = null;
					for (String es : especs) {
						especs2 = es.split(" E ");
					}
					for (String es2 : especs2) {
						UnidadeSaudeEspecialidade e = null;
						try {
							e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", es2.trim()).getSingleResult();
						} catch (Exception ex) {}
						if (e == null) {
							e = new UnidadeSaudeEspecialidade();
							e.setNome(es2.trim());
							em.getTransaction().begin();
							em.persist(e);
							em.getTransaction().commit();
						}
						if (!unidade.getEspecialidades().contains(e)) {
							unidade.addEspecialidade(e);	
						}
					}

					unidade.setMicroRegiao(Integer.parseInt(record.get("micro_regiao")));
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();
					
				}
			} finally {
				reader.close();
			}
			
			/*** Importando Policlinicas ***/
			url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2013-10-14T16%3A21%3A25.828Z/policlinicas.csv");
			reader = new InputStreamReader(url.openStream());
			records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.POLICLINICA);
//					unidade.setRpa(Integer.parseInt(record.get("rpa")));
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("fone"));
					unidade.setLatitude((record.get("latitude")));
					unidade.setLongitude((record.get("longitude")));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					String espec = record.get("especialidades");
					String[] especs = espec.split(",");
					for (String es : especs) {
						
						String[] especs2 = es.split("\\.");
						
						for (String es2 : especs2) {
							
							if (es2.equals("REF") || es2.equals("CRIANCAS E ADOLESC")) {
								continue;
							}
							
							UnidadeSaudeEspecialidade e = null;
							try {
								e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", es2.trim()).getSingleResult();
							} catch (Exception ex) {}
							if (e == null && !es2.trim().isEmpty() && !es2.trim().equals("''")) {
								e = new UnidadeSaudeEspecialidade();
								e.setNome(es2.trim());
								em.getTransaction().begin();
								em.persist(e);
								em.getTransaction().commit();
							}
							if (e != null && !unidade.getEspecialidades().contains(e)) {
								unidade.addEspecialidade(e);	
							}
						
						}
					}

					unidade.setMicroRegiao(Integer.parseInt(record.get("micro_regiao")));
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();
					
				}
			} finally {
				reader.close();
			}
			
			/*** Importando SPAs ***/
			url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2013-10-14T17%3A01%3A09.474Z/servicodeprontoatendimento.csv");
			reader = new InputStreamReader(url.openStream());
			records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.SPA);
					unidade.setRpa(Integer.parseInt(record.get("rpa")));
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("fone"));
					unidade.setLatitude((record.get("latitude")));
					unidade.setLongitude((record.get("longitude")));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					UnidadeSaudeEspecialidade e = null;
					try {
						e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", "URGENCIA E EMERGENCIA").getSingleResult();
					} catch (Exception ex) {}
					
					if (e == null) {
						e = new UnidadeSaudeEspecialidade();
						e.setNome("URGENCIA E EMERGENCIA");
						em.getTransaction().begin();
						em.persist(e);
						em.getTransaction().commit();
					}

					if (!unidade.getEspecialidades().contains(e)) {
						unidade.addEspecialidade(e);
					}

					unidade.setMicroRegiao(Integer.parseInt(record.get("micro_regiao")));
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();
					
				}
			} finally {
				reader.close();
			}
			
			/*** Importando UBSs ***/
			url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2013-10-14T17%3A07%3A28.389Z/ubs.csv");
			reader = new InputStreamReader(url.openStream());
			records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.UBS);
					unidade.setRpa(Integer.parseInt(record.get("rpa")));
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("fone"));
					unidade.setLatitude((record.get("latitude")));
					unidade.setLongitude((record.get("longitude")));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					String espec = record.get("especialidades");
					String[] especs = espec.split(",");
					String[] especs2 = null;
					for (String es : especs) {
						especs2 = es.split(" E ");
					}
					for (String es2 : especs2) {
						UnidadeSaudeEspecialidade e = null;
						try {
						e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", es2.trim()).getSingleResult();
						} catch (Exception ex) {}
						if (e == null && !es2.trim().isEmpty() && !es2.trim().equals("''")) {
							e = new UnidadeSaudeEspecialidade();
							e.setNome(es2.trim());
							em.getTransaction().begin();
							em.persist(e);
							em.getTransaction().commit();
						}
						if (e != null && !unidade.getEspecialidades().contains(e)) {
							unidade.addEspecialidade(e);	
						}
					}

					unidade.setMicroRegiao(Integer.parseInt(record.get("micro_regiao")));
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();

					
				}
			} finally {
				reader.close();
			}
			
			/*** Importando Maternidades ***/
			url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2014-07-14T18%3A25%3A35.952Z/maternidades.csv");
			reader = new InputStreamReader(url.openStream());
			records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					UnidadeSaude unidade = new UnidadeSaude();
					unidade.setTipo(UnidadeSaudeTipo.MATERNIDADE);
					unidade.setCnes(Integer.parseInt(record.get("cnes")));
					unidade.setUnidade(record.get("unidade"));
					unidade.setFone(record.get("telefone"));
					unidade.setLatitude(record.get("latitude"));
					unidade.setLongitude(record.get("longitude"));
					unidade.setEndereco(record.get("endereco"));
					unidade.setBairro(record.get("bairro"));
					
					UnidadeSaudeEspecialidade e = null;
					try {
						e = (UnidadeSaudeEspecialidade) em.createNamedQuery("unidadeespecialidade.findByNome").setParameter("nome", "MATERNIDADE").getSingleResult();
					} catch (Exception ex) {}
					
					if (e == null) {
						e = new UnidadeSaudeEspecialidade();
						e.setNome("MATERNIDADE");
						em.getTransaction().begin();
						em.persist(e);
						em.getTransaction().commit();
					}

					if (!unidade.getEspecialidades().contains(e)) {
						unidade.addEspecialidade(e);
					}
					
					em.getTransaction().begin();
					em.persist(unidade);
					em.getTransaction().commit();

					
				}
			} finally {
				reader.close();
			}
			
			
		}
		
	}
	
	public void importarHospitais() throws IOException {

		Query query = em.createQuery("select count(h) FROM Hospital h");
		long total = (Long) query.getSingleResult();

		if (total == 0) {

			final URL url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2015-01-26T030116/hospital2015.csv");
			final Reader reader = new InputStreamReader(url.openStream());
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					Hospital hospital = new Hospital();
					hospital.setCodigo(Integer.parseInt(record.get("hospital_codigo").trim()));
					hospital.setNome(record
							.get("hospital_nome"));
					
					Query query2 = em.createQuery("SELECT b FROM Bairro b WHERE b.codigo = :bairro");
					query2.setParameter("bairro",Integer.parseInt(record.get("bairrosaude_codigo")));
					Bairro bairro = (Bairro)query2.getSingleResult();
					
					hospital.setBairro(bairro);

					em.getTransaction().begin();
					em.persist(hospital);
					em.getTransaction().commit();
				}
			} finally {
				reader.close();
			}
		}

	}
	
	public void importarEspecialidades() throws IOException {

		Query query = em.createQuery("select count(e) FROM Especialidade e");
		long total = (Long) query.getSingleResult();

		if (total == 0) {

			final URL url = new URL(
					"http://dados.recife.pe.gov.br/storage/f/2015-01-26T030121/especialidademedica2015.csv");
			final Reader reader = new InputStreamReader(url.openStream());
			Iterable<CSVRecord> records = CSVFormat.newFormat(';').withHeader()
					.parse(reader);
			try {
				for (final CSVRecord record : records) {
					
					Integer codigoEspecialidade = Integer.parseInt(record
							.get("especialidade_codigo"));
					
					Query query2 = em.createQuery("SELECT count(e) FROM Especialidade e WHERE e.codigo = :codigo"); 
					query2.setParameter("codigo", codigoEspecialidade);
					long especialidadesExistentes = (Long)query2.getSingleResult();
					
					if (especialidadesExistentes == 0) {
				
						Especialidade especialidade = new Especialidade();
						especialidade.setCodigo(Integer.parseInt(record.get("especialidade_codigo")));
						especialidade.setDescricao(record.get("especialidade_descricao"));
						Query query3 = em.createQuery("SELECT h FROM Hospital h WHERE h.codigo = :hospital");
						query3.setParameter("hospital", Integer.parseInt(record.get("hospital_codigo")));
						Hospital hospital = (Hospital)query3.getSingleResult();
						especialidade.addHospital(hospital);

						em.getTransaction().begin();
						em.persist(especialidade);
						em.getTransaction().commit();
						
					} else {
						
						Query query3 = em.createQuery("SELECT e FROM Especialidade e WHERE e.codigo = :especialidade");
						query3.setParameter("especialidade", Integer.parseInt(record.get("especialidade_codigo")));
						Especialidade especialidade = (Especialidade)query3.getSingleResult();
						
						Query query4 = em.createQuery("SELECT h FROM Hospital h WHERE h.codigo = :hospital");
						query4.setParameter("hospital", Integer.parseInt(record.get("hospital_codigo")));
						Hospital hospital = (Hospital)query4.getSingleResult();
						
						if (!especialidade.getHospitais().contains(hospital)) {
							especialidade.addHospital(hospital);
						}
						
						em.getTransaction().begin();
						em.merge(especialidade);
						em.getTransaction().commit();
						
					}
				}
			} finally {
				reader.close();
			}
		}

	}
	
	public void importarBairroGeometria() throws IOException {
		
		URL urlGeo = new URL("http://dados.recife.pe.gov.br/storage/f/2013-07-15T15%3A17%3A15.285Z/bairros.geojson");

		byte[] mapData = IOUtils.toByteArray(urlGeo.openStream());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map = mapper.readValue(mapData, HashMap.class);
		
//		System.out.println(map);
		ArrayList<LinkedHashMap> features = (ArrayList) map.get("features");
		
		for (LinkedHashMap object : features) {
			System.out.println(object);
			LinkedHashMap properties = (LinkedHashMap) object.get("properties");
			System.out.println("id= "+properties.get("bairro_codigo"));

			LinkedHashMap geometry = (LinkedHashMap) object.get("geometry");
			System.out.println("geometry ="+ geometry);
			
		}
		
	}
	
	public void importarHospitalGeometria() throws IOException {
		
		
		URL urlGeo = new URL("http://dados.recife.pe.gov.br/storage/f/2013-07-15T05%3A45%3A07.990Z/saudemunicipalestadual.geojson");

		byte[] mapData = IOUtils.toByteArray(urlGeo.openStream());
		
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = new HashMap<String, Object>();
		map = mapper.readValue(mapData, HashMap.class);
		
//		System.out.println(map);
		ArrayList<LinkedHashMap> features = (ArrayList) map.get("features");
		
		int achados=0;
		int naoachados=0;
		
		for (LinkedHashMap object : features) {
			System.out.println(object);
			LinkedHashMap properties = (LinkedHashMap) object.get("properties");
			
			String nome = (String) properties.get("NMUNIDAD");
			System.out.println("NOME ="+ nome);
			
			Hospital hospital = null;
			List<Hospital> hospitais = em.createNamedQuery("hospital.findByNome").setParameter("nome","'%"+nome+"%'").getResultList();
			if (hospitais.size() > 0) {
				hospital = hospitais.get(0);
				achados++;
			} else {
				naoachados++;
			}
			
			System.out.println("Achados = "+achados);
			System.out.println("Nao achados = "+ naoachados);
			
			System.out.println("HOSPITAL = "+ hospital);
			
//			LinkedHashMap geometry = (LinkedHashMap) object.get("geometry");

//			System.out.println("geometry ="+ geometry);
			
		}
		
	}

}
