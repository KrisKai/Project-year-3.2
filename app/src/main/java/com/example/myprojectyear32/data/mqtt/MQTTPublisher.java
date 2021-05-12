package com.example.myprojectyear32.data.mqtt;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTPublisher extends AppCompatActivity {
    static MqttAndroidClient client;
    public static void Connect(Context context, String serverIP) {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("Henefisa103");
        options.setPassword("henefisa103".toCharArray());
        String clientId = MqttClient.generateClientId();

//        192.168.1.200:1883
        client =
                new MqttAndroidClient(context, "tcp://"+serverIP,
                        clientId);

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("mqtt", "onSuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.gw    . connection timeout or firewall problems
                    Log.d("mqtt", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
