package kr.co.anonymous_evcar.evcar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.adapter.CarListAdpter;
import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class AllCarsList extends AppCompatActivity {
    ListView carlist_listview;

    DBManager dbManager;

    ArrayList<CarsData> carsData = new ArrayList<>();

    LinearLayout btn_gomyinfo;
    LinearLayout move_carslist;
    LinearLayout finder;
    LinearLayout move_mycarmanage;

    CarListAdpter carListAdpter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cars_list);
        carlist_listview = findViewById(R.id.carlist_listview);

        dbManager = new DBManager(AllCarsList.this,"EVCarDB.db",null,1);

        carsData = dbManager.getCarsDatas();

        carsData.remove(0);
        carListAdpter = new CarListAdpter(carsData);
        carlist_listview.setAdapter(carListAdpter);

        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        move_carslist = findViewById(R.id.move_carslist);
        finder = findViewById(R.id.finder);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        final LoginService login_member = LoginService.getInstance();

        carlist_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CarsData item = carsData.get(position);

                Intent intent1 = new Intent(AllCarsList.this, WebViewPage.class);

                intent1.putExtra("page",item.getPage());

                startActivity(intent1);


                finish();


            }
        });

        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCarsList.this,AllCarsList.class);
                startActivity(intent);
                finish();
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCarsList.this,Member_info.class);
                startActivity(intent);
                finish();
            }
        });

        finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCarsList.this, MapsActivity.class);
                intent.putExtra("targetLat", 37.502695);
                intent.putExtra("targetLong", 127.025);
                startActivity(intent);
                finish();
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AllCarsList.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
