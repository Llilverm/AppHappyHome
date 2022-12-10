package com.example.apphappy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.apphappy.R;
import com.example.apphappy.models.User;
import com.example.apphappy.providers.AuthProviders;
import com.example.apphappy.providers.UsersProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import dmax.dialog.SpotsDialog;

public class CompleteProfileActivity extends AppCompatActivity {
    TextInputEditText mTextImputUsername;
    Button mButtonRegister;
    //FirebaseAuth mAuth;
    //FirebaseFirestore mFirestore;

    AuthProviders mAuthProviders;
    UsersProviders mUsersproviders;
    AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_profile);

        mTextImputUsername=findViewById(R.id.textInputEditTextUsernameCompletar);
        mButtonRegister=findViewById(R.id.btnregistroCompletar);

        //Aca los estoy instanciando
        //mAuth=FirebaseAuth.getInstance();
        //mFirestore=FirebaseFirestore.getInstance();

        mAuthProviders=new AuthProviders();
        mUsersproviders=new UsersProviders();

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();

        //Aca estoy creando el metodo

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String username=mTextImputUsername.getText().toString();
        if(!username.isEmpty()){
            updateUser(username);
        }else{
            Toast.makeText(this, "Para continuar inserta el nombre del usuario", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUser(String username) {
        //esto ..String id=mAuth.getCurrentUser().getUid(); se reemplazo por ....String id=mAuthProviders.getUid();

        String id=mAuthProviders.getUid();
        User user=new User();
        user.setUsername(username);
        user.setId(id);
         //Aca el profe en la gravacion dijo setId pero selecciono setEmail?????????????????
        mDialog.show();


        //cambiamos ... mFirestore.collection("Users").document(id).update(map) por ....

        //Map<String,Object>map=new HashMap<>();
        //map.put("username",username);

        mUsersproviders.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){
                    Intent intent=new Intent(CompleteProfileActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);


                    startActivity(intent);                    
                }else{
                    Toast.makeText(CompleteProfileActivity.this, "No se almaceno el usuario en la base de datos", Toast.LENGTH_SHORT).show();
                    
                }

            }
        });
    }


}