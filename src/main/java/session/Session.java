package session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Session {

    private static final ThreadLocal<Session> CTX = ThreadLocal.withInitial(Session::new);

    private final Map<SessionKey, Object> data = new ConcurrentHashMap<>();

    private Session() {
    }

    public static Session context() {
        return CTX.get();
    }

    public static void destroy() {
        CTX.remove();
    }

    public <T> void put(SessionKey key, T value) {
        data.put(key, value);
    }

    public <T> T get(SessionKey key, Class<T> type) {
        Object value = data.get(key);
        if (value == null) {
            throw new IllegalStateException("Session key not found: " + key);
        }
        return cast(key, value, type);
    }

    public <T> void addToList(SessionKey key, T value) {
        list(key).add(value);
    }

    public <T> List<T> getList(SessionKey key, Class<T> elementType) {
        Object value = data.get(key);
        if (value == null) return List.of();

        if (!(value instanceof List<?> rawList)) {
            throw new IllegalStateException("Session key '" + key + "' is not a List. Actual: " + value.getClass());
        }

        List<T> typed = new ArrayList<>(rawList.size());
        for (Object item : rawList) {
            if (item == null) {
                typed.add(null);
            } else if (!elementType.isInstance(item)) {
                throw new IllegalStateException(
                        "Session list '" + key + "' contains " + item.getClass()
                                + ", expected: " + elementType
                );
            } else {
                typed.add(elementType.cast(item));
            }
        }
        return Collections.unmodifiableList(typed);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> list(SessionKey key) {
        return (List<T>) data.computeIfAbsent(key, k -> new ArrayList<>());
    }

    private static <T> T cast(SessionKey key, Object value, Class<T> type) {
        if (!type.isInstance(value)) {
            throw new IllegalStateException(
                    "Session key '" + key + "' type mismatch. Expected: " + type + ", actual: " + value.getClass()
            );
        }
        return type.cast(value);
    }
}
