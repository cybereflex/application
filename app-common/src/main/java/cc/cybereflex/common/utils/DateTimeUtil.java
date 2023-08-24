package cc.cybereflex.common.utils;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SuppressWarnings("unused")
public class DateTimeUtil {

    public static Date toDate(@NonNull LocalDate localDate){
        return Date.from(
                localDate.atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    public static Date toDate(@NonNull LocalDateTime localDateTime){
        return Date.from(
                localDateTime.atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    public static LocalDate toLocalDate(@NonNull Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static LocalDateTime toLocalDateTime(@NonNull Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
