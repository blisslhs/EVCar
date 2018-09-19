package kr.co.anonymous_evcar.evcar.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EvcCharge {
    public int ChargeTp[] = new int[2];
    public int cpTp[] = new int[10];
    private String addr;
    private String csNm;
    private String lat;  //위도
    private String longi; //경도

}
