package kr.co.anonymous_evcar.evcar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    EditText reg_id;
    EditText reg_pw;
    EditText reg_pw2;
    TextView pw_info;
    TextView email_info;

    EditText reg_name;
    EditText reg_phone;
    EditText reg_location;
    EditText reg_email;

    Button reg_bt;
    Button cancel_bt;

    RadioGroup rg_gender;
    RadioButton rb_male;
    RadioButton rb_female;

    String gender = "";
    DBManager dbManager;
    SQLiteDatabase sqlitedb;
    Button overlap;


    private EditText et_keyNum = null;

    private Button button = null;
    Button certified;

    String certifiedKey;

    boolean check = false;
    boolean email_check = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()

                .permitDiskReads()

                .permitDiskWrites()

                .permitNetwork().build());


        reg_id = findViewById(R.id.reg_id);
        reg_pw = findViewById(R.id.reg_pw);
        reg_pw2 = findViewById(R.id.reg_pw2);
        pw_info = findViewById(R.id.pw_info);

        reg_name = findViewById(R.id.reg_name);
        reg_phone = findViewById(R.id.reg_phone);
        reg_location = findViewById(R.id.reg_location);
        reg_email = findViewById(R.id.reg_email);
        reg_bt = findViewById(R.id.reg_bt);
        cancel_bt = findViewById(R.id.cancel_bt);
        rg_gender = findViewById(R.id.radiogroup_gender);
        rb_female=findViewById(R.id.reg_female);
        rb_male = findViewById(R.id.reg_male);
        overlap = findViewById(R.id.overlap);
        et_keyNum = findViewById(R.id.et_keyNum);
        certified = findViewById(R.id.certified);
        button = (Button) findViewById(R.id.button);
        email_info = findViewById(R.id.email_info);


        dbManager = new DBManager(RegisterActivity.this,"EVCarDB.db",null,1);
        overlap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mem_id = reg_id.getText().toString();
                if(dbManager.idCheck(mem_id)) {
                    pw_info.setText("아이디가 존재합니다!");
                    check = false;
                }else{
                    if(mem_id.equals("")){
                        pw_info.setText("아이디를 입력하세요.");
                        check = false;
                    }else {
                        pw_info.setText("사용 가능합니다.");
                        check = true;
                    }
                }



            }
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                try {

                    GMailSender gMailSender = new GMailSender("jhyoon8718@gmail.com", "rnrmf8718");

                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
                    String contents = "Eco드라이버가 되신걸 환영합니다!\n아래 인증키를 입력하세요▽\n";
                    certifiedKey =  gMailSender.getEmailCode();
                    contents+=certifiedKey;
                    gMailSender.sendMail("인증번호 입니다.", contents, reg_email.getText().toString());

                    Toast.makeText(getApplicationContext(), "인증번호 발송", Toast.LENGTH_SHORT).show();

                } catch (SendFailedException e) {

                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();

                } catch (MessagingException e) {

                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    e.printStackTrace();

                }

            }

        });


        certified.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String tmpStr = et_keyNum.getText().toString();

                if(tmpStr.equals(certifiedKey)){
                    email_info.setText("인증성공");
                    email_check = true;
                }
                else{
                    email_info.setText("인증키가 올바르지 않습니다.");
                }
            }
        });
        cancel_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    reg_bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String mem_id = reg_id.getText().toString();
            final String mem_pw = reg_pw.getText().toString();
            String mem_pw2 = reg_pw2.getText().toString();
            final String name = reg_name.getText().toString();
            final String phone = reg_phone.getText().toString();
            final String location = reg_location.getText().toString();
            final String email = reg_email.getText().toString();

            if(dbManager.idCheck(mem_id)){
                if(check == false){
                    Toast.makeText(RegisterActivity.this,"중복확인부터 해주세요", Toast.LENGTH_SHORT).show();
                }else if(email_check == false) {
                    Toast.makeText(RegisterActivity.this, "이메일 인증을 해주세요", Toast.LENGTH_SHORT).show();
                }else if(!mem_pw.equals(mem_pw2)){
                    Toast.makeText(RegisterActivity.this,"비밀번호 오류", Toast.LENGTH_SHORT).show();
                }
            }else {
                if (!mem_pw.equals("") && !mem_pw2.equals("")) {
                    if (mem_pw.equals(mem_pw2)) {
                        if (check == false) {
                            Toast.makeText(RegisterActivity.this, "중복확인부터 해주세요", Toast.LENGTH_SHORT).show();
                        }else if(email_check == false){
                            Toast.makeText(RegisterActivity.this,"이메일 인증을 해주세요", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            if (!mem_id.equals("") && !mem_pw.equals("")) {
                                if (!name.equals("")) {
                                    if (!phone.equals("")) {
                                        if (!location.equals("")) {
                                            if (!email.equals("")) {
                                                if (rg_gender.getCheckedRadioButtonId() == R.id.reg_male) {
                                                    gender = "0";       //남자
                                                } else {
                                                    gender = "1";       //여자
                                                }


                                                dbManager.insertMember(name,mem_id,mem_pw,Integer.parseInt(gender),phone,location,email);

                                                /*    Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().join_ok(mem_id,mem_pw,name,gender,phone,location,email);
                                                observ.enqueue(new Callback<Void>() {
                                                    @Override
                                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                                        if (response.isSuccessful()){
                                                            dbManager.insertMember(name,mem_id,mem_pw,Integer.parseInt(gender),phone,location,email);
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Void> call, Throwable t) {

                                                    }
                                                });
                                                */
                                                Toast.makeText(RegisterActivity.this, "회원가입 성공", Toast.LENGTH_LONG).show();


                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "비밀번호가 다릅니다", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RegisterActivity.this, "정보를 입력 해주세요", Toast.LENGTH_SHORT).show();
                }
            }


        }
    });










    }
}
