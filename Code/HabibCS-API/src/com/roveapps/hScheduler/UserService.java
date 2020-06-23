package com.roveapps.hScheduler;

public class UserService {
    public Boolean isUserLoggedIn(String loginPageBody){
        return loginPageBody.contains("var  totalTimeoutMilliseconds = 1200000;");
    }
}
