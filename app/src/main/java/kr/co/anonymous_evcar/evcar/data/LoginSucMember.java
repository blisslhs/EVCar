package kr.co.anonymous_evcar.evcar.data;

public class LoginSucMember {
    private Member member;          //로그인한 멤버 데이터
    private CarsData carsData;      //login_member의 fk로 묶은 차량추가 데이터
    private MyCar myCar;        //login_member의 차량추가한 데이터

    public LoginSucMember(){

    }

    public LoginSucMember(Member member, CarsData carsData, MyCar myCar) {
        this.member = member;
        this.carsData = carsData;
        this.myCar = myCar;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public CarsData getCarsData() {
        return carsData;
    }

    public void setCarsData(CarsData carsData) {
        this.carsData = carsData;
    }

    public MyCar getMyCar() {
        return myCar;
    }

    public void setMyCar(MyCar myCar) {
        this.myCar = myCar;
    }
}
