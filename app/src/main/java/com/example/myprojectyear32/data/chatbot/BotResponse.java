package com.example.myprojectyear32.data.chatbot;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.mqtt.MQTTPublisher;
import com.example.myprojectyear32.data.notification.Notification;
import com.example.myprojectyear32.session.SessionManager;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class BotResponse {

    private static String message;


    public BotResponse(String _message){
        message = _message.toLowerCase();
    }

    public static String basicResponses(Context context) {
        SessionManager session = new SessionManager(context);
        HashMap<String,String> userDetails = session.getUserDetailFromSession();
        String doorStatus = userDetails.get(SessionManager.KEY_DOORLR);
        String lightStatus = userDetails.get(SessionManager.KEY_LIGHTINGLR);
        Notification notification;
        DatabaseReference notiReference = null, statusReference = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = dateFormat.format(calendar.getTime());
        int maxID = 0;
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
//                if(doorStatus.equals("True")){
                    outmessage = "Đang bật đèn..";

                    notification = new Notification();
                    notification.setDescription("Bật đèn phòng khách.");
                    notification.setImage(R.mipmap.lighting);
                    notification.setTime(date);


                    notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                    statusReference.child("lighting").setValue("True");

                    MQTTPublisher.Connect(context, "192.168.1.200:1883");
                    new Handler().postDelayed(() -> {
                        //do sth
                        MQTTPublisher.Publisher("led1");
                    },1000);
//                }
//                else{
//                    outmessage = "Đèn đã được mở";
//                }
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

            }
            else {
                outmessage = "Bạn muốn bật quạt phòng nào ?";
                //Do sth
            }
        }
        // Open the door
        if (message.contains("mở")&&message.contains("cửa")) {
                    if(message.contains("phòng")){
                        if(doorStatus.equals("True")){
                            outmessage = "Đang mở cửa..";

                            notification = new Notification();
                            notification.setDescription("Mở cửa phòng khách.");
                            notification.setImage(R.mipmap.security);

                            notification.setTime(date);

                            notiReference.child(String.valueOf(maxID + 1)).setValue(notification);
                            statusReference.child("door").setValue("True");
                            MQTTPublisher.Connect(context, "192.168.1.200:1883");
                            new Handler().postDelayed(() -> {
                                //do sth
                                MQTTPublisher.Publisher("door");
                            },1000);
                        }
                        else{
                            outmessage = "Cửa đã được mở";
                        }

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
