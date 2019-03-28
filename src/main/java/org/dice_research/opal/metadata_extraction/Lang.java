package org.dice_research.opal.metadata_extraction;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("lang")
@RequestScoped
public class Lang {

	/**
	 * Returns detected language.
	 * 
	 * http://localhost:9080/metadata/lang/text?text=Willkommen
	 */
	@GET
	@Path("{text}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(@QueryParam("text") String text) {
		try {
			return Response.ok(new LanguageDetector().detectLanguageString(text)).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}