package my.project.subject.application.common.filters.data;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class RequestLogEntryData {
    public String method;
    public String contentType;
    public String path;
    public String payload;
    public String headers;
    public String address;
}
