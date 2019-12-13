package my.project.subject.infrastructure.web;

import my.project.subject.application.common.filters.data.RequestLogEntryData;
import my.project.subject.application.common.filters.data.ResponseLogEntryData;
import io.vertx.core.http.HttpServerRequest;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.Providers;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicInteger;

@Provider
public class CustomRequestResponseLogger implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomRequestResponseLogger.class);

    final AtomicInteger requestId = new AtomicInteger(0); // TODO: instead use spans from from quarkus-smallrye-open-tracing

    final String LOGGING_ID_PROPERTY = "logId";

    @Context
    private Providers providers;

    @Context
    UriInfo info;

    @Context
    HttpServerRequest request;

    @Override
    public void filter(final ContainerRequestContext requestContext) {

        final long logId = requestId.incrementAndGet();

        if (isJson(requestContext)) {
            try {
                final RequestLogEntryData requestLogEntryData = new RequestLogEntryData();
                requestLogEntryData.method = requestContext.getMethod();
                requestLogEntryData.contentType = requestContext.getMediaType() != null ? requestContext.getMediaType().toString(): null;
                requestLogEntryData.path =  info.getAbsolutePath().toString();
                requestLogEntryData.payload = IOUtils.toString(requestContext.getEntityStream(), java.nio.charset.StandardCharsets.UTF_8);
                requestLogEntryData.address = request.remoteAddress().toString();
                requestLogEntryData.headers = requestContext.getHeaders().toString();

                final String json = JacksonUtil.toJsonString(requestLogEntryData);

                LOGGER.info("Request: [{}], {}", logId, json);

                final InputStream in = IOUtils.toInputStream(json, java.nio.charset.StandardCharsets.UTF_8);
                requestContext.setEntityStream(in);

            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        requestContext.setProperty(LOGGING_ID_PROPERTY, logId);
    }

    @Override
    public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext responseContext) throws IOException {

        final ResponseLogEntryData responseLogEntryData = new ResponseLogEntryData();
        responseLogEntryData.contentType = responseContext.getMediaType() != null ? responseContext.getMediaType().toString(): "";
        responseLogEntryData.payload = payloadMessage(responseContext);

        long logId = (long) requestContext.getProperty(LOGGING_ID_PROPERTY);

        LOGGER.info("Response: [{}] {}", logId, JacksonUtil.toJsonString(responseLogEntryData));
    }

    // --


    boolean isJson(ContainerRequestContext request) {

        if (request.getMediaType() != null) {
            return request.getMediaType().toString().contains("application/json");
        } else {
            return false;
        }
    }


    private String payloadMessage(final ContainerResponseContext responseContext) throws IOException {

        final StringBuilder message = new StringBuilder();

        if (responseContext.hasEntity()) {

            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            final Class<?> entityClass = responseContext.getEntityClass();

            final Type entityType = responseContext.getEntityType();
            final Annotation[] entityAnnotations = responseContext.getEntityAnnotations();
            final MediaType mediaType = responseContext.getMediaType();

            @SuppressWarnings("unchecked")
            final MessageBodyWriter<Object> bodyWriter = (MessageBodyWriter<Object>) providers.getMessageBodyWriter(
                                                                                                entityClass,
                                                                                                entityType,
                                                                                                entityAnnotations,
                                                                                                mediaType);
            bodyWriter.writeTo(responseContext.getEntity(),
                                entityClass,
                                entityType,
                                entityAnnotations,
                                mediaType,
                                responseContext.getHeaders(),
                                baos);

            message.append(new String(baos.toByteArray()));
        }

        return message.toString();
    }

}
