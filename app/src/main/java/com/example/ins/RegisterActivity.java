package com.example.ins;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    EditText username, fullname, email, password;
    Button register;
    TextView txt_login;
    TextView txt_hint,if_success;

    FirebaseAuth auth;
    DatabaseReference reference;
    ProgressDialog pd;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super. onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        txt_hint=findViewById(R.id.text_hint);
        auth = FirebaseAuth. getInstance();
        txt_login = findViewById(R.id.txt_login);
        if_success= findViewById(R.id.if_success);
        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
     /*   register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Plz ");
                pd.show();
                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_password= password.getText().toString();

                if(username.getText().toString().isEmpty()||fullname.getText().toString().isEmpty()||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(RegisterActivity.this,"ALL",Toast.LENGTH_SHORT).show();
                }
            }
        });*/
       register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
//           pd = new ProgressDialog(RegisterActivity.this);
//           pd.setMessage("Please wait....");
//           pd.show();

           String str_username = username.getText().toString();
           String str_fullname = fullname.getText().toString();
           String str_email = email.getText().toString();
           String str_password= password.getText().toString();

           if (username.getText().toString().isEmpty()||TextUtils.isEmpty(str_fullname)||TextUtils.isEmpty(str_email)||TextUtils.isEmpty(str_password)){
              // Toast.makeText (RegisterActivity.this, "All fileds are required!", Toast.LENGTH_SHORT).show();
               txt_hint.setText("All fileds are required!");
           } else if (str_password.length() <6) {
               //Toast.makeText(RegisterActivity.this, "Password must have 6 characters", Toast.LENGTH_SHORT).show();
               //register.setText("HI");
               txt_hint.setText("Password must have 6 characters");
           } else {
               register(str_username, str_fullname, str_email, str_password);
              txt_hint.setText(str_username+","+str_fullname+","+str_email+","+str_password);
               }

       }
    });}

    private void register(final String username, final String fullname, String email, String password) {
        auth.createUserWithEmailAndPassword (email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful ()) {

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            if_success.setText("successful");

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);

                            HashMap<String, Object> hashMap = new HashMap<>();;
                            hashMap.put("id", userid);
                            hashMap.put("username", username.toLowerCase());
                            hashMap.put("fullname", fullname);
                            hashMap.put("bio", "");
                            hashMap.put("imageurl", "https://firebasestorage.googleapis.com/v0/b/insta-60161.appspot.com/o/indigenous-removebg-preview.png?alt=media&token=25f31538-7c4a-4452-bdcc-5aac3fe01ea3");
                            // go to firebase----> storage ----> get started-----> got it----->upload file----->select a picture as placeholder-----> file location------> copy imageurl

                            reference. setValue(hashMap). addOnCompleteListener(new OnCompleteListener<Void>()  {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //pd.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);//change back to main activity
                                        intent. addFlags (Intent. FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            //pd.dismiss();
                            //Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                            if_success.setText("You can't register with this email or password");
                        }
                    }
                } );

    }
}
