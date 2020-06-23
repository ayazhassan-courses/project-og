package com.roveapps.hScheduler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NetworkingService {


    public static NetworkingService shared = new NetworkingService();
    UserService userService = new UserService();

    Map<String,String> cookies;
    private String username;
    private String password;



    private NetworkingService(){}

    public Boolean isConnectionEstablished(){
        return cookies != null;
    }

    public void setCredentials(String username,String password){
        this.username = username;
        this.password = password;
    }





    private Boolean establishConnection(){
        HashMap<String,String> postData = new HashMap<String,String>();


        //HTTP Post request
        postData.put("timezoneOffset","-300");
        postData.put("ptmode","f");
        postData.put("ptlang","ENG");
        postData.put("userid",username);
        postData.put("pwd",password);
        postData.put("ptlansel","ENG");
        postData.put("Submit","Login");
        postData.put("ptinstalledlang","ENG");

        Connection.Response response = request(Connection.Method.POST,postData,MyConstants.loginURL,null);
        String responseDoc = response.body();
        cookies = response.cookies();

        if (responseDoc != null){
            return userService.isUserLoggedIn(responseDoc);

        }
        return false;



    }


    public Document getCourses(String major){

        Document cached = CachingService.shared.cacheAvailable("courseList"+major);
        if (cached != null){
            return cached;
        }


        if(!isConnectionEstablished()){
            establishConnection();
        }

        //System.out.println(cookies);
        HashMap<String,String> postData = new HashMap<>();

        postData.put("ICAction","CLASS_SRCH_WRK2_SSR_PB_CLASS_SRCH");

        postData.put("ICSID",getToken());
        postData.put("ICAGTarget","true");
        postData.put("ICActionPrompt","false");
        postData.put("ICTypeAheadID","");
        postData.put("ICBcDomData","UnknownValue");
        postData.put("ICPanelHelpUrl","");
        postData.put("ICPanelName","");
        postData.put("ICFind","");
        postData.put("ICAddCount","");
        postData.put("ICAPPCLSDATA","");
        postData.put("DERIVED_SSTSNAV_SSTS_MAIN_GOTO$27$","9999");
        postData.put("CLASS_SRCH_WRK2_INSTITUTION$31$","HUNIV");
        postData.put("CLASS_SRCH_WRK2_STRM$35$","2015");
        postData.put("SSR_CLSRCH_WRK_SUBJECT_SRCH$0",major);
        postData.put("SSR_CLSRCH_WRK_SSR_EXACT_MATCH1$1","E");
        postData.put("SSR_CLSRCH_WRK_CATALOG_NBR$1","");
        postData.put("SSR_CLSRCH_WRK_ACAD_CAREER$2","UGRD");
        postData.put("SSR_CLSRCH_WRK_SSR_OPEN_ONLY$chk$3","N");
        postData.put("SSR_CLSRCH_WRK_OEE_IND$chk$4","N");
        postData.put("ptus_defaultlocalnode","PSFT_HR");
        postData.put("ptus_dbname","HRCSX");
        postData.put("ptus_portal","EMPLOYEE");
        postData.put("ptus_node","SA");
        postData.put("ptus_workcenterid","");
        postData.put("ptus_componenturl","https://pscs.habib.edu.pk/psp/ps/EMPLOYEE/SA/c/SA_LEARNER_SERVICES.CLASS_SEARCH.GBL");

        try {
            Connection.Response response = request(Connection.Method.POST,postData,MyConstants.courseSearchURL,cookies);
            Document doc = response.parse();
            CachingService.shared.saveHTMLData(doc,"courseList"+major);
            return doc;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    private String getToken(){
        if(!isConnectionEstablished()){
            establishConnection();
        }

        try{
        Connection.Response response = Jsoup.connect(MyConstants.courseSearchURL).cookies(cookies).execute();
        Element buddynameInput = response.parse().select("input[name=ICSID]").first();

        return buddynameInput.val();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }



    private Connection.Response request(Connection.Method method, HashMap<String,String> postData, String URL, Map<String,String> cookies){

        try{
            Connection connection = Jsoup.connect(URL).data(postData).method(method);
            if (cookies != null){
                connection =  connection.cookies(cookies);
            }
            Connection.Response response = connection.execute();


            return response;

        }catch (IOException e){
            System.out.println("An error occurred establishing connection.");
            System.out.println(e);
            return null;
        }
    }





}
