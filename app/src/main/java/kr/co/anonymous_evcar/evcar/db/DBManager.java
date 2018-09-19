package kr.co.anonymous_evcar.evcar.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.data.CarsData;
import kr.co.anonymous_evcar.evcar.data.Member;
import kr.co.anonymous_evcar.evcar.data.MyCar;
import kr.co.anonymous_evcar.evcar.data.MyCarCost;
import kr.co.anonymous_evcar.evcar.data.MyCarPush;


public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context){
        super(context, "EVCarDB.db", null, 1);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String  query = "CREATE TABLE Member(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT ,mem_id TEXT, mem_pw TEXT, gender INTEGER, phone TEXT, location TEXT, email TEXT);";
        db.execSQL(query);
        query = "CREATE TABLE BuyHistory(pk INTEGER PRIMARY KEY AUTOINCREMENT, login_id TEXT, time INTEGER, detailStr TEXT, goodsPrice TEXT, year INTEGER,month INTEGER,day INTEGER);";
        db.execSQL(query);
        query = "CREATE TABLE Location(pk INTEGER PRIMARY KEY AUTOINCREMENT, login_id TEXT, address TEXT, latitude REAL, longitude REAL, year INTEGER,month INTEGER,day INTEGER, time INTEGER);";
        db.execSQL(query);
        query = "CREATE TABLE CarsData(pk INTEGER PRIMARY KEY AUTOINCREMENT, mycarname TEXT, price INTEGER , mileage DOUBLE, images INTEGER,makingcategory INTEGER, page TEXT);";
        db.execSQL(query);
        query = "CREATE TABLE MyCar(pk INTEGER PRIMARY KEY AUTOINCREMENT,nickname TEXT,member_pk INTEGER, carsdata_pk INTEGER , distance INTEGER, divison INTEGER,oil_dis INTEGER,coolant_dis INTEGER,tire_dis INTEGER,wiper_dis LONG);";
        db.execSQL(query);
        query = "CREATE TABLE MyCarCost(pk INTEGER PRIMARY KEY AUTOINCREMENT,MyCar_fk INTEGER,partCategory  INTEGER, cost INTEGER , hipassCost INTEGER, partTotalCost INTEGER,hipassTotalCost INTEGER,year INTEGER,month INTEGER);";
        db.execSQL(query);
        query = "CREATE TABLE MyCarPush(pk INTEGER PRIMARY KEY AUTOINCREMENT, MyCar_fk TEXT, oil_division INTEGER, coolant_division INTEGER, tire_division INTEGER, wiper_division INTEGER);";
        db.execSQL(query);


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public MyCarPush setMyCarPushOilDiv(Integer MyCar_fk,Integer oil_division){
        MyCarPush myCarPush = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarPush set oil_division = '"+oil_division+"' where MyCar_fk='" + MyCar_fk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer pk = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer toil_division = cursor.getInt(2);
            Integer coolant_division = cursor.getInt(3);
            Integer tire_division = cursor.getInt(4);
            Integer wiper_division = cursor.getInt(5);
            myCarPush = new MyCarPush(pk, tMyCar_fk, toil_division, coolant_division, tire_division, wiper_division);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return myCarPush;
    }

    public boolean login(String id,String pw) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "select Count(*) from Member where mem_id='" + id + "' and mem_pw='" + pw + "';";
        Integer count = 0;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            count = cursor.getInt(0);

            if (count > 0) {
                return true;
            }
        }
        cursor.close();

        return false;
    }

    public boolean idCheck(String id){
        SQLiteDatabase db = getReadableDatabase();
        String query = "select Count(*) from Member where mem_id='" + id + "';";
        Integer count = 0;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            count = cursor.getInt(0);

            if (count > 0) {
                return true;
            }
        }
        cursor.close();

        return false;

    }
    public void insertMember(String name, String mem_id ,String mem_pw, Integer gender, String phone, String loaction, String email){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "Insert into Member values (null,'" +name+"', '"+mem_id+"' , '"+mem_pw+"', '"+gender+"', '"+phone+"' , '"+loaction+"', '"+email+"');";
        db.execSQL(query);
    }

    public void updateMemberInfo(String id, String pw, String phone, String location, String email){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "update Member set mem_pw='"+pw+"', phone ='"+ phone+"' , location='"+location+"', email = '"+email+ "';"+
                "where mem_id=id";
        db.execSQL(query);


    }

    public void deleteMember(int param_pk){
        SQLiteDatabase db = getReadableDatabase();

        String query = "delete from Member where id = "+param_pk+";";
        db.execSQL(query);

    }
    public Member getMemberInfo(String login_id, String login_pw) {

        Member member = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from Member where mem_id='" + login_id + "' and mem_pw='" + login_pw + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Long pk = cursor.getLong(0);
            String name = cursor.getString(1);
            String mem_id = cursor.getString(2);
            String mem_pw = cursor.getString(3);
            Integer gender = cursor.getInt(4);
            String phone = cursor.getString(5);
            String location = cursor.getString(6);
            String email = cursor.getString(7);

            member = new Member(pk, name, mem_id, mem_pw, gender, phone, location,email);

        }
        cursor.close();

        return member;
    }



    public void insertCarsData(String mycarname, Integer price ,Double mileage,Integer images, Integer makingcategory, String page){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "Insert into CarsData values (null,'" +mycarname+"', '"+price+"' , '"+mileage+"', '"+images+"', '"+makingcategory+"' , '"+page+"');";
        db.execSQL(query);
    }


    public ArrayList<CarsData> getCarsDatas(){
        ArrayList<CarsData> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from CarsData ";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String mycarname = cursor.getString(1);
            Integer price = cursor.getInt(2);
            Double mileage = cursor.getDouble(3);
            Integer images = cursor.getInt(4);
            Integer makingcategory = cursor.getInt(5);
            String page = cursor.getString(6);

            CarsData item = new CarsData(ID,mycarname,price,mileage,images,makingcategory,page);
            items.add(item);
        }
        cursor.close();
        return items;
    }

    public ArrayList<CarsData> getCarsListDatas(Integer makingcategory){
        ArrayList<CarsData> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from CarsData where makingcategory = "+makingcategory+";";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String mycarname = cursor.getString(1);
            Integer price = cursor.getInt(2);
            Double mileage = cursor.getDouble(3);
            Integer images = cursor.getInt(4);
            Integer mkcategory = cursor.getInt(5);
            String page = cursor.getString(6);

            CarsData item = new CarsData(ID,mycarname,price,mileage,images,mkcategory,page);
            items.add(item);
        }
        cursor.close();
        return items;
    }


    public void insertMyCar(String nickname,Integer member_pk, Integer carsdata_pk ,Integer distance,Integer divison,Integer oil_dis ,Integer coolant_dis ,Integer tire_dis ,long wiper_dis ){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "Insert into MyCar values (null,'"+nickname+"','" +member_pk+"', '"+carsdata_pk+"' , '"+distance+"', '"+divison+"' , '"+oil_dis+"', '"+coolant_dis+"', '"+tire_dis+"', '"+wiper_dis+"');";
        db.execSQL(query);
    }

    public MyCar getMyCarUserInfo(Integer member_fk,Integer division){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from MyCar where member_pk='" + member_fk + "' and divison = "+division+" ;";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String nickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            item = new MyCar(ID,nickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }

    public CarsData getCarData(Integer carsdata_pk){
        CarsData item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from CarsData where pk='" + carsdata_pk + "' ;";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String mycarname = cursor.getString(1);
            Integer price = cursor.getInt(2);
            Double mileage = cursor.getDouble(3);
            Integer images = cursor.getInt(4);
            Integer mkcategory = cursor.getInt(5);
            String page = cursor.getString(6);

            item = new CarsData(ID,mycarname,price,mileage,images,mkcategory,page);
        }
        cursor.close();

        return item;
    }

    public ArrayList<MyCar>getMyCarList(Integer member_fk){
        ArrayList<MyCar> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from MyCar where member_pk = "+member_fk+";";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String nickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            MyCar item = new MyCar(ID,nickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public ArrayList<MyCar>getMyCarAllList(){
        ArrayList<MyCar> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from MyCar ";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String nickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            MyCar item = new MyCar(ID,nickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public ArrayList<MyCar>reSetMyCar(Integer member_fk,Integer zeronumber){
        ArrayList<MyCar> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "update MyCar set divison = '"+ zeronumber +"'; where member_pk = '"+member_fk+"'";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String nickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            MyCar item = new MyCar(ID,nickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);

            items.add(item);
        }
        cursor.close();
        return items;
    }

    public void deleteMyCar(int Mycar_fk){
        SQLiteDatabase db = getReadableDatabase();

        String query = "delete from MyCar where pk = "+Mycar_fk+";";
        db.execSQL(query);
    }

    public void deleteMemberMyCar(int mem_fk){
        SQLiteDatabase db = getReadableDatabase();

        String query = "delete from MyCar where member_pk = "+mem_fk+";";
        db.execSQL(query);
    }

    public CarsData getTmpChangeCarsData(String mycarname){
        CarsData item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from CarsData where mycarname='" + mycarname + "' ;";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String mycarnames = cursor.getString(1);
            Integer price = cursor.getInt(2);
            Double mileage = cursor.getDouble(3);
            Integer images = cursor.getInt(4);
            Integer mkcategory = cursor.getInt(5);
            String page = cursor.getString(6);

            item = new CarsData(ID,mycarnames,price,mileage,images,mkcategory,page);
        }
        cursor.close();

        return item;
    }


    public ArrayList<MyCar>SetMyCar(Integer member_fk,Integer carsdata_fk,Integer number){
        ArrayList<MyCar> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "update MyCar set divison = '"+ number +"' where member_pk = '"+member_fk+"'and carsdata_pk = '"+carsdata_fk+"';";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String nickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            MyCar item = new MyCar(ID,nickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);

            items.add(item);
        }
        cursor.close();
        return items;
    }



    public MyCar setMyCarNickname(Integer mycarfk,String nickname){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set nickname = '"+nickname+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            item = new MyCar(ID,tnickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }

    public MyCar setMyCarDistance(Integer mycarfk,Integer distance){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set distance = '"+distance+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer tdistance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            Long wiper_dis = cursor.getLong(9);


             item = new MyCar(ID,tnickname,member_pks,carsdata_pk,tdistance,divisions,oil_dis,coolant_dis,tire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }


    public MyCar setMyCarOil_dis(Integer mycarfk,Integer oil_dis){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set oil_dis = '"+oil_dis+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer toil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            item = new MyCar(ID,tnickname,member_pks,carsdata_pk,distance,divisions,toil_dis,coolant_dis,tire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }

    public MyCar setMyCarCoolant_dis(Integer mycarfk,Integer coolant_dis){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set coolant_dis = '"+coolant_dis+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer tcoolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            item = new MyCar(ID,tnickname,member_pks,carsdata_pk,distance,divisions,oil_dis,tcoolant_dis,tire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }
    public MyCar setMyCarTire_dis(Integer mycarfk,Integer tire_dis){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set tire_dis = '"+tire_dis+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer ttire_dis = cursor.getInt(8);
            long wiper_dis = cursor.getLong(9);


            item = new MyCar(ID,tnickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,ttire_dis,wiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }
    public MyCar setMyCarWiper_dis(Integer mycarfk,long wiper_dis){
        MyCar item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCar set wiper_dis = '"+wiper_dis+"' where pk='" + mycarfk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            String tnickname = cursor.getString(1);
            Integer member_pks = cursor.getInt(2);
            Integer carsdata_pk = cursor.getInt(3);
            Integer distance = cursor.getInt(4);
            Integer divisions = cursor.getInt(5);
            Integer oil_dis = cursor.getInt(6);
            Integer coolant_dis = cursor.getInt(7);
            Integer tire_dis = cursor.getInt(8);
            long twiper_dis = cursor.getLong(9);


            item = new MyCar(ID,tnickname,member_pks,carsdata_pk,distance,divisions,oil_dis,coolant_dis,tire_dis,twiper_dis);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }
    public void insertMyCarCostPart(Integer MyCar_fk, Integer partCategory, Integer cost, Integer hipassCost, Integer partTotalCost,Integer hipassTotalCost,Integer year,Integer month){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "Insert into MyCarCost values (null,'" +MyCar_fk+"', '"+ partCategory+ "', '"+ cost+ "', '"+ hipassCost+ "', '"+ partTotalCost+ "', '"+ hipassTotalCost+ "', '"+ year+ "', '"+ month+ "');";
        db.execSQL(query);
    }

    public ArrayList<MyCarCost>getMyCarCostPartAll(Integer MyCar_fk,Integer year, Integer month){
        ArrayList<MyCarCost> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from MyCarCost where MyCar_fk = '"+MyCar_fk+"'and year = '"+year+"'and month = '"+month+"';";

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer partCategory = cursor.getInt(2);
            Integer cost = cursor.getInt(3);
            Integer hipassCost = cursor.getInt(4);
            Integer partTotalCost = cursor.getInt(5);
            Integer hipassTotalCost = cursor.getInt(6);
            Integer tyear = cursor.getInt(7);
            Integer tmonth = cursor.getInt(8);

            MyCarCost item = new MyCarCost(ID,tMyCar_fk,partCategory,cost,hipassCost,partTotalCost,hipassTotalCost,tyear,tmonth);

            items.add(item);
        }
        cursor.close();
        return items;
    }


    public MyCarCost setMyCarCostPartTotal(Integer MyCar_fk,Integer partTotalCost,Integer year, Integer month){
        MyCarCost item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarCost set partTotalCost = '"+partTotalCost+"'  where MyCar_fk = '"+MyCar_fk+"'and year = '"+year+"'and month = '"+month+"';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer partCategory = cursor.getInt(2);
            Integer cost = cursor.getInt(3);
            Integer hipassCost = cursor.getInt(4);
            Integer tpartTotalCost = cursor.getInt(5);
            Integer hipassTotalCost = cursor.getInt(6);
            Integer tyear = cursor.getInt(7);
            Integer tmonth = cursor.getInt(8);

            item = new MyCarCost(ID,tMyCar_fk,partCategory,cost,hipassCost,tpartTotalCost,hipassTotalCost,tyear,tmonth);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }

    public MyCarCost setMyCarCostHipassTotal(Integer MyCar_fk,Integer hipassTotalCost,Integer year, Integer month){
        MyCarCost item = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarCost set hipassTotalCost = '"+hipassTotalCost+"'  where MyCar_fk = '"+MyCar_fk+"'and year = '"+year+"'and month = '"+month+"';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer ID = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer partCategory = cursor.getInt(2);
            Integer cost = cursor.getInt(3);
            Integer hipassCost = cursor.getInt(4);
            Integer partTotalCost = cursor.getInt(5);
            Integer thipassTotalCost = cursor.getInt(6);
            Integer tyear = cursor.getInt(7);
            Integer tmonth = cursor.getInt(8);

            item = new MyCarCost(ID,tMyCar_fk,partCategory,cost,hipassCost,partTotalCost,thipassTotalCost,tyear,tmonth);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return item;
    }

    public void insertMyCarPush(Integer MyCar_fk, Integer oil_division ,Integer coolant_division,Integer tire_division, Integer wiper_division){
        SQLiteDatabase db = getReadableDatabase();
        String  query = "Insert into MyCarPush values (null,'" +MyCar_fk+"', '"+oil_division+"' , '"+coolant_division+"', '"+tire_division+"', '"+wiper_division+"');";
        db.execSQL(query);
    }

    public boolean MyCarPushCheck(Integer MyCar_fk){
        SQLiteDatabase db = getReadableDatabase();
        String query = "select Count(*) from MyCarPush where MyCar_fk='" + MyCar_fk + "';";
        Integer count = 0;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            count = cursor.getInt(0);

            if (count > 0) {
                return true;
            }
        }
        cursor.close();

        return false;

    }

    public MyCarPush getMyCarPush(Integer MyCar_fk) {

        MyCarPush myCarPush = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "select * from MyCarPush where MyCar_fk='" + MyCar_fk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer pk = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer oil_division = cursor.getInt(2);
            Integer coolant_division = cursor.getInt(3);
            Integer tire_division = cursor.getInt(4);
            Integer wiper_division = cursor.getInt(5);
            myCarPush = new MyCarPush(pk, tMyCar_fk, oil_division, coolant_division, tire_division, wiper_division);

        }
        cursor.close();

        return myCarPush;
    }
    public MyCarPush setMyCarPushCoolantDiv(Integer MyCar_fk,Integer coolant_division){
        MyCarPush myCarPush = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarPush set coolant_division = '"+coolant_division+"' where MyCar_fk='" + MyCar_fk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer pk = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer oil_division = cursor.getInt(2);
            Integer tcoolant_division = cursor.getInt(3);
            Integer tire_division = cursor.getInt(4);
            Integer wiper_division = cursor.getInt(5);
            myCarPush = new MyCarPush(pk, tMyCar_fk, oil_division, tcoolant_division, tire_division, wiper_division);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return myCarPush;
    }
    public MyCarPush setMyCarPushTireDiv(Integer MyCar_fk,Integer tire_division){
        MyCarPush myCarPush = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarPush set tire_division = '"+tire_division+"' where MyCar_fk='" + MyCar_fk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer pk = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer oil_division = cursor.getInt(2);
            Integer coolant_division = cursor.getInt(3);
            Integer ttire_division = cursor.getInt(4);
            Integer wiper_division = cursor.getInt(5);
            myCarPush = new MyCarPush(pk, tMyCar_fk, oil_division, coolant_division, ttire_division, wiper_division);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return myCarPush;
    }
    public MyCarPush setMyCarPushWiperDiv(Integer MyCar_fk,Integer wiper_division){
        MyCarPush myCarPush = null;
        SQLiteDatabase db = getReadableDatabase();

        String query = "update  MyCarPush set wiper_division = '"+wiper_division+"' where MyCar_fk='" + MyCar_fk + "';";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToNext()) {
            Integer pk = cursor.getInt(0);
            Integer tMyCar_fk = cursor.getInt(1);
            Integer oil_division = cursor.getInt(2);
            Integer coolant_division = cursor.getInt(3);
            Integer tire_division = cursor.getInt(4);
            Integer twiper_division = cursor.getInt(5);
            myCarPush = new MyCarPush(pk, tMyCar_fk, oil_division, coolant_division, tire_division, twiper_division);
        }else {
            cursor.close();

            return null;
        }
        cursor.close();


        return myCarPush;
    }





}
