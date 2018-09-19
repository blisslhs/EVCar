package kr.co.anonymous_evcar.evcar.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Evc {
    private String addr;
    private String chargeTp;   //1:완속 2:급속
    private String cpTp;   //1:B타입(5핀) 2:C타입(5핀) 3:BC타입(5핀) 4:BC타입(7핀) 5:DC차데모 6:AC3상 7:DC콤보 8:DC차데모
    private String csNm;    //충전소 명칭
    private String lat;     //위도
    private String longi;   //경도
}
