package kr.co.anonymous_evcar.evcar.receiver;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;



import java.util.Calendar;

import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.db.DBManager;
import kr.co.anonymous_evcar.evcar.service.LoginService;


public class SmsReceiver extends BroadcastReceiver {
    DBManager dbManager;
    SQLiteDatabase sqlitedb;
    LoginService loginService = LoginService.getInstance();
    Member loginMember;
    MyCar myCar;


    @Override
    public void onReceive(Context context, Intent intent) {
        Integer i_price=0;
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            Object messages[] = (Object[]) bundle.get("pdus");

            SmsMessage smsMessage[] = new SmsMessage[messages.length];

            for (int i = 0; i < messages.length; i++) {
                String format = bundle.getString("format");
                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i], format);
            }

            String message = smsMessage[0].getMessageBody().toString();

            dbManager = new DBManager(context);

            loginMember = loginService.getLoginMember();

            myCar = dbManager.getMyCarUserInfo((int)(long)loginMember.getId(), 1);

            Log.d("yjs", "onReceive()실행");

            if (myCar != null) {

                if (message.contains("KB국민체크") && message.contains("충전소")) {
                    Log.d("yjs", "KB국민체크 파싱 실행");
                    String[] tmpStr = message.split("\n");
                    String[] tmpPrice = tmpStr[4].split("원");
                    String price = tmpPrice[0];
                    price = price.replace(",", "");
                    i_price = Integer.parseInt(price);


                } else if (message.contains("우리") && message.contains("충전소") && message.contains("체크승인")) {
                    String[] tmpStr = message.split("\n");
                    String tmpPrice = tmpStr[3];
                    String tmpDetail = tmpStr[5];

                } else if (message.contains("현대카드 M2")) {
                    String[] tmpStr = message.split("\n");
                    String[] tmpPrice = tmpStr[3].split("원");
                    String price = tmpPrice[0];
                    price = price.replace(",", "");
                    i_price = Integer.parseInt(price);


                }

                Calendar calendar = Calendar.getInstance();
                Integer year = calendar.get(Calendar.YEAR);
                Integer month = calendar.get(Calendar.MONTH);
                sqlitedb = dbManager.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("MyCar_fk", myCar.getPk());
                values.put("partCategory", 6);
                values.put("cost", i_price);
                values.put("year", year);
                values.put("month", month);

                long newRowId = sqlitedb.insert("MyCarCost", null, values);
                sqlitedb.close();
                dbManager.close();

                if (newRowId == -1) {
                    Log.d("yjh", "파싱 등록 실패");
                }

            }
        }

    }
}
