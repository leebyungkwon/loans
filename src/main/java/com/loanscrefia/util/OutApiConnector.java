package com.loanscrefia.util;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 외부 API 연동
 */
@Log4j
public class OutApiConnector {
    public OutApiConnector() {

    }

    public static class setApi {
        private String receiver;        //api 수신자
        private String excuteName;      //api 실행 명칭
        private String url="";          //api url
        private String parameter="";    //api param
        private String token="";        //api 인증 토큰
        private String method="";       //
        private Boolean outLog=false;       //
        private JSONObject parameterJson = new JSONObject();
        
        private String clientId="";
        private String clientSecret="";

        public setApi(String receiver, String excuteName) {
            this.receiver   = receiver;
            this.excuteName = excuteName;
        }

        public setApi url(String url){
            this.url = url;
            return this;
        }
        public setApi parameter(String parameter){
            this.parameter = parameter;
            return this;
        }
        public setApi parameterJson(JSONObject parameter){
            this.parameterJson = parameter;
            return this;
        }
        public setApi token(String token){
            this.token = "Bearer " + token;
            return this;
        }
        public setApi method(String method){
            this.method = method;
            return this;
        }
        public setApi clientId(String clientId){
            this.clientId = clientId;
            return this;
        }
        public setApi clientSecret(String clientSecret){
            this.clientSecret = clientSecret;
            return this;
        }

        public void requestInfo(){
            String str = "";
            str += "\n***************************************************************\n";
            str += "***********OutApiConnector = requestInfo***********\n";
            str += "receiver --> " + receiver + "\n";
            str += "excuteName --> " + excuteName + "\n";
            str += "token --> " + token + "\n";
            str += "method --> " + method + "\n";
            str += "url --> " + url + "\n";
            if("GET".equals(method))    str += "parameter --> " + parameter+  "\n";
            else                        str += "parameter --> " + parameterJson.toString() + "\n";
            log.info(str);
        }
        public void responseInfo(Response response) throws IOException {
            String str = "";
            str += "\n***************************************************************\n";
            str += "***********OutApiConnector = responseInfo***********\n";
            str += "header --> \n";
            str += response.headers()+"\n";
            str += "code --> \n";
            str += response.code()+"\n";
            str += "message --> \n";
            str += response.message()+"\n";
            //str += "body --> \n";
            //str += res.body().string()+"\n";
            str += "***************************************************************";
            log.info(str);
        }

        public Response call() throws JSONException, IOException {
            if(outLog) requestInfo();

            Response res = null;
            if(method.equals("GET"))    res = GET(this);
            else                        res = POST(this);

            if(outLog)  responseInfo(res);

            return res;
        }
    }

    private static Response GET(setApi b) throws IOException {
        Builder builder = new Builder()
                                .url(b.url + b.parameter)
                                .method(b.method, null);

        builder.addHeader("Content-Type", "application/json;charset=utf-8");
        builder.addHeader("Accept", "application/json;charset=utf-8");
        builder.addHeader("Authorization", b.token);

        return send(builder.build());
    }

    private static Response POST(setApi b) throws IOException {
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, b.parameterJson.toString());

        Builder builder = new Builder()
                                .url(b.url)
                                .method(b.method, body);
        builder.addHeader("Content-Type", "application/json;charset=utf-8");
        builder.addHeader("Accept", "application/json;charset=utf-8");
        builder.addHeader("Authorization", b.token);

        if("getAuthCode".equals(b.excuteName)) {
            builder.addHeader("X-Kfb-Client-Id", b.clientId);
            builder.addHeader("X-Kfb-User-Secret", b.clientSecret);
        }
        return send(builder.build());
    }

    private static Response send(Request request) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
