package gt.com.tigo.integradorhome.util;

public class DateTimeFormatterFactory {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final String DATETIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public IDateTimeFormat getFormatter(DateTimeFormatterType type) {

        switch (type) {
            case DATE: return new DateTimeFormat(DATE_FORMAT);
            case TIME: return new DateTimeFormat(TIME_FORMAT);
            default: return new DateTimeFormat(DATETIME_FORMAT);
        }

    }

}
