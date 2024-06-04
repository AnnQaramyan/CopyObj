import java.lang.reflect.*;
import java.util.*;


public class Main {

    public static Map<Object, Object> visited = new HashMap<>();

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        List<String> favoriteBooks = new ArrayList<>();
        favoriteBooks.add("The Catcher in the Rye");
        favoriteBooks.add("1984");
        Man man = new Man("John Doe", 30, favoriteBooks);
        Man manCopy = deepCopy(man);
        if (Objects.equals(man.getName(), manCopy.getName()) &&
                man.getAge() == manCopy.getAge() && man.getFavoriteBooks().equals(manCopy.getFavoriteBooks())) {
            System.out.println("The Mens are equal");
        }
        man.getFavoriteBooks().add("Brave New World");
        if (man.getFavoriteBooks() != manCopy.getFavoriteBooks()) {
            System.out.println("The Mens are not equal after modification");
        }
    }


    public static <T> T deepCopy(T object) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        if (object == null) {
            return null;
        }

        if (visited.containsKey(object)) {
            return (T) visited.get(object);
        }

        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            int length = Array.getLength(object);
            Object newArray = Array.newInstance(clazz.getComponentType(), length);
            visited.put(object, newArray);
            for (int i = 0; i < length; i++) {
                Array.set(newArray, i, deepCopy(Array.get(object, i)));
            }
            return (T) newArray;
        }
        if (object instanceof Collection<?>) {
            Collection<?> originalCollection = (Collection<?>) object;
            Collection<Object> copiedCollection;
            if (object instanceof List<?>) {
                copiedCollection = new ArrayList<>();
            } else if (object instanceof Set<?>) {
                copiedCollection = new HashSet<>();
            } else {
                throw new IllegalArgumentException("Collection type we are not supporting: " + object.getClass());
            }
            visited.put(object, copiedCollection);
            for (Object element : originalCollection) {
                copiedCollection.add(deepCopy(element));
            }
            return (T) copiedCollection;
        } else if (clazz == Character.class ||
                clazz == Integer.class || clazz == Long.class || clazz == Double.class ||
                clazz == Float.class || clazz == Short.class || clazz == Byte.class ||
                clazz == Boolean.class || clazz == String.class) {
            return object;
        } else {
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                int parameterCount = constructor.getParameterCount();
                if (parameterCount != 0) {
                    Object[] params = new Object[parameterCount];
                    for (int i = 0; i < parameterCount; i++) {
                        params[i] = getDefaultValue(constructor.getParameterTypes()[i]);
                    }
                    T copy = (T) constructor.newInstance(params);
                    visited.put(object, copy);
                    copyFields(object, copy);

                    return copy;
                }
            }

            T copy = (T) clazz.newInstance();
            visited.put(object, copy);
            copyFields(object, copy);
            return copy;
        }
    }

    private static <T> void copyFields(T source, T destination) throws IllegalAccessException {
        Class<?> sourceClass = source.getClass();
        Class<?> destinationClass = destination.getClass();

        while (sourceClass != null) {
            for (Field field : sourceClass.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    field.setAccessible(true);
                    Field destinationField;
                    try {
                        destinationField = destinationClass.getDeclaredField(field.getName());
                        destinationField.setAccessible(true);
                        destinationField.set(destination, deepCopy(field.get(source)));
                    } catch (NoSuchFieldException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            sourceClass = sourceClass.getSuperclass();
            destinationClass = destinationClass.getSuperclass();
        }
    }

    private static Object getDefaultValue(Class<?> type) {
        if (type == boolean.class) {
            return false;
        } else if (type == byte.class || type == short.class || type == int.class || type == long.class) {
            return 0;
        } else if (type == float.class || type == double.class) {
            return 0.0;
        } else if (type == char.class) {
            return '\u0000';
        } else {
            return null;
        }
    }
}