package com.example.myprojectyear32.data.chatbot;

import android.content.Context;
import android.os.Handler;

import com.example.myprojectyear32.data.mqtt.MQTTPublisher;
import com.example.myprojectyear32.session.SessionManager;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class BotResponse {

    private static String message;


    public BotResponse(String _message){
        message = _message.toLowerCase();
    }

    public static String basicResponses(Context context) {
        SessionManager session = new SessionManager(context);
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String doorStatus = userDetails.get(SessionManager.KEY_DOORLR);
        int maxID = 0;
        final int min = 0;
        final int max = 2;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String result;
        AtomicReference<String> outmessage = null;
        switch(random) {
            case 0:
                outmessage.set("Tôi không hiểu...");
                break;
            case 1:
                outmessage.set("Thử nói cái gì khác");
                break;
            case 2:
                outmessage.set("Hmmmm! Bạn có thể hỏi lại được không?");
                break;
            default:
                outmessage.set("error");
        }
        if (message.contains("hello")) {
            switch (random) {
                case 0:
                    outmessage.set("Hello there!");
                    break;
                case 1:
                    outmessage.set("Sup");
                    break;
                case 2:
                    outmessage.set("Ey yo!");
                    break;
                default:
                    outmessage.set("error");
            }
        }

        if (message.contains("bật")&&message.contains("đèn")) {
//
            if(message.contains("phòng")){
                    outmessage.set("Đang bật đèn..");
                    MQTTPublisher.Connect(context, "192.168.1.200:1883");
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("led1");
                    },1000);
            }
            else {
                outmessage.set("Bạn muốn bật đèn phòng nào ?");
                //Do sth
            }
        }
        if (message.contains("nhiệt")&&message.contains("độ")) {

            if(message.contains("phòng")){

                MQTTPublisher.Connect(context, "192.168.1.200:1883");
//                MQTTPublisher.Subcriber("living");
//                MQTTPublisher.MessageOutput();
//                MQTTPublisher.Publisher("sensor");
                outmessage.set("Đang lấy dữ liệu..");
                new Handler().postDelayed(() -> {
                    //do sth
                    MQTTPublisher.Subcriber("living");
                    MQTTPublisher.Publisher("sensor");
                    MQTTPublisher.MessageOutput();
                    new Handler().postDelayed(() -> {
                        String message = MQTTPublisher.msg;
                        if(message.contains("Temp")){
                            outmessage.set(message);
                        }
                    },6000);
                },1000);
            }
            else {
                outmessage.set("Bạn muốn biết nhiệt độ phòng nào ?");
                //Do sth
            }
        }
        // Open the door
        if (message.contains("mở")&&message.contains("cửa")) {
                    if(message.contains("phòng")){
                            outmessage.set("Đang mở cửa..");
                            MQTTPublisher.Connect(context, "192.168.1.200:1883");
                            new Handler().postDelayed(() -> {
                                //do sth
                                MQTTPublisher.Publisher("door");
                            },1000);
                    }
                    else {
                        outmessage.set("Bạn muốn mở cửa phòng nào ?");
                        //Do sth
                    }
                }

        if (message.contains("flip") && message.contains("coin")) {
            result = random == 0 ? "heads" : "tails";
            outmessage.set("I flipped a coin and it landed on " + result);
        }
        if (message.contains("hello")) {
            switch (random) {
                case 0:
                    outmessage.set("Hello there!");
                    break;
                case 1:
                    outmessage.set("Sup");
                    break;
                case 2:
                    outmessage.set("Buongiorno!");
                    break;
                default:
                    outmessage.set("error");
            }


//            } else {

//            }
            return outmessage.get();
        }
        if(message.contains("how are you")) {
            switch (random) {
                case 0:
                    outmessage.set("I'm doing fine, thanks!");
                    break;
                case 1:
                    outmessage.set("I'm hungry...");
                    break;
                case 2:
                    outmessage.set("Pretty good! How about you?");
                    break;
                default:
                    outmessage.set("error");
            }
        }
        if (message.contains("open") && message.contains("google")) {
            outmessage.set("Opening Google...");
        }
        return outmessage.get();
    }
}
