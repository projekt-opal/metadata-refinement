package org.dice_research.opal.metadata_extraction.webservice;

import java.util.List;

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

import org.dice_research.opal.metadata_extraction.fox.Extraction;
import org.dice_research.opal.metadata_extraction.fox.FoxRemote;
import org.json.JSONArray;

@Path("fox/location/names")
@RequestScoped
public class FoxServiceLocationNames {

	/**
	 * Returns location names as JSON array.
	 * 
	 * e.g.
	 * http://localhost:9080/metadata/fox/location/names?text=Paderborn%20und%20Bad%20Oeynhausen%20sind%20in%20NRW&lang=de
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationNames(@Context UriInfo info) {
		try {
			String text = info.getQueryParameters().getFirst("text");
			String lang = info.getQueryParameters().getFirst("lang");
			FoxRemote fox = new FoxRemote().setEndpoint(FoxService.FOX_API_ENDPOINT);
			List<String> locationNames = new Extraction().extractLocationNames(fox.getTurtle(text, lang));
			return Response.ok(new JSONArray(locationNames).toString()).build();
		} catch (Throwable t) {
			System.err.println(t + " " + this.getClass().getName());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}