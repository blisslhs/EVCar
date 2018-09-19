package kr.co.anonymous_evcar.evcar.data;


public class CarsData {
    private Integer pk;     //개인 키
    private String mycarname;       //차량 이름 ex) 모닝
    private Integer price;          //차량 가격
    private double mileage;        //차량 연비
    private Integer image;          //차량 회사 이미지
    private Integer makingcategory;  //차량 회사 카테고리 번호 1: 기아 2:르노삼성 3:한국GM 4:BMW 5:닛산 6:현대
    private String page;            //차량에 맞는 페이지

    public CarsData(){

    }

    public CarsData(Integer pk, String mycarname, Integer price, double mileage, Integer image, Integer makingcategory, String page) {
        this.pk = pk;
        this.mycarname = mycarname;
        this.price = price;
        this.mileage = mileage;
        this.image = image;
        this.makingcategory = makingcategory;
        this.page = page;
    }

    public CarsData(String mycarname, Integer price, double mileage, Integer image, Integer makingcategory, String page) {
        this.mycarname = mycarname;
        this.price = price;
        this.mileage = mileage;
        this.image = image;
        this.makingcategory = makingcategory;
        this.page = page;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getMycarname() {
        return mycarname;
    }

    public void setMycarname(String mycarname) {
        this.mycarname = mycarname;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getMakingcategory() {
        return makingcategory;
    }

    public void setMakingcategory(Integer makingcategory) {
        this.makingcategory = makingcategory;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
