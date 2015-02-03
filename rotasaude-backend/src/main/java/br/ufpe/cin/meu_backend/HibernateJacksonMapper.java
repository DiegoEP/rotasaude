package br.ufpe.cin.meu_backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * @author diego
 */
public class HibernateJacksonMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1728126302234523188L;

	/**
	 * 
	 */
	public HibernateJacksonMapper() {
		super();
		registerModule(new Hibernate4Module());
	}

}
