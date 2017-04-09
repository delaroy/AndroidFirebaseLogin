package com.delaroystudios.firebaselogin.Firebase;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.delaroystudios.firebaselogin.Adapters.RecyclerViewAdapter;
import com.delaroystudios.firebaselogin.Helper.Helper;
import com.delaroystudios.firebaselogin.UserProfile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private static final String TAG = FirebaseDatabaseHelper.class.getSimpleName();

    private DatabaseReference databaseReference;

    public FirebaseDatabaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void createUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity){
        Map<String, FirebaseUserEntity> user = new HashMap<String, FirebaseUserEntity>();
        user.put(userId, firebaseUserEntity);
        databaseReference.child("users").setValue(user);
    }

    public void isUserKeyExist(final String uid, final Context context, final RecyclerView recyclerView){
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println("User login 1 " + dataSnapshot.getKey() + " " + dataSnapshot.getValue());
                List<UserProfile> userData = adapterSourceData(dataSnapshot, uid);
                System.out.println("User login Size " + userData.size());
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter((Activity)context, userData);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                List<UserProfile> userData = adapterSourceData(dataSnapshot, uid);
                System.out.println("User login Size " + userData.size());
                RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter((Activity)context, userData);
                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<UserProfile> adapterSourceData(DataSnapshot dataSnapshot, String uId){
        List<UserProfile> allUserData = new ArrayList<UserProfile>();
        if(dataSnapshot.getKey().equals(uId)){
            FirebaseUserEntity userInformation = dataSnapshot.getValue(FirebaseUserEntity.class);
            allUserData.add(new UserProfile(Helper.NAME, userInformation.getName()));
            allUserData.add(new UserProfile(Helper.EMAIL, userInformation.getEmail()));
            allUserData.add(new UserProfile(Helper.BIRTHDAY, userInformation.getBirthday()));
            allUserData.add(new UserProfile(Helper.PHONE_NUMBER, userInformation.getPhone()));
            allUserData.add(new UserProfile(Helper.HOBBY_INTEREST, userInformation.getHobby()));
        }
        return allUserData;
    }
}
