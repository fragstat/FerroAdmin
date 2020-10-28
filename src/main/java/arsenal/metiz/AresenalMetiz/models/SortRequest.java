package arsenal.metiz.AresenalMetiz.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SortRequest {

    public List<String> mark;
    public List<String> packing;
    public Float DL;
    public Float DM;

}
