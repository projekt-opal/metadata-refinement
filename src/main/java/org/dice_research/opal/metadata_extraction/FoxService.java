package org.dice_research.opal.metadata_extraction;

import java.io.IOException;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

@Path("fox")
@RequestScoped
public class FoxService {

	public static final String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";

	/**
	 * Returns FOX result
	 * 
	 * e.g.
	 * http://localhost:9080/metadata/fox?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(@Context UriInfo info) {
		try {
			String text = info.getQueryParameters().getFirst("text");
			String lang = info.getQueryParameters().getFirst("lang");
			org.dice_research.opal.metadata_extraction.fox.Fox fox = new org.dice_research.opal.metadata_extraction.fox.Fox()
					.setEndpoint(FOX_API_ENDPOINT);
			return Response.ok(fox.send(text, lang)).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}