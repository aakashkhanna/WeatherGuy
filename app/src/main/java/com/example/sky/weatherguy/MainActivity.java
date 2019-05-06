package com.example.sky.weatherguy;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String Weather="";
    String Description;
    public class DownloadJSON extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                String JSONresult="";
                URL url=new URL(strings[0]);
                HttpURLConnection httpURLConnection=null;
                httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inp=httpURLConnection.getInputStream();
                InputStreamReader inpr=new InputStreamReader(inp);
                int data=inpr.read();
                while(data!=-1)
                {
                 char curr=(char)data;
                 JSONresult+=curr;
                 data=inpr.read();
                }
                return JSONresult;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("JSON",""+s);
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weatherInfo=jsonObject.getString("weather");
                JSONArray arr=new JSONArray(weatherInfo);
                for(int i=0;i<arr.length();i++){
                    JSONObject jsonPart;
                    jsonPart = arr.getJSONObject(i);
                    Weather=jsonPart.getString("main");
                    Description=jsonPart.getString("description");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*DownloadJSON task=new DownloadJSON();
        task.execute("http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22");*/
    }
    public void checkWeather(View view){
        EditText textField=(EditText)findViewById(R.id.CityName);
        TextView resultText=(TextView)findViewById(R.id.Result) ;
        DownloadJSON task=new DownloadJSON();
        String TargetURL="http://api.openweathermap.org/data/2.5/weather?q="+textField.getText().toString()+"&appid=";
        task.execute(TargetURL);
        resultText.setText(Weather);
        InputMethodManager m=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(textField.getWindowToken(),0);
    }
}
