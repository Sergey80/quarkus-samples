package my.project.subject.utils;

import my.project.subject.infrastructure.web.JacksonUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class TestUtils {

    private String readJsonFromFile(final String fileName) {
        try {

            final InputStream inputStream = getClass()
                    .getClassLoader().getResourceAsStream(fileName);

            final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            return br.lines().collect(Collectors.joining());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Object readObjectFromFile(final String fileName, Class clazz) {
        try {
            return JacksonUtil.jsonToObject(readJsonFromFile(fileName), clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

}
