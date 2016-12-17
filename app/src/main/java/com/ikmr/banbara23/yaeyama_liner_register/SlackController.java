package com.ikmr.banbara23.yaeyama_liner_register;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SlackController {

    public SlackController() {
    }

    public static void post(String message) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("token", BuildConfig.SLACK_TOKEN)
                .add("channel", "yaimafuni")
                .add("text", message)
                .add("username", "android-bot")
                .add("icon_emoji", ":monkey_face:")
                .build();

        Request request = new Request.Builder()
                .url("https://slack.com/api/chat.postMessage")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.e("SlackController", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Logger.d("SlackController", response.message());
            }
        });
    }
}
