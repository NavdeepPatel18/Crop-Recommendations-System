package com.example.crp_recomendation.crprecom;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crp_recomendation.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class crop_recomen extends Fragment {

    FirebaseFirestore objectFirebaseFirestore;
    DocumentReference objectDocumentReference;
    Button btnGetCrop;

    TextInputEditText tieCrop;
    TextView tvDisplayCrop;
    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_crop_recomen,container,false);
        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        tieCrop =  view.findViewById(R.id.crop);
        tvDisplayCrop = view.findViewById(R.id.displaycrop);
        btnGetCrop = view.findViewById(R.id.btnGetCrop);


        btnGetCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCropData(v);
            }
        });

        return view;
    }


    public void getCropData(View view){
        try {
            if(!tieCrop.getText().toString().isEmpty()) {
                objectDocumentReference = objectFirebaseFirestore.collection("District_list").document(
                        tieCrop.getText().toString()
                );

                objectDocumentReference.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                final String Soil = documentSnapshot.getString("Soil_id");

                                objectFirebaseFirestore.collection("Crop_list")
                                        .whereEqualTo("Soil_id",Soil)
                                        .limit(1)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                List<DocumentSnapshot> doc=  queryDocumentSnapshots.getDocuments();

                                                int size = doc.size();


                                                String Crop_name = doc.get(0).getString("crop_name");
                                                String Crop_type = doc.get(0).getString("Crop_type");
                                                String Crop_term = doc.get(0).getString("Crop_term");
                                                //String soil = doc.get(0).getString("Soil_id");
                                                String Crop_price = doc.get(0).getString("crop_prices");
                                                String Crop_selling_Price = doc.get(0).getString("crop_selling_price");



                                                tvDisplayCrop.setText(
                                                        "Crop Name  \t\t\t\t\t" + Crop_name + "\n" +
                                                                "Term  \t\t\t\t\t" + Crop_type + "\n" +
                                                                "Type  \t\t\t\t\t" + Crop_term + "\n" +
                                                                "Soil id  \t\t\t\t\t" + "" + "\n" +
                                                                "Buying Price  \t\t\t\t\t" + Crop_price + "\n" +
                                                                "Selling Price  \t\t\t\t\t" + Crop_selling_Price + "\n"
                                                );
                                            }
                                        });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "field is empty" , Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
