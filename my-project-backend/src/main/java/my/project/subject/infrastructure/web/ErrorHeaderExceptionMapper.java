package my.project.subject.infrastructure.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This exception mapper handles uncaught exceptions in any Jersey ReST
 * endpoints, converts them to WebApplicationExceptions (if necessary), and sets
 * the warning header on the response back to the caller using the exception in
 * the message.
 */
@Provider
public class ErrorHeaderExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = LoggerFactory
            .getLogger(ErrorHeaderExceptionMapper.class);

    public static final String WARNING_HEADER = "Warning";

    @Override
    public Response toResponse(final Exception exception) {

        final WebApplicationException waexc = translate(exception);

        final Response response = waexc.getResponse();
        response.getHeaders().add(WARNING_HEADER, exception.getMessage());

        logger.error("An Exception occurred. HTTP status: {}, cause: {}, message: {}",
                response.getStatus(), waexc.getCause(), waexc.getMessage());

        return response;
    }

    // --

    private WebApplicationException translate(final Exception ex) {

        final String message = ex.getMessage() != null ? ex.getMessage().trim() : "";

        final ErrorResponseData errorResponseData = ErrorResponseData.builder()
                .message(message)
                .details(ex.getCause() != null ? ex.getCause().toString(): "")
                .errorCode("0")
                .build();

        Response.Status responseStatus = null;

        if (isInputException(ex)) {
            responseStatus = Response.Status.BAD_REQUEST;
        } else if(isWebApplicationBadRequest(ex)) {
            responseStatus = Response.Status.BAD_REQUEST;
        } else {
            responseStatus = Response.Status.INTERNAL_SERVER_ERROR;
        }

        return new AppWebApplicationException(responseStatus, errorResponseData);
    }

    private boolean isInputException(final Exception ex) {
        return (ex instanceof IllegalArgumentException || ex instanceof JsonProcessingException);
    }

    private boolean isWebApplicationBadRequest(final Exception ex) {
        if(ex instanceof WebApplicationException) {
            final WebApplicationException exception = (WebApplicationException) ex;
            if (exception.getResponse().getStatus() == Response.Status.BAD_REQUEST.getStatusCode()) {
                return true;
            }
        }
        return false;
    }
}
