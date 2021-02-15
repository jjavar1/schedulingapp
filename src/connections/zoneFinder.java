package connections;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class zoneFinder {
    public static java.sql.Timestamp getTimeZone() {
        ZoneId zoneid = ZoneId.of("UTC");
        LocalDateTime localDateTime = LocalDateTime.now(zoneid);
        java.sql.Timestamp timeZone = Timestamp.valueOf(localDateTime);
        return timeZone;
    }
    public static java.sql.Date getDateZone() {
        java.sql.Date dateZone = java.sql.Date.valueOf(LocalDate.now());
        return dateZone;
    }
}
