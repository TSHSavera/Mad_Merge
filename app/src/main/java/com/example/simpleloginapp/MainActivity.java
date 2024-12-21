package com.example.simpleloginapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Context context = this;
    String passed_username = "", passed_password = "", passed_name= "";
    byte[] passed_image;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout mainLayout = findViewById(R.id.layout_main);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        Typeface appTitleFont = ResourcesCompat.getFont(context, R.font.delius_swash_caps);
        LinearLayout.LayoutParams appTitleTVParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView appTitle = new TextView(context);
        appTitle.setId(ViewGroup.generateViewId());
        appTitle.setLayoutParams(appTitleTVParams);
        appTitle.setTypeface(appTitleFont, Typeface.BOLD);
        appTitleTVParams.setMargins(0, 270, 0, 250);
        appTitle.setText("SIMPLE LOGIN APP");
        appTitle.setTextSize(30);
        appTitle.setTextColor(Color.parseColor("#000000"));
        appTitle.setGravity(Gravity.CENTER);
        mainLayout.addView(appTitle);

        LinearLayout.LayoutParams labelOfETParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView usernameTV = new TextView(context);
        usernameTV.setId(ViewGroup.generateViewId());
        usernameTV.setLayoutParams(labelOfETParams);
        usernameTV.setText("Username:");
        usernameTV.setPadding(55, 0, 0, 0);
        usernameTV.setTextSize(18);
        usernameTV.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(usernameTV);


        LinearLayout.LayoutParams ETParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200);
        EditText usernameET = new EditText(context);
        usernameET.setLayoutParams(ETParams);
        usernameET.setTextSize(18);
        usernameET.setTextColor(Color.parseColor("#000000"));
        usernameET.setPadding(40, 0, 0, 0);
        ETParams.setMargins(50, 18, 50, 20);
        usernameET.setBackgroundResource(R.drawable.custom_edit_text);
        mainLayout.addView(usernameET);


        TextView passwordTV = new TextView(context);
        passwordTV.setId(ViewGroup.generateViewId());
        passwordTV.setLayoutParams(labelOfETParams);
        labelOfETParams.setMargins(0,20, 0, 0);
        passwordTV.setText("Password:");
        passwordTV.setPadding(55, 0, 0, 0);
        passwordTV.setTextSize(18);
        passwordTV.setTextColor(Color.parseColor("#000000"));
        mainLayout.addView(passwordTV);

        EditText passwordET = new EditText(context);
        passwordET.setId(ViewGroup.generateViewId());
        passwordET.setLayoutParams(ETParams);
        passwordET.setTextSize(18);
        passwordET.setTextColor(Color.parseColor("#000000"));
        passwordET.setPadding(40, 0, 0, 0);
        ETParams.setMargins(50, 18, 50, 20);
        passwordET.setBackgroundResource(R.drawable.custom_edit_text);
        passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        mainLayout.addView(passwordET);

        LinearLayout.LayoutParams BTNParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 225);
        Button loginBtn = new Button(context);
        loginBtn.setId(ViewGroup.generateViewId());
        loginBtn.setLayoutParams(BTNParams);
        BTNParams.setMargins(50, 470, 50, 35);
        loginBtn.setTextSize(18);
        loginBtn.setText("Login");
        loginBtn.setBackgroundResource(R.drawable.custom_button);
        loginBtn.setTextColor(Color.parseColor("#f5f5f5"));
        mainLayout.addView(loginBtn);


        LinearLayout.LayoutParams registerLinkLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        LinearLayout registerLinkLayout = new LinearLayout(context);
        registerLinkLayout.setOrientation(LinearLayout.HORIZONTAL);
        registerLinkLayout.setLayoutParams(registerLinkLayoutParams);
        registerLinkLayout.setGravity(Gravity.CENTER);
        registerLinkLayoutParams.setMargins(0, 40, 0,0);

        LinearLayout.LayoutParams registerLabelParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView registerLabelTV = new TextView(context);
        registerLabelTV.setLayoutParams(registerLabelParams);
        registerLabelTV.setText("Not yet registered?");
        registerLabelTV.setTextColor(Color.parseColor("#000000"));
        registerLabelTV.setTextSize(18);
        registerLabelParams.setMargins(0, 0, 8, 0);
        registerLinkLayout.addView(registerLabelTV);

        LinearLayout.LayoutParams registerLinkParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView registerLinkTV = new TextView(context);
        registerLinkTV.setId(View.generateViewId());
        registerLinkTV.setLayoutParams(registerLinkParams);
        registerLinkTV.setText("Click Here");
        registerLinkTV.setTextColor(Color.parseColor("#e5002f"));
        registerLinkTV.setTextSize(18);
        registerLinkTV.setTypeface(null, Typeface.BOLD);
        registerLinkLayout.addView(registerLinkTV);
        registerLinkParams.setMargins(8, 0, 0, 0);
        mainLayout.addView(registerLinkLayout);

        String account1 = "Jeff", password1="12345";
        String account2 = "Joan", password2="567890";
        String account3 = "Dani", password3="ASDFGH";

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            passed_username = extras.getString("username");
            passed_password = extras.getString("password");
            passed_name = extras.getString("name");
            passed_image = extras.getByteArray("image_data");
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usernameET.getText().toString().equals("") && !passwordET.getText().toString().equals("")) {
                    if(usernameET.getText().toString().equals(account1) && passwordET.getText().toString().equals(password1)) {

                        usernameET.setText("");
                        passwordET.setText("");
                    }
                    else if (usernameET.getText().toString().equals(account2) && passwordET.getText().toString().equals(password2)) {

                        usernameET.setText("");
                        passwordET.setText("");
                    }
                    else if (usernameET.getText().toString().equals(account3) && passwordET.getText().toString().equals(password3)) {

                        usernameET.setText("");
                        passwordET.setText("");
                    }
                    // Get the username and password from the extras - passed_username and passed_password
                    else if (usernameET.getText().toString().equals(passed_username) && passwordET.getText().toString().equals(passed_password)) {
                        Intent welcome_intent = new Intent(context, welcome.class);
                        welcome_intent.putExtra("name", passed_name);
                        welcome_intent.putExtra("image", passed_image);
                        startActivity(welcome_intent);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Login failed");
                        builder.setMessage("Username or Password is incorrect. Please try again.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                usernameET.setText("");
                                passwordET.setText("");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                else {
                    Toast.makeText(context, "Please make sure the input fields are not empty. Try again.", Toast.LENGTH_LONG).show();
                }
            }
        });

        registerLinkTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Registration");
                builder.setMessage("You will be redirected to the Registration Page.");
                builder.setCancelable(false);

                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent registration_intent = new Intent(context, Registration_Form.class);
                        startActivity(registration_intent);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}