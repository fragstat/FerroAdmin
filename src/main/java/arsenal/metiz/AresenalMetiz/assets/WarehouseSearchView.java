package arsenal.metiz.AresenalMetiz.assets;

import arsenal.metiz.AresenalMetiz.models.WarehousePackage;
import arsenal.metiz.AresenalMetiz.models.WarehousePosition;
import lombok.Data;

@Data
public class WarehouseSearchView {

    private long id;

    private long createdFrom;

    private String mark, diameter, packing, date, comment, part, plav, manufacturer;

    private double mass;

    private PositionStatus status;

    private SearchType type;

    public WarehouseSearchView(WarehousePosition position) {
        this.id = position.getId();
        this.createdFrom = position.getCreatedFrom();
        this.mark = position.getMark();
        this.diameter = position.getDiameter();
        this.packing = position.getPacking();
        this.date = position.getDate();
        this.comment = position.getComment();
        this.part = position.getPart();
        this.plav = position.getPlav();
        this.manufacturer = position.getManufacturer();
        this.mass = position.getMass();
        this.status = position.getStatus();
        this.type = SearchType.POSITION;
    }

    public WarehouseSearchView(WarehousePackage position) {
        this.id = position.getId();
        this.createdFrom = -1;
        this.mark = position.getMark();
        this.diameter = position.getDiameter();
        this.packing = position.getPacking();
        this.date = position.getDate();
        this.comment = position.getComment();
        this.part = position.getPart();
        this.plav = position.getPlav();
        this.manufacturer = position.getManufacturer();
        this.mass = position.getMass();
        this.status = position.getStatus();
        this.type = SearchType.PACKAGE;
    }
}
