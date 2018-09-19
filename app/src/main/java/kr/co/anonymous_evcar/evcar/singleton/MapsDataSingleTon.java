package kr.co.anonymous_evcar.evcar.singleton;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.data.Evc;
import kr.co.anonymous_evcar.evcar.data.EvcCharge;


public class MapsDataSingleTon {
    private static MapsDataSingleTon curr = null;
    private ArrayList<EvcCharge> items;

    public static MapsDataSingleTon getInstance() {
        if(curr == null) {
            curr = new MapsDataSingleTon();
        }
        return curr;
    }
    private MapsDataSingleTon() {
        items = new ArrayList<>();
    }

    public ArrayList<EvcCharge> getItems() {
        return items;
    }

    public void setItems(ArrayList<EvcCharge> items) {
        this.items = items;
    }
}
