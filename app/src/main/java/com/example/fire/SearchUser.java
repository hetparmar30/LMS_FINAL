package com.example.fire;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchUser extends AppCompatActivity {
    private ListView list1;
    private SearchView SV;
    private List<String> namelist=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        SV = (SearchView) findViewById(R.id.SV);
        list1 = (ListView) findViewById(R.id.list1);
        Intent intent = getIntent();
        String name = intent.getStringExtra(UserInterface.EXTRA_PASS);
        ArrayAdapter adapter = new ArrayAdapter(SearchUser.this, android.R.layout.simple_list_item_1, namelist);
//        show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
        FirebaseFirestore.getInstance().collection("Admins").document(name).collection("Books").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                namelist.clear();
                for(DocumentSnapshot s: value){
                    namelist.add("\n"+"Book ID: "+s.getString("bookID")+"\n"+"Name of the Book: "+s.getString("nameOfBook")+"\n"+"Name of the Author: "+s.getString("nameOfAuthor")+"\n"+"Book Description: "+s.getString("bookDescription")+"\n");
                }

                adapter.notifyDataSetChanged();
                list1.setAdapter(adapter);
            }
        });

        SV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(namelist.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(SearchUser.this, "No Match found", Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                    adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}