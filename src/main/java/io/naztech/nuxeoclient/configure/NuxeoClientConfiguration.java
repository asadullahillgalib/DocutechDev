package io.naztech.nuxeoclient.configure;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nuxeo.client.NuxeoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nazdaqTechnologies.core.message.processor.json.gson.GsonJsonMessageProcessor;
import com.nazdaqTechnologies.core.service.ServiceMap;
import com.nazdaqTechnologies.jdbc.JdbcService;

import io.naztech.nuxeoclient.model.DocDetails;
import io.naztech.nuxeoclient.model.Document;
import io.naztech.nuxeoclient.model.Template;
import io.naztech.nuxeoclient.service.DocDetailsService;
import io.naztech.nuxeoclient.service.DocumentService;
import io.naztech.nuxeoclient.service.ServiceCoordinator;
import io.naztech.nuxeoclient.service.TemplateService;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-10-17
 **/

@Configuration
public class NuxeoClientConfiguration {
	
	@Value("${nuxeo.url}")
	private String nuxeoUrl;

	@Value("${nuxeo.user}")
	private String nuxeoUser;

	@Value("${nuxeo.pass}")
	private String nuxeoPass;

	@Autowired
	JdbcService jdbcService;

	@Bean
	ServiceCoordinator serviceCoordinator() {

		ServiceCoordinator sc = new ServiceCoordinator();
		sc.setServiceMap(serviceMap());

		return sc;
	}

	@Bean
	GsonJsonMessageProcessor gsonJsonMessageProcessor() {

		GsonJsonMessageProcessor gsn = new GsonJsonMessageProcessor();

		Map<String, String> classMap = new LinkedHashMap<>();

		classMap.put(Template.class.getSimpleName(), Template.class.getName());
		classMap.put(Document.class.getSimpleName(), Document.class.getName());
		classMap.put(DocDetails.class.getSimpleName(), DocDetails.class.getName());

		gsn.setClassMap(classMap);

		return gsn;
	}

	@Bean
	public ServiceMap serviceMap() {

		ServiceMap sMap = new ServiceMap();

		sMap.addService(templateService(jdbcService));
		sMap.addService(documentService(jdbcService));
		sMap.addService(docDestailsService(jdbcService));

		return sMap;
	}

	@Bean
	public NuxeoClient getNuxeoClient() {
		return new NuxeoClient.Builder().url(nuxeoUrl).authentication(nuxeoUser, nuxeoPass).connect();
	}

	@Bean
	public TemplateService templateService(JdbcService jdbcService) {

		TemplateService ob = new TemplateService();
		ob.setJdbcService(jdbcService);

		return ob;
	}

	@Bean
	public DocumentService documentService(JdbcService jdbcService) {

		DocumentService ob = new DocumentService();
		ob.setJdbcService(jdbcService);

		return ob;
	}

	@Bean
	public DocDetailsService docDestailsService(JdbcService jdbcService) {

		DocDetailsService ob = new DocDetailsService();
		ob.setJdbcService(jdbcService);

		return ob;
	}
}
