package com.example.dbkpandroidapp;

import android.os.Bundle;

import com.example.dbkpandroidapp.Model.UserModel;
import com.example.dbkpandroidapp.Model.UsersList;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UsersActivity extends AppCompatActivity {

    ArrayList<String> listItems=new ArrayList<String>();
    private String TAG = "UsersList";

    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);

        ListView listView = findViewById(R.id.UsersListView);
        listView.setAdapter(adapter);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dbkp.azurewebsites.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
//        SharedPreferences pref = getSharedPreferences("pref", Context.MODE_PRIVATE);
       String login = getIntent().getExtras().getString("Login");
       String password = getIntent().getExtras().getString("Password");

        Call<UsersList> call = apiService.getAllUsers(login, password);
        call.enqueue(new Callback<UsersList>() {
            @Override
            public void onResponse(Call<UsersList> call, Response<UsersList> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG,  password );

                    if (response.body().getStatus().equals("InvalidLogin")) {
                        Toast.makeText(getApplicationContext(), "Invalid login or password", Toast.LENGTH_SHORT).show();
                    } else {
                        List<UserModel> users = response.body().getUsersList();
                        for (int i = 0; i < users.size(); i++) {
                            UserModel user = users.get(i);
                            adapter.add(user.getId() + " " + user.getLogin() + " " + user.getMoney());

                        }
                    }

                } else {
                    Log.d(TAG, "get all users error");
                }

            }

            @Override
            public void onFailure(Call<UsersList> call, Throwable t) {
                Log.e(TAG, "Error");
                Log.e(TAG, t.getMessage());
            }
        });



////        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
////        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        toolBarLayout.setTitle(getTitle());
    }

}