package kr.co.anonymous_evcar.evcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.adapter.AddMyCarAdpter;
import kr.co.anonymous_evcar.evcar.adapter.CarListAdpter;
import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class CarList extends AppCompatActivity {
    ListView carlist_listview;

    DBManager dbManager;

    ArrayList<CarsData>tmpCarsData = new ArrayList<>();
    ArrayList<CarsData>carsData = new ArrayList<>();

    CarListAdpter carListAdpter;


    Integer div;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        carlist_listview = findViewById(R.id.carlist_listview);

        dbManager = new DBManager(CarList.this,"EVCarDB.db",null,1);

        Intent intent = getIntent();

        div = intent.getIntExtra("category",0);


        tmpCarsData = dbManager.getCarsDatas();
        carsData = dbManager.getCarsListDatas(div);

        carListAdpter = new CarListAdpter(carsData);

        carlist_listview.setAdapter(carListAdpter);

        final LoginService login_member = LoginService.getInstance();


        carlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final CarsData item = carsData.get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarList.this);


                alertDialog.setTitle("등록하시겠습니까?");
                alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Long member_pk = login_member.getLoginMember().getId();      //멤버 개인 pk
                        Integer carsdata_pk = item.getPk();                             //자동차 개인 pk
                        Integer distance = 0;  //주행거리
                        Integer divison = 1;                                            //디폴트
                        Integer oil_dis = 0;
                        Integer coolant_dis = 0;
                        Integer tire_dis = 0;
                        Integer wiper_dis = 0;
                        dbManager.reSetMyCar((int)(long)login_member.getLoginMember().getId(),0);
                        dbManager.insertMyCar("",(int)(long)member_pk,carsdata_pk,distance,divison,oil_dis,coolant_dis,tire_dis,wiper_dis);

                        Intent intent1 = new Intent(CarList.this,Member_info.class);

                        //intent1.putExtra("CarsDataPk",item.getPk());

                        startActivity(intent1);


                        finish();

                        Toast.makeText(CarList.this, "추가 완료", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();

            }
        });


    }


}
