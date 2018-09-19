package kr.co.anonymous_evcar.evcar;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.retrofit.RetrofitService;
import kr.co.anonymous_evcar.evcar.service.LoginService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginView extends AppCompatActivity {
    EditText login_id;
    EditText login_pw;

    CheckBox checkBox;
    Boolean saveLoginData;
    SharedPreferences appData;

    CheckBox auto_login;
    Boolean loginChecked;

    Button mem_register;
    Button mem_login;
    DBManager dbManager;

    String id;
    String pw;

    SQLiteDatabase sqlitedb;

    String admin = "admin";
    String adminpw = "admin123";
    String adminname = "admin";
    String adminphone = "01012345678";
    String adminlocation = "seoul";
    String adminemail = "test@test";

    ArrayList<Member> members = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        login_id = findViewById(R.id.login_id);
        login_pw = findViewById(R.id.login_pw);
        mem_register = findViewById(R.id.mem_register);
        mem_login = findViewById(R.id.mem_login);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        auto_login = (CheckBox) findViewById(R.id.auto_login);

        dbManager = new DBManager(LoginView.this,"EVCarDB.db",null,1);

        /*  Call<ArrayList<Member>> observ = RetrofitService.getInstance().getRetrofitRequest().getMemberList();
        observ.enqueue(new Callback<ArrayList<Member>>() {
            @Override
            public void onResponse(Call<ArrayList<Member>> call, Response<ArrayList<Member>> response) {
                if(response.isSuccessful()) {
                    members = response.body();

                        for (int i = 0; i < members.size(); i++) {

                            //insert member
                            if(dbManager.idCheck(members.get(i).getMem_id())){

                            }else {
                                dbManager.insertMember(members.get(i).getName(), members.get(i).getMem_id(), members.get(i).getMem_pw(), members.get(i).getGender(),
                                        members.get(i).getPhone(), members.get(i).getLoaction(), members.get(i).getEmail());
                            }

                        }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Member>> call, Throwable t) {

            }

        });
        */

        // 로그인 정보 기억하기에 대한 설정값 불러오기
        appData = getSharedPreferences("appData", Activity.MODE_PRIVATE);
        load();

        // 이전에 로그인 정보를 저장시킨 기록이 있다면
        if (saveLoginData) {
            login_id.setText(id);
            login_pw.setText(pw);
            checkBox.setChecked(saveLoginData);
            auto_login.setChecked(loginChecked);
        }

        if(loginChecked) {
            login_id.setText(id);
            login_pw.setText(pw);
            checkBox.setChecked(saveLoginData);
            auto_login.setChecked(loginChecked);

            id = login_id.getText().toString();
            pw = login_pw.getText().toString();

            dbManager = new DBManager(LoginView.this,"EVCarDB.db",null,1);

            if (dbManager.login(id,pw)) {
                LoginService loginService = LoginService.getInstance();

                Member loginMember = dbManager.getMemberInfo(id, pw);
                loginService.setLoginMember(loginMember);

                // 로그인 정보 기억하기에 대한 로그인 성공시 저장 처리
                save();

                Intent intent = new Intent(LoginView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }

        mem_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = login_id.getText().toString();
                String pw = login_pw.getText().toString();


                dbManager = new DBManager(LoginView.this,"EVCarDB.db",null,1);

                if (dbManager.login(id,pw)) {
                    LoginService loginService = LoginService.getInstance();

                    Member loginMember = dbManager.getMemberInfo(id,pw);
                    loginService.setLoginMember(loginMember);

                    // 로그인 정보 기억하기에 대한 로그인 성공시 저장 처리
                    save();

                    Intent intent = new Intent(LoginView.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginView.this,"계정 오류",Toast.LENGTH_SHORT).show();
                }

                //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                //startActivity(intent);
            }
        });

        mem_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginView.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        if(dbManager.idCheck(admin)) {                  //관리자 계정
        }else {

                dbManager.insertMember(adminname,admin,adminpw,0,adminphone,adminlocation,adminemail);

        }


    }


    // 로그인 정보 기억하기에 대한 설정값을 저장하는 함수
    private void save() {
        // SharedPreferences 객체만으론 저장 불가능 Editor 사용
        SharedPreferences.Editor editor = appData.edit();

        // 에디터객체.put타입( 저장시킬 이름, 저장시킬 값 )
        // 저장시킬 이름이 이미 존재하면 덮어씌움
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putBoolean("AUTO_LOGIN", auto_login.isChecked());
        editor.putString("ID", login_id.getText().toString().trim());
        editor.putString("PW", login_pw.getText().toString().trim());

        // apply, commit 을 안하면 변경된 내용이 저장되지 않음
        editor.apply();
    }

    // 로그인 정보 기억하기에 대한 설정값을 불러오는 함수
    private void load() {
        // SharedPreferences 객체.get타입( 저장된 이름, 기본값 )
        // 저장된 이름이 존재하지 않을 시 기본값
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        loginChecked = appData.getBoolean("AUTO_LOGIN", false);
        id = appData.getString("ID", "");
        pw = appData.getString("PW", "");
    }

}

