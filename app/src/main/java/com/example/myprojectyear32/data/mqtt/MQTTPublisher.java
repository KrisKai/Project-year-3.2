package com.example.myprojectyear32.data.mqtt;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MQTTPublisher extends AppCompatActivity {
    static MqttAndroidClient client;


    public static void Connect(Context context, String serverIP) {
//        new Handler().postDelayed(() -> {

        String clientId = "android_test";
        if (client == null) {
            client =
                    new MqttAndroidClient(context, "tcp://" + serverIP,
                            clientId);
        }

        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("Henefisa103");
        options.setPassword("henefisa103".toCharArray());
        //            String clientId = MqttClient.generateClientId();

        //        192.168.1.200:1883

        if (!client.isConnected()) {
            try {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d("mqtt", "onSuccess Con");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.gw    . connection timeout or firewall problems
                        Log.d("mqtt", "onFailure Con");

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("mqtt", "Connected");
        }
        //do sth
//        },1000);
    }

    public static void Publisher(String msg){
        if (client.isConnected()) {
            String topic = "living";
            String payload = msg;
            byte[] encodedPayload = new byte[0];
            try {
                encodedPayload = payload.getBytes("UTF-8");
                MqttMessage message = new MqttMessage(encodedPayload);
                client.publish(topic, message);
                Log.d("mqtt", "onSuccess Pub");
            } catch (UnsupportedEncodingException | MqttException e) {
                e.printStackTrace();
                Log.d("mqtt", "onFailure Pub");
            }
        } else {
            Log.d("mqtt", "onFailure Pub-Con");
        }
    }

    public static void Subcriber(String topic){
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d("mqtt", "onSuccess Sub");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.d("mqtt", "onFailure Sub");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void MessageOutput(){
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("mqtt", message.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
