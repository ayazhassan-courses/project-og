package com.roveapps.hScheduler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class CachingService {

    private CachingService(){}
    static CachingService shared = new CachingService();

    public Boolean saveHTMLData(Document doc, String filename){
        filename = filename.replace("/","-");
        String data = doc.html();
        try{
        PrintWriter printWriter = new PrintWriter(filename+".html");
        printWriter.println(data);
        printWriter.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

   public Document cacheAvailable(String filename){
        filename = filename.replace("/","-");
        File cached = new File(filename+".html");
        if (cached.exists()){
            try {
                Document doc = Jsoup.parse(cached,null);
                return doc;
            }catch (IOException e){
                e.printStackTrace();
            }

        }


        return  null;
   }

}
