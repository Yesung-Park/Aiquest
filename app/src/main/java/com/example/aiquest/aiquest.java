package com.example.aiquest;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//주석 새로 달았습니다.
public class aiquest extends AppCompatActivity {

    private EditText etUserInput;
    private ImageButton btnSend;
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private final List<ChatMessage> messageList = new ArrayList<>();

    private final String apiKey = "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"; // 실제 API 키로 교체

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aiquest);

        etUserInput = findViewById(R.id.etUserInput);
        btnSend = findViewById(R.id.btnSend);
        recyclerView = findViewById(R.id.recyclerView);

        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        ChatMessage welcomeMessage = new ChatMessage("안녕하세요. 어떤 증상이 있으신가요?", false);
        messageList.add(welcomeMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);

        btnSend.setOnClickListener(view -> {
            String userInput = etUserInput.getText().toString().trim();
            if (!userInput.isEmpty()) {
                messageList.add(new ChatMessage(userInput, true));
                chatAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerView.scrollToPosition(messageList.size() - 1);
                etUserInput.setText("");
                getDiagnosisRecommendation(userInput);
            } else {
                Toast.makeText(this, "증상을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDiagnosisRecommendation(String symptom) {
        String prompt = "사용자가 '" + symptom + "' 라는 증상을 말했을 때, 증상에 따른 발병 가능한 병명을 얘기 해주고 마지막에는 추천 진료과목을 알려줘. 예) 이비인후과 </p> 내과";

        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo");
            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.put(userMessage);
            json.put("messages", messages);
            json.put("temperature", 0.3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(aiquest.this, "오류: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String resBody = response.body().string();
                    try {
                        JSONObject responseJson = new JSONObject(resBody);
                        String reply = responseJson.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        runOnUiThread(() -> {
                            messageList.add(new ChatMessage(reply.trim(), false));
                            chatAdapter.notifyItemInserted(messageList.size() - 1);
                            recyclerView.scrollToPosition(messageList.size() - 1);
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}