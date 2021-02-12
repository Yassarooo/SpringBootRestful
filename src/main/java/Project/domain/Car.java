package Project.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Integer version;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "price")
    private Float price;

    @Column(name = "year")
    private Date year;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "buyername")
    private String buyername;

    @Column(name = "selldate")
    private Date selldate;

    @Column(name = "sellprice")
    private Float sellprice;

    @Column(name = "level")
    private String level;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "sold")
    private Boolean sold;

    @Column(name = "paramid")
    private Integer paramid;

    @Column(name = "specsid")
    private Integer specsid;

    @JoinColumn(name = "images")
    private List<String> images = new ArrayList<String>();

    @ManyToOne
    @JoinColumn(name = "Params_ID")
    private Parameters params;


    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "car")
    private Specs specs;

    public Car() {

    }

    public Car(String model,String brand, Float price, Integer seats, String buyername, Float sellprice, Date selldate, Boolean sold, Integer paramid,String level,double rate,List<String> images,Date year) {
        this.price = price;
        this.model = model;
        this.brand = brand;
        this.seats = seats;
        this.buyername = buyername;
        this.sellprice = sellprice;
        this.selldate = selldate;
        this.sold = sold;
        this.paramid = paramid;
        this.level = level;
        this.rate = rate;
        this.year = year;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
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

    public Integer getParamid() {
        return paramid;
    }

    public void setParamid(Integer paramid) {
        this.paramid = paramid;
    }

    public Integer getVersion() { return version; }

    public void setVersion(Integer version) { this.version = version; }

    public Integer getSpecsid() {
        return specsid;
    }

    public void setSpecsid(Integer specsid) {
        this.specsid = specsid;
    }

    public Specs getSpecs() {
        return specs;
    }

    public void setSpecs(Specs specs) {
        this.specs = specs;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }
}
