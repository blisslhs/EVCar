package kr.co.anonymous_evcar.evcar.data;

public class BasicData {
    public static int OUTLINE = 1;      //개요
    public static int ADVANTEAGE = 2;    //장점
    public static int MANAGE = 3;       //관리
    private Integer pk;
    private String content;
    private Integer category;

    public BasicData(Integer pk, String content, Integer category) {
        this.pk = pk;
        this.content = content;
        this.category = category;
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }
}
