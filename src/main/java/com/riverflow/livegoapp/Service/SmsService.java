package com.riverflow.livegoapp.Service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

@Service
@AllArgsConstructor
public class SmsService {

    private final static String apiUrl        =    "https://sslsms.cafe24.com/sms_sender.php";
    private final static String userAgent    =    "Mozilla/5.0";
    private final static String charset        =    "UTF-8";
    private final static boolean isTest        =    true;
    private final static String apiId        =    "persona03";
    private final static String apiSecret        =    "ea7523a8fa061214b4848d5344478167";
    private final static String sendNum1  = "02";
    private final static String sendNum2  = "762";
    private final static String sendNum3  = "8713";

    public String sendSMSAsync(String to, String message, Boolean mms) {
        try{
            URL obj    = new URL(apiUrl);
            HttpsURLConnection con= (HttpsURLConnection) obj.openConnection();
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", userAgent);


            String postParams = "user_id="+base64Encode(apiId)
                    +"&secure="+base64Encode(apiSecret)
                    +"&rphone="+base64Encode(to)
                    +"&msg="+base64Encode(message)+"&sphone1="+base64Encode(sendNum1)+"&sphone2="+base64Encode(sendNum2)+"&sphone3="+base64Encode(sendNum3)
                    +"&mode="+base64Encode("1");
            if(mms) {
                postParams += "&smsType=L";
            } else postParams += "&smsType=S";

            System.out.println("params "+postParams);
            //For POST only    - START
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(postParams.getBytes());
            os.flush();
            os.close();
            //For POST only - END
            int responseCode = con.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){ // success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer buf  = new StringBuffer();
                while((inputLine=in.readLine())!=null){
                    buf.append(inputLine);
                }
                in.close();
                System.out.println("SMS :"+buf.toString());
                return buf.toString();

            }else{
                return "error";

            }

        }catch(IOException ex){
            System.out.println("SMS IOException:"+ex.getMessage());
            return "error";
        }

    }

    public static String base64Encode(String str)  throws java.io.IOException {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }
}
