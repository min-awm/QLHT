package com.example.btl_android.chat_ai;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_android.DatabaseHelper;
import com.example.btl_android.R;
import com.example.btl_android.lich_hoc.TimeTable;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatAiActivity extends AppCompatActivity {
    private ChatView chatView;
    private String allDatabaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_ai);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar_chat);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        DatabaseHelper myDB = new DatabaseHelper(ChatAiActivity.this);
        allDatabaseData = myDB.getAllData();

        chatView = (ChatView) findViewById(R.id.chat_view);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                String prompt = "Dữ liệu:\n" + allDatabaseData + "\nDựa vào dữ liệu trên, trả lời câu hỏi sau:\n" + chatMessage.getMessage();
                sendToGemini(prompt);
                return true;
            }
        });


    }

    void sendToGemini(String prompt) {
        GeminiApi.generateContent(prompt, new GeminiApi.GeminiCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(() -> {
                    ChatMessage receivedMessage = new ChatMessage(response, System.currentTimeMillis(), ChatMessage.Type.RECEIVED, "Chatbot");
                    chatView.addMessage(receivedMessage);
                });
            }

            @Override
            public void onError(String error) {
                Log.e("Gemini Error", error);
            }
        });
    }
}