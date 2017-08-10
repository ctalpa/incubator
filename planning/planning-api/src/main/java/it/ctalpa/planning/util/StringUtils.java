package it.ctalpa.planning.util;

import com.google.common.base.CaseFormat;
import org.apache.tomcat.util.codec.binary.Base64;

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

    private static String NL="\n";
    private static String CR="\r";
    public static final String WHITE_SPACE_REGEX="\\s";
    public static final String EMPTY_STRING="";

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

    /**
     * Convert a raw array of blob bytes into a String encoded by Base64 to use into dto beans
     *
     * @param rawBlobData byte array
     * @param contentType content type of the raw data
     * @return a string Base64-encoded, built with the UTF-8 characters set
     */
    public static String encodeBlobIntoBase64String (final byte[] rawBlobData, final String contentType) {
        StringBuilder sb = new StringBuilder();
        sb.append("data:").append(contentType).append(";base64,")
            .append(org.apache.commons.codec.binary.StringUtils.newStringUtf8(Base64.encodeBase64(rawBlobData, false)));
        return sb.toString();
    }

    public static String removeLineBreak(String value){
        String result=null;
        if(value!=null && !value.isEmpty()){
            result=value.replaceAll(NL,"").replaceAll(CR,"");
        }
        return  result;
    }

    /**
     * Check if the string is null or empty, if not to do the trim method on string.
     *
     * @param value Sting to check
     * @return null if the value is  null or return trimmed string.
     */

    public static String checkAndTrimString(String value){

        String result=null;
        if(value!=null && !value.isEmpty()){
            result=value.trim();
        }
        return result;
    }

    /**
     * Check if the string is not null or not empty.
     *
     * @param value Sting to check
     * @return true if the value is not null or not empty, false if it's.
     */

    public static Boolean isStringIfNotNull(String value){
        Boolean result=Boolean.FALSE;

        String stringTrimmed=checkAndTrimString(value);

        if(stringTrimmed!=null && !stringTrimmed.isEmpty()){
            result=Boolean.TRUE;
        }
        return result;
    }
}
