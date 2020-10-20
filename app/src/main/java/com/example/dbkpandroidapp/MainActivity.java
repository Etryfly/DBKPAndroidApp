  package com.example.dbkpandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.dbkpandroidapp.Model.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        AndroidNetworking.initialize(getApplicationContext());
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
          AndroidNetworking.get("https://dbkp.azurewebsites.net/admin/getId")
                  .addQueryParameter("login", login)
                  .addQueryParameter("passwordHash", passwordHash )
                  .setPriority(Priority.MEDIUM)
                  .build()
                  .getAsJSONObject(new JSONObjectRequestListener() {
                      @Override
                      public void onResponse(JSONObject response) {
                          try {
                              Log.i(TAG,response.get("code").toString());
                              //TODO add password validation
                              Intent myIntent = new Intent(context, Users.class);
                              startActivity(myIntent);
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                      }
                      @Override
                      public void onError(ANError error) {
                          Log.e(TAG, "Error");
                          Log.e(TAG, error.getErrorDetail());
                      }
                  });

//          UserModel userModel = db.getUserByLogin(login);
//          Log.i(TAG,Integer.toString(userModel.getId()));
      }
  }