package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsedCertificate {
    private String id;
    private String customer;
    private String diameter;
    private String plav;
    private String part;
    private String weight;
    private String mark;
    private String qr;

    public DBCertificate getDBCertificate() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return new DBCertificate(id, mark, part, plav, diameter, weight, customer, "sergey",
                dateFormat.format(date), timeFormat.format(date));
    }
}
