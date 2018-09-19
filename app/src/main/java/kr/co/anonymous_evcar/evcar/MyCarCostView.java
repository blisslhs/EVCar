package kr.co.anonymous_evcar.evcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.data.MyCarCost;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class MyCarCostView extends AppCompatActivity {
    TextView mycarcost_main_txt;
    Button mycarcost_btn_close;
    Button btn_left;
    Button btn_right;
    TextView txt_mycarname;
    TextView total_cost;
    TextView mycar_date;
    TextView mycar_mileage;
    TextView total_cost2;
    TextView oil_cost;
    TextView coolant_cost;
    TextView tire_cost;
    TextView wiper_cost;
    TextView hipass_cost;
    TextView total_cost3;
    TextView charge_cost;

    LinearLayout btn_gomyinfo;
    LinearLayout move_carslist;
    LinearLayout finder;
    LinearLayout move_mycarmanage;



    Integer year = 0;
    Integer month = 0;
    Integer day =0;


    DBManager dbManager;
    LoginService loginService = LoginService.getInstance();
    Member login_member;
    MyCar myCar;
    CarsData carsData;

    Integer partTotalCost = 0;
    Integer hipassTotalCost = 0;


    Integer txtoil_cost = 0;
    Integer txtcoolant_cost = 0;
    Integer txttire_cost = 0;
    Integer txtwiper_cost = 0;
    Integer txthipass_cost = 0;
    Integer txtcharge_cost = 0;
    Integer txttotal_cost = 0;
    Calendar calendar;
    ArrayList<MyCarCost>myCarCosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_cost_view);
        mycarcost_main_txt = findViewById(R.id.mycarcost_main_txt);
        mycarcost_btn_close = findViewById(R.id.mycarcost_btn_close);
        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        txt_mycarname = findViewById(R.id.txt_mycarname);
        total_cost = findViewById(R.id.total_cost);
        mycar_date = findViewById(R.id.mycar_date);
        mycar_mileage = findViewById(R.id.mycar_mileage);
        total_cost2 = findViewById(R.id.total_cost2);
        oil_cost = findViewById(R.id.oil_cost);
        coolant_cost = findViewById(R.id.coolant_cost);
        tire_cost = findViewById(R.id.tire_cost);
        wiper_cost = findViewById(R.id.wiper_cost);
        hipass_cost = findViewById(R.id.hipass_cost);
        total_cost3 = findViewById(R.id.total_cost3);
        charge_cost = findViewById(R.id.charge_cost);

        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        move_carslist = findViewById(R.id.move_carslist);
        finder = findViewById(R.id.finder);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        btn_right.setVisibility(View.GONE);
        setData(month);

        dbManager = new DBManager(MyCarCostView.this, "EVCarDB.db", null, 1);
        login_member = loginService.getLoginMember();
        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);
        carsData = dbManager.getCarData(myCar.getCarsdata_pk());
        myCarCosts = dbManager.getMyCarCostPartAll(myCar.getPk(),year,month);

        setMyCarCost(myCarCosts);

        mycar_date.setText(year.toString() + "/" + (month + 1) + "/" + "1" + " ~ " + year.toString() + "/" + (month + 1)+"/" + day);           //날짜


        mycarcost_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarCostView.this,AllCarsList.class);
                startActivity(intent);
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarCostView.this,Member_info.class);
                startActivity(intent);
            }
        });

        finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarCostView.this, MapsActivity.class);
                intent.putExtra("targetLat", 37.502695);
                intent.putExtra("targetLong", 127.025);
                startActivity(intent);
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyCarCostView.this,Main2Activity.class);
                startActivity(intent);
            }
        });


        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.add(Calendar.MONTH, -1);
                calendar1.set(Calendar.DAY_OF_MONTH, day);


                year = calendar1.get(Calendar.YEAR);
                month = calendar1.get(Calendar.MONTH);
                day = calendar1.get(Calendar.DAY_OF_MONTH);

                myCarCosts = dbManager.getMyCarCostPartAll(myCar.getPk(),year,month);

                setMyCarCost(myCarCosts);

                Integer tmonth = calendar.get(Calendar.MONTH);

                if((month+1) != (tmonth+1)) {
                    mycar_date.setText(year.toString() + "/" + (month + 1) + "/" + "1" + " ~ " + year.toString() + "/" + (month + 1) +"/"+calendar1.getActualMaximum(Calendar.DATE) );
                    btn_right.setVisibility(View.VISIBLE);
                }else{
                    mycar_date.setText(year.toString() + "/" + (month + 1) + "/" + "1" + " ~ " + year.toString() + "/" + (month + 1)+"/" + day);           //날짜
                    btn_right.setVisibility(View.GONE);
                }
            }
        });
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DAY_OF_MONTH, day);
                calendar1.add(Calendar.MONTH, 1);

                year = calendar1.get(Calendar.YEAR);
                month = calendar1.get(Calendar.MONTH);
                day = calendar1.get(Calendar.DAY_OF_MONTH);

                myCarCosts = dbManager.getMyCarCostPartAll(myCar.getPk(),year,month);

                setMyCarCost(myCarCosts);

                Integer tmonth = calendar.get(Calendar.MONTH);

                if((month+1) != (tmonth+1)) {
                    mycar_date.setText(year.toString() + "/" + (month + 1) + "/" + "1" + " ~ " + year.toString() + "/" + (month + 1) +"/"+calendar1.getActualMaximum(Calendar.DATE) );
                    btn_right.setVisibility(View.VISIBLE);
                }else{
                    mycar_date.setText(year.toString() + "/" + (month + 1) + "/" + "1" + " ~ " + year.toString() + "/" + (month + 1) +"/"+ day);           //날짜
                    btn_right.setVisibility(View.GONE);
                }
            }
        });
    }
    public void setMyCarCost(ArrayList<MyCarCost>myCarCosts){
        myCarCosts = dbManager.getMyCarCostPartAll(myCar.getPk(),year,month);
        partTotalCost = 0;
        hipassTotalCost = 0;


        txtoil_cost = 0;
        txtcoolant_cost = 0;
        txttire_cost = 0;
        txtwiper_cost = 0;
        txthipass_cost = 0;
        txtcharge_cost = 0;
        txttotal_cost = 0;


        for(int i=0; i<myCarCosts.size(); i++){
            partTotalCost += myCarCosts.get(i).getCost();
            if(myCarCosts.get(i).getPartCategory()==1){
                txtoil_cost+=myCarCosts.get(i).getCost();
            }else if (myCarCosts.get(i).getPartCategory()==2){
                txtcoolant_cost+=myCarCosts.get(i).getCost();
            }else if (myCarCosts.get(i).getPartCategory()==3){
                txttire_cost+=myCarCosts.get(i).getCost();
            }else if (myCarCosts.get(i).getPartCategory()==4){
                txtwiper_cost+=myCarCosts.get(i).getCost();
            }else if (myCarCosts.get(i).getPartCategory()==5){
                txthipass_cost+=myCarCosts.get(i).getCost();
            }else if(myCarCosts.get(i).getPartCategory()==6){
                txtcharge_cost+=myCarCosts.get(i).getCost();
            }
        }


        for(int i=0; i<myCarCosts.size(); i++){
            hipassTotalCost += myCarCosts.get(i).getHipassCost();
        }

        for(int i=0; i<myCarCosts.size(); i++){
            txttotal_cost += myCarCosts.get(i).getCost();
            txttotal_cost += myCarCosts.get(i).getHipassCost();
        }

        dbManager.setMyCarCostPartTotal(myCar.getPk(),partTotalCost,year,month);
        dbManager.setMyCarCostHipassTotal(myCar.getPk(),hipassTotalCost,year,month);

        setData(month);

        txt_mycarname.setText(carsData.getMycarname());                     //차량 이름
        mycar_mileage.setText(Double.toString(carsData.getMileage()));      //연비
        oil_cost.setText(txtoil_cost.toString()+ " 원");
        coolant_cost.setText(txtcoolant_cost.toString()+ " 원");
        tire_cost.setText(txttire_cost.toString()+ " 원");
        wiper_cost.setText(txtwiper_cost.toString()+ " 원");
        hipass_cost.setText(txthipass_cost.toString()+ " 원");
        total_cost.setText(txttotal_cost.toString()+ " 원");
        total_cost2.setText(txttotal_cost.toString()+ " 원");
        total_cost3.setText(txttotal_cost.toString() + " 원");
        charge_cost.setText(txtcharge_cost.toString() + " 원");
    }

    public void setData(int month) {
        mycarcost_main_txt.setText((month+1) + "월 지출내역");
    }
}
