package se.unify.example3;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/invoice")
public interface IRestInvoice {

    @POST
    @Path("/create")
    @Produces("application/xml")
    public Response newInvoice(CanonicalInvoice invoice);
   
}
