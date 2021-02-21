package be.vinci.pae.api;

import java.util.List;

import org.glassfish.jersey.server.ContainerRequest;

import be.vinci.pae.api.filters.AnonymousOrAuthorize;
import be.vinci.pae.api.filters.Authorize;
import be.vinci.pae.domain.Page;
import be.vinci.pae.domain.User;
import be.vinci.pae.services.DataServicePageCollection;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Singleton
@Path("/pages")
public class PageResource {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page create(Page page, @Context ContainerRequest request) {
		User currentUser = (User) request.getProperty("user");
		if (page == null || page.getTitle() == null  || page.getContent() == null
				||  page.getPublicationStatus() == null || page.getUri() == null)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity("Lacks of mandatory info or wrong publication status").type("text/plain").build());
		DataServicePageCollection.addPage(page, currentUser);
		return page;
	}

	/**
	 * This method can either allows anonymous request or authenticated user (note
	 * that in the exercise, it was simplified to allow only authenticated user)
	 * 
	 * @param request : this will contain the user if the request contains an
	 *                authorization header
	 * @return
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	// @Authorize
	@AnonymousOrAuthorize
	public Page getPage(@PathParam("id") int id, @Context ContainerRequest request) {

		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		User authenticatedUser = (User) request.getProperty("user");
		Page pageFound;
		// deals with anonymous requests
		if (authenticatedUser == null)
			pageFound = DataServicePageCollection.getPage(id);
		else
			pageFound = DataServicePageCollection.getPage(id, authenticatedUser);
		// deals with authenticated requests
		if (pageFound == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return pageFound;
	}

	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Authorize
	public Page updatePage(Page page, @PathParam("id") int id, @Context ContainerRequest request) {
		User currentUser = (User) request.getProperty("user");
		if (page == null || page.getTitle() == null || page.getContent() == null
				||  page.getPublicationStatus() == null || page.getUri() == null)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
					.entity("Lacks of mandatory info or wrong publication status").type("text/plain").build());

		page.setId(id);
		Page updatedPage = DataServicePageCollection.updatePage(page, currentUser);

		if (updatedPage == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return updatedPage;
	}

	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Authorize
	public Page deletePage(@PathParam("id") int id, @Context ContainerRequest request) {
		User currentUser = (User) request.getProperty("user");
		if (id == 0)
			throw new WebApplicationException(Response.status(Status.BAD_REQUEST).entity("Lacks of mandatory id info")
					.type("text/plain").build());

		Page deletedPage = DataServicePageCollection.deletePage(id, currentUser);

		if (deletedPage == null)
			throw new WebApplicationException(Response.status(Status.NOT_FOUND)
					.entity("Ressource with id = " + id + " could not be found").type("text/plain").build());

		return deletedPage;
	}

	/**
	 * This method can either allows anonymous request or authenticated user (note
	 * that in the exercise, it was simplified to allow only authenticated user)
	 * 
	 * @param request : this will contain the user if the request contains an
	 *                authorization header
	 * @return
	 */

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	// @Authorize
	@AnonymousOrAuthorize
	public List<Page> getAllPages(@Context ContainerRequest request) {
		User authenticatedUser = (User) request.getProperty("user");
		// deals with anonymous requests
		if (authenticatedUser == null) {
			return DataServicePageCollection.getPages();
		}
		// deals with authenticated requests
		return DataServicePageCollection.getPages(authenticatedUser);

	}

}
