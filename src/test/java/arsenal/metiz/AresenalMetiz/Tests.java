package arsenal.metiz.AresenalMetiz;

import arsenal.metiz.AresenalMetiz.assets.DocCreating;
import arsenal.metiz.AresenalMetiz.assets.ManufactureTransferView;
import arsenal.metiz.AresenalMetiz.assets.TableView;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class Tests {

    @Test
    public void testParagraphs() {
        ManufactureTransferView view = new ManufactureTransferView("Солнечногорск", "А199АА750", "120", null);
        TableView tv1 = new TableView("Св-1", 1.5f, "К300", 788);
        TableView tv2 = new TableView("Св-2", 2.5f, "К500", 12);
        Set<TableView> set = new HashSet<>();
        set.add(tv1);
        set.add(tv2);
        try {
            DocCreating.writeTransferDocument(view, 5846656L, 7838.7d, set);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert true;
    }


}
