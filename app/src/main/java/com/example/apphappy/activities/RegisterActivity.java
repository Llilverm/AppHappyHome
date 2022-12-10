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
import com.google.firebase.auth.AuthResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class RegisterActivity extends AppCompatActivity {

    CircleImageView mCircleImageViewBack;
    CircleImageView mCircleImageViewBack2;

    TextInputEditText mtextInputEditTextUsername;
    TextInputEditText mtextInputEditTextEmailRegistro;
    TextInputEditText mtextInputEditTextPasswordRegistro;
    TextInputEditText mtextInputEditTextConfirmPasswordRegistro;
    Button mButtonRegister;

    //FirebaseAuth mAut;
    //FirebaseFirestore mFirestore;

    AuthProviders mAuthProviders;
    UsersProviders mUsersProviders;
    AlertDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //instancias

        mtextInputEditTextUsername=findViewById(R.id.textInputEditTextUsername);
        mtextInputEditTextEmailRegistro=findViewById(R.id.textInputEditTextEmailRegistro);
        mtextInputEditTextPasswordRegistro=findViewById(R.id.textInputEditTextPasswordRegistro);
        mtextInputEditTextConfirmPasswordRegistro=findViewById(R.id.textInputEditTextConfirmPasswordRegistro);
        mButtonRegister=findViewById(R.id.btnregistro);


        //mAut=FirebaseAuth.getInstance();
        //mFirestore=FirebaseFirestore.getInstance();

        mAuthProviders =new AuthProviders();
        mUsersProviders=new UsersProviders();

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();



        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se crea el metodo para que el boton register osea REGISTRARSE en la app traiga los datos ingresados
                register();
            }
        });


        mCircleImageViewBack=findViewById(R.id.circleimageback);
        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCircleImageViewBack2=findViewById(R.id.circleimageback2);
        mCircleImageViewBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void register() {
        String username=mtextInputEditTextUsername.getText().toString();
        String emailregistro=mtextInputEditTextEmailRegistro.getText().toString();
        String passwordregistro=mtextInputEditTextPasswordRegistro.getText().toString();
        String confirmpasswordregistro=mtextInputEditTextConfirmPasswordRegistro.getText().toString();

        if (!username.isEmpty() && !emailregistro.isEmpty() && !passwordregistro.isEmpty() && !confirmpasswordregistro.isEmpty()){

            if(isEmailValid(emailregistro)){
                if(passwordregistro.equals(confirmpasswordregistro)){
                    if(passwordregistro.length()>=6){
                        createUser(username,emailregistro,passwordregistro);
                    }else{
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(this, "Las contraseñas No coinciden",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Se inserto todos los campos y el correo es valido",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this,"Se insertaron todos los campos pero el correo No es valido",Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this,"Has insertado todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Para continuar insertar todos los campos",Toast.LENGTH_SHORT).show();
        }
    }

    private void createUser(final String username,final String emailregistro, final String passwordregistro) {
        mDialog.show();

        //esta parte mAut.createUserWithEmailAndPassword(emailregistro, passwordregistro) se reemplazo por mAuthProviders.register(emailregistro,passwordregistro)

        mAuthProviders.register(emailregistro,passwordregistro)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id=mAuthProviders.getUid(); //asi me traigo los id de emailregistro,passewordregistro,etc
                                                                // se cambio mAut.getCurrentUser() por mAuthProviders
                    //Map<String,Object> map =new HashMap<>();
                    //map.put("Email",emailregistro);
                    //map.put("username",username);
                    //map.put("password",passwordregistro);
                    //Asi me traigo los datos para ser insertados en la coleccion de firestore

                    User user=new User();
                    user.setId(id);
                    user.setEmail(emailregistro);
                    user.setUsername(username);
                    user.setPassword(passwordregistro);

                    // se reemplazo mFirestore.collection("Users").document(id).set(map) por mUsersProviders.create(user)

                    mUsersProviders.create(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"El usuario se almaceno correctamente",Toast.LENGTH_SHORT).show();

                                //ESTE ES EL METODO DE LIMPIAR LAS BANDERAS PARA INICIAR UNA NUEVA ACTIVIDAD
                                //esto es para que despues de registrarse se vaya al Home
                                Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
                                //esto es para que me limpie la aplicacion y no quede en segundo plano y asi la actividad muera
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }else {
                                Toast.makeText(RegisterActivity.this,"No se pudo almacenar en la base de datos",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Toast.makeText(RegisterActivity.this, "El usuario se ha registrado correctamente",Toast.LENGTH_SHORT).show();
                }else{
                    mDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "No se pudo registrar el usuario",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean isEmailValid(String emailregistro) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailregistro);
        return matcher.matches();
    }

}