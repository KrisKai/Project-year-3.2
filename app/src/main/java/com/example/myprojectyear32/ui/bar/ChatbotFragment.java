package com.example.myprojectyear32.ui.bar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myprojectyear32.MainActivity;
import com.example.myprojectyear32.R;
import com.example.myprojectyear32.data.chatbot.BotResponse;
import com.example.myprojectyear32.data.chatbot.ChatAdapter;
import com.example.myprojectyear32.data.chatbot.Message;

import java.util.ArrayList;
import java.util.Locale;

public class ChatbotFragment extends Fragment {

    private RecyclerView recyclerView;
    private ChatAdapter mAdapter;
    private ArrayList messageArrayList;
    private EditText inputMessage;
    private boolean initialRequest;
    private TextToSpeech textToSpeech;
    private TextView textView1, textView2, textView3;
    private String storedStrForLed = "message for led",storedStrForSensor = "message for sensor",storedStrForDoor = "message for door";
    private boolean storedStatusForLed = false;
    private boolean storedStatusForSensor = false;
    private boolean storedStatusForDoor = false;

    public ChatbotFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chatbot, container, false);
        inputMessage = view.findViewById(R.id.message);
        ImageButton btnSend = view.findViewById(R.id.btn_send);
        ImageView returnBtn = view.findViewById(R.id.returnCB);
        returnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(),MainActivity.class);
            startActivity(intent);
        });
        recyclerView = view.findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();
        mAdapter = new ChatAdapter(messageArrayList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        this.inputMessage.setText("");
        this.initialRequest = true;

        CardView cardView1 = view.findViewById(R.id.recommendation1);
        CardView cardView2 = view.findViewById(R.id.recommendation2);
        CardView cardView3 = view.findViewById(R.id.recommendation3);
        textView1 = view.findViewById(R.id.recommendationText1);
        textView2 = view.findViewById(R.id.recommendationText2);
        textView3 = view.findViewById(R.id.recommendationText3);

        cardView1.setOnClickListener(v -> {
            Message inputMessage = new Message();
            inputMessage.setMessage(textView1.getText().toString());
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            storedChatForLed(textView1.getText().toString(),storedStatusForLed);
            BotResponse botResponse = new BotResponse(storedStrForLed);
            String response = botResponse.basicResponses(getContext());
            botResponse(response);
            speak(response);


        });

        cardView2.setOnClickListener(v -> {
            Message inputMessage = new Message();
            inputMessage.setMessage(textView2.getText().toString());
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            BotResponse botResponse = new BotResponse(textView2.getText().toString());
            String response = botResponse.basicResponses(getContext());
            botResponse(response);
            speak(response);
        });

        cardView3.setOnClickListener(v -> {
            Message inputMessage = new Message();
            inputMessage.setMessage(textView3.getText().toString());
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            storedChatForSensor(textView1.getText().toString(),storedStatusForSensor);
            BotResponse botResponse = new BotResponse(storedStrForSensor);
            String response = botResponse.basicResponses(getContext());
            botResponse(response);
            speak(response);
        });

        ImageButton btnRecord = view.findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
            try {
                startActivityForResult(intent, 10);
            }
            catch (ActivityNotFoundException e){
                Toast.makeText(getContext(),"Your device is not support speech input",Toast.LENGTH_SHORT).show();
            }
        });

        btnSend.setOnClickListener(v -> sendMessage());

        sendMessage();
        String response = "Xin chào! Tôi có thể giúp gì cho bạn?";
        botResponse(response);

        textToSpeech = new TextToSpeech(getContext(), status -> {
            if(status == TextToSpeech.SUCCESS){
                int result = textToSpeech.setLanguage(Locale.forLanguageTag("vi"));
                if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                    Toast.makeText(getContext(),"Language not supported",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void speak(String message){
        if(!message.equals("")){
            textToSpeech.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        }
    }
    // Sending a message to Watson Assistant Service
    private void sendMessage() {

        final String inputmessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            if(inputmessage.contains("đèn")||(inputmessage.contains("phòng")&&storedStrForLed.contains("đèn"))){
                storedChatForLed(inputmessage,storedStatusForLed);
                BotResponse botResponse = new BotResponse(storedStrForLed);
                String response = botResponse.basicResponses(getContext());
                botResponse(response);
                speak(response);
            }
            if(inputmessage.contains("nhiệt")||(inputmessage.contains("phòng")&&storedStrForSensor.contains("nhiệt"))){
                storedChatForSensor(inputmessage,storedStatusForSensor);
                BotResponse botResponse = new BotResponse(storedStrForSensor);
                String response = botResponse.basicResponses(getContext());
                botResponse(response);
                speak(response);
            }
            if(inputmessage.contains("cửa")||(inputmessage.contains("phòng")&&storedStrForDoor.contains("cửa"))){
                storedChatForDoor(inputmessage,storedStatusForDoor);
                BotResponse botResponse = new BotResponse(storedStrForDoor);
                String response = botResponse.basicResponses(getContext());
                botResponse(response);
                speak(response);
            }
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        } else {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("100");
            this.initialRequest = false;

        }
        this.inputMessage.setText("");
    }

    private void botResponse(String response){
        Message outMessage = new Message();
        outMessage.setMessage(response);
        outMessage.setId("2");
        messageArrayList.add(outMessage);
        recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
        mAdapter.notifyDataSetChanged();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if (resultCode == MainActivity.RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Message inputMessage = new Message();
                    inputMessage.setMessage(result.get(0));
                    inputMessage.setId("1");
                    messageArrayList.add(inputMessage);
                    recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    if(result.get(0).contains("đèn")||(result.get(0).contains("phòng")&&storedStrForLed.contains("đèn"))){
                        storedChatForLed(result.get(0),storedStatusForLed);
                        BotResponse botResponse = new BotResponse(storedStrForLed);
                        String response = botResponse.basicResponses(getContext());
                        botResponse(response);
                        speak(response);
                    }
                    if(result.get(0).contains("nhiệt")||(result.get(0).contains("phòng")&&storedStrForSensor.contains("quạt"))){
                        storedChatForSensor(result.get(0),storedStatusForSensor);
                        BotResponse botResponse = new BotResponse(storedStrForSensor);
                        String response = botResponse.basicResponses(getContext());
                        botResponse(response);
                        speak(response);
                    }
                    if(result.get(0).contains("cửa")||(result.get(0).contains("phòng")&&storedStrForDoor.contains("cửa"))){
                        storedChatForDoor(result.get(0),storedStatusForDoor);
                        BotResponse botResponse = new BotResponse(storedStrForDoor);
                        String response = botResponse.basicResponses(getContext());
                        botResponse(response);
                        speak(response);
                    }
                }
                break;
        }

    }

    private void storedChatForLed(String message, boolean status){
        if(!status){
            if(message.contains("bật")&&message.contains("đèn")&&!message.contains("phòng")){
                storedStrForLed = message;
                storedStatusForLed = true;
            }else{
                storedStrForLed = message;
                storedStatusForLed = false;
            }
        }else {
            storedStrForLed = storedStrForLed + " " + message;
            storedStatusForLed = false;
        }

    }
    private void storedChatForSensor(String message, boolean status){
        if(!status){
            if(message.contains("nhiệt")&&message.contains("độ")&&!message.contains("phòng")){
                storedStrForSensor = message;
                storedStatusForSensor = true;
            }else{
                storedStrForSensor = message;
                storedStatusForSensor = false;
            }
        }else {
            storedStrForSensor = storedStrForSensor + " " + message;
            storedStatusForSensor = false;
        }

    }
    private void storedChatForDoor(String message, boolean status){
        if(!status){
            if(message.contains("mở")&&message.contains("cửa")&&!message.contains("phòng")){
                storedStrForDoor = message;
                storedStatusForDoor = true;
            }else{
                storedStrForDoor = message;
                storedStatusForDoor = false;
            }
        }else {
            storedStrForDoor = storedStrForDoor + " " + message;
            storedStatusForDoor = false;
        }

    }
}