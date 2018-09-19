package kr.co.anonymous_evcar.evcar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.data.Evc;
import kr.co.anonymous_evcar.evcar.data.EvcCharge;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.retrofit.RetrofitService;
import kr.co.anonymous_evcar.evcar.service.LoginService;
import kr.co.anonymous_evcar.evcar.singleton.MapsDataSingleTon;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.net.sip.SipErrorCode.TIME_OUT;

public class MainActivity extends AppCompatActivity {
    Button main_btn_outline;
    Button main_btn_advanteage;
    Button main_btn_manage;
    Button main_btn_addmycar;
    Button move_community;
    LinearLayout finder;
    LinearLayout move_carslist;
    LinearLayout btn_gomyinfo;
    LinearLayout move_mycarmanage;
    int cnt = 0;
    ProgressDialog dialog;
    ArrayList<EvcCharge> info_items = new ArrayList<>();
    ArrayList<Evc> items = new ArrayList<>();
    LocationManager manager;

    DBManager dbManager;
    ArrayList<CarsData> carsData = new ArrayList<>();

    MyCar MyCar = new MyCar();
    CarsData MyCarData  = new CarsData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        new GetXMLTask1().execute();



        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(MainActivity.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
                .check();

        dbManager = new DBManager(MainActivity.this,"EVCarDB.db",null,1);

        carsData = dbManager.getCarsDatas();
        if(carsData.size()==0) {
            insertCarsData(carsData, dbManager);
        }
        final LoginService loginService = LoginService.getInstance();

        if(loginService.getLoginMember()==null){


        }else {
            Toast.makeText(this, loginService.getLoginMember().getName() + "님 환영합니다", Toast.LENGTH_SHORT).show();
        }
        main_btn_outline = findViewById(R.id.main_btn_outline);
        main_btn_advanteage = findViewById(R.id.main_btn_advanteage);
        main_btn_manage = findViewById(R.id.main_btn_manage);
        main_btn_addmycar = findViewById(R.id.main_btn_addmycar);
        move_community = findViewById(R.id.move_community);
        finder = findViewById(R.id.finder);
        move_carslist = findViewById(R.id.move_carslist);
        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        main_btn_outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BasicDetail.class);
                intent.putExtra("category",1);         // BasicData.OUTLINE = 개요  숫자 1
                startActivity(intent);
            }
        });

        main_btn_advanteage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BasicDetail.class);
                intent.putExtra("category",3);      //BasicData.MANAGE == 관리 숫자 3
                startActivity(intent);
            }
        });

        main_btn_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,BasicDetail.class);
                intent.putExtra("category",2);        //BasicData.ADVANTEAGE = 장점 숫자 2
                startActivity(intent);
            }
        });

        main_btn_addmycar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddMyCar.class);
                startActivity(intent);
            }
        });

        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AllCarsList.class);
                startActivity(intent);
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Member_info.class);
                startActivity(intent);
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });

        move_community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Member login_member = loginService.getLoginMember();
                final String mem_id = login_member.getMem_id();
                final String mem_pw = login_member.getMem_pw();

                Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().login_ok_json(mem_id,mem_pw);
                observ.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()){
                            Intent intent1 = new Intent(MainActivity.this, WebViewPage.class);

                            intent1.putExtra("page","http://192.168.4.65:8090/anonymous_evcar/login_ok.do?mem_id="+mem_id+"&&mem_pw="+mem_pw);  //서버 주소 get방식
                            //login_ok.do?mem_id="+mem_id+"&&mem_pw="+mem_pw

                            startActivity(intent1);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        });

    }

    @OnClick(R.id.finder)
    void onCallClick(View view) {
        showProgressDialog("위치를 검색중입니다.");
        startLocationService();
        Log.d("test", "startLocationService();");

    }

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {



        public void handleMessage(Message msg) {

            if (msg.what == TIME_OUT) { // 타임아웃이 발생하면

                dialog.dismiss(); // ProgressDialog를 종료

            }

        }

    };

    private class GetXMLTask1 extends AsyncTask<String, Void, Document> {
        @Override
        protected Document doInBackground(String... urls) {

            URL url;
            Document doc;
            try {
                url = new URL("http://openapi.kepco.co.kr/service/evInfoService/getEvSearchList?addr=%EC%84%9C%EC%9A%B8&pageNo=1&numOfRows=128&ServiceKey=tEaGlxH27VIl1Ulb35W9NPz25DYz%2FgaaXxkPE9Qw8BI8KdWR8wgytpbtozEnynLnGmvf70xz8SZMkkaghNw3Xw%3D%3D");
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                return doc;
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Document doc) {

            String s = "";
            NodeList nodeList = doc.getElementsByTagName("item");
            Integer num = nodeList.getLength();

            for(int i = 0; i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                NodeList addr = fstElmnt.getElementsByTagName("addr");

                NodeList chargeTp = fstElmnt.getElementsByTagName("chargeTp");

                NodeList cpTp  = fstElmnt.getElementsByTagName("cpTp");

                NodeList csNm = fstElmnt.getElementsByTagName("csNm");

                NodeList lat = fstElmnt.getElementsByTagName("lat");

                NodeList longi = fstElmnt.getElementsByTagName("longi");

                Evc evc = new Evc(addr.item(0).getChildNodes().item(0).getNodeValue(),
                        chargeTp.item(0).getChildNodes().item(0).getNodeValue(),
                        cpTp.item(0).getChildNodes().item(0).getNodeValue(),
                        csNm.item(0).getChildNodes().item(0).getNodeValue(),
                        lat.item(0).getChildNodes().item(0).getNodeValue(),
                        longi.item(0).getChildNodes().item(0).getNodeValue());
                items.add(evc);
            }



            for(int i=0; i<items.size(); i++){

                if(items.get(i).getLat().equals("")&&items.get(i).getLongi().equals("")) {

                    continue;

                }
                EvcCharge temp = new EvcCharge();
                temp.setLat(items.get(i).getLat());
                temp.setLongi(items.get(i).getLongi());
                temp.setAddr(items.get(i).getAddr());
                temp.setCsNm(items.get(i).getCsNm());
                temp.ChargeTp[Integer.parseInt(items.get(i).getChargeTp())-1] = 1;
                temp.cpTp[Integer.parseInt(items.get(i).getCpTp())-1] = 1;
                info_items.add(temp);
                cnt++;
                Log.d("yjh", ""+info_items.get(0).getLat());

                for(int j=0; j<items.size(); j++){

                    if(info_items.get(cnt-1).getCsNm().equals(items.get(j).getCsNm()))
                    {
                        info_items.get(cnt-1).ChargeTp[(Integer.parseInt(items.get(j).getChargeTp()))-1]=1;
                        //Log.d("ayjh", ""+info_items.get(cnt-1).ChargeTp[(Integer.parseInt(items.get(j).getChargeTp()))-1]);

                        info_items.get(cnt-1).cpTp[(Integer.parseInt(items.get(j).getCpTp()))-1] = 1;
                        // Log.d("ayjh", "인덱스: "+  Integer.parseInt(items.get(j).getCpTp()));

                        items.get(j).setLat("");
                        items.get(j).setLongi("");
                    }


                }

            }
            MapsDataSingleTon mapsDataSingleton = MapsDataSingleTon.getInstance();
            mapsDataSingleton.setItems(info_items);

            //status1.setText(s);

            super.onPostExecute(doc);
        }
    }

    public void insertCarsData(ArrayList<CarsData>carsData, DBManager dbManager){

        dbManager = new DBManager(MainActivity.this,"EVCarDB.db",null,1);
             dbManager.insertCarsData("차량을 등록해주세요",0,0.0,R.drawable.xicon,0,"https://auto.naver.com/car/lineup.nhn?yearsId=125863");
            //기아
            dbManager.insertCarsData("2018니로 EV",4780,5.3,R.drawable.kia,1,"https://auto.naver.com/car/lineup.nhn?yearsId=125863");
            dbManager.insertCarsData("2018 니로 하이브리드",2346,19.5,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=117939");
            dbManager.insertCarsData("2018 더 뉴 k5 하이브리드",2848,17.2,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=125097");
            dbManager.insertCarsData("2017 K5 플러그인 하이브리드",3885,16.4,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=64855");
            dbManager.insertCarsData("2017 K5 하이브리드",2865,17.0,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=66647");
            dbManager.insertCarsData("2018 K7 하이브리드",3522,16.2,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=122523");
            dbManager.insertCarsData("2017 K7 하이브리드",3495,16.2,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=67301");
            dbManager.insertCarsData("2018 쏘울 EV",4280,5.2,R.drawable.kia,1,"https://auto.naver.com/car/main.nhn?yearsId=117787");

            //삼성
            dbManager.insertCarsData("2018 SM3 Z.E.",3950,4.5,R.drawable.samsung,2,"https://auto.naver.com/car/main.nhn?yearsId=122337");
            dbManager.insertCarsData("2017 트위지",1500,7.9,R.drawable.samsung,2,"https://auto.naver.com/car/main.nhn?yearsId=119555");
            dbManager.insertCarsData("2017 sm3 Z.E.",3900,4.4,R.drawable.samsung,2,"https://auto.naver.com/car/main.nhn?yearsId=64961");
            dbManager.insertCarsData("2015 SM3 Z.E.",4190,4.4,R.drawable.samsung,2,"https://auto.naver.com/car/main.nhn?yearsId=58537");
            dbManager.insertCarsData("2014 SM3 Z.E.",4200,4.4,R.drawable.samsung,2,"https://auto.naver.com/car/main.nhn?yearsId=29520");

            //GM대우 쉐보레
            dbManager.insertCarsData("2018 볼트 플러그인 하이브리드",3667,17.8,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=124661");
            dbManager.insertCarsData("2017 볼트 플러그인 하이브리드",3656,17.8,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=68049");
            dbManager.insertCarsData("2016 볼트 플러그인 하이브리드",3640,17.8,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=56799");
            dbManager.insertCarsData("2017 말리부 하이브리드",3180,17.1,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=66025");
            dbManager.insertCarsData("2016 말리부 하이브리드",3208,17.0,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=57727");
            dbManager.insertCarsData("2018쉐보레 볼트 EV",4558,5.5,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=123177");
            dbManager.insertCarsData("2017쉐보레 볼트 EV",4479,5.5,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=68921");
            dbManager.insertCarsData("2016 스파크 EV",3990,6.0,R.drawable.chevroiet,3,"https://auto.naver.com/car/main.nhn?yearsId=61425");

            //bmw
        dbManager.insertCarsData("2019 BMW 2시리즈 액티브 투어러 PHEV",5130,0.0,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=123145");
        dbManager.insertCarsData("2016 BMW 2시리즈 액티브 투어러 PHEV",4890,2.0,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=60393");
        dbManager.insertCarsData("2018 BMW X5 PHEV",10370,9.4,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=122681");
        dbManager.insertCarsData("2018 BMW 3시리즈 PHEV",5830,0.0,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=122685");
        dbManager.insertCarsData("2018 BMW 7시리즈 PHEV",14490,0.0,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=122689");
        dbManager.insertCarsData("2017 BMW i3",6000,5.4,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=63831");
        dbManager.insertCarsData("2014 BMW i3",5760,5.9,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=44345");
        dbManager.insertCarsData("2015 BMW i8",19580,13.7,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=57623");
        dbManager.insertCarsData("2013 BMW 액티브하이브리드 7",13620,10.8,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=18675");
        dbManager.insertCarsData("2014 BMW 액티브하이브리드 5  ",10420,0.0,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=32340");
        dbManager.insertCarsData("2013 BMW 액티브하이브리드 3 ",8560,12.2,R.drawable.bmw,4,"https://auto.naver.com/car/main.nhn?yearsId=28645");


            //닛산
            dbManager.insertCarsData("2018 닛산 엑스트레일 하이브리드",2875,20.8,R.drawable.nissan,5,"https://auto.naver.com/car/main.nhn?yearsId=119933");
            dbManager.insertCarsData("2017 닛산 무라노 하이브리드",5490,11.1,R.drawable.nissan,5,"https://auto.naver.com/car/main.nhn?yearsId=125411");
            dbManager.insertCarsData("2016 닛산 무라노 하이브리드",5490,11.1,R.drawable.nissan,5,"https://auto.naver.com/car/main.nhn?yearsId=123411");
            dbManager.insertCarsData("2016 닛산 리프",4590,5.0,R.drawable.nissan,5,"https://auto.naver.com/car/main.nhn?yearsId=62869");
            dbManager.insertCarsData("2016 닛산 e-NV200",3915,14.2,R.drawable.nissan,5,"https://auto.naver.com/car/main.nhn?yearsId=63847");

            //현대
            dbManager.insertCarsData("2018 코나 일렉트릭",4660,5.6,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124705");
            dbManager.insertCarsData("2018 아이오닉 하이브리드",2200,22.4,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124463");
            dbManager.insertCarsData("2017 아이오닉 하이브리드",2197,20.2,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=119949");
            dbManager.insertCarsData("2018 아이오닉 플러그인 하이브리드",3183,20.5,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124455");
            dbManager.insertCarsData("2017 아이오닉 플러그인 하이브리드",3230,20.5,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=68479");
            dbManager.insertCarsData("2018 아이오닉 일렉트릭",3915,6.3,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124459");
            dbManager.insertCarsData("2017 아이오닉 일렉트릭",3840,6.3,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=68425");
            dbManager.insertCarsData("2018 쏘나타 뉴 라이즈 하이브리드",2854,17.4,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124389");
            dbManager.insertCarsData("2017 쏘나타 뉴 라이즈 하이브리드",3029,18.0,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=118761");
            dbManager.insertCarsData("2018 쏘나타 뉴 라이즈 플러그인 하이브리드",3885,17.1,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=124393");
            dbManager.insertCarsData("2017 쏘나타 뉴 라이즈 플러그인 인 하이브리드",4078,17.1,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=120141");
            dbManager.insertCarsData("2018 그랜저 하이브리드",3512,16.2,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=122031");
            dbManager.insertCarsData("2017 그랜저 하이브리드",3540,16.2,R.drawable.hyndai,6,"https://auto.naver.com/car/main.nhn?yearsId=69007");

            //혼다

          dbManager.insertCarsData("2018 혼다 어코드 하이브리드",4240,18.9,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=125033");
          dbManager.insertCarsData("2017 혼다 어코드 하이브리드",4320,19.3,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=67641");
          dbManager.insertCarsData("2017 혼다 NSX",24000,12.4,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=68847");
          dbManager.insertCarsData("2016 혼다 베젤 하이브리드",2308,19.8,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=63177");
          dbManager.insertCarsData("2016 혼다 피트 하이브리드",1856,33.6,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=62303");
          dbManager.insertCarsData("2016 혼다 오딧세이 하이브리드",4067,24.4,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=62299");
          dbManager.insertCarsData("2016 혼다 CR-Z 하이브리드",2290,14.4,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=60829");
          dbManager.insertCarsData("2016 혼다 CR-Z",2745,20.6,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=59913");
          dbManager.insertCarsData("2013 혼다 CR-Z",2618,23.0,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=18803");
          dbManager.insertCarsData("2012 혼다 CR-Z",3380,20.6,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=14757");
          dbManager.insertCarsData("2013 혼다 프리드",2333,21.6,R.drawable.honda,7,"https://auto.naver.com/car/main.nhn?yearsId=40281");

          //토요타

        dbManager.insertCarsData("2018 토요타 센추리",19900,13.6,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=125727");
        dbManager.insertCarsData("2018 토요타 프리우스 C",2490,18.6,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124427");
        dbManager.insertCarsData("2019 토요타 아발론 하이브리드 ",4119,18.7,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124899");
        dbManager.insertCarsData("2018 토요타 캠리 하이브리드",4190,16.7,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=121661");
        dbManager.insertCarsData("2018 토요타 오리스 하이브리드 ",2665,30.4,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124165");
        dbManager.insertCarsData("2018 토요타 에스티마 하이브리드",4384,18.0,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124133");
        dbManager.insertCarsData("2018 토요타 아쿠아 크로스오버",2100,34.4,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124119");
        dbManager.insertCarsData("2018 토요타 아쿠아 GR 스포츠 ",2372,34.4,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124125");
        dbManager.insertCarsData("2018 토요타 아쿠아",1815,34.4,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=124113");
        dbManager.insertCarsData("2018 토요타 프리우스 C",2330,19.5,R.drawable.toyota,8,"https://auto.naver.com/car/main.nhn?yearsId=122131");

        //링컨

        dbManager.insertCarsData("2018 링컨 MKZ 하이브리드",4890,15.8,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=125763");
        dbManager.insertCarsData("2017 링컨 MKZ 하이브리드",5900,16.8,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=66021");
        dbManager.insertCarsData("2016 링컨 MKZ 하이브리드",5070,16.8,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=60623");
        dbManager.insertCarsData("2015 링컨 MKZ 하이브리드",5070,16.8,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=56041");
        dbManager.insertCarsData("2014 링컨 MKZ 하이브리드",5490,16.8,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=53233");
        dbManager.insertCarsData("2013 링컨 MKZ 하이브리드",4050,19.1,R.drawable.lincoln,9,"https://auto.naver.com/car/main.nhn?yearsId=29376");

        //포드

        dbManager.insertCarsData("2018 포드 퓨전 하이브리드",2850,17.8,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=121311");
        dbManager.insertCarsData("2017 포드 퓨전 하이브리드",2840,17.8,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=63765");
        dbManager.insertCarsData("2014 포드 퓨전 하이브리드",4665,17.9,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=55487");
        dbManager.insertCarsData("2018 포드 퓨전 에너지",3530,17.8,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=121313");
        dbManager.insertCarsData("2017 포드 C-MAX 에너지",3060,16.5,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=122173");
        dbManager.insertCarsData("2013 포드 C-MAX 에너지",3720,18.2,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=29530");
        dbManager.insertCarsData("2018 포드 C-MAX 하이브리드",2720,17.0,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=122177");
        dbManager.insertCarsData("2013 포드 C-MAX 하이브리드",2840,18.2,R.drawable.ford,10,"https://auto.naver.com/car/main.nhn?yearsId=29529");

        //벤츠

        dbManager.insertCarsData("2018 벤츠 GLC 클래스 플러그인 하이브리드",6700,0.0,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=125367");
        dbManager.insertCarsData("2018 벤츠 S클래스 플러그인 하이브리드",11290,0.0,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=121467");
        dbManager.insertCarsData("2015 벤츠 S클래스 플러그인 하이브리드",12690,35.7,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=56667");
        dbManager.insertCarsData("2015 벤츠 E클래스 하이브리드 ",7980,17.2,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=123479");
        dbManager.insertCarsData("2014 벤츠 E클래스 하이브리드 ",8090,17.2,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=29608");
        dbManager.insertCarsData("2016 벤츠 GLE PHEV",7010,3.3,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=57651");
        dbManager.insertCarsData("2013 벤츠 S클래스 하이브리드",16000,8.8,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=19075");
        dbManager.insertCarsData("2012 벤츠 S클래스 하이브리드",16100,9.2,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=18096");
        dbManager.insertCarsData("2010 벤츠 S클래스 하이브리드",16570,9.2,R.drawable.benz,11,"https://auto.naver.com/car/main.nhn?yearsId=10300");

        //포르쉐

        dbManager.insertCarsData("2018 포르쉐 파나메라4 E-하이브리드",15980,12.3,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=123959");
        dbManager.insertCarsData("2018 포르쉐 파나메라 E-하이브리드 스포츠 투리스모",24400,3.0,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=126159");
        dbManager.insertCarsData("2018 포르쉐 파나메라4 E-하이브리드 스포츠 투리스모",16320,0.0,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=68825");
        dbManager.insertCarsData("2017 포르쉐 파나메라4 E-하이브리드",15980,2.5,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=66203");
        dbManager.insertCarsData("2015 포르쉐 파나메라 S E-하이브리드",15900,10.9,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=58511");
        dbManager.insertCarsData("2015 포르쉐 카이엔 S E-하이브리드",11870,9.4,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=57655");
        dbManager.insertCarsData("2015 포르쉐 카이엔 S 하이브리드",11310,10.0,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=53931");
        dbManager.insertCarsData("2015 포르쉐 918 스파이더 ",95480,0.0,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=47909");
        dbManager.insertCarsData("2013 포르쉐 파나메라 하이브리드 ",16200,10.9,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=28819");
        dbManager.insertCarsData("2012 포르쉐 파나메라 하이브리드 ",16540,10.2,R.drawable.porsche,12,"https://auto.naver.com/car/main.nhn?yearsId=16469");

        //테슬라

        dbManager.insertCarsData("2018 테슬라 모델3",3950,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=63053");
        dbManager.insertCarsData("2017 테슬라 모델S",11570,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=68431");
        dbManager.insertCarsData("2016 테슬라 모델X",13347,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=60339");
        dbManager.insertCarsData("2016 테슬라 모델S",8210,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=63269");
        dbManager.insertCarsData("2013 테슬라 모델S",7180,4.5,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=29834");
        dbManager.insertCarsData("2010 테슬라 로드스터 스포츠",14520,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=126155");
        dbManager.insertCarsData("2008 테슬라 로드스터 ",11100,0.0,R.drawable.teslr,13,"https://auto.naver.com/car/main.nhn?yearsId=126145");

        //렉서스

        dbManager.insertCarsData("2018 렉서스 RX450h",7760,12.8,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=124841");
        dbManager.insertCarsData("2018 렉서스 LS 하이브리드",14900,10.6,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=12287");
        dbManager.insertCarsData("2018 렉서스 NX300h",5570,12.0,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=122145");
        dbManager.insertCarsData("2017 렉서스 CT 200h F 스포츠 ",4460,17.0,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=121367");
        dbManager.insertCarsData("2017 렉서스 CT 200h",3970,17.0,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=121363");
        dbManager.insertCarsData("2018 렉서스 LC500h",17760,10.9,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=121169");
        dbManager.insertCarsData("2017 렉서스 NX300h",5580,12.6,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=66231");
        dbManager.insertCarsData("2016 렉서스 RX450h",7740,12.8,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=62019");
        dbManager.insertCarsData("2016 렉서스 RX F SPORT ",8740,17.0,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=120717");
        dbManager.insertCarsData("2016 렉서스 GS 하이브리드",7930,12.7,R.drawable.lexus,14,"https://auto.naver.com/car/main.nhn?yearsId=122201");


    }

    private void showProgressDialog(String message) {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);

        dialog.show();
        mHandler.sendEmptyMessageDelayed(TIME_OUT, 2000);
    }


    private void startLocationService() {
        // 위치 관리자 객체 참조
     manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("targetLat", latitude);
                intent.putExtra("targetLong", longitude);
                startActivity(intent);
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }



    }

    /**
     * 리스너 클래스 정의
     */
    private class GPSListener implements LocationListener {
        /**
         * 위치 정보가 확인될 때 자동 호출되는 메소드
         */
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : "+ latitude + "\nLongitude:"+ longitude;
            Log.i("GPSListener", msg);

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("targetLat", latitude);
            intent.putExtra("targetLong", longitude);
            startActivity(intent);

            manager.removeUpdates(GPSListener.this);

        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }


}
