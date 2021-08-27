package banca.uy.core.services.implementations;

import org.apache.logging.log4j.Level;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import banca.uy.core.services.interfaces.IEnviarPeticionApiDeLaBancaService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class EnviarPeticionApiDeLaBancaService implements IEnviarPeticionApiDeLaBancaService {

	private RestTemplate clientesService;

	@Value("${apiBanca.url}")
	String apiBancaUrl;

	public EnviarPeticionApiDeLaBancaService() {
		this.clientesService = new RestTemplate();
	}

	@Override
	public String enviarPeticionApiDeLaBanca(String fechaSorteo, String url) {
		String responseString = "";
		try {
			clientesService = new RestTemplate();
			String apiBancaUrlTombola = apiBancaUrl + url;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			headers.set("X-Requested-With", "XMLHttpRequest");
			MultiValueMap<String, String> formData= new LinkedMultiValueMap<>();
			formData.add("fecha_sorteo", fechaSorteo);
			HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);
			ResponseEntity<String> response = clientesService.postForEntity(apiBancaUrlTombola, requestEntity, String.class);
			responseString = response.getBody();
		} catch (Exception e) {
			//no hacer nada
		}
		return responseString;
	}

}
