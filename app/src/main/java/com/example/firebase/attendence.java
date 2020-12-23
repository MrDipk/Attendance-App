package com.example.firebase;


public class attendence {
    public String id, dt_login,dt_logout ;
    public Double co_lt_login,co_ln_login,co_lt_logout,co_ln_logout;

    public  attendence(){

    }
    public  attendence(String id,String dt_login,String dt_logout,Double co_lt_login,Double co_ln_login,Double co_lt_logout,Double co_ln_logout)
    {
        this.id=id;
        this.dt_login=dt_login;
        this.dt_logout=dt_logout;
        this.co_lt_login=co_lt_login;
        this.co_ln_login=co_ln_login;
        this.co_ln_logout=co_ln_logout;
        this.co_lt_logout=co_lt_logout;
    }
}


//
//