package br.com.solutis.treinamento.apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class ApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
	}

	/**
	 *	Método para definir o timezone padrão da aplicação como UTC.
	 *  <i>Nota:Este método é invocado depois do processo de injeção de dependências.</i>
	 * */

	@PostConstruct
	void setUTCTimeZone(){
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

}
