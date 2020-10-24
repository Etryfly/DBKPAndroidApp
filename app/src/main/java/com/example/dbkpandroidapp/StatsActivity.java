package com.example.dbkpandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dbkpandroidapp.Model.StatsModel;
import com.example.dbkpandroidapp.Model.UsersList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StatsActivity extends AppCompatActivity {

    private String login;
    private String passwordHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        login = getIntent().getExtras().getString("Login");
        passwordHash = getIntent().getExtras().getString("Password");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbkp.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<StatsModel> call = apiService.getStatistics(login, passwordHash);
        call.enqueue(new Callback<StatsModel>() {
            @Override
            public void onResponse(Call<StatsModel> call, Response<StatsModel> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        int winned = response.body().getWinned();
                        int payed = response.body().getPayed();
                        TextView payedTextView = (TextView) findViewById(R.id.payedTextView);

                        payedTextView.setText(Integer.toString(payed));
                        TextView winnedTextView = (TextView) findViewById(R.id.winnedTextView);
                        winnedTextView.setText(Integer.toString(winned));
                        TextView profitTextView = (TextView) findViewById(R.id.profitTextView);
                        profitTextView.setText(Integer.toString(payed - winned));
                    } else {
                        Log.d("Stats", "response body is null");
                    }
                } else {
                    Log.d("Stats", "get stats response error");
                }
            }

            @Override
            public void onFailure(Call<StatsModel> call, Throwable t) {
                Log.d("Stats", "get stats error");
            }
        });
    }


}