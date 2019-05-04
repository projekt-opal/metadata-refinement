package org.dice_research.opal.metadata_extraction.webservice;

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
import org.dice_research.opal.metadata_extraction.lang_detection.LangDetectorJena;

/**
 * Webservice for language detection.
 *
 * @author Adrian Wilke
 */
@Path("lang/model")
@RequestScoped
public class LangServiceModel {

	/**
	 * Detects languages of metadata of datasets and adds them to model.
	 * 
	 * e.g. http://localhost:9080/metadata/lang/model?turtleBytes=...
	 */
	@GET
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response addToModel(@QueryParam("turtleBytes") byte[] turtleBytes) {
		try {
			Model model = ModelSerialization.deserialize(turtleBytes);
			model = new LangDetectorJena().addLanguage(model);
			turtleBytes = ModelSerialization.serialize(model);
			return Response.ok(turtleBytes).build();
		} catch (Throwable t) {
			System.err.println(t + " " + this.getClass().getName());
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
	}

}