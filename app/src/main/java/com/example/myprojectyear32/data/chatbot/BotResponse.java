package com.example.myprojectyear32.data.chatbot;

import android.content.Context;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.data.mqtt.MQTTPublisher;
import com.example.myprojectyear32.session.SessionManager;

import java.util.HashMap;
import java.util.Random;

public class BotResponse {

    private static String message;
    private Boolean state;

    public BotResponse(String _message){
        message = _message.toLowerCase();
    }

    public static String basicResponses(Context context) {
        SessionManager session = new SessionManager(context);
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String connect = userDetails.get(SessionManager.KEY_LASTNAME);
        final int min = 0;
        final int max = 2;
        final int random = new Random().nextInt((max - min) + 1) + min;
//        String message = _message.toLowerCase();
        String result;
        String outmessage;
        switch(random) {
            case 0:
                outmessage = "Tôi không hiểu...";
                break;
            case 1:
                outmessage = "Thử nói cái gì khác";
                break;
            case 2:
                outmessage = "Hmmmm! Bạn có thể hỏi lại được không?";
                break;
            default:
                outmessage = "error";
        }
        if (message.contains("hello")) {
            switch (random) {
                case 0:
                    outmessage = "Hello there!";
                    break;
                case 1:
                    outmessage = "Sup";
                    break;
                case 2:
                    outmessage = "Ey yo!";
                    break;
                default:
                    outmessage = "error";
            }
        }

        if (message.contains("bật")&&message.contains("đèn")) {
//
            if(message.contains("phòng")){
                outmessage = "Đang bật đèn..";


//                MQTTPublisher.Connect(context, "192.168.1.200:1883");
                MQTTPublisher.Publisher("testpub");
//                MQTTPublisher.MessageOutput();
//                MQTTPublisher.Subcriber("android_receiver");

            }
            else {
                outmessage = "Bạn muốn bật đèn phòng nào ?";
                //Do sth
            }
        }
        // Turn on the fan
        if (message.contains("bật")&&message.contains("quạt")) {

            if(message.contains("phòng")){
                outmessage = "Đang bật quạt..";


                MQTTPublisher.Publisher("testpub");

            }
            else {
                outmessage = "Bạn muốn bật quạt phòng nào ?";
                //Do sth
            }
        }
        // Open the door
        if (message.contains("mở")&&message.contains("cửa")) {
                    if(message.contains("phòng")){
                        outmessage = "Đang mở cửa..";


                        MQTTPublisher.Publisher("testpub");

                    }
                    else {
                        outmessage = "Bạn muốn mở cửa phòng nào ?";
                        //Do sth
                    }
                }

        if (message.contains("flip") && message.contains("coin")) {
            result = random == 0 ? "heads" : "tails";
            outmessage = "I flipped a coin and it landed on " + result;
        }
        if (message.contains("hello")) {
            switch (random) {
                case 0:
                    outmessage = "Hello there!";
                    break;
                case 1:
                    outmessage = "Sup";
                    break;
                case 2:
                    outmessage = "Buongiorno!";
                    break;
                default:
                    outmessage = "error";
            }


//            } else {

//            }
            return outmessage;
        }
        if(message.contains("how are you")) {
            switch (random) {
                case 0:
                    outmessage = "I'm doing fine, thanks!";
                    break;
                case 1:
                    outmessage = "I'm hungry...";
                    break;
                case 2:
                    outmessage = "Pretty good! How about you?";
                    break;
                default:
                    outmessage = "error";
            }
        }
        if (message.contains("open") && message.contains("google")) {
            outmessage = "Opening Google...";
        }
        return outmessage;
    }
}
