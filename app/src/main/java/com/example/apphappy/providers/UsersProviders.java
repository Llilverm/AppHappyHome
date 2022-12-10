package com.example.apphappy.providers;

import com.example.apphappy.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UsersProviders {

    private CollectionReference mcollection;

    public UsersProviders() {
        mcollection=FirebaseFirestore.getInstance().collection("Users");
    }

    public Task<DocumentSnapshot> getUser(String id){ return mcollection.document(id).get(); }

    public Task<Void> create(User user){
        return mcollection.document(user.getId()).set(user);
    }

    public Task<Void> update(User user){

        Map<String,Object>map=new HashMap<>();
        map.put("username",user.getUsername());
        //map.put("email",user.getEmail());                     se comentaron estas 2 lineas para que en el firebase database me cargue bien la identificacion con google
        //map.put("password",user.getPassword());               se comentaron estas 2 lineas para que en el firebase database me cargue bien la identificacion con google
        return mcollection.document(user.getId()).update(map);
    }


}
