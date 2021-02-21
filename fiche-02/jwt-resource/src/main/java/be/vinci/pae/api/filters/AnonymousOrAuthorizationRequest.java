package be.vinci.pae.api.filters;

import java.io.IOException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import be.vinci.pae.services.DataServiceUserCollection;
import be.vinci.pae.utils.Config;
import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.ext.Provider;

@Singleton
@Provider
@AnonymousOrAuthorize
public class AnonymousOrAuthorizationRequest implements ContainerRequestFilter {

	private final Algorithm jwtAlgorithm = Algorithm.HMAC256(Config.getProperty("JWTSecret"));
	private final JWTVerifier jwtVerifier = JWT.require(this.jwtAlgorithm).withIssuer("auth0").build();
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException{
		String token = requestContext.getHeaderString("Authorization");
		if(token != null) {
			DecodedJWT decodedToken = null;
			try {
				decodedToken = this.jwtVerifier.verify(token);
			}catch (Exception e) {
				throw new WebApplicationException(Response.status(Status.UNAUTHORIZED).entity("Malfromer token : " +e.getMessage()).type("text/plain").build());
			}
			requestContext.setProperty("user", DataServiceUserCollection.getUser(decodedToken.getClaim("user").asInt()));
		}
	}
}
