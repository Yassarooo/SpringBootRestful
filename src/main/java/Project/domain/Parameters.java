package Project.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity

@Table(name = "parameters")
public class Parameters implements Serializable, Comparable< Parameters > {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "seats")
    private Integer seats = 4;

    @Column(name = "percentage")
    private Float percentage = 5f;


    public Parameters() {

    }

    public Parameters(String name, Integer seats, Float percentage) {
        this.name = name;
        this.seats = seats;
        this.percentage = percentage;
    }

    @Override
    public int compareTo(Parameters o) {
        return this.getId().compareTo(o.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Integer getSeats() { return seats; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSeats(Integer seats) { this.seats = seats; }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

}
