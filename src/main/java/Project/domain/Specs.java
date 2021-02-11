package Project.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "specs")
public class Specs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Integer version;

    @Column(name = "doors")
    private Integer doors;

    @Column(name = "fueltype")
    private String fueltype;

    @Column(name = "power")
    private Float power;

    @Column(name = "tank")
    private Integer tank;

    @Column(name = "transmission")
    private Integer transmission;

    @Column(name = "topspeed")
    private Integer topspeed;

    @Column(name = "acceleration")
    private Float acceleration;

    @Column(name = "consumption")
    private Float consumption;

    @Column(name = "drive")
    private String drive;

    @Column(name = "turnangle")
    private Float turnangle;

    @Column(name = "turbo")
    private Boolean turbo;

    @Column(name = "frontstabilizer")
    private Boolean frontstabilizer;

    @Column(name = "rearstabilizer")
    private Boolean rearstabilizer;

    @Column(name = "abs")
    private Boolean abs;

    @Column(name = "carid")
    private Integer carid;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    public Specs() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }


    public Integer getDoors() {
        return doors;
    }

    public void setDoors(Integer doors) {
        this.doors = doors;
    }

    public String getFueltype() {
        return fueltype;
    }

    public void setFueltype(String fueltype) {
        this.fueltype = fueltype;
    }

    public Float getPower() {
        return power;
    }

    public void setPower(Float power) {
        this.power = power;
    }

    public Integer getTank() {
        return tank;
    }

    public void setTank(Integer tank) {
        this.tank = tank;
    }

    public Integer getTransmission() {
        return transmission;
    }

    public void setTransmission(Integer transmission) {
        this.transmission = transmission;
    }

    public Integer getTopspeed() {
        return topspeed;
    }

    public void setTopspeed(Integer topspeed) {
        this.topspeed = topspeed;
    }

    public Float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Float acceleration) {
        this.acceleration = acceleration;
    }

    public Float getConsumption() {
        return consumption;
    }

    public void setConsumption(Float consumption) {
        this.consumption = consumption;
    }

    public String getDrive() {
        return drive;
    }

    public void setDrive(String drive) {
        this.drive = drive;
    }

    public Float getTurnangle() {
        return turnangle;
    }

    public void setTurnangle(Float turnangle) {
        this.turnangle = turnangle;
    }

    public Boolean getTurbo() {
        return turbo;
    }

    public void setTurbo(Boolean turbo) {
        this.turbo = turbo;
    }

    public Boolean getFrontstabilizer() {
        return frontstabilizer;
    }

    public void setFrontstabilizer(Boolean frontstabilizer) {
        this.frontstabilizer = frontstabilizer;
    }

    public Boolean getRearstabilizer() {
        return rearstabilizer;
    }

    public void setRearstabilizer(Boolean rearstabilizer) {
        this.rearstabilizer = rearstabilizer;
    }

    public Boolean getAbs() {
        return abs;
    }

    public void setAbs(Boolean abs) {
        this.abs = abs;
    }

    public Integer getCarid() {
        return carid;
    }

    public void setCarid(Integer carid) {
        this.carid = carid;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
