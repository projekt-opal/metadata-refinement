package org.dice_research.opal.metadata_extraction.webservice;

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

import org.apache.jena.rdf.model.Model;
import org.dice_research.opal.common.utilities.ModelSerialization;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetector;
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetectorJena;

/**
 * Webservice for language detection.
 *
 * @author Adrian Wilke
 */
@Path("lang")
@RequestScoped
public class LangService {

	/**
	 * Returns detected language of plain text.
	 * 
	 * e.g. http://localhost:9080/metadata/lang/text?text=Sprachen%20lernen
	 */
	@GET
	@Path("{text}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(@QueryParam("text") String text) {
		try {
			return Response.ok(new LangDetector().detectLanguageString(text)).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * Detects language and adds it to Jena model.
	 * 
	 * e.g. http://localhost:9080/metadata/lang/model?bytes=...
	 */
	@GET
	@Path("{model}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addModel(@QueryParam("bytes") byte[] bytes) {
		try {
			Model model = ModelSerialization.deserialize(bytes);
			model = new LangDetectorJena().addLanguage(model);
			bytes = ModelSerialization.serialize(model);
			return Response.ok(bytes).build();
		} catch (IOException e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}