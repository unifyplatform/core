package se.unify.example2;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/")
public interface IInvoiceService {

    @PUT
    @Path("/invoices")
    @Produces("application/xml")
    public Response newInvoice(String invoice);
        
}
