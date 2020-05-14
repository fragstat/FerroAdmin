package arsenal.metiz.AresenalMetiz.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ArchivedRequests {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name, email, request, date;

    public  ArchivedRequests() {
    }

    public ArchivedRequests(String name, String email, String request, String date) {
        this.name = name;
        this.email = email;
        this.request = request;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
