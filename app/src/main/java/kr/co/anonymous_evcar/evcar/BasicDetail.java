package kr.co.anonymous_evcar.evcar;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import kr.co.anonymous_evcar.evcar.adapter.fragmentAdpter;
import kr.co.anonymous_evcar.evcar.adapter.fragmentAdpter2;
import kr.co.anonymous_evcar.evcar.adapter.fragmentAdpter3;
import kr.co.anonymous_evcar.evcar.bus.BusProvider;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.event.MoveViewPager;

public class BasicDetail extends AppCompatActivity {

    //ImageView btn_back;

    LinearLayout move_carslist;
    LinearLayout btn_gomyinfo;
    LinearLayout finder;
    LinearLayout move_mycarmanage;
    int category;
    DBManager dbManager;

    fragmentAdpter fragmentAdpter;
    fragmentAdpter2 fragmentAdpter2;
    fragmentAdpter3 fragmentAdpter3;

    ViewPager basic_viewpager;

    int count = 0;

    Bus bus = BusProvider.getInstance().getBus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_detail);
        bus.register(this);

       // btn_back = findViewById(R.id.btn_back);


        move_carslist = findViewById(R.id.move_carslist);
        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        finder = findViewById(R.id.finder);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        basic_viewpager = findViewById(R.id.basic_viewpager);
        Intent intent = getIntent();
        category =  intent.getIntExtra("category",0);

        dbManager = new DBManager(BasicDetail.this,"EVCarDB.db",null,1);

        if(category == 1){
            fragmentAdpter = new fragmentAdpter(getSupportFragmentManager());
            basic_viewpager.setAdapter(fragmentAdpter);
        }else if (category ==2){
            fragmentAdpter2 = new fragmentAdpter2(getSupportFragmentManager());
            basic_viewpager.setAdapter(fragmentAdpter2);
        }else if (category == 3){
            fragmentAdpter3 = new fragmentAdpter3(getSupportFragmentManager());
            basic_viewpager.setAdapter(fragmentAdpter3);
        }




        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicDetail.this,AllCarsList.class);
                startActivity(intent);
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicDetail.this,Member_info.class);
                startActivity(intent);
            }
        });

        finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicDetail.this, MapsActivity.class);
                intent.putExtra("targetLat", 37.502695);
                intent.putExtra("targetLong", 127.025);
                startActivity(intent);
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BasicDetail.this,Main2Activity.class);
                startActivity(intent);
            }
        });

    }

    @Subscribe
    public void moveViewPager(MoveViewPager event){
        basic_viewpager.setCurrentItem(event.getPage());
    }
}
