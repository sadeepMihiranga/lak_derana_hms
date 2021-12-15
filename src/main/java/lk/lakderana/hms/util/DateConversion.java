package lk.lakderana.hms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

public final class DateConversion {

    public static final String STANDARD_DATE_FORMAT_WITH_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String STANDARD_DATE_FORMAT = "yyyy-MM-dd";

    private DateConversion() {
        throw new IllegalStateException("Date Conversion Utility class");
    }

    /** Convert the Date to a  given Date Format */
    public static Date convertDate(Date date, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.ENGLISH);
        Date convertedDate = null;
        if(date != null){
            try {
                String formattedDate = df.format(date);
                convertedDate = df.parse(formattedDate);
                return convertedDate;
            } catch (ParseException e) {
                Logger.getLogger("Exception - Date Format : ", e.toString());
                return null;
            }
        }
        return convertedDate;
    }
}
