package kr.co.anonymous_evcar.evcar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;

public class CheckMember extends AppCompatActivity {
    @BindView(R.id.user_id1)
    TextView user_id1;
    @BindView(R.id.user_id2)
    TextView user_id2;
    @BindView(R.id.user_pw1)
    EditText user_pw1;
    @BindView(R.id.btn_cen)
    Button btn_cen;
    @BindView(R.id.btn_sel)
    Button btn_sel;
    @BindView(R.id.btn_logout)
    Button btn_logout;

    Member loginMember;
    DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_member);
        ButterKnife.bind(this);

        LoginService loginService = LoginService.getInstance();
        dbManager = new DBManager(CheckMember.this, "EVCarDB.db", null, 1);

        loginMember = dbManager.getMemberInfo(loginService.getLoginMember().getMem_id()
                , loginService.getLoginMember().getMem_pw());

        user_id1.setText(loginMember.getMem_id());
        user_id2.setText(loginMember.getMem_id());
    }

    @OnClick(R.id.btn_cen)
    public void cenClick(View view) {
        finish();
    }

    @OnClick(R.id.btn_sel)
    public void selClick(View view) {
        String id = user_id2.getText().toString();
        String pw = user_pw1.getText().toString();
        String userPw = loginMember.getMem_pw();
        if (pw.equals(userPw)) {
            Intent intent1 = new Intent(CheckMember.this, UserInfo.class);
            startActivity(intent1);
        } else {
            Toast.makeText(CheckMember.this, "비밀번호 오류", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_logout)
    public void logoutClick(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckMember.this);
        alertDialog.setTitle("경고");
        alertDialog.setMessage("정말 로그아웃 하시겠습니까?");
        alertDialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //SharedPreferences를 불러옵니다. 메인에서 만든 이름으로
                SharedPreferences appData = getSharedPreferences("appData", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = appData.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();

                restart();
                Toast.makeText(CheckMember.this, "로그아웃", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        user_pw1.setText("");

    }

    public void restart() {
        Intent i = getBaseContext().getPackageManager().
                getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}