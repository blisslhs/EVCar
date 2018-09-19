package kr.co.anonymous_evcar.evcar.data;

public class MakingCategory {
    public static int KIA = 1;
    public static int SAMSUNG =2;
    public static int GMCHEVROIET = 3;
    public static int BMW = 4;
    public static int NISSAN = 5;
    public static int HYUNDAI = 6;
    public static int HONDA = 7;
    private Integer pk;
    private String makingname;
    private int makingiamge;

    public MakingCategory(Integer pk, String makingname, int makingiamge) {
        this.pk = pk;
        this.makingname = makingname;
        this.makingiamge = makingiamge;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getMakingname() {
        return makingname;
    }

    public void setMakingname(String makingname) {
        this.makingname = makingname;
    }

    public int getMakingiamge() {
        return makingiamge;
    }

    public void setMakingiamge(int makingiamge) {
        this.makingiamge = makingiamge;
    }
}
