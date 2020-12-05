package Project.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "cars")
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Float price;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "buyername")
    private String buyername;

    @Column(name = "selldate")
    private Date selldate;

    @Column(name = "sellprice")
    private Float sellprice;

    @Column(name = "sold")
    private Boolean sold;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Params_ID")
    private Parameters params;

    public Car() {

    }

    public Car(String name, Float price, Integer seats, String buyername, Float sellprice, Date selldate, Boolean sold) {
        this.price = price;
        this.name = name;
        this.seats = seats;
        this.buyername = buyername;
        this.sellprice = sellprice;
        this.selldate = selldate;
        this.sold = sold;
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

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public Parameters getParams() {
        return params;
    }

    public void setParams(Parameters params) {
        this.params = params;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public Date getSelldate() {
        return selldate;
    }

    public void setSelldate(Date selldate) {
        this.selldate = selldate;
    }

    public Float getSellprice() {
        return sellprice;
    }

    public void setSellprice(Float sellprice) {
        this.sellprice = sellprice;
    }

    public Boolean getSold() {
        return sold;
    }

    public void setSold(Boolean sold) {
        this.sold = sold;
    }
}
