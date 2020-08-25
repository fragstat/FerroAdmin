package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DBCertificate {
    private String id;
    private String mark;
    private String part;
    private String plav;
    private String diameter;
    private String weight;
    private String customer;
    private String createdBy;
    private String date;
    private String time;

    public ParsedCertificate getParsedCertificate(String qr) {
        return new ParsedCertificate(id, customer, diameter, plav, part, weight, mark, qr);
    }
}
