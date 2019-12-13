package my.project.subject.infrastructure.web;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class AppWebApplicationException extends WebApplicationException {
    private static final long serialVersionUID = 1L;

    public AppWebApplicationException(final Response.Status status, final Object entity) {
        super(Response.status(status).entity(entity).build());
    }
}
