package kr.co.anonymous_evcar.evcar;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.data.MyCarCost;
import kr.co.anonymous_evcar.evcar.data.MyCarPush;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class Main2Activity extends AppCompatActivity {

    @BindView(R.id.btn_back) ImageView btn_back;

    @BindView(R.id.et_distance) TextView et_distance;
    @BindView(R.id.btn_input) Button btn_input;
    @BindView(R.id.btn_gomyinfo) LinearLayout btn_gomyinfo;
    @BindView(R.id.move_carslist) LinearLayout move_carslist;
    @BindView(R.id.finder) LinearLayout finder;
    @BindView(R.id.wipper_per) TextView wipper_per;
    @BindView(R.id.tire_reset) Button tire_reset;
    @BindView(R.id.coolant_reset) Button coolant_reset;
    @BindView(R.id.oil_reset) Button oil_reset;
    DBManager dbManager;
    Member login_member;
    SQLiteDatabase sqlitedb;
    MyCar myCar;

    Integer distance;
    LinearLayout layout1;
    LinearLayout layout2;
    @BindView(R.id.car_gomycarcostview)TextView car_gomycarcostview;
    @BindView(R.id.total_cost)TextView total_cost;
    @BindView(R.id.goaddmycar)TextView goaddmycar;


    @BindView(R.id.txt_oil) TextView txt_oil;
    @BindView(R.id.progressbar_oil) ProgressBar progressbar_oil;
    @BindView(R.id.txt_coolant) TextView txt_coolant;
    @BindView(R.id.progressbar_coolant) ProgressBar progressbar_coolant;
    @BindView(R.id.txt_tire) TextView txt_tire;
    @BindView(R.id.progressbar_tire) ProgressBar progressbar_tire;
    @BindView(R.id.txt_wiper) TextView txt_wiper;
    @BindView(R.id.progressbar_wiper) ProgressBar progressbar_wiper;
    @BindView(R.id.txt_calender) TextView txt_calender;

    Calendar calendar = Calendar.getInstance();
    Calendar calendar2 = Calendar.getInstance();

    Integer year = calendar.get(Calendar.YEAR);
    Integer month = calendar.get(Calendar.MONTH);
    Integer day = calendar.get(Calendar.DAY_OF_MONTH);

    long result = 0;
    int i_result = 0;

    Integer txttotal_cost = 0;

    ArrayList<MyCarCost> myCarCosts = new ArrayList<>();

    MyCarPush myCarPush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        LoginService loginService = LoginService.getInstance();
        login_member = loginService.getLoginMember();
        ButterKnife.bind(this);
        dbManager = new DBManager(Main2Activity.this, "EVCarDB.db", null, 1);

        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);
        goaddmycar.setPaintFlags(goaddmycar.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        car_gomycarcostview.setPaintFlags(car_gomycarcostview.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);




        progressbar_oil.getProgressDrawable().setColorFilter(R.drawable.progressbar_green, PorterDuff.Mode.SRC_IN);

        if (myCar != null) {
            myCarCosts = dbManager.getMyCarCostPartAll(myCar.getPk(),year,month);
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
            distance = myCar.getDistance();
            result = myCar.getWiper_dis();
            et_distance.setText(distance.toString());
            setTireProgress(distance);
            setOilProgress(distance);
            setCoolantProgress(distance);
            setWiperProgress();


            if(dbManager.MyCarPushCheck(myCar.getPk())){
                myCarPush = dbManager.getMyCarPush(myCar.getPk());
            }else {
                dbManager.insertMyCarPush(myCar.getPk(),0,0,0,0);
            }


            for(int i=0; i<myCarCosts.size(); i++) {
                txttotal_cost += myCarCosts.get(i).getCost();
                txttotal_cost += myCarCosts.get(i).getHipassCost();
            }
            total_cost.setText(txttotal_cost.toString());
        } else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            Toast.makeText(this, "차량을 등록해주세요", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.btn_back)
    void btn_backClick() {
        finish();
    }

    @OnClick(R.id.car_gomycarcostview)
    void car_gomycarcostviewClick(){
        Intent intent = new Intent(Main2Activity.this,MyCarCostView.class);
        startActivity(intent);
    }

    @OnClick(R.id.goaddmycar)
    void goaddmycarClick(){
        Intent intent = new Intent(Main2Activity.this,AddMyCar.class);
        startActivity(intent);
        finish();
    }
    @OnClick(R.id.oil_reset)
    void oilReset() {

        LinearLayout layout = new LinearLayout(Main2Activity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(Main2Activity.this);
        editText.setHint("숫자만 입력하세요");
        editText.setSingleLine(true);
        layout.setPadding(80, 50, 100, 0);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("금액을 입력해주세요.");
        builder.setView(layout);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        AlertDialog.Builder innBuilder = new AlertDialog.Builder(Main2Activity.this);
        innBuilder.setTitle("정말로 교체 하시겠습니까 ? ");
        innBuilder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        String tmpcost = editText.getText().toString();
                        Integer cost = Integer.parseInt(tmpcost);

                        dbManager.insertMyCarCostPart(myCar.getPk(),1,cost,0,0,0,year,month);

                        dbManager.setMyCarOil_dis(myCar.getPk(), myCar.getDistance());
                        dbManager.setMyCarPushOilDiv(myCar.getPk(),0);
                        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);
                        distance = myCar.getDistance();
                        setOilProgress(distance);
                        finish();
                        startActivity(new Intent(Main2Activity.this, Main2Activity.class));
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

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

    @OnClick(R.id.tire_reset)
    void TireReset() {

        LinearLayout layout = new LinearLayout(Main2Activity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(Main2Activity.this);
        editText.setHint("숫자만 입력하세요");
        editText.setSingleLine(true);
        layout.setPadding(80, 50, 100, 0);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("금액을 입력해주세요.");
        builder.setView(layout);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        AlertDialog.Builder innBuilder = new AlertDialog.Builder(Main2Activity.this);
        innBuilder.setTitle("정말로 교체 하시겠습니까 ? ");
        innBuilder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        String tmpcost = editText.getText().toString();
                        Integer cost = Integer.parseInt(tmpcost);

                        dbManager.insertMyCarCostPart(myCar.getPk(),3,cost,0,0,0,year,month);

                        dbManager.setMyCarTire_dis(myCar.getPk(), myCar.getDistance());

                        dbManager.setMyCarPushTireDiv(myCar.getPk(),0);

                        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);
                        distance = myCar.getDistance();
                        setTireProgress(distance);
                        finish();
                        startActivity(new Intent(Main2Activity.this, Main2Activity.class));
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

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }


    @OnClick(R.id.coolant_reset)
    void coolantReset() {


        LinearLayout layout = new LinearLayout(Main2Activity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(Main2Activity.this);
        editText.setHint("숫자만 입력하세요");
        editText.setSingleLine(true);
        layout.setPadding(80, 50, 100, 0);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("금액을 입력해주세요.");
        builder.setView(layout);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
        AlertDialog.Builder innBuilder = new AlertDialog.Builder(Main2Activity.this);
        innBuilder.setTitle("정말로 교체 하시겠습니까 ? ");
        innBuilder.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(
                            DialogInterface dialog,
                            int which) {
                        String tmpcost = editText.getText().toString();
                        Integer cost = Integer.parseInt(tmpcost);

                        dbManager.insertMyCarCostPart(myCar.getPk(),2,cost,0,0,0,year,month);

                        dbManager.setMyCarCoolant_dis(myCar.getPk(), myCar.getDistance());

                        dbManager.setMyCarPushCoolantDiv(myCar.getPk(),0);

                        myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);

                        distance = myCar.getDistance();

                        setCoolantProgress(distance);

                        finish();
                        startActivity(new Intent(Main2Activity.this, Main2Activity.class));
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

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }

    @OnClick(R.id.wiper_reset)
    void change_wiper() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view,final int y, int m, int d) {
                year = y;
                month = m;
                day = d;


                LinearLayout layout = new LinearLayout(Main2Activity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER_HORIZONTAL);
                final EditText editText = new EditText(Main2Activity.this);
                editText.setHint("숫자만 입력하세요");
                editText.setSingleLine(true);
                layout.setPadding(80, 50, 100, 0);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(editText);

                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setTitle("금액을 입력해주세요.");
                builder.setView(layout);
                builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder innBuilder = new AlertDialog.Builder(Main2Activity.this);
                innBuilder.setTitle("정말로 변경 하시겠습니까 ? ");
                innBuilder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {

                                String tmpcost = editText.getText().toString();
                                Integer cost = Integer.parseInt(tmpcost);

                                dbManager.insertMyCarCostPart(myCar.getPk(),4,cost,0,0,0,year,month);

                                dbManager.setMyCarPushWiperDiv(myCar.getPk(),0);

                                myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);


                                distance = myCar.getDistance();


                                setDate(year, month + 1, day);

                                setWiperProgress();
                                finish();
                                startActivity(new Intent(Main2Activity.this,Main2Activity.class));
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

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
        };

        DatePickerDialog dialog = new DatePickerDialog(Main2Activity.this, dateSetListener, year, month, day);
        dialog.show();
    }

    @OnClick(R.id.btn_input)
    void inputDistances() {
        LinearLayout layout = new LinearLayout(Main2Activity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);
        final EditText editText = new EditText(Main2Activity.this);
        editText.setHint("숫자만 입력하세요");
        editText.setSingleLine(true);
        layout.setPadding(80, 50, 100, 0);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(editText);

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("거리를 입력해주세요.");
        builder.setView(layout);
        builder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder innBuilder = new AlertDialog.Builder(Main2Activity.this);
                innBuilder.setTitle("정말로 변경 하시겠습니까 ? ");
                innBuilder.setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    DialogInterface dialog,
                                    int which) {
                                String Distances = editText.getText().toString();

                                Integer setDistances = Integer.parseInt(Distances);
                                Integer oil = Integer.parseInt(Distances);
                                Integer coolant = Integer.parseInt(Distances);
                                Integer tire = Integer.parseInt(Distances);

                                Integer myDistances = myCar.getDistance();

                                if (setDistances <= myDistances) {
                                    Toast.makeText(Main2Activity.this, "전에 주행거리보다 변경하시는 주행거리가 낮습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (myCar != null) {
                                        setTireProgress(tire);
                                        setCoolantProgress(coolant);
                                        setOilProgress(oil);
                                        dbManager.setMyCarDistance(myCar.getPk(), setDistances);

                                        dbManager.setMyCarPushOilDiv(myCar.getPk(),0);
                                        dbManager.setMyCarPushCoolantDiv(myCar.getPk(),0);
                                        dbManager.setMyCarPushTireDiv(myCar.getPk(),0);
                                        dbManager.setMyCarPushWiperDiv(myCar.getPk(),0);


                                        dialog.dismiss();
                                        finish();
                                        startActivity(new Intent(Main2Activity.this, Main2Activity.class));
                                    } else {
                                        Toast.makeText(Main2Activity.this, "차량을 등록해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                }


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

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();


    }

    @OnClick(R.id.btn_gomyinfo)
    void moveMyInfo() {
        Intent intent = new Intent(Main2Activity.this, Member_info.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.move_carslist)
    void moveCarsList() {
        Intent intent = new Intent(Main2Activity.this, AllCarsList.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.finder)
    void movidFinder() {
        Intent intent = new Intent(Main2Activity.this, MapsActivity.class);
        intent.putExtra("targetLat", 37.502695);
        intent.putExtra("targetLong", 127.025);
        startActivity(intent);
        finish();
    }

    public void setTireProgress(int x) {
        x -= myCar.getTire_dis();
        if (x >= 30000) {
            x %= 30000;
        }

        double percent = x / 30000d * 100;

        progressbar_tire.setProgress((int) percent);
        txt_tire.setText((int) percent + "% ");
        setColorProgressBar((int) percent, progressbar_tire);

        if(myCar != null) {
            if (dbManager.MyCarPushCheck(myCar.getPk())) {
                myCarPush = dbManager.getMyCarPush(myCar.getPk());

                if (myCarPush.getTire_division() == 0) {
                    if (percent >= 95 && percent <= 99) {

                        Bitmap mLargeiconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.login);

                        PendingIntent mPendingIntent = PendingIntent.getActivity(Main2Activity.this, 0, new Intent(getApplicationContext(), Intro.class)
                                , PendingIntent.FLAG_UPDATE_CURRENT);        //클릭했을때 이화면으로 오기


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(Main2Activity.this)
                                        .setSmallIcon(R.drawable.login)
                                        .setContentTitle("EVCar")              //제목
                                        .setContentText("타이어 교체 시점이 얼마 남지 않았습니다.")               //내용
                                        .setLargeIcon(mLargeiconForNoti)            //알림 화면의 옆 쪽 큰 아이콘
                                        .setDefaults(Notification.DEFAULT_VIBRATE)          //진동으로 푸시알림
                                        .setAutoCancel(true)       //사용자가 터치하면 없어짐
                                        .setContentIntent(mPendingIntent);

                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());        //알람보내기     0,은 우선순위

                        dbManager.setMyCarPushTireDiv(myCar.getPk(), 1);
                    } else {
                        dbManager.insertMyCarPush(myCar.getPk(), 0, 0, 0, 0);
                    }
                }
            }
        }
    }

    public void setOilProgress(int x) {
        x -= myCar.getOil_dis();
        if (x >= 10000) {
            x %= 10000;
        }

        double percent = x / 10000d * 100;

        progressbar_oil.setProgress((int) percent);
        txt_oil.setText((int) percent + "% ");
        setColorProgressBar((int) percent, progressbar_oil);


        if(myCar != null) {
            if (dbManager.MyCarPushCheck(myCar.getPk())) {
                myCarPush = dbManager.getMyCarPush(myCar.getPk());

                if (myCarPush.getOil_division() == 0) {
                    if (percent >= 95 && percent <= 99) {

                        Bitmap mLargeiconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.login);

                        PendingIntent mPendingIntent = PendingIntent.getActivity(Main2Activity.this, 0, new Intent(getApplicationContext(), Intro.class)
                                , PendingIntent.FLAG_UPDATE_CURRENT);        //클릭했을때 이화면으로 오기


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(Main2Activity.this)
                                        .setSmallIcon(R.drawable.login)
                                        .setContentTitle("EVCar")              //제목
                                        .setContentText("엔진오일 교체 시점이 얼마 남지 않았습니다.")               //내용
                                        .setLargeIcon(mLargeiconForNoti)            //알림 화면의 옆 쪽 큰 아이콘
                                        .setDefaults(Notification.DEFAULT_VIBRATE)          //진동으로 푸시알림
                                        .setAutoCancel(true)       //사용자가 터치하면 없어짐
                                        .setContentIntent(mPendingIntent);


                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());        //알람보내기     0,은 우선순위


                        dbManager.setMyCarPushOilDiv(myCar.getPk(),1);
                    } else {
                        dbManager.insertMyCarPush(myCar.getPk(), 0, 0, 0, 0);
                    }
                }
            }



        }
    }

    public void setCoolantProgress(int x) {
        x -= myCar.getCoolant_dis();
        if (x >= 60000) {
            x %= 60000;
        }

        double percent = x / 60000d * 100;

        progressbar_coolant.setProgress((int) percent);
        txt_coolant.setText((int) percent + "% ");
        setColorProgressBar((int) percent, progressbar_coolant);

        if(myCar != null) {
            if (dbManager.MyCarPushCheck(myCar.getPk())) {
                myCarPush = dbManager.getMyCarPush(myCar.getPk());

                if (myCarPush.getCoolant_division() == 0) {
                    if (percent >= 95 && percent <= 99) {

                        Bitmap mLargeiconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.login);

                        PendingIntent mPendingIntent = PendingIntent.getActivity(Main2Activity.this, 0, new Intent(getApplicationContext(), Intro.class)
                                , PendingIntent.FLAG_UPDATE_CURRENT);        //클릭했을때 이화면으로 오기


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(Main2Activity.this)
                                        .setSmallIcon(R.drawable.login)
                                        .setContentTitle("EVCar")              //제목
                                        .setContentText("냉각수 교체 시점이 얼마 남지 않았습니다.")               //내용
                                        .setLargeIcon(mLargeiconForNoti)            //알림 화면의 옆 쪽 큰 아이콘
                                        .setDefaults(Notification.DEFAULT_VIBRATE)          //진동으로 푸시알림
                                        .setAutoCancel(true)       //사용자가 터치하면 없어짐
                                        .setContentIntent(mPendingIntent);


                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());        //알람보내기     0,은 우선순위


                        dbManager.setMyCarPushCoolantDiv(myCar.getPk(), 1);
                    } else {
                        dbManager.insertMyCarPush(myCar.getPk(), 0, 0, 0, 0);
                    }
                }
            }
        }


    }

    public void setWiperProgress() {

        if(myCar.getWiper_dis()==0l){
            txt_calender.setText("");
            return;
        }
        else{
            long date = myCar.getWiper_dis();
            calendar2.setTimeInMillis(date);
        }

        Integer date_y = calendar2.get(Calendar.YEAR);
        Integer date_m= calendar2.get(Calendar.MONTH);
        Integer date_d= calendar2.get(Calendar.DAY_OF_MONTH);

        setDate(date_y, date_m+1, date_d);

        long start = calendar2.getTimeInMillis();
        long today = calendar.getTimeInMillis();


        if(start <= today) {
            result = (today - start) / 1000;

            i_result = ((int)(result / (24 * 60 * 60))) *100/182;
        }

        progressbar_wiper.setProgress(i_result);

        wipper_per.setText(i_result+"%");


        setColorProgressBar(i_result,progressbar_wiper);

        if(myCar != null) {
            if (dbManager.MyCarPushCheck(myCar.getPk())) {
                myCarPush = dbManager.getMyCarPush(myCar.getPk());

                if (myCarPush.getWiper_division() == 0) {
                    if (i_result >= 95 && i_result <= 99) {

                        Bitmap mLargeiconForNoti = BitmapFactory.decodeResource(getResources(), R.drawable.login);

                        PendingIntent mPendingIntent = PendingIntent.getActivity(Main2Activity.this, 0, new Intent(getApplicationContext(), Intro.class)
                                , PendingIntent.FLAG_UPDATE_CURRENT);        //클릭했을때 이화면으로 오기


                        NotificationCompat.Builder mBuilder =
                                new NotificationCompat.Builder(Main2Activity.this)
                                        .setSmallIcon(R.drawable.login)
                                        .setContentTitle("EVCar")              //제목
                                        .setContentText("와이퍼 교체 시점이 얼마 남지 않았습니다.")               //내용
                                        .setLargeIcon(mLargeiconForNoti)            //알림 화면의 옆 쪽 큰 아이콘
                                        .setDefaults(Notification.DEFAULT_VIBRATE)          //진동으로 푸시알림
                                        .setAutoCancel(true)       //사용자가 터치하면 없어짐
                                        .setContentIntent(mPendingIntent);


                        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        mNotificationManager.notify(0, mBuilder.build());        //알람보내기     0,은 우선순위


                        dbManager.setMyCarPushWiperDiv(myCar.getPk(), 1);
                    } else {
                        dbManager.insertMyCarPush(myCar.getPk(), 0, 0, 0, 0);
                    }
                }
            }
        }

    }

    public void setColorProgressBar(int x, ProgressBar progressBar) {
        if (x > 85) {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_red));
        } else if (x > 70) {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_yellow));
        } else {
            progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_green));
        }
    }

    public void setDate(final Integer year, final Integer month, final Integer day) {


        calendar2.set(Calendar.YEAR, year);
        calendar2.set(Calendar.MONTH, month - 1);
        calendar2.set(Calendar.DAY_OF_MONTH, day);

        final long start = calendar2.getTimeInMillis();
        final long today = calendar.getTimeInMillis();

        if(start <= today){
            result = (today-start)/1000;

            result = (result/(24*60*60));

            dbManager.setMyCarWiper_dis(myCar.getPk(), start);

            myCar = dbManager.getMyCarUserInfo((int)(long)login_member.getId(), 1);
            txt_calender.setText(year + "/" + month + "/" + day );
        }else {
            Toast.makeText(Main2Activity.this, "잘못된 날짜입니다.", Toast.LENGTH_SHORT).show();
        }

    }

}