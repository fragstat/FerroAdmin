package arsenal.metiz.AresenalMetiz.models;

import javax.persistence.*;

@Entity
@Table(name = "prod")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String mark, diameter, packing, date;

    private Float mass;

    public Order() {
    }

    public Order(String mark, String diameter, String packing, String date, Float mass) {
        this.mark = mark;
        this.diameter = diameter;
        this.packing = packing;
        this.date = date;
        this.mass = mass;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getPacking() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Float getMass() {
        return mass;
    }

    public void setMass(Float mass) {
        this.mass = mass;
    }
}
