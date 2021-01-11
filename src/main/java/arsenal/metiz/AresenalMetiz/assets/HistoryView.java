package arsenal.metiz.AresenalMetiz.assets;

import arsenal.metiz.AresenalMetiz.models.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class HistoryView {

    public Long id;

    public String bill;

    public String contrAgent;

    public String date;

    public float mass;

    public List<Position> positions;
}
