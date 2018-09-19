package kr.co.anonymous_evcar.evcar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.retrofit.RetrofitService;
import kr.co.anonymous_evcar.evcar.service.LoginService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfo extends AppCompatActivity {
    TextView user_id;
    Member loginMember;
    DBManager dbManager;
    EditText user_pw;
    EditText user_pw2;
    EditText user_phone;
    EditText user_location;
    EditText user_email;
    Button complete_modify;
    Button cancel_modify;
    Button btn_del;
    LoginService loginService = LoginService.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        user_id = findViewById(R.id.user_id);
        user_pw = findViewById(R.id.user_pw);
        user_pw2 = findViewById(R.id.user_pw2);
        user_phone = findViewById(R.id.user_phone);
        user_location = findViewById(R.id.user_location);
        user_email = findViewById(R.id.user_email);
        complete_modify = findViewById(R.id.complete_modify);
        cancel_modify = findViewById(R.id.cancel_modify);
        btn_del = findViewById(R.id.btn_del);

        dbManager = new DBManager(UserInfo.this,"EVCarDB.db",null,1);

        loginMember = LoginService.getInstance().getLoginMember();

        final String tmp_id = loginMember.getMem_id();
        user_phone.setText(loginMember.getPhone());
        user_location.setText(loginMember.getLoaction());
        user_email.setText(loginMember.getEmail());

        user_id.setText(tmp_id);
       btn_del.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserInfo.this);
               alertDialog.setTitle("경고");
               alertDialog.setMessage("이 계정의 정보가 모두 지워집니다.\n정말 탈퇴 하시겠습니까?");
               alertDialog.setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       String mem_id = loginMember.getMem_id();
                      /* Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().deleteMember_json(mem_id);
                       observ.enqueue(new Callback<Void>() {
                           @Override
                           public void onResponse(Call<Void> call, Response<Void> response) {
                               if (response.isSuccessful()){
                                   int pkId = (int)(long)loginMember.getId();
                                   dbManager.deleteMember(pkId);
                                   dbManager.deleteMemberMyCar((int)(long)loginMember.getId());
                                   restart();
                                   Toast.makeText(UserInfo.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();
                               }
                           }

                           @Override
                           public void onFailure(Call<Void> call, Throwable t) {

                           }

                       });
                       */
                       int pkId = (int)(long)loginMember.getId();
                       dbManager.deleteMember(pkId);
                       dbManager.deleteMemberMyCar((int)(long)loginMember.getId());
                       restart();
                       Toast.makeText(UserInfo.this, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();

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


        cancel_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        complete_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tmp_pw = user_pw.getText().toString();
                final String tmp_pw2 = user_pw2.getText().toString();
                final String tmp_phone = user_phone.getText().toString();
                final String tmp_location = user_location.getText().toString();
                final String tmp_email = user_email.getText().toString();


                if(!tmp_pw.equals(tmp_pw2)){
                    Toast.makeText(UserInfo.this, "비밀번호 불일치", Toast.LENGTH_SHORT).show();

                }else{

                    final String mem_id = loginMember.getMem_id();
                    final String mem_pw = user_pw2.getText().toString();
                    final  String phone = user_phone.getText().toString();
                    final  String loaction =  user_location.getText().toString();
                    final    String email = user_email.getText().toString();


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserInfo.this);
                    alertDialog.setTitle("경고");
                    alertDialog.setMessage("정말 수정 하시겠습니까?");
                    alertDialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            /*Call<Void> observ = RetrofitService.getInstance().getRetrofitRequest().updateMemberInfo_json(mem_id,mem_pw,phone,loaction,email);
                            observ.enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()){

                                        dbManager.updateMemberInfo(tmp_id, tmp_pw, tmp_phone, tmp_location, tmp_email);

                                        loginService.setLoginMember(dbManager.getMemberInfo(tmp_id, tmp_pw));

                                        Intent intent = new Intent(UserInfo.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {

                                }
                            });
                            */
                            dbManager.updateMemberInfo(tmp_id, tmp_pw, tmp_phone, tmp_location, tmp_email);

                            loginService.setLoginMember(dbManager.getMemberInfo(tmp_id, tmp_pw));
                            Toast.makeText(UserInfo.this, "수정 되었습니다.", Toast.LENGTH_SHORT).show();

                        }
                    });
                    alertDialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();


                }
            }
        });






    }


    public void restart() {
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}
