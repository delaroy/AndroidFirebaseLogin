package com.delaroystudios.firebaselogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.delaroystudios.firebaselogin.Firebase.FirebaseDatabaseHelper;
import com.delaroystudios.firebaselogin.Firebase.FirebaseUserEntity;
import com.delaroystudios.firebaselogin.Helper.Helper;
import com.delaroystudios.firebaselogin.R;

public class EditProfileActivity extends AppCompatActivity {

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    private EditText editProfileName;

    private EditText editProfileCountry;

    private EditText editProfilePhoneNumber;

    private EditText editProfileHobby;

    private EditText editProfileBirthday;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        setTitle("Edit Profile Information");

        editProfileName = (EditText)findViewById(R.id.profile_name);
        editProfileCountry = (EditText)findViewById(R.id.profile_country);
        editProfilePhoneNumber = (EditText)findViewById(R.id.profile_phone);
        editProfileHobby = (EditText)findViewById(R.id.profile_hobby);
        editProfileBirthday = (EditText)findViewById(R.id.profile_birth);

        Button saveEditButton = (Button)findViewById(R.id.save_edit_button);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileName = editProfileName.getText().toString();
                String profileCountry = editProfileCountry.getText().toString();
                String profilePhoneNumber = editProfilePhoneNumber.getText().toString();
                String profileHobby = editProfileHobby.getText().toString();
                String profileBirthday = editProfileBirthday.getText().toString();

                // update the user profile information in Firebase database.
                if(TextUtils.isEmpty(profileName) || TextUtils.isEmpty(profileCountry) || TextUtils.isEmpty(profilePhoneNumber)
                        || TextUtils.isEmpty(profileHobby) || TextUtils.isEmpty(profileBirthday)){
                    Helper.displayMessageToast(EditProfileActivity.this, "All fields must be filled");
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Intent firebaseUserIntent = new Intent(EditProfileActivity.this, LoginActivity.class);
                    startActivity(firebaseUserIntent);
                    finish();
                } else {
                    String userId = user.getProviderId();
                    String id = user.getUid();
                    String profileEmail = user.getEmail();

                    FirebaseUserEntity userEntity = new FirebaseUserEntity(id, profileEmail, profileName, profileCountry, profilePhoneNumber, profileBirthday, profileHobby);
                    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                    firebaseDatabaseHelper.createUserInFirebaseDatabase(id, userEntity);

                    editProfileName.setText("");
                    editProfileCountry.setText("");
                    editProfilePhoneNumber.setText("");
                    editProfileHobby.setText("");
                    editProfileBirthday.setText("");
                }
            }
        });
    }
}
