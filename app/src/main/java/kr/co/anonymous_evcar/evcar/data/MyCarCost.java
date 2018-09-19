package kr.co.anonymous_evcar.evcar.data;

public class MyCarCost {
    private Integer pk;                  //개인 ID
    private Integer MyCar_fk;           //MyCar fk
    private Integer partCategory;        // 1 : 엔진오일 , 2 : 냉각수 , 3 : 타이어 , 4 : 와이퍼   ,5 : 하이패스 ,  6 : 충전
    private Integer cost;               //PartCategory에 대한 금액
    private Integer hipassCost;         //하이패스 금액
    private Integer partTotalCost;          //PartCategory 총 금액
    private Integer hipassTotalCost;        //HipassCost 총 금액
    private Integer year;               //년
    private Integer month;              //월

    public MyCarCost(){

    }

    public MyCarCost(Integer pk, Integer myCar_fk, Integer partCategory, Integer cost, Integer hipassCost, Integer partTotalCost, Integer hipassTotalCost, Integer year, Integer month) {
        this.pk = pk;
        MyCar_fk = myCar_fk;
        this.partCategory = partCategory;
        this.cost = cost;
        this.hipassCost = hipassCost;
        this.partTotalCost = partTotalCost;
        this.hipassTotalCost = hipassTotalCost;
        this.year = year;
        this.month = month;
    }

    public MyCarCost(Integer myCar_fk, Integer partCategory, Integer cost, Integer hipassCost, Integer partTotalCost, Integer hipassTotalCost, Integer year, Integer month) {
        MyCar_fk = myCar_fk;
        this.partCategory = partCategory;
        this.cost = cost;
        this.hipassCost = hipassCost;
        this.partTotalCost = partTotalCost;
        this.hipassTotalCost = hipassTotalCost;
        this.year = year;
        this.month = month;
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

    public Integer getPartCategory() {
        return partCategory;
    }

    public void setPartCategory(Integer partCategory) {
        this.partCategory = partCategory;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getHipassCost() {
        return hipassCost;
    }

    public void setHipassCost(Integer hipassCost) {
        this.hipassCost = hipassCost;
    }

    public Integer getPartTotalCost() {
        return partTotalCost;
    }

    public void setPartTotalCost(Integer partTotalCost) {
        this.partTotalCost = partTotalCost;
    }

    public Integer getHipassTotalCost() {
        return hipassTotalCost;
    }

    public void setHipassTotalCost(Integer hipassTotalCost) {
        this.hipassTotalCost = hipassTotalCost;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
