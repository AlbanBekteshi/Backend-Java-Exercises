package be.vinci.pae.api;

import java.util.List;

import be.vinci.pae.domain.Film;
import be.vinci.pae.domain.Texte;
import be.vinci.pae.services.DataServiceFilmCollection;
import be.vinci.pae.services.DataServiceTextCollection;
import jakarta.annotation.Priority;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/texts")
public class TexteResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Texte create(Texte text) {
		if(text == null || text.getContent() == null || text.getContent().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());
		DataServiceTextCollection.addTexte(text);
		
		return text;
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Texte getTexte(@PathParam("id") int id) {
		if (id==0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());
		Texte textFound = DataServiceTextCollection.getTexte(id);
		
		if (textFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return textFound;
	}
	
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Texte updateText(Texte text, @PathParam("id") int id) {
		if(text == null || text.getContent() == null || text.getContent().isEmpty())
			throw new WebApplicationException(
					Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory info").type("text/plain").build());

		text.setId(id);
		Texte updatedText = DataServiceTextCollection.updateTexte(text);
		
		if (updatedText == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updatedText;
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Texte deleteTexte(@PathParam("id") int id) {
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		Texte deletedTexte = DataServiceTextCollection.deleteTexte(id);

		if (deletedTexte == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedTexte;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Texte> getAllTexts(@DefaultValue("-1") @QueryParam("select-level") String selectLevel){
		if(!selectLevel.equals("-1"))
			return DataServiceTextCollection.getTexts(selectLevel);
		return DataServiceTextCollection.getTexts();

	}

}
