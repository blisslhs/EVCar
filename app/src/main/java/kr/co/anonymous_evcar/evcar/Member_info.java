package kr.co.anonymous_evcar.evcar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class Member_info extends AppCompatActivity {

    ImageView btn_back;
    Button move_addmycar;
    TextView member_mainname;
    TextView car_name;
    ImageView company_img;
    TextView car_nickname;
    TextView car_mileage;
    TextView car_distance;
    TextView member_name;
    TextView member_email;
    TextView member_phone;
    TextView car_gowebview;
    Button member_setmycar;
    LinearLayout btn_modify;
    LinearLayout delete_mycar;


    LinearLayout btn_gomyinfo;
    LinearLayout move_carslist;
    LinearLayout finder;
    LinearLayout move_mycarmanage;



    String CarsDataPk;

    DBManager dbManager;

    Member login_member;
    MyCar myCar;
    CarsData carsData;

    ArrayList<MyCar>MyCaritems = new ArrayList<>();
    List<CarsData>CarsItems = new ArrayList<>();

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);

        btn_back = findViewById(R.id.btn_back);
        move_addmycar = findViewById(R.id.move_addmycar);
        member_mainname = findViewById(R.id.member_mainname);
        car_name = findViewById(R.id.car_name);
        company_img = findViewById(R.id.company_img);
        car_nickname = findViewById(R.id.car_nickname);
        car_mileage = findViewById(R.id.car_mileage);
        car_distance = findViewById(R.id.car_distance);
        member_name = findViewById(R.id.member_name);
        member_email = findViewById(R.id.member_email);
        member_phone = findViewById(R.id.member_phone);
        car_gowebview = findViewById(R.id.car_gowebview);
        member_setmycar = findViewById(R.id.member_setmycar);
        btn_modify = findViewById(R.id.btn_modify);
        delete_mycar = findViewById(R.id.delete_mycar);

        btn_gomyinfo = findViewById(R.id.btn_gomyinfo);
        move_carslist = findViewById(R.id.move_carslist);
        finder = findViewById(R.id.finder);
        move_mycarmanage = findViewById(R.id.move_mycarmanage);

        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goCheckMember = new Intent(Member_info.this, CheckMember.class);

                startActivity(goCheckMember);

            }
        });

        car_gowebview.setPaintFlags(car_gowebview.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        dbManager = new DBManager(Member_info.this,"EVCarDB.db",null,1);

        car_gowebview.setVisibility(View.INVISIBLE);
        delete_mycar.setVisibility(View.INVISIBLE);

        login_member = LoginService.getInstance().getLoginMember();
        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(),1);

        if(myCar==null){
            carsData = dbManager.getCarData(1);
        }else{
            carsData = dbManager.getCarData(myCar.getCarsdata_pk());
            car_gowebview.setVisibility(View.VISIBLE);
            delete_mycar.setVisibility(View.VISIBLE);
        }

        final Intent intent = getIntent();

        //CarsDataPk = intent.getStringExtra("CarsDataPk");

        member_mainname.setText(login_member.getName());
        car_name.setText(carsData.getMycarname());  //차량 이름
        company_img.setBackgroundResource(carsData.getImage());

        if(myCar==null){
            car_nickname.setText("");       //닉네임
        }else if(myCar.getNickname().toString().equals("")){
            car_nickname.setText("닉네임");       //닉네임
            car_gowebview.setVisibility(View.VISIBLE);
        }else{
            car_nickname.setText(myCar.getNickname().toString());       //닉네임
        }
        if(myCar == null){
            car_mileage.setText("");        //연비
        }else if (carsData.getMileage() ==0.0){
            car_mileage.setText("정보없음");//연비
        }
        else {
            car_mileage.setText(Double.toString(carsData.getMileage()));//연비
        }


        if(myCar==null){
            car_distance.setText("");       //주행거리
        }else if(myCar.getDistance().toString().equals("0")){
            car_distance.setText("미입력");       //주행거리
        }else{
            car_distance.setText(myCar.getDistance().toString());       //주행거리
        }
        member_name.setText(login_member.getName());                      //로그인멤버 이름
        member_email.setText(login_member.getEmail().toString());         //로그인멤버 이메일
        member_phone.setText(login_member.getPhone());         //로그인멤버 핸드폰번호

        car_gowebview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Member_info.this,WebViewPage.class);
                intent1.putExtra("page",carsData.getPage());
                startActivity(intent1);
            }
        });

        MyCaritems = dbManager.getMyCarList((int)(long)login_member.getId());


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        move_addmycar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Member_info.this,AddMyCar.class);
                startActivity(intent1);
                finish();
            }
        });

       member_setmycar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Member_info.this);
               alertBuilder.setIcon(R.mipmap.ic_launcher);

               final ArrayAdapter<String>adpter = new ArrayAdapter<String>(
                       Member_info.this,android.R.layout.select_dialog_singlechoice
               );

               for(int i=0; i<MyCaritems.size(); i++){
                   CarsData tmpItem = dbManager.getCarData(MyCaritems.get(i).getCarsdata_pk());
                   adpter.add(tmpItem.getMycarname());
               }


               alertBuilder.setNegativeButton("취소",
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog,
                                               int which) {
                               dialog.dismiss();
                           }
                       });

               // Adapter 셋팅
               alertBuilder.setAdapter(adpter,
                       new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog,
                                               final int id) {

                               // AlertDialog 안에 있는 AlertDialog
                               String strName = adpter.getItem(id);
                               AlertDialog.Builder innBuilder = new AlertDialog.Builder(context);
                               innBuilder.setMessage(strName);
                               innBuilder.setTitle("정말로 변경 하시겠습니까 ? ");
                               innBuilder.setPositiveButton("확인",
                                               new DialogInterface.OnClickListener() {
                                                   public void onClick(
                                                           DialogInterface dialog,
                                                           int which) {
                                                       dbManager.reSetMyCar((int)(long)login_member.getId(),0);
                                                       String tmpMyCarsName = adpter.getItem(id);
                                                       CarsData tmpCarsData = dbManager.getTmpChangeCarsData(tmpMyCarsName);
                                                       dbManager.SetMyCar((int)(long)login_member.getId(),tmpCarsData.getPk(),1);
                                                       finish();
                                                       startActivity(new Intent(Member_info.this,Member_info.class));
                                                       dialog.dismiss();
                                                   }
                                               });
                               innBuilder.setNegativeButton("취소",
                                       new DialogInterface.OnClickListener() {
                                           public void onClick(DialogInterface dialog,
                                                               int which) {

                                           }
                                       });


                               innBuilder.show();
                           }
                       });
               alertBuilder.show();
           }
       });

        if(myCar != null) {
            car_nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout layout = new LinearLayout(Member_info.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    final EditText editText = new EditText(Member_info.this);
                    editText.setHint("8글자까지 등록할 수 있습니다. ");
                    InputFilter[] FilterArray = new InputFilter[1];
                    FilterArray[0] = new InputFilter.LengthFilter(8);
                    editText.setSingleLine(true);
                    editText.setFilters(FilterArray);
                    layout.setPadding(60, 50, 100, 0);
                    layout.addView(editText);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_info.this);
                    builder.setTitle("애칭을 입력해주세요.");
                    builder.setView(layout);
                    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbManager.setMyCarNickname(myCar.getPk(), editText.getText().toString());
                            finish();
                            startActivity(new Intent(Member_info.this, Member_info.class));
                            Toast.makeText(Member_info.this, "글자수는 2~8글자만 가능합니다", Toast.LENGTH_SHORT).show();
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });
        }

        if(myCar != null) {
            car_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LinearLayout layout = new LinearLayout(Member_info.this);
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    final EditText editText = new EditText(Member_info.this);
                    editText.setHint("숫자만 입력하세요");
                    editText.setSingleLine(true);
                    layout.setPadding(80, 50, 100, 0);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    layout.addView(editText);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Member_info.this);
                    builder.setTitle("거리를 입력해주세요.");
                    builder.setView(layout);
                    builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbManager.setMyCarDistance(myCar.getPk(), Integer.parseInt(editText.getText().toString()));
                            finish();
                            startActivity(new Intent(Member_info.this, Member_info.class));
                        }
                    });

                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    builder.show();
                }
            });
        }

        delete_mycar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(Member_info.this);
                alertDialog.setTitle("경고");
                alertDialog.setMessage("이 차의 기록이 모두 지워집니다.\n정말 삭제 하시겠습니까?");
                alertDialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(Member_info.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();
                        dbManager.deleteMyCar(myCar.getPk());
                        Intent intent = new Intent(Member_info.this,Member_info.class);
                        startActivity(intent);
                        finish();


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

        move_carslist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_info.this,AllCarsList.class);
                startActivity(intent);
                finish();
            }
        });

        btn_gomyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_info.this,Member_info.class);
                startActivity(intent);
                finish();
            }
        });

        finder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_info.this, MapsActivity.class);
                intent.putExtra("targetLat", 37.502695);
                intent.putExtra("targetLong", 127.025);
                startActivity(intent);
                finish();
            }
        });

        move_mycarmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Member_info.this,Main2Activity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
