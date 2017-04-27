package ct.app.apms.util;

import com.google.common.collect.Sets;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Stream;

public final class ModelUtils {

    // private ctor to prevent instantiation
    private ModelUtils() {
    }

    /**
     * Merges the non-{@code null} properties of two objects of the same type together.
     *
     * @param source         the object to pull properties from
     * @param target         the object to fill properties into
     * @param excludedFields an array of field names that will not have data copied from
     */
    public static void merge(Object source, Object target, String... excludedFields) {
        excludedFields = Stream.concat(Arrays.stream(getNullFieldNames(source)), Arrays.stream(excludedFields)).toArray(String[]::new);

        BeanUtils.copyProperties(source, target, excludedFields);
    }

    /**
     * Merges the non-{@code null} properties of two objects of the same type together.
     *
     * @param source         the object to pull properties from
     * @param target         the object to fill properties into
     * @param includedFields an array of field names that will have data copied from
     */
    public static void mergeOnly(Object source, Object target, String... includedFields) {
        String[] excludedFields = Stream.concat(
            Arrays.stream(getNullFieldNames(source)), Arrays.stream(getExcludedFieldNames(source, includedFields))
        ).toArray(String[]::new);

        BeanUtils.copyProperties(source, target, excludedFields);
    }

    // Copied from: http://stackoverflow.com/a/32066155
    private static String[] getNullFieldNames(Object source) {
        BeanWrapper wrappedSource = new BeanWrapperImpl(source);

        return Stream.of(wrappedSource.getPropertyDescriptors())
            .map(FeatureDescriptor::getName)
            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
            .toArray(String[]::new);
    }

    private static String[] getExcludedFieldNames(Object source, String[] includedFields) {
        BeanWrapper wrappedSource = new BeanWrapperImpl(source);

        HashSet<String> includedPropertyNames = Sets.newHashSet(includedFields);

        return Stream.of(wrappedSource.getPropertyDescriptors())
            .map(FeatureDescriptor::getName)
            .filter(propertyName -> !includedPropertyNames.contains(propertyName))
            .toArray(String[]::new);
    }
}
