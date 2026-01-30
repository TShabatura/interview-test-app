package util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;

public class AllureUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private AllureUtils() {
    }

    public static void attachDto(String name, Object dto) {
        try {
            String json = MAPPER.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(dto);
            Allure.addAttachment(name, "application/json", json);
        } catch (Exception e) {
            Allure.addAttachment(name, "error", e.getMessage());
        }
    }
}
