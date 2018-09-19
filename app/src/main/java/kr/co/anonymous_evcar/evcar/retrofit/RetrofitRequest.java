package kr.co.anonymous_evcar.evcar.retrofit;

import java.util.ArrayList;

import kr.co.anonymous_evcar.evcar.data.Member;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitRequest {


    @FormUrlEncoded
    @POST("join_json_ok.do")
    Call<Void>join_ok(@Field("id")String id,@Field("pw2")String pw,@Field("name")String name,@Field("gender")String gender,@Field("phone")String phone,@Field("location")String location,@Field("email")String email);

    @GET("getMemberList.do")
    Call<ArrayList<Member>> getMemberList();

    @FormUrlEncoded
    @POST("login_ok_json.do")
    Call<Void>login_ok_json(@Field("mem_id")String mem_id,@Field("mem_pw")String mem_pw);

    @FormUrlEncoded
    @POST("updateMemberInfo_json.do")
    Call<Void>updateMemberInfo_json(@Field("mem_id")String mem_id,@Field("mem_pw")String mem_pw,@Field("phone")String phone,@Field("loaction")String loaction,@Field("email")String email);

    @FormUrlEncoded
    @POST("deleteMember_json.do")
    Call<Void>deleteMember_json(@Field("mem_id")String mem_id);

}
