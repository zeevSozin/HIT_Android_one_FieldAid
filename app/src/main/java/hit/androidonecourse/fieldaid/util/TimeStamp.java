package hit.androidonecourse.fieldaid.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeStamp {
    public static String getTimeStamp(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(Calendar.getInstance().getTime());
    }
}
