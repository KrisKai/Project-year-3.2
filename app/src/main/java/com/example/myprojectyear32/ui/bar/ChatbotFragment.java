package com.example.myprojectyear32.ui.bar;

import android.content.ActivityNotFoundException;
import android.content.Context;
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
    private ImageButton btnSend;
    private ImageButton btnRecord;
    private boolean initialRequest;
    private TextToSpeech textToSpeech;
    private CardView cardView1, cardView2, cardView3;
    private TextView textView1, textView2, textView3;
    private String storedStr;
    private boolean storedStatus = false;
    private ImageView returnBtn;

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
        btnSend = view.findViewById(R.id.btn_send);
        returnBtn = view.findViewById(R.id.returnCB);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),MainActivity.class);
                startActivity(intent);
            }
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

        cardView1 = (CardView) view.findViewById(R.id.recommendation1);
        cardView2 = (CardView) view.findViewById(R.id.recommendation2);
        cardView3 = (CardView) view.findViewById(R.id.recommendation3);
        textView1 = (TextView) view.findViewById(R.id.recommendationText1);
        textView2 = (TextView) view.findViewById(R.id.recommendationText2);
        textView3 = (TextView) view.findViewById(R.id.recommendationText3);

        cardView1.setOnClickListener(v -> {
            Message inputMessage = new Message();
            inputMessage.setMessage(textView1.getText().toString());
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            storeChat(textView1.getText().toString(),storedStatus);
            BotResponse botResponse = new BotResponse(storedStr);
            String response = botResponse.basicResponses();
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
            String response = botResponse.basicResponses();
            botResponse(response);
            speak(response);
        });

        cardView3.setOnClickListener(v -> {
            Message inputMessage = new Message();
            inputMessage.setMessage(textView3.getText().toString());
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            BotResponse botResponse = new BotResponse(textView3.getText().toString());
            String response = botResponse.basicResponses();
            botResponse(response);
            speak(response);
        });

        btnRecord = (ImageButton)view.findViewById(R.id.btn_record) ;
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
    ;
    // Sending a message to Watson Assistant Service
    private void sendMessage() {

        final String inputmessage = this.inputMessage.getText().toString().trim();
        if (!this.initialRequest) {
            Message inputMessage = new Message();
            inputMessage.setMessage(inputmessage);
            inputMessage.setId("1");
            messageArrayList.add(inputMessage);
            storeChat(inputmessage,storedStatus);
            recyclerView.scrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            BotResponse botResponse = new BotResponse(storedStr);
            String response = botResponse.basicResponses();
            botResponse(response);
            speak(response);
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
                    storeChat(result.get(0),storedStatus);
                    BotResponse botResponse = new BotResponse(storedStr);
                    String response = botResponse.basicResponses();
                    botResponse(response);
                    speak(response);

                }
                break;
        }

    }

    private void storeChat(String message, boolean status){
        if(status==false){
            if(message.contains("bật")&&message.contains("đèn")&&!message.contains("phòng")){
                storedStr = message;
                storedStatus = true;
            }else{
                storedStr = "";
                storedStatus = false;
            }
        }else {
            storedStr = storedStr + " " + message;
            storedStatus = false;
        }

    }
}