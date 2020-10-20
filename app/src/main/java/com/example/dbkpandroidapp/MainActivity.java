  package com.example.dbkpandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.dbkpandroidapp.Model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

  public class MainActivity extends AppCompatActivity {
      private EditText editTextLogin ;
      private EditText editTextPassword;


      private static final String TAG = "MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"Create");
        setContentView(R.layout.activity_main);
        editTextLogin = (EditText)findViewById(R.id.editTextLogin);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);

    }

      private String getHash(final String s) {
          final String MD5 = "MD5";
          try {
              // Create MD5 Hash
              MessageDigest digest = java.security.MessageDigest
                      .getInstance(MD5);
              digest.update(s.getBytes());
              byte messageDigest[] = digest.digest();

              // Create Hex String
              StringBuilder hexString = new StringBuilder();
              for (byte aMessageDigest : messageDigest) {
                  String h = Integer.toHexString(0xFF & aMessageDigest);
                  while (h.length() < 2)
                      h = "0" + h;
                  hexString.append(h);
              }
              return hexString.toString();

          } catch (NoSuchAlgorithmException e) {
              e.printStackTrace();
          }
          return "";
      }

      public void buttonLoginOnClick(View view) throws ClassNotFoundException {
//          Log.i(TAG,"buttonLogin click");
          Context context = this;
          String password = editTextPassword.getText().toString();
          String login = editTextLogin.getText().toString();
          String passwordHash = getHash(login + password);
          Log.i(TAG, "password:" + password + " passHash:" + passwordHash);
          Retrofit retrofit = new Retrofit.Builder()
                  .baseUrl("https://dbkp.azurewebsites.net/")
                  .addConverterFactory(GsonConverterFactory.create())
                  .build();
          ApiService apiService = retrofit.create(ApiService.class);
          Call<UserModel> call = apiService.getId(login, passwordHash);
          call.enqueue(new Callback<UserModel>() {
              @Override
              public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                  if (response.isSuccessful()) {
                      Log.i(TAG, Integer.toString(response.body().getId()));
                      //TODO add password validation
                      Intent myIntent = new Intent(context, Users.class);
                      startActivity(myIntent);
                  } else {
                      Log.d(TAG, "getUserById error");
                  }

              }

              @Override
              public void onFailure(Call<UserModel> call, Throwable t) {
                  Log.e(TAG, "Error");
                  Log.e(TAG, t.getMessage());
              }
          });

//          UserModel userModel = db.getUserByLogin(login);
//          Log.i(TAG,Integer.toString(userModel.getId()));
      }
  }