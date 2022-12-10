package com.example.apphappy.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apphappy.R;
import com.example.apphappy.models.User;
import com.example.apphappy.providers.AuthProviders;
import com.example.apphappy.providers.UsersProviders;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {


    TextView mTextViewRegister;

    TextInputEditText mTextInputEditTextEmail;
    TextInputEditText mTextInputEditTextPassword;
    Button mButtonIniciarSesion;
    SignInButton mBtnGoogle;

    AuthProviders mAuthProviders;


    private GoogleSignInClient mGoogleSignInClient; //Esto se copio del repositorio del link de la guia,como no aparecia se importo la clase xq estana encapsulada  y aparecio en la linea 14
    private final int REQUEST_CODE_GOOGLE=1;        //este se creo para poder traer el cliente a traves de un codigo, esto va ligado a la linea 80 y esta de tipo final" ara que solo me deje hacer una autenticacion

     UsersProviders mUsersproviders;               //FirebaseFirestore mFirestore;este se creo para que si la tarea es exitosa me almacene en la coleccion el usuario pero se borro y se cambio al revisar la arquitectura

    AlertDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextInputEditTextEmail=findViewById(R.id.textInputEditTextEmail);
        mTextInputEditTextPassword=findViewById(R.id.textInputEditTextPassword);
        mButtonIniciarSesion=findViewById(R.id.btnIniciarSesion);
        mBtnGoogle=findViewById(R.id.btnloginSignInGoogle);


        mAuthProviders=new AuthProviders();              //aca se instancio el metodo
        mUsersproviders=new UsersProviders();           //mFirestore=FirebaseFirestore.getInstance();aca se instancio luego se borro y cambio al hacer el reordenamiento en la arquitectura

        mDialog=new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espere un momento...")
                .setCancelable(false)
                .build();





        mBtnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        //Esto se copio del repositorio del link de la guia, desde GoogleSignInOptions gso hasta .build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        mButtonIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniciarSesion();
            }
        });




        mTextViewRegister=findViewById(R.id.TextViewRegister);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    //aca se creo el metodo Y se debe
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Error", "Google sign in failed", e);
            }
        }
    }





    private void firebaseAuthWithGoogle(GoogleSignInAccount account){
        mDialog.show();
        mAuthProviders.googleIniciarSesion(account)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    String id=mAuthProviders.getUid();
                    checkUserExist(id);
                    //si la señal es correcta continua con la informacion del usuario
                    
                }else {
                    mDialog.dismiss();

                    //si la señal es errada muestre un mensaje de error al usuario
                    Log.w("Error","sigInWitheCredential:failure",task.getException());
                }
            }
        });

    }

    private void checkUserExist(final String id) {
        //mFirestore.collection("Users").document(id).get() se cambio por mUsersproviders.getUser(id)
        mUsersproviders.getUser(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                mDialog.dismiss();
                if (documentSnapshot.exists()){
                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);//aca le estoy diciendo al boton de google que se vaya al home principal
                    startActivity(intent);
                }else{
                    String email=mAuthProviders.getEmail();
                    User user=new User();
                    user.setEmail(email);
                    user.setId(id);

                    // Map<String,Object>map=new HashMap<>();
                    // map.put("email",email);
                    // se elimino la siguiente linea  mFirestore.collection("Users").document(id).set(map)
                    mUsersproviders.create(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mDialog.dismiss();
                            if(task.isSuccessful()){
                                Intent intent=new Intent(MainActivity.this,CompleteProfileActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(MainActivity.this, "No se pudo almacenar el usuario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }


    private void IniciarSesion() {
        String email=mTextInputEditTextEmail.getText().toString();
        String password=mTextInputEditTextPassword.getText().toString();

        mDialog.show();

        //este se creo para poder verificar que el email y la contraseña esten bien para luego llevarnos al Home
        mAuthProviders.IniciarSesion(email,password).
        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mDialog.dismiss();
                if (task.isSuccessful()){

                    Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "El email y la contraseña NO son correctos", Toast.LENGTH_SHORT).show();
                }
            }
        });



        Log.d("campo","email"+email);
        Log.d("campo","password"+password);
    }
}