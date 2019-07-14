package com.ritik.twitterclone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmailSignUp, edtUsernameSignUp, edtPasswordSignUp;
    private Button btnSignUpSignUp, btnLoginSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("SignUp");

        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtUsernameSignUp = findViewById(R.id.edtUsernameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);

        edtPasswordSignUp.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    onClick(btnSignUpSignUp);
                }
                return false;
            }
        });

        btnSignUpSignUp = findViewById(R.id.btnSignUpSignUp);
        btnLoginSignUp = findViewById(R.id.btnLoginSignUp);

        btnSignUpSignUp.setOnClickListener(MainActivity.this);
        btnLoginSignUp.setOnClickListener(MainActivity.this);

        if(ParseUser.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, TwitterUsers.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUpSignUp:
                if(edtUsernameSignUp.getText().toString().equals("") || edtEmailSignUp.getText().toString().equals("") || edtPasswordSignUp.getText().toString().equals("")) {
                    FancyToast.makeText(MainActivity.this, "No Fields Must Be Left Blank", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                }
                else {
                    final ParseUser parseUser = new ParseUser();
                    parseUser.setEmail(edtEmailSignUp.getText().toString());
                    parseUser.setUsername(edtUsernameSignUp.getText().toString());
                    parseUser.setPassword(edtPasswordSignUp.getText().toString());

                    final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Signing Up...");
                    progressDialog.show();

                    parseUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                FancyToast.makeText(MainActivity.this, parseUser.getUsername() + " is Signed Up Successfully", FancyToast.LENGTH_SHORT, FancyToast.SUCCESS, false).show();
                                Intent intent = new Intent(MainActivity.this, TwitterUsers.class);
                                startActivity(intent);
                                finish();
                            } else {
                                FancyToast.makeText(MainActivity.this, e.getMessage(), FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
                }
                break;

            case R.id.btnLoginSignUp:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void rootLayoutTapped(View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
