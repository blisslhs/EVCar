package kr.co.anonymous_evcar.evcar.event;

public class MoveViewPager {
    private Integer page;

    public MoveViewPager(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
