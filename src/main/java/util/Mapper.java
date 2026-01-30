package util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Objects;

public class Mapper {
    public static Map<String, Object> convertToQueryParams(Object dto) {
        Map<String, Object> queryParams = new ObjectMapper().convertValue(dto, new TypeReference<>() {
        });
        queryParams.values().removeIf(Objects::isNull);
        return queryParams;
    }
}
