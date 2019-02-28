package com.ceviche.sareb.salvisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ceviche.sareb.salvisapp.Clases.Usuarios;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class VerProducto extends AppCompatActivity {

    String titulo, descripcion, precio, categoria, estado, imagenproducto, imagenusuario, usuarioCreadorUid;
    private DatabaseReference usuariosDB;
    AppBarLayout encabezadoConFoto;


    String email, nombre, apellidos, biografia, pais, ciudad, direccion, fotoDePerfil, uid, tlfContacto;

    TextView tvTitulo, tvDescripcion, tvPrecio, tvEstado, tvCategoria, tvUsuario, tvEmail, tvMetodoContacto;

    ImageView ivFotoProducto, ivFotoUsuario;


    ///////////
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(VerProducto.this, InterfazPrincipalActivity.class));
                    break;
                case R.id.navigation_contactar:
                    ScrollView scroll = (ScrollView) findViewById(R.id.scrollproducto);
                    scroll.fullScroll(View.FOCUS_DOWN);
                    tvEmail.setText(Html.fromHtml("<b>Email: </b>" + email));

                    if (tlfContacto.isEmpty()) {
                        tlfContacto = "Sin métodos de contacto.";
                    }

                    tvMetodoContacto.setText(Html.fromHtml("<b>Método(s) de contacto: </b>" + tlfContacto));

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_producto_activity);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        tvTitulo = (TextView) findViewById(R.id.ilTextoTitulo);
        tvDescripcion = (TextView) findViewById(R.id.ilTextoDescripcion);
        tvPrecio = (TextView) findViewById(R.id.ilTextoPrecio);
        tvEstado = (TextView) findViewById(R.id.ilTextoEstado);
        tvCategoria = (TextView) findViewById(R.id.ilTextoCategoria);
        tvUsuario = (TextView) findViewById(R.id.ilTextoUsuario);

        tvEmail = (TextView) findViewById(R.id.ilTextoEmail);
        tvMetodoContacto = (TextView) findViewById(R.id.ilTextoMetodoContacto);

        ivFotoProducto = (ImageView) findViewById(R.id.ilImagenProducto);
        ivFotoUsuario = (ImageView) findViewById(R.id.ilImagenUsuario);
        //tvDescripcion.setText("111DescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcionDescripcion222");


        Intent outputInterfazPrincipalDatosUsu = getIntent();
        Bundle datos = outputInterfazPrincipalDatosUsu.getExtras();

        if (datos != null) {
            titulo = (String) datos.get("titulo");
            descripcion = (String) datos.get("descripcion");
            precio = (String) datos.get("precio");
            categoria = (String) datos.get("categoria");
            estado = (String) datos.get("estado");
            imagenproducto = (String) datos.get("imagenproducto");
            imagenusuario = (String) datos.get("imagenusuario");
            usuarioCreadorUid = (String) datos.get("usuarioCreadorUid");

            tvTitulo.setText(titulo);
            tvDescripcion.setText(Html.fromHtml("<b>Descripción: </b>" + descripcion));
            tvPrecio.setText(Html.fromHtml("<b>Precio: </b>" + precio + "€"));
            tvCategoria.setText(Html.fromHtml("<b>Categoría: </b>\n" + categoria));
            tvEstado.setText(Html.fromHtml("<b>Estado: </b>" + estado));

            if (imagenproducto.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.productosinfoto)
                        .into(ivFotoProducto);
            } else {
                Picasso.with(getApplicationContext())
                        .load(imagenproducto)
                        .placeholder(R.drawable.productosinfoto)
                        .into(ivFotoProducto);
            }

            if (imagenusuario.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.avatardefault)
                        .resize(370, 370)
                        .into(ivFotoUsuario);
            } else {
                Picasso.with(getApplicationContext())
                        .load(imagenusuario)
                        .placeholder(R.drawable.avatardefault)
                        .into(ivFotoUsuario);
            }

        } else {
            tvTitulo.setText("No se ha podido cargar los datos");
            tvDescripcion.setText(Html.fromHtml("<b>Descripción: </b>?"));
            tvPrecio.setText(Html.fromHtml("<b>Precio: </b>?"));
            tvCategoria.setText(Html.fromHtml("<b>Categoría: </b>?"));
            tvEstado.setText(Html.fromHtml("<b>Estado: </b>?"));

            Picasso.with(getApplicationContext())
                    .load(R.drawable.productosinfoto)
                    .into(ivFotoProducto);

            Picasso.with(getApplicationContext())
                    .load(R.drawable.avatardefault)
                    .resize(370, 370)
                    .into(ivFotoUsuario);
        }

        System.out.println("*\n*\n*\n*TITULO: " + titulo
                + "\nDescripcion:" + descripcion
                + "\nDescripcion:" + descripcion
                + "\nprecio:" + precio
                + "\ncategoria:" + categoria
                + "\nestado:" + estado
                + "\nimagenproducto:" + imagenproducto
                + "\nimagenusuario:" + imagenusuario
                + "\nusuarioCreadorUid:" + usuarioCreadorUid
                + "*\n*\n*");


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        usuariosDB = FirebaseDatabase.getInstance().getReference("Usuarios");
        DatabaseReference usuariosFirebase = FirebaseDatabase.getInstance().getReference("Usuarios").child("UsuariosRegistrados").child(usuarioCreadorUid);
        usuariosFirebase.addValueEventListener(valueEventListener);


    } //OnCreate


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Usuarios elemento = dataSnapshot.getValue(Usuarios.class);

            email = elemento.getEmail();
            nombre = elemento.getNombre();
            apellidos = elemento.getApellidos();
            biografia = elemento.getBiografia();
            pais = elemento.getPais();
            ciudad = elemento.getCiudad();
            direccion = elemento.getDireccion();
            fotoDePerfil = elemento.getFotoPerfil();
            uid = elemento.getUsuarioUid();
            tlfContacto = elemento.getTlfContacto();

           /* txtPerfilNombre.setText(nombre);
            txtPerfilApellidos.setText(apellidos);
            txtPerfilBiografia.setText(biografia);
            txtPerfilPais.setText(pais);
            txtPerfilCiudad.setText(ciudad);
            txtPerfilDireccion.setText(direccion);
*/
           /* if (fotoDePerfil.isEmpty()){
                Picasso.with(getApplicationContext())
                        .load(R.mipmap.imagenperfilpordefecto_round)
                        .resize(370,370)
                        .into(ivImagenPerfil);
            } else{
                Picasso.with(getApplicationContext())
                        .load(fotoDePerfil)
                        .placeholder(R.mipmap.imagenperfilpordefecto_round)
                        .resize(370,370)
                        .into(ivImagenPerfil);
            }

*/

            Log.e("[[[[[[[[[[[[[[nombreUsuario]]]]]]]]]]]]]", nombre);
            Log.e("[[[[[[[[[[[[[[apellidos]]]]]]]]]]]]]", apellidos);
            Log.e("[[[[[[[[[[[[[[pais]]]]]]]]]]]]]", pais);
            Log.e("[[[[[[[[[[[[[[ciudad]]]]]]]]]]]]]", ciudad);
            Log.e("[[[[[[[[[[[[[[fotoperfil]]]]]]]]]]]]]", fotoDePerfil);
            Log.e("[[[[[[[[[[[[[[UID]]]]]]]]]]]]]", uid);
            Log.e("[[[[[[[[[[[[[[email]]]]]]]]]]]]]", email);
            Log.e("[[[[[[[[[[[[[[direccion]]]]]]]]]]]]]", direccion);
            Log.e("[[[[[[[[[[[[[[tlfContacto]]]]]]]]]]]]]", tlfContacto);
            Log.e("[[[     --->INFO DE USUARIO REALIZADA<---     ]]]", "realizada");


            if (fotoDePerfil.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.avatardefault)
                        .resize(370, 370)
                        .into(ivFotoUsuario);
            } else {
                Picasso.with(getApplicationContext())
                        .load(fotoDePerfil)
                        .placeholder(R.drawable.avatardefault)
                        .into(ivFotoUsuario);
            }

            tvUsuario.setText(nombre);


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; // FIN EventListener


    @Override
    public void onBackPressed() {
        //super.onBackPressed();


        Intent intent = new Intent(VerProducto.this, InterfazPrincipalActivity.class);

        startActivity(intent);


    } //OnBackPressed*/


}
