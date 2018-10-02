package com.example.bandhan.springbookingsystem;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private static final String URL_FOR_ADMIN = " http://52.66.132.37/booking.springsportsacademy.com/api/login/validate";
    private static final String URL_FOR_USER = " http://52.66.132.37/booking.springsportsacademy.com/api/login/user_signin";
    ProgressDialog progressDialog;
    private EditText loginInputEmail, loginInputPassword;
    private Button btnUserlogin;
    private Button btnAdminLogin;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginInputEmail = (EditText) findViewById(R.id.login_input_email);
        loginInputPassword = (EditText) findViewById(R.id.login_input_password);
        btnUserlogin = (Button) findViewById(R.id.btnUserlogin);
        btnAdminLogin = (Button) findViewById(R.id.btnAdminlogin);
        requestQueue = AppSingleton.getInstance(this.getApplicationContext())
                .getRequestQueue();
        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        btnUserlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser(loginInputEmail.getText().toString(),
                        loginInputPassword.getText().toString());
            }
        });
        btnAdminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin(loginInputEmail.getText().toString(),
                        loginInputPassword.getText().toString());
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
    private void loginAdmin(final String email, final String password) {

        progressDialog.setMessage("Logging you in...");
        showDialog();

        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FOR_ADMIN, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("Admin Login ", "Response: " + response.toString());
                try {
                    JSONArray adminArray = response.getJSONArray("data");
                    for (int i = 0; i < adminArray.length(); i++) {
                        JSONObject eventsObject = adminArray.getJSONObject(i);
                        String adminEmail = eventsObject.getString("user_email_id");
                        String adminPassword = eventsObject.getString("password");
                        Toast.makeText(MainActivity.this,"Login email : "+adminEmail,Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this,"Login password : "+adminPassword,Toast.LENGTH_SHORT).show();
                      /*  if (email.equals(adminEmail) && password.equals(adminPassword)) {
                           Intent dashboardIntent = new Intent(MainActivity.this, AdminDashboard.class);
                          startActivity(dashboardIntent);
                          finish();
                        }else{
                            Toast.makeText(MainActivity.this,"Error in login",Toast.LENGTH_SHORT).show();
                        }*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Login ", "Error: " + error.getMessage());
                hideDialog();
            }
        });
        requestQueue.add(jsonReq);
    }
    private void loginUser( final String email, final String password) {
        // Tag used to cancel the request
        progressDialog.setMessage("Logging you in...");
        showDialog();
        JsonObjectRequest jsonReq = new JsonObjectRequest(Request.Method.GET,
                URL_FOR_USER, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d("User Login ", "Response: " + response.toString());
                try {
                    JSONArray adminArray = response.getJSONArray("data");
                    for (int i = 0; i < adminArray.length(); i++) {
                        JSONObject eventsObject = adminArray.getJSONObject(i);
                        String userEmail = eventsObject.getString("email");
                        String userPassword = eventsObject.getString("password");
                        if (email.equals(userEmail) && password.equals(userPassword)) {
                            Intent userIntent = new Intent(MainActivity.this, UserDashboard.class);
                            startActivity(userIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this,"Error in login",Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    hideDialog();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("User Login ", "Error: " + error.getMessage());
                hideDialog();
            }
        });
        requestQueue.add(jsonReq);
    }
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



}
