package arsenal.metiz.AresenalMetiz.models;

import lombok.Data;

import java.util.List;

@Data
public class MDeparture {

    private List<SimpleDepartureObj> data;
    private String contrAgent;
    private Long account;
}
