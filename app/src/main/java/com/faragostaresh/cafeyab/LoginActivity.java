package com.faragostaresh.cafeyab;

/*
 * https://github.com/sourcey/materiallogindemo
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final int REQUEST_SIGNUP = 0;

    String email = "";
    String password = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set for support RTL
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Set photo Header
        View photoHeader = findViewById(R.id.photoHeader);
        try {
            CircleImageView imageView = (CircleImageView) findViewById(R.id.civProfilePic);
            Glide.with(this).load(R.drawable.logo).skipMemoryCache(true).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoHeader.setTranslationZ(6);
            photoHeader.invalidate();
        }

        // User info
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userCheck = settings.getString("user_check", "").toString();
        String userSessionId = settings.getString("user_sessionid", "").toString();
        Log.e(TAG, userCheck);
        Log.e(TAG, userSessionId);

        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);
        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        //ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                //overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);
        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("در حال ورود ...");
        progressDialog.show();

        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();








        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String str = "https://www.cafeyab.com/usmartphone/login";

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userSessionId = settings.getString("user_sessionid", "").toString();

        String data = Uri.encode("identity", "UTF-8") + "=" + Uri.encode(email, "UTF-8");
        data += "&" + Uri.encode("credential", "UTF-8") + "=" + Uri.encode(password, "UTF-8");

        BufferedReader reader = null;

        // Send data
        try {

            // Defined URL  where to send data
            URL url = new URL(str);

            // Send POST data request

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "pisess=" + userSessionId);
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();


            int responseCode = conn.getResponseCode();
            Log.e(TAG, "nSending 'POST' request to URL : " + str);
            Log.e(TAG, "Post Data : " + data);
            Log.e(TAG, "Response Code : " + responseCode);

            // Get the server response

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            Log.e(TAG, stringBuffer.toString());
            JSONObject response = new JSONObject(stringBuffer.toString());

            if(response != null)  {
                try {

                    String login = response.getString("login");
                    String check = response.getString("check");
                    String sessionid = response.getString("sessionid");
                    String uid = response.getString("uid");
                    String identity = response.getString("identity");
                    String email = response.getString("email");
                    String name = response.getString("name");
                    String avatar = response.getString("avatar");
                    String message = response.getString("message");

                    // User info
                    SharedPreferences settings1 = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings1.edit();
                    editor.putString("user_sessionid",sessionid);
                    editor.putString("user_check", check);
                    editor.putString("user_uid", uid);
                    editor.putString("user_identity", identity);
                    editor.putString("user_email", email);
                    editor.putString("user_name", name);
                    editor.putString("user_avatar", avatar);
                    editor.commit();

                    progressDialog.dismiss();

                    if (String.valueOf(login).equals("1")) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("message", message);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        onLoginFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Failure", e);
                }
            } else {
                onLoginFailed();
            }

        } catch (Exception ex) {

        } finally {
            try {
                reader.close();
            } catch (Exception ex) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);
        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "خطا در ورود به سیستم", Toast.LENGTH_LONG).show();

        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);
        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        Button _loginButton = (Button) findViewById(R.id.btn_login);
        TextView _signupLink = (TextView) findViewById(R.id.link_signup);
        EditText _emailText = (EditText) findViewById(R.id.input_email);
        EditText _passwordText = (EditText) findViewById(R.id.input_password);

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("لطفا یک آدرس ایمیل معتبر وارد نمایید");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 32) {
            _passwordText.setError("لطفا یک واژه رمز بین ۶ تا ۳۲ کارکتر وارد نمایید");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}