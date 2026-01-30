package util;

import java.lang.reflect.Field;

public class ObjectFieldUtils {

    public static Object getFieldValue(String fieldName, Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object must not be null");
        }

        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                try {
                    field.setAccessible(true);
                    return field.get(object);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        throw new IllegalArgumentException(
                "Field '" + fieldName + "' not found in class " + object.getClass().getName()
        );
    }
}
