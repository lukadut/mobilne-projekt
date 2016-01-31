package amsa.projekt;

/**
 * Created by Łukasz on 2016-01-31.
 */
public class Fuel {
    private String name;
    private Double price;
    private Integer station;
    private Long time;

    public Fuel() {
    }

    public Fuel(Long time, String name, Double price, Integer station) {
        this.time = time;
        this.name = name;
        this.price = price;
        this.station = station;
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

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
