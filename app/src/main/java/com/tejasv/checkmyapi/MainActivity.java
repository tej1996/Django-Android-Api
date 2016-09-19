package com.tejasv.checkmyapi;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button get,post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        get = (Button) findViewById(R.id.button_get);
        post = (Button) findViewById(R.id.button_post);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DataTransmitGet(getApplicationContext()).execute();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DataTransmitPost(getApplicationContext()).execute("Ajay Sharma","158");
            }
        });

    }
}

class DataTransmitPost extends AsyncTask<String,String,String>
{
    private Context context;
    public DataTransmitPost(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... params) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("name",params[0])
                .add("roll",params[1])
                .build();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/test/")
                .post(body)
                .build();
        Response response = null;
        int status=0;
        try {
            response = client.newCall(request).execute();
            try {
                JSONObject result = new JSONObject(response.body().string());
                status = result.getInt("status");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(status==1)
            {
                return "Successful";
            }
            return "Unsucessful";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        Toast.makeText(context, "Response :"+ res, Toast.LENGTH_SHORT).show();
    }
}

class DataTransmitGet extends AsyncTask<String,String,JSONArray>
{
    private Context context;
    public DataTransmitGet(Context context) {
        this.context=context;
    }

    @Override
    protected JSONArray doInBackground(String... params) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8000/test")
                .build();

        try {
            Response response= client.newCall(request).execute();
            String jsonData= response.body().string();

            try {
                JSONArray Jarray = new JSONArray(jsonData);
                return Jarray;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray res) {
        super.onPostExecute(res);
        String result="";
        JSONObject resObj = new JSONObject();
        for(int i =0;i< res.length();i++)
        {
            try {
                resObj =res.getJSONObject(i);
                result = result + "Name: " + resObj.getString("name") + "& Roll :"+resObj.getString("roll")+"\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }
}
