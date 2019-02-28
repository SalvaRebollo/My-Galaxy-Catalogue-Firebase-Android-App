package com.ceviche.sareb.salvisapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ceviche.sareb.salvisapp.Adaptadores.AdaptadorFirebaseProductos;
import com.ceviche.sareb.salvisapp.Adaptadores.AdaptadorListMisProductos;
import com.ceviche.sareb.salvisapp.Adaptadores.AdaptadorListNovedades;
import com.ceviche.sareb.salvisapp.Clases.Novedades;
import com.ceviche.sareb.salvisapp.Clases.Productos;
import com.ceviche.sareb.salvisapp.Clases.UsuarioProductosItemListClass;
import com.ceviche.sareb.salvisapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NovedadesDesarrollo extends AppCompatActivity {


    String cabecera, cuerpo;

    ArrayList<Novedades> listaDeNovedades = new ArrayList<>();

    RecyclerView recyclerNovedades;

    DatabaseReference novedadesFirebase = FirebaseDatabase.getInstance().getReference("Novedades");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novedades_desarrollo);




        recyclerNovedades = findViewById(R.id.recyclerNovedades);

        // NOVEDADES FIREBASE

        Query query = FirebaseDatabase.getInstance().getReference("Novedades").child("");


        query.addValueEventListener(valueEventListener);

        //novedadesFirebase.addValueEventListener(valueEventListener);


    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Novedades novedadi = snapshot.getValue(Novedades.class);

                    listaDeNovedades.add(novedadi); //LISTA CON TODOS LOS PRODUCTOS GUARDADOS

                    String cabecera = novedadi.getCabecera();
                    String cuerpo = novedadi.getCuerpo();


                    /*Log.e("[[[[[[[[[[[[[[titulo]]]]]]]]]]]]]", titulo);
                    Log.e("[[[[[[[[[[[[[[productoid]]]]]]]]]]]]]", productoid);
                    Log.e("[[[[[[[[[[[[[[usuarioCreadorUid]]]]]]]]]]]]]", usuarioCreadorUid);
                    Log.e("[[[[[[[[[[[[[[descripcion]]]]]]]]]]]]]", descripcion);
                    Log.e("[[[[[[[[[[[[[[precio]]]]]]]]]]]]]", precio);
                    Log.e("[[[[[[[[[[[[[[categoria]]]]]]]]]]]]]", categoria);
                    Log.e("[[[[[[[[[[[[[[estado]]]]]]]]]]]]]", estado);
                    Log.e("[[[[[[[[[[[[[[imagen]]]]]]]]]]]]]", imagen);
                    Log.e("[[[[[[[[[[[[[[nombreUsuarioCreador]]]]]]]]]]]]]", nombreUsuarioCreador);
                    Log.e("[[[[[[[[[[[[[[fotoUsuarioCreador]]]]]]]]]]]]]", fotoUsuarioCreador);


                    Log.e("[[[     --->INFO DE PRODUCTOS REALIZADA<---     ]]]", "realizada");
                    */
                }

                for (Novedades novedades : listaDeNovedades) {

                    cabecera = novedades.getCabecera();
                    System.out.println("*\n*\n[cabecera]" + cabecera);

                    cuerpo = novedades.getCuerpo();
                    System.out.println("*\n*\n[cuerpo]" + cuerpo);

                    System.out.println("************************************************" +
                            "\n************************************************");

                }

                // AdaptadorListMisProductos adaptadorListMisProductos = new AdaptadorListMisProductos(UsuarioProductosItemListClass.class, R.layout.item_list_productos,
                //       AdaptadorListMisProductos.ViewHolderMisProductos.class, misProductosLista, MisProductosActivity.this);


                recyclerNovedades.setLayoutManager(new GridLayoutManager(NovedadesDesarrollo.this, 1,
                        LinearLayoutManager.VERTICAL, false));

                AdaptadorListNovedades adapter = new AdaptadorListNovedades(listaDeNovedades, NovedadesDesarrollo.this);

                recyclerNovedades.setAdapter(adapter);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }


    };


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent(NovedadesDesarrollo.this, MenuNavegacion.class);
        startActivity(intent);
        finish();


    }

}
