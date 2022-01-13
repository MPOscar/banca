package banca.uy.core.auth;

import banca.uy.core.resources.dto.UsuarioJwt;
import banca.uy.core.db.ParamsDAO;
import banca.uy.core.db.UsuariosDAO;
import org.hibernate.SessionFactory;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.keys.HmacKey;

import com.github.toastshaman.dropwizard.auth.jwt.JwtAuthFilter;

import io.dropwizard.auth.AuthFilter;
import banca.uy.core.auth.jwt.JwtAuthenticator;
import banca.uy.core.auth.jwt.JwtAuthoriser;

public class AuthFilterUtils {

	private SessionFactory session;

	private UsuariosDAO usuariosDAO;

	@SuppressWarnings("unused")
	private ParamsDAO paramsDAO;

	public AuthFilterUtils(SessionFactory session, UsuariosDAO usuariosDAO, ParamsDAO paramsDAO) {
		this.session = session;
		this.usuariosDAO = usuariosDAO;
		this.paramsDAO = paramsDAO;
	}

	public AuthFilter<JwtContext, UsuarioJwt> buildJwtAuthFilter() {
		// Param JWT_KEY = paramsDAO.findByNombre("JWT_SECRET_KEY");
		final JwtConsumer consumer = new JwtConsumerBuilder().setAllowedClockSkewInSeconds(300).setRequireSubject()
				.setRequireExpirationTime().setAllowedClockSkewInSeconds(30)
				.setDecryptionKey(new HmacKey(Secrets.JWT_SECRET_KEY))
				.setVerificationKey(new HmacKey(Secrets.JWT_SECRET_KEY)).build();

		return new JwtAuthFilter.Builder<UsuarioJwt>().setJwtConsumer(consumer).setRealm("realm").setPrefix("Bearer")
				.setAuthenticator(new JwtAuthenticator(session, usuariosDAO))
				.setAuthorizer(new JwtAuthoriser()).buildAuthFilter();
	}
}
