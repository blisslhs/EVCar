package kr.co.anonymous_evcar.evcar.service;


import kr.co.anonymous_evcar.evcar.data.Member;

public class LoginService {
    private static LoginService curr = null;
    private Member loginMember;

    public static LoginService getInstance(){
        if(curr==null){
            curr = new LoginService();
        }
        return curr;
    }

    private LoginService(){

    }

    public void logOut(){
        loginMember = null;
    }
    public Member getLoginMember(){
        return loginMember;
    }
    public void setLoginMember(Member loginMember){

        this.loginMember = loginMember;
    }




}
