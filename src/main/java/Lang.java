import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("lang")
@RequestScoped
public class Lang {

	/**
	 * basic path test
	 * 
	 * http://localhost:9080/metadata/lang
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get() {
		return Response.ok("hello world").build();
	}

//	/**
//	 * Just prints an ID
//	 * 
//	 * http://localhost:9080/metadata/lang/no1
//	 */
//	@GET
//	@Path("{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	public Response getBook(@PathParam("id") String id) {
//		return Response.ok(id).build();
//	}

	/**
	 * Prints parameter
	 * 
	 * http://localhost:9080/metadata/lang/text?text=Hi
	 */
	@GET
	@Path("{text}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(@QueryParam("text") String text) {
		return Response.ok(text).build();
	}
}