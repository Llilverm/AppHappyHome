package com.example.apphappy.providers;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthProviders {

    private FirebaseAuth mAuth;

    public AuthProviders() {

        mAuth=FirebaseAuth.getInstance();
    }

    public Task<AuthResult> register(String email,String password){
        return mAuth.createUserWithEmailAndPassword(email, password);
    }



    //Este es el metodo que se encargara de Iniciar Sesion
    public Task<AuthResult> IniciarSesion(String email, String password){
        return mAuth.signInWithEmailAndPassword(email,password);
    }
    //Este es el metodo que se encargara de la autenticacion con google
    public Task<AuthResult>googleIniciarSesion(GoogleSignInAccount googleSignInAccount){
        AuthCredential credential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
        return mAuth.signInWithCredential(credential);
    }

    public String getEmail(){
        if (mAuth.getCurrentUser() !=null){
            return mAuth.getCurrentUser().getEmail();
        }else{
            return null;
        }

    }

    public String getUid(){
        if (mAuth.getCurrentUser() !=null){
            return mAuth.getCurrentUser().getUid();
        }else{
            return null;
        }
    }
}
