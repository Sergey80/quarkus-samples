package my.project.subject.infrastructure.web;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

final public class JacksonUtil {

    static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String toJsonString(final T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Object jsonToObject(String jsonText, Class<?> cls) {
        try {
            return objectMapper.readValue(jsonText, cls);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
