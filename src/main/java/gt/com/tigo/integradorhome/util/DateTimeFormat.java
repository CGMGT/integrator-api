package gt.com.tigo.integradorhome.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeFormat implements IDateTimeFormat {

    private DateFormat dateFormat;

    public DateTimeFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
    }

    public String format(Date date) {
        if (date == null) {
            return "";
        }

        return this.dateFormat.format(date);
    }

}
