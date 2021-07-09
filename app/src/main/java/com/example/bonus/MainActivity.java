package com.example.bonus;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements CategoryFragment.ICat, NewsSourcesFragment.INewSource, NewsFragment.INFrag {

    /**
     * Assignment #MidTerm Makeup Bonus
     * MainActivity.java
     * Ivy Pham
     */

    public final String BASE_URL = "https://newsapi.org/v2/";

    ProgressDialog dialog;

    private final OkHttpClient client = new OkHttpClient();

    static final String NEWS_API = "c29a354609d34b2d95a4b64ff56e4c87";

    private void toggleDialog(boolean toggle, String msg){
        if(toggle){
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setCancelable(false);
            dialog.show();
        }else{
            dialog.dismiss();
        }
    }

    private void sendRequest(Request request, APIResponse callback) {
        toggleDialog(true, getString(R.string.loading));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    toggleDialog(false, null);
                    alert(e.toString());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                runOnUiThread(() -> toggleDialog(false, null));

                ResponseBody responseBody = response.body();
                JSONObject jsonObject;
                if (responseBody != null) {
                    try {
                        jsonObject = new JSONObject(responseBody.string());
                        if(jsonObject.getString("status").equals("ok")){
                            runOnUiThread(() -> callback.onResponse(jsonObject));
                        }else{
                            if(jsonObject.has("message")) {
                                alert(jsonObject.getString("message"));
                            }
                            runOnUiThread(() -> callback.onError(jsonObject));
                        }
                    }catch (JSONException exception){
                        exception.printStackTrace();
                        return;
                    }
                }
            }
        });
    }

    public void sendNewsSourceRequest(APIResponse response, String cat){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "sources").newBuilder();
        urlBuilder.addQueryParameter("apiKey", NEWS_API);
        urlBuilder.addQueryParameter("category", cat);
        urlBuilder.addQueryParameter("country", "us");

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        sendRequest(request, response);
    }

    public void sendEverythingRequest(APIResponse response, String source){
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL + "everything").newBuilder();
        urlBuilder.addQueryParameter("sources", source);
        urlBuilder.addQueryParameter("apiKey", NEWS_API);

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        sendRequest(request, response);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendCategoryFragment();
    }

    public void sendCategoryFragment(){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new CategoryFragment())
                .commit();
    }

    public void sendNewsSourceFragment(String category){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, NewsSourcesFragment.newInstance(category))
                .addToBackStack(null)
                .commit();
    }

    public void sendNewsFragment(String source, String name){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, NewsFragment.newInstance(source, name))
                .addToBackStack(null)
                .commit();
    }

    public void alert(String alert){
        runOnUiThread(() -> {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.title)
                    .setMessage(alert)
                    .setPositiveButton("Okay", null)
                    .show();
        });
    }

    interface APIResponse{

        void onResponse(JSONObject jsonObject);

        void onError(JSONObject jsonObject);

    }

}