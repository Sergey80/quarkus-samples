package my.project.subject.application.common.filters.data;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
final public class ResponseLogEntryData {
    public String contentType;
    public String payload;
    public String headers;
}
