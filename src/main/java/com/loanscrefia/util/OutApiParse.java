package com.loanscrefia.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Response;

public class OutApiParse {

    public static int getCode(Response res){
        int code = res.code();
        return code;
    }

    public static String getData(Response res, String getStr) throws IOException {
        String ret = "";
        switch (getStr) {
            case "Location":
                String r = res.headers().get("Location");
                //System.out.println("#### getData["+getStr+"] 1 ==> " +r);
                ret = r.split("/")[r.split("/").length-1];
                break;
            default:
                ret = res.body().string();
                break;
        }
        //System.out.println("#### getData["+getStr+"] 2 ==> " +ret);
        return ret;
    }
    public static JSONObject getError(Response res) throws IOException, JSONException {
        String resBody = res.body().string();

        //System.out.println("#### getError ==> " + resBody);
        JSONObject json = null;
        if(resBody != null && !resBody.isEmpty()) json = new JSONObject(resBody);
        return json;
    }
}
