package Project.domain;

import javax.persistence.*;

@Entity

@Table(name = "parameters")
public class Parameters {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "seats")
    private Integer seats = 4;

    @Column(name = "percentage")
    private Float percentage = 5f;


    public Parameters() {

    }

    public Parameters(Integer seats, Float percentage) {
        this.seats = seats;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

}
