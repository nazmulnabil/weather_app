package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextInputLayout textInputLayout;
    TextInputEditText editText;
    TextView temptv,mintemptv,maxtemptv,humiditytv;
    Double temp = null;
    Button searchbutton;


    static String base_url = "https://api.openweathermap.org/data/2.5/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInputLayout=findViewById(R.id.textinputlayoutid);
        editText = findViewById(R.id.cityetid);
        temptv = findViewById(R.id.temp);
        mintemptv=findViewById(R.id.tempmin);
        maxtemptv=findViewById(R.id.tempmax);
        humiditytv=findViewById(R.id.humidity);
        searchbutton = findViewById(R.id.btnid);

        searchbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getcurrentweather();
            }
        });

    }

    public void getcurrentweather() {


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getUnsafeOkHttpClient().build())
                .build();

        WeatherApi weatherApi = retrofit.create(WeatherApi.class);

        Call<Example> call = weatherApi.getweather(textInputLayout.getEditText().getText().toString().trim(),"8c5d490a28277a704f172dcdcfd0f436");

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {

                if (response.isSuccessful()) {
                    Example mydata = response.body();
                    Main main = mydata.getMain();
                    temp = main.getTemp();
                    Integer temaparature = (int) (temp - 273.15);
                    temptv.setText("Temparature: "+String.valueOf(temaparature) + "C");

                  Double temp_min=response.body().getMain().getTempMin();
                    Integer mintemaparature = (int) (temp_min - 273.15);
                    mintemptv.setText("Min Temparature: "+String.valueOf(mintemaparature) + "C");

                    Double temp_max=main.getTempMax();
                    Integer maxtemaparature = (int) (temp_max - 273.15);
                    maxtemptv.setText("Max Temparature: "+String.valueOf(maxtemaparature) + "C");


                    Integer huidity=main.getHumidity();
                    humiditytv.setText("Humidity:"+String.valueOf(huidity));

                }
                else if(response.code()==404){

                    Toast.makeText(MainActivity.this,"enter valid city",Toast.LENGTH_SHORT).show();

                }
                else if(!(response.isSuccessful())){
                    Toast.makeText(MainActivity.this, Integer.toString(response.code()), Toast.LENGTH_LONG).show();
                }
            }


            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                temptv.setText(t.getMessage());
            }
        });
    }

    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}