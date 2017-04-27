package ct.app.apms.util.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class JSR310DateConverters {

    public static final String DEFAULT_PATTERN_DATE_TIME="yyyy-MM-dd HH:mm:ss";

    public static final String DEFAULT_PATTERN_DATE="yyyy-MM-dd";

    public static final String DEFAULT_PATTERN_TIME="HHmm";

    private JSR310DateConverters() {
    }

    public static class LocalDateToDateConverter implements Converter<LocalDate, Date> {

        public static final LocalDateToDateConverter INSTANCE = new LocalDateToDateConverter();

        private LocalDateToDateConverter() {
        }

        @Override
        public Date convert(LocalDate source) {
            return source == null ? null : Date.from(source.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateConverter implements Converter<Date, LocalDate> {
        public static final DateToLocalDateConverter INSTANCE = new DateToLocalDateConverter();

        private DateToLocalDateConverter() {
        }

        @Override
        public LocalDate convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault()).toLocalDate();
        }
    }

    public static class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
        public static final ZonedDateTimeToDateConverter INSTANCE = new ZonedDateTimeToDateConverter();

        private ZonedDateTimeToDateConverter() {
        }

        @Override
        public Date convert(ZonedDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    public static class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
        public static final DateToZonedDateTimeConverter INSTANCE = new DateToZonedDateTimeConverter();

        private DateToZonedDateTimeConverter() {
        }

        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public static class LocalDateTimeToDateConverter implements Converter<LocalDateTime, Date> {
        public static final LocalDateTimeToDateConverter INSTANCE = new LocalDateTimeToDateConverter();

        private LocalDateTimeToDateConverter() {
        }

        @Override
        public Date convert(LocalDateTime source) {
            return source == null ? null : Date.from(source.atZone(ZoneId.systemDefault()).toInstant());
        }
    }

    public static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {
        public static final DateToLocalDateTimeConverter INSTANCE = new DateToLocalDateTimeConverter();

        private DateToLocalDateTimeConverter() {
        }

        @Override
        public LocalDateTime convert(Date source) {
            return source == null ? null : LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    public static LocalDateTime convertLocalDateToLocalDateTime(LocalDate localdate){
        LocalDateTime result=null;
        if(localdate!=null){
            Instant instant=localdate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            result = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        return result;
    }

    public static LocalDateTime convertStringToLocalDateTime(String dateTime, String patternDateTime){
        LocalDateTime result=null;

        if(dateTime!=null && !dateTime.isEmpty()){

            if(patternDateTime==null || patternDateTime.isEmpty()){
                patternDateTime=DEFAULT_PATTERN_DATE_TIME;
            }

            final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(patternDateTime);
            result = LocalDateTime.parse(dateTime, DATE_FORMAT);

        }
        return result;
    }

    public static LocalTime convertStringToLocalTime(String time, String patternTime){
        LocalTime result=null;

        if(time!=null && !time.isEmpty()){

            if(patternTime==null || patternTime.isEmpty()){
                patternTime=DEFAULT_PATTERN_TIME;
            }

            final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(patternTime);
            result = LocalTime.parse(time, DATE_FORMAT);

        }
        return result;
    }

}
