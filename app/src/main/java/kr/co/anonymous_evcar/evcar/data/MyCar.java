package kr.co.anonymous_evcar.evcar.data;

public class MyCar {
    private Integer pk;
    private String nickname;            //차량 닉네임(애칭);
    private Integer member_pk;          //멤버 개인 pk;
    private Integer carsdata_pk;        //차량데이터 개인 pk;
    private Integer distance;           //사용자 개인 주행거리;
    private Integer divison;            //디폴트값;
    private Integer oil_dis;
    private Integer coolant_dis;
    private Integer tire_dis;
    private long wiper_dis;



    public MyCar() {
    }

    public MyCar(Integer pk, String nickname, Integer member_pk, Integer carsdata_pk, Integer distance, Integer divison, Integer oil_dis, Integer coolant_dis, Integer tire_dis, long wiper_dis) {
        this.pk = pk;
        this.nickname = nickname;
        this.member_pk = member_pk;
        this.carsdata_pk = carsdata_pk;
        this.distance = distance;
        this.divison = divison;
        this.oil_dis = oil_dis;
        this.coolant_dis = coolant_dis;
        this.tire_dis = tire_dis;
        this.wiper_dis = wiper_dis;
    }

    public MyCar(String nickname, Integer member_pk, Integer carsdata_pk, Integer distance, Integer divison, Integer oil_dis, Integer coolant_dis, Integer tire_dis, long wiper_dis) {
        this.nickname = nickname;
        this.member_pk = member_pk;
        this.carsdata_pk = carsdata_pk;
        this.distance = distance;
        this.divison = divison;
        this.oil_dis = oil_dis;
        this.coolant_dis = coolant_dis;
        this.tire_dis = tire_dis;
        this.wiper_dis = wiper_dis;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public Integer getMember_pk() {
        return member_pk;
    }

    public void setMember_pk(Integer member_pk) {
        this.member_pk = member_pk;
    }

    public Integer getCarsdata_pk() {
        return carsdata_pk;
    }

    public void setCarsdata_pk(Integer carsdata_pk) {
        this.carsdata_pk = carsdata_pk;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDivison() {
        return divison;
    }

    public void setDivison(Integer divison) {
        this.divison = divison;
    }

    public Integer getOil_dis() {
        return oil_dis;
    }

    public void setOil_dis(Integer oil_dis) {
        this.oil_dis = oil_dis;
    }

    public Integer getCoolant_dis() {
        return coolant_dis;
    }

    public void setCoolant_dis(Integer coolant_dis) {
        this.coolant_dis = coolant_dis;
    }

    public Integer getTire_dis() {
        return tire_dis;
    }

    public void setTire_dis(Integer tire_dis) {
        this.tire_dis = tire_dis;
    }

    public long getWiper_dis() {
        return wiper_dis;
    }

    public void setWiper_dis(long wiper_dis) {
        this.wiper_dis = wiper_dis;
    }
}
