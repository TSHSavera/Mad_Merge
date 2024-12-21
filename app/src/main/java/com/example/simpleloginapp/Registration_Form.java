package com.example.simpleloginapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class Registration_Form extends AppCompatActivity {

    public static Bitmap profileImage;

    Context context = this;
    RadioGroup genderRG;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;
    Bitmap capturedImage; // This will hold the captured image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration_form);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Start App
        startUp();
    }

    private void startUp() {

        // Create the array of security questions
        String[] securityQuestions = {
                "What is your mother's maiden name?",
                "What is the name of your first pet?",
                "What is the name of your first school?",
                "What is your favorite color?",
                "What is your favorite movie?",
                "What is your favorite book?",
                "What is your favorite food?",
                "What is your favorite song?"
        };

        // Upon loading the view, load the spinner with the list of security questions
        Spinner spinner = findViewById(R.id.question1SP);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, securityQuestions);
        // Change the color of the selected spinner item
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        spinner.setAdapter(adapter);

        // Second spinner
        Spinner spinner2 = findViewById(R.id.question2SP);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, securityQuestions);
        // Change the color of the selected spinner item
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        spinner2.setAdapter(adapter2);

        // Third spinner
        Spinner spinner3 = findViewById(R.id.question3SP);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, securityQuestions);
        // Change the color of the selected spinner item
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                ((TextView)parentView.getChildAt(0)).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        spinner3.setAdapter(adapter3);

        //Add listener to the date picker button
        findViewById(R.id.datePickerBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the date picker
                DatePicker datePicker = findViewById(R.id.datePickerDateOfBirth);
                //Disable future dates
                datePicker.setMaxDate(System.currentTimeMillis());
                //Show the date picker
                datePicker.setVisibility(View.VISIBLE);

                //Also show the submit button
                findViewById(R.id.submitDateOfBirthBtn).setVisibility(View.VISIBLE);
                //Add listener to the submit button
                findViewById(R.id.submitDateOfBirthBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Get the date of birth
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth() + 1;
                        int year = datePicker.getYear();
                        //Convert Month to String
                        String monthString = "";
                        switch (month) {
                            case 1:
                                monthString = "January";
                                break;
                            case 2:
                                monthString = "February";
                                break;
                            case 3:
                                monthString = "March";
                                break;
                            case 4:
                                monthString = "April";
                                break;
                            case 5:
                                monthString = "May";
                                break;
                            case 6:
                                monthString = "June";
                                break;
                            case 7:
                                monthString = "July";
                                break;
                            case 8:
                                monthString = "August";
                                break;
                            case 9:
                                monthString = "September";
                                break;
                            case 10:
                                monthString = "October";
                                break;
                            case 11:
                                monthString = "November";
                                break;
                            case 12:
                                monthString = "December";
                                break;
                        }
                        //Set the date of birth to the text view
                        ((TextView) findViewById(R.id.datePickerTV)).setText(day + " " + monthString + " " + year);
                        //Hide the date picker
                        datePicker.setVisibility(View.GONE);
                        //Hide the submit button
                        findViewById(R.id.submitDateOfBirthBtn).setVisibility(View.GONE);
                    }
                });
            }
        });

        genderRG = findViewById(R.id.genderRG);
        genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedID) {
                if (checkedID == R.id.othersRB) {
                    findViewById(R.id.genderOthersET).setVisibility(View.VISIBLE);
                }
                else {
                    findViewById(R.id.genderOthersET).setVisibility(View.GONE);
                }
            }
        });


        findViewById(R.id.takePhotoBtn).setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });

        findViewById(R.id.takeAnotherPhotoBtn).setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            } else {
                requestCameraPermission();
            }
        });


        // Add listener to the submit button
        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values of the fields
                String username = ((EditText) findViewById(R.id.usernameET)).getText().toString();
                String password = ((EditText) findViewById(R.id.passwordET)).getText().toString();
                String confirmPassword = ((EditText) findViewById(R.id.confirm_passwordET)).getText().toString();
                String firstName = ((EditText) findViewById(R.id.fnameET)).getText().toString();
                String lastName = ((EditText) findViewById(R.id.lnameET)).getText().toString();
                String email = ((EditText) findViewById(R.id.emailET)).getText().toString();
                //Here goes Birthdate
                // Get the value of selected radio in the radio group in gender
                int selectedGender = genderRG.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedGender);
                String address = ((EditText) findViewById(R.id.addressET)).getText().toString();
                String contact = ((EditText) findViewById(R.id.contact_numberET)).getText().toString();
                // Get the value of selected hobbies in the checkbox
                String hobbies = "";
                String gender = "";

                if (radioButton.getId() == R.id.MaleRB) {
                    gender = "Male";
                }
                else if (radioButton.getId() == R.id.femaleRB) {
                    gender = "Female";
                }
                else if (radioButton.getId() == R.id.othersRB) {
                    gender = ((EditText) findViewById(R.id.genderOthersET)).getText().toString();
                }

                if (((CheckBox) findViewById(R.id.dancingCB)).isChecked()) {
                    hobbies += "Dancing, ";
                }
                if (((CheckBox) findViewById(R.id.singingCB)).isChecked()) {
                    hobbies += "Singing, ";
                }
                if (((CheckBox) findViewById(R.id.video_gamesCB)).isChecked()) {
                    hobbies += "Video Games, ";
                }
                if (((CheckBox) findViewById(R.id.playing_guitarCB)).isChecked()) {
                    hobbies += "Playing Guitar, ";
                }
                if (((CheckBox) findViewById(R.id.playing_pianoCB)).isChecked()) {
                    hobbies += "Playing Piano, ";
                }
                if (((CheckBox) findViewById(R.id.exerciseCB)).isChecked()) {
                    hobbies += "Exercise, ";
                }
                if (((CheckBox) findViewById(R.id.watching_anime_seriesCB)).isChecked()) {
                    hobbies += "Watching Anime Series, ";
                }
                if (((CheckBox) findViewById(R.id.watching_moviesCB)).isChecked()) {
                    hobbies += "Watching Movies, ";
                }
                if (((CheckBox) findViewById(R.id.readingCB)).isChecked()) {
                    hobbies += "Reading, ";
                }
                if (((CheckBox) findViewById(R.id.BasketballCB)).isChecked()) {
                    hobbies += "Basketball, ";
                }
                String question1 = spinner.getSelectedItem().toString();
                String question2 = spinner2.getSelectedItem().toString();
                String question3 = spinner3.getSelectedItem().toString();

                String answer1 = ((EditText) findViewById(R.id.answer1ET)).getText().toString();
                String answer2 = ((EditText) findViewById(R.id.answer2ET)).getText().toString();
                String answer3 = ((EditText) findViewById(R.id.answer3ET)).getText().toString();

                // Check if the password and confirm password match + show the password and confirm password + show an alert dialog
                if (!password.equals(confirmPassword)) {
                    showAlertDialog("Password Mismatch", "The password and confirm password do not match");
                    return;
                }

                // Check if the username is empty + show an alert dialog
                if (username.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a username");
                    return;
                }

                // Check if the password is empty + show an alert dialog
                if (password.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a password");
                    return;
                }

                // Check if the confirm password is empty + show an alert dialog
                if (confirmPassword.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a confirm password");
                    return;
                }

                // Check if the first name is empty + show an alert dialog
                if (firstName.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a first name");
                    return;
                }

                // Check if the last name is empty + show an alert dialog
                if (lastName.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a last name");
                    return;
                }

                // Check if the email is empty + show an alert dialog
                if (email.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter an email");
                    return;
                }

                // Check if the address is empty + show an alert dialog
                if (address.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter an address");
                    return;
                }

                // Check if the contact number is empty + show an alert dialog
                if (contact.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter a contact number");
                    return;
                }

                // Check if the question1 doesn't have the same value as question2 or question3 + show an alert dialog
                if (question1.equals(question2) || question1.equals(question3)) {
                    showAlertDialog("Duplicate Security Question", "Please select a different security question for each field");
                    return;
                }

                // Check if the question2 doesn't have the same value as question1 or question3 + show an alert dialog
                if (question2.equals(question1) || question2.equals(question3)) {
                    showAlertDialog("Duplicate Security Question", "Please select a different security question for each field");
                    return;
                }

                // Check if the question3 doesn't have the same value as question1 or question2 + show an alert dialog
                if (question3.equals(question1) || question3.equals(question2)) {
                    showAlertDialog("Duplicate Security Question", "Please select a different security question for each field");
                    return;
                }

                // Check if the first security question is empty + show an alert dialog
                if (answer1.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter an answer for the first security question");
                    return;
                }

                // Check if the second security question is empty + show an alert dialog
                if (answer2.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter an answer for the second security question");
                    return;
                }

                // Check if the third security question is empty + show an alert dialog
                if (answer3.isEmpty()) {
                    showAlertDialog("Empty Field", "Please enter an answer for the third security question");
                    return;
                }

                // Check if the hobbies is empty + show an alert dialog
                if (hobbies.isEmpty()) {
                    showAlertDialog("Empty Field", "Please select at least one hobby");
                    return;
                }

                // Check if the Date of Birth is empty + show an alert dialog
                if (((TextView) findViewById(R.id.datePickerTV)).getText().toString().equalsIgnoreCase("Click the button to select date")) {
                    showAlertDialog("Empty Field", "Please enter your date of birth");
                    return;
                }

                // Check if email is valid
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    showAlertDialog("Invalid Email", "Please enter a valid email address");
                    return;
                }

                // Check if contact number is valid
                if (contact.length() <= 10) {
                    showAlertDialog("Invalid Contact Number", "Please enter a valid contact number");
                    return;
                }

                // Check if password follows the rules - at least 8 characters, 1 uppercase, 1 lowercase, 1 number, 1 special character
                if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")) {
                    showAlertDialog("Invalid Password", "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number, and 1 special character");
                    return;
                }


                // If all the fields are filled, then proceed to the next step
                // In this case, we will just show a dialog box with the values
                String message = "Username: " + username + "\n" +
                        "Password: " + password + "\n" +
                        "First Name: " + firstName + "\n" +
                        "Last Name: " + lastName + "\n" +
                        "Email: " + email + "\n" +
                        "Date of Birth: " + ((TextView) findViewById(R.id.datePickerTV)).getText() + "\n" +
                        "Gender: " + gender + "\n" +
                        "Address: " + address + "\n" +
                        "Contact Number: " + contact + "\n" +
                        "Hobbies: " + hobbies + "\n" +
                        "Security Question 1: " + question1 + "\n" +
                        "Answer 1: " + answer1 + "\n" +
                        "Security Question 2: " + question2 + "\n" +
                        "Answer 2: " + answer2 + "\n" +
                        "Security Question 3: " + question3 + "\n" +
                        "Answer 3: " + answer3;


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Registration Details");
                builder.setMessage(message);
                builder.setCancelable(false);
                // On clicking the OK button, show a toast message telling the user that the registration was successful
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                        Intent registration_form_intent = new Intent(context, MainActivity.class);
                        registration_form_intent.putExtra("name", firstName + " " + lastName);
                        registration_form_intent.putExtra("username", username);
                        registration_form_intent.putExtra("password", password);

                        if (capturedImage != null) {
                            // Convert Bitmap to byte array to pass through Intent
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            capturedImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            registration_form_intent.putExtra("image_data", byteArray); // Pass the image data
                        }
                        startActivity(registration_form_intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    // Alert Dialog
    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Method to check if the camera permission is granted
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    // Method to request the camera permission
    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            // Show an explanation if needed
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_SHORT).show();
        }
        // Request the camera permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    // Handling the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the camera
                openCamera();
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the captured image here
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");

                // Save the captured image to a global variable
                profileImage = imageBitmap;

                capturedImage = imageBitmap;

                // Display the captured image in ImageView
                ((ImageView) findViewById(R.id.myImageView)).setImageBitmap(capturedImage);
                ((ImageView) findViewById(R.id.myImageView)).setVisibility(View.VISIBLE);

                // Show the "Take Another Photo" button
                ((Button) findViewById(R.id.takeAnotherPhotoBtn)).setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.takePhotoBtn)).setVisibility(View.GONE);
            }
        }
    }

    // Change the color of the spinner text
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
    }
}