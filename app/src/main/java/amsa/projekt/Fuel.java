package amsa.projekt;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class Fuel {
    private Long id;
    private String name;
    private Double price;
    private Long station;
    private Long time;
    private int active;

    public Fuel() {
    }

    public Fuel(Long id, Long time, String name, Double price, Long station, int active) {
        this.time = time;
        this.name = name;
        this.price = price;
        this.station = station;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getStation() {
        return station;
    }

    public void setStation(Long station) {
        this.station = station;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
