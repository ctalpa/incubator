package ct.app.apms.util;

import com.google.common.base.CaseFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class containing a number of utility methods for
 * doing common String manipulation work.
 *
 * @author Derek McKinnon
 */
public final class StringUtils {

    // Private ctor to prevent instantiation
    private StringUtils() {
    }

    /**
     * Converts a {@code snake_case} string into a {@code camelCase} one.
     *
     * @param string The string to convert
     * @return The camelCase version of the string
     */
    public static String snakeCaseToCamelCase(String string) {
        String[] columnNameParts = string.split("_");
        if (columnNameParts.length == 1) {
            return string;
        }

        String newColumnName = columnNameParts[0];
        for (int i = 1; i < columnNameParts.length; i++) {
            newColumnName += toUpperCaseFirstLetter(columnNameParts[i]);
        }

        return newColumnName;
    }

    public static String camelCaseToSnakeCase(String string) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, string);
    }

    private static String toUpperCaseFirstLetter(String s) {
        char[] parts = s.toCharArray();
        parts[0] = Character.toUpperCase(parts[0]);

        return String.valueOf(parts);
    }

    /**
     * Joins a {@link List<T>} of elements together using the specified delimiter.
     *
     * @param list      The list of elements
     * @param delimiter A string used to separate the elements
     * @see StringUtils#listToString(List)
     */
    public static <T> String listToString(List<T> list, String delimiter) {
        return list.stream().map(Object::toString).collect(Collectors.joining(delimiter));
    }

    /**
     * Joins a {@link List<T>} of elements together using a comma ({@code ,})
     *
     * @param list The list of elements
     */
    public static <T> String listToString(List<T> list) {
        return listToString(list, ",");
    }

    /**
     * Joins a {@link List<T>} of elements together using a comma ({@code ,})
     * and surrounds it by braces to make it into an array literal.
     *
     * @param list the list of elements
     */
    public static <T> String listToArrayLiteral(List<T> list) {
        return "'{" + listToString(list) + "}'";
    }

    public static String dateToTimestampLiteral(LocalDateTime date) {
        String format = date.format(DateTimeFormatter.ISO_DATE_TIME);

        return "'" + format + "'::timestamp";
    }


}
