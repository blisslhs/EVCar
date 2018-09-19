package kr.co.anonymous_evcar.evcar.data;

public class MyCarPush {
    private Integer pk;
    private Integer MyCar_fk;
    private Integer oil_division;
    private Integer coolant_division;
    private Integer tire_division;
    private Integer wiper_division;

    public MyCarPush(){

    }

    public MyCarPush(Integer pk, Integer myCar_fk, Integer oil_division, Integer coolant_division, Integer tire_division, Integer wiper_division) {
        this.pk = pk;
        MyCar_fk = myCar_fk;
        this.oil_division = oil_division;
        this.coolant_division = coolant_division;
        this.tire_division = tire_division;
        this.wiper_division = wiper_division;
    }

    public MyCarPush(Integer myCar_fk, Integer oil_division, Integer coolant_division, Integer tire_division, Integer wiper_division) {
        MyCar_fk = myCar_fk;
        this.oil_division = oil_division;
        this.coolant_division = coolant_division;
        this.tire_division = tire_division;
        this.wiper_division = wiper_division;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getMyCar_fk() {
        return MyCar_fk;
    }

    public void setMyCar_fk(Integer myCar_fk) {
        MyCar_fk = myCar_fk;
    }

    public Integer getOil_division() {
        return oil_division;
    }

    public void setOil_division(Integer oil_division) {
        this.oil_division = oil_division;
    }

    public Integer getCoolant_division() {
        return coolant_division;
    }

    public void setCoolant_division(Integer coolant_division) {
        this.coolant_division = coolant_division;
    }

    public Integer getTire_division() {
        return tire_division;
    }

    public void setTire_division(Integer tire_division) {
        this.tire_division = tire_division;
    }

    public Integer getWiper_division() {
        return wiper_division;
    }

    public void setWiper_division(Integer wiper_division) {
        this.wiper_division = wiper_division;
    }
}
