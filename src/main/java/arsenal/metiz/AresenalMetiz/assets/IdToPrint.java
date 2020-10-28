package arsenal.metiz.AresenalMetiz.assets;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class IdToPrint {
    private List<Long> id;
    private String file;
}
