package org.dice_research.opal.metadata_extraction.webservice;

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

import org.dice_research.opal.metadata_extraction.fox.Extraction;
import org.dice_research.opal.metadata_extraction.fox.FoxRemote;
import org.json.JSONArray;

@Path("fox")
@RequestScoped
public class FoxService {

	public static final String FOX_API_ENDPOINT = "http://fox.cs.upb.de:4444/fox";

	/**
	 * Returns FOX result in TURTLE format.
	 * 
	 * e.g.
	 * http://localhost:9080/metadata/fox/getTurtle?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en
	 */
	@GET
	@Path("{getTurtle}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getTurtle(@Context UriInfo info) {
		try {
			String text = info.getQueryParameters().getFirst("text");
			String lang = info.getQueryParameters().getFirst("lang");
			FoxRemote fox = new FoxRemote().setEndpoint(FOX_API_ENDPOINT);
			return Response.ok(fox.getTurtle(text, lang)).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Returns location names as JSON array.
	 * 
	 * e.g.
	 * http://localhost:9080/metadata/fox/getLocationNames?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en
	 */
	@GET
	@Path("{getLocationNames}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationNames(@Context UriInfo info) {
		try {
			String text = info.getQueryParameters().getFirst("text");
			String lang = info.getQueryParameters().getFirst("lang");
			FoxRemote fox = new FoxRemote().setEndpoint(FOX_API_ENDPOINT);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(new Extraction().extractLocationNames(fox.getTurtle(text, lang)));
			return Response.ok(jsonArray).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Returns location names as JSON array.
	 * 
	 * e.g.
	 * http://localhost:9080/metadata/fox/getLocationUris?text=A.%20Einstein%20was%20born%20in%20Ulm.&lang=en
	 */
	@GET
	@Path("{getLocationUris}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLocationUris(@Context UriInfo info) {
		try {
			String text = info.getQueryParameters().getFirst("text");
			String lang = info.getQueryParameters().getFirst("lang");
			FoxRemote fox = new FoxRemote().setEndpoint(FOX_API_ENDPOINT);
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(new Extraction().extractLocationUris(fox.getTurtle(text, lang)));
			return Response.ok(jsonArray).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}