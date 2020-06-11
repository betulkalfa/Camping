package com.sbk.camping.malzeme;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sbk.camping.R;
import com.sbk.camping.model.Malzeme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MalzemeAdapter extends RecyclerView.Adapter<MalzemeAdapter.RowHolder> {


    private List<Malzeme> malzemeList;
    DatabaseReference myRef;
    DatabaseReference ref;
    Task<Void> df;
    private Context context;
    FirebaseDatabase database;
    private Malzeme malzeme;





    public MalzemeAdapter(List<Malzeme> malzemeList, Context context) {
        this.malzemeList = malzemeList;
        this.context=context;

    }

    public class RowHolder extends RecyclerView.ViewHolder {
        private TextView title,description;
        private Button button,button2;
        public RowHolder( View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            button=itemView.findViewById(R.id.button);
            button2=itemView.findViewById(R.id.button2);
        }
    }


    @NonNull
    @Override
    public RowHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_title_description,parent,false);
        return new  RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RowHolder holder, int position) {

       final Malzeme malzeme = malzemeList.get(position);
       holder.title.setText(malzeme.getAdi());
       holder.description.setText(malzeme.getTuru());
       holder.button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

              deleteMalzeme(malzeme.getId());
           }
       });

       holder.button2.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               updateMalzeme(malzeme);
           }
       });

    }


    @Override
    public int getItemCount() {
        return malzemeList != null ? malzemeList.size() : 0;
    }


    private void deleteMalzeme(final  String id) {

        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Kamp Malzemesi Silinsin mi?");

        ad.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String cihazID=Settings.Secure.getString(context.getContentResolver() ,Settings.Secure.ANDROID_ID);

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference(cihazID);
                ref= myRef.child("malzeme").child(id);

                ref.removeValue();


            }
        });

        ad.setNegativeButton("İptal", null);

        ad.create().show();

    }



    public void updateMalzeme(final Malzeme malzeme){



        LayoutInflater layout = LayoutInflater.from(context);
        View tasarim = layout.inflate(R.layout.alert_malzeme_ekle, null);
        final EditText edtMalzemeAdi = tasarim.findViewById(R.id.edtMalzemeUrunAd);
        final Spinner spMalzemeTur = tasarim.findViewById(R.id.sp);

        edtMalzemeAdi.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        final AlertDialog.Builder ad = new AlertDialog.Builder(context);
        ad.setTitle("Kamp Malzemesi Güncelleyin");
        ad.setView(tasarim);

        spMalzemeTur.setSelection(spMalzemeTur.getSelectedItemPosition());
        spMalzemeTur.getSelectedItem();

       edtMalzemeAdi.setText(malzeme.getAdi());



        ad.setPositiveButton("Güncelle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String adi=edtMalzemeAdi.getText().toString().trim();
                int pozisyon = spMalzemeTur.getSelectedItemPosition();
                String turu = (String) spMalzemeTur.getItemAtPosition(pozisyon);

                Map<String,Object> bilgiler = new HashMap<>();


                bilgiler.put("adi",adi);
                bilgiler.put("turu",turu);

                String cihazID=Settings.Secure.getString(context.getContentResolver() ,Settings.Secure.ANDROID_ID);

                database = FirebaseDatabase.getInstance();
                myRef = database.getReference(cihazID);

                df= myRef.child("malzeme").child(malzeme.getId()).updateChildren(bilgiler);

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtMalzemeAdi.getWindowToken(), 0);



            }
        });

        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edtMalzemeAdi.getWindowToken(), 0);
            }
        });

        ad.create().show();
    }

}


