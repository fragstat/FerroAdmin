package arsenal.metiz.AresenalMetiz;

import arsenal.metiz.AresenalMetiz.models.WarehouseAddPosition;
import arsenal.metiz.AresenalMetiz.service.manufactureservice.ManufactureServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ManufactureServiceTests {

    @Autowired
    private ManufactureServiceImpl service;


    @Test
    public void verifyTest() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С-О", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest2() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.2", "К300", "", "Ф7000/1", "151515", "ферро", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest3() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К400", "", "Ф7000/1", "151515", "ферро", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest4() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/2", "151515", "ферро", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest5() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "202020", "ферро", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest6() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферротрейд", 20f));
        assert (!service.verify(list));
    }

    @Test
    public void verifyTest7() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 20f));
        assert (service.verify(list));
    }

    @Test
    public void verifyTest8() {
        List<WarehouseAddPosition> list = new ArrayList<>();
        list.add(new WarehouseAddPosition("Св-08Г2С", "5.0", "К300", "", "Ф7000/1", "151515", "ферро", 15f));
        assert (!service.verify(list));
    }

}
