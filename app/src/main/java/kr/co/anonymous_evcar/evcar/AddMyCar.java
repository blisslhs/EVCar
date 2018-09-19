package kr.co.anonymous_evcar.evcar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import android.widget.GridView;
import android.widget.LinearLayout;


import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.adapter.AddMyCarAdpter;
import kr.co.anonymous_evcar.evcar.data.MakingCategory;


public class AddMyCar extends AppCompatActivity {
    Button addmycar_btn_close;
    GridView addmycar_grid;
    LinearLayout move_carslist;
    LinearLayout finder;
    LinearLayout btn_gomyinfo;
    LinearLayout move_mycarmanage;
    MakingCategory cate1 = new MakingCategory(1,"기아",R.drawable.kia);
    MakingCategory cate2 = new MakingCategory(2,"삼성",R.drawable.samsung);
    MakingCategory cate3 = new MakingCategory(3,"쉐보레",R.drawable.chevroiet);
    MakingCategory cate4 = new MakingCategory(4,"BMW",R.drawable.bmw);
    MakingCategory cate5 = new MakingCategory(5,"닛산",R.drawable.nissan);
    MakingCategory cate6 = new MakingCategory(6,"현대",R.drawable.hyndai);
    MakingCategory cate7 = new MakingCategory(7,"혼다",R.drawable.honda);
    MakingCategory cate8 = new MakingCategory(8,"토요타",R.drawable.toyota);
    MakingCategory cate9 = new MakingCategory(9,"링컨",R.drawable.lincoln);
    MakingCategory cate10 = new MakingCategory(10,"포드",R.drawable.ford);
    MakingCategory cate11 = new MakingCategory(11,"벤츠",R.drawable.benz);
    MakingCategory cate12 = new MakingCategory(12,"포르쉐",R.drawable.porsche);
    MakingCategory cate13 = new MakingCategory(13,"테슬라",R.drawable.teslr);
    MakingCategory cate14 = new MakingCategory(14,"렉서스",R.drawable.lexus);


    ArrayList<MakingCategory> items = new ArrayList<>();
    AddMyCarAdpter addMyCarAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_my_car);
        items.add(cate1);
        items.add(cate2);
        items.add(cate3);
        items.add(cate4);
        items.add(cate5);
        items.add(cate6);
        items.add(cate7);
        items.add(cate8);
        items.add(cate9);
        items.add(cate10);
        items.add(cate11);
        items.add(cate12);
        items.add(cate13);
        items.add(cate14);

        addmycar_btn_close = findViewById(R.id.addmycar_btn_close);
        addmycar_grid = findViewById(R.id.addmycar_grid);
        move_carslist = findViewById(R.id.move_carslist);
        finder = findViewById(R.id.finder);
        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        addMyCarAdpter = new AddMyCarAdpter(items);
        addmycar_grid.setAdapter(addMyCarAdpter);


        addmycar_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addmycar_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MakingCategory item = items.get(position);

                Intent intent = new Intent(AddMyCar.this,CarList.class);

                intent.putExtra("category",item.getPk());

                startActivity(intent);
                finish();

            }
        });

        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMyCar.this,AllCarsList.class);
                startActivity(intent);

                finish();
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMyCar.this,Member_info.class);
                startActivity(intent);

                finish();
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddMyCar.this,Main2Activity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}