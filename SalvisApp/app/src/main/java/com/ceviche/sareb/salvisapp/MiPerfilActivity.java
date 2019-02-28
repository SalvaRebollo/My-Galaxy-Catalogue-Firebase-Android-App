package com.ceviche.sareb.salvisapp;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceviche.sareb.salvisapp.Clases.Usuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class MiPerfilActivity extends AppCompatActivity {

    private static final String TAG = "TaG";
    ImageView ivImagenPerfil;

    boolean continuar = true;

    Button btnActualizarPerfil;
    TextView txtPerfilTlfContacto, txtPerfilNombre, txtPerfilApellidos, txtPerfilBiografia, txtPerfilPais, txtPerfilCiudad, txtPerfilDireccion;
    private ProgressDialog progressDialog;
    private DatabaseReference firebaseAuth;
    private DatabaseReference usuariosDB;
    private Uri imageUri;
    private StorageReference filePath;
    private StorageReference myFirebaseStorage;

    String email, nombre, apellidos, biografia, pais, ciudad, direccion, fotoDePerfil, uid, tlfContacto = "";
    private static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        getSupportActionBar().setTitle("MyGalaxyCatalogue");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round) ;
        getSupportActionBar().setSubtitle("Mi Perfil");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

        btnActualizarPerfil = (Button) findViewById(R.id.btnPerfilActualizar);
        txtPerfilNombre = (TextView) findViewById(R.id.txtPerfilNombre);
        txtPerfilApellidos = (TextView) findViewById(R.id.txtPerfilApellidos);
        txtPerfilBiografia = (TextView) findViewById(R.id.txtPerfilBiografia);
        txtPerfilPais = (TextView) findViewById(R.id.txtPerfilPais);
        txtPerfilCiudad = (TextView) findViewById(R.id.txtPerfilCiudad);
        txtPerfilDireccion = (TextView) findViewById(R.id.txtPerfilDireccion);
        txtPerfilTlfContacto = (TextView) findViewById(R.id.txtPerfilTlfContacto);
        progressDialog = new ProgressDialog(this);

        //Declaramos un objeto FirebaseUser llamado user para obtener de el por ejemplo su Uid a posteriori con diferentes métodos. Tambien vamos a configuar el acceso anónimo sin usuario registrado
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseDatabase.getInstance().getReference();
        usuariosDB = FirebaseDatabase.getInstance().getReference("Usuarios");
        //STORAGE2 obtenemos la referencia de storage de firebase e inicializarla.
        myFirebaseStorage = FirebaseStorage.getInstance().getReference();
        /* Consulta Firebase: SELECT * FROM Usuarios/UsuariosRegistrados WHERE ChildUid = user.getUid();Referencia a la base de datos a la tabla de nuestros usuarios*/
        DatabaseReference usuariosFirebase = FirebaseDatabase.getInstance().getReference("Usuarios").child("UsuariosRegistrados").child(user.getUid());
        //////// Lo añado al Listener "valueEventListener" creado debajo del OnCreate. Con addListenerForSingleValueEvent lo que hago es obtener los datos del listener 1 VEZ y no estoy continuamente a la escucha para actualizar el dato
        usuariosFirebase.addValueEventListener(valueEventListener);


        String url = "https://i.imgur.com/7uZE61J.png";


        ivImagenPerfil = (ImageView) findViewById(R.id.imagenPerfil);
        //ivImagenPerfil.setImageResource(R.mipmap.imagenperfilpordefecto);
        Picasso.with(getApplicationContext())
                .load(R.drawable.avatardefault)
                //.load(R.mipmap.imagenperfilpordefecto)
                .placeholder(R.drawable.avatardefault)
                .into(ivImagenPerfil);
        Log.e("WE","SE PUSO LA FOTO POR DEFECTO");




        /*On Click Listener BOTON ACTUALIZAR PERFIL */
        btnActualizarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (continuar == true) {
                    actualizarPerfil();
                } else {
                    Toast.makeText(MiPerfilActivity.this, "Subiendo fotografía, por favor espere Ó presione 'BOTÓN ATRÁS' para cancelar.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        /*FIN On Click Listener*/

        /*On Click Listener IMAGEN PERFIL */
        ivImagenPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                continuar = false;
                System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, GALLERY_INTENT); //SUBE IMAGEN4


            }
        });
        /*FIN On Click Listener*/


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

            txtPerfilNombre.setText(nombre);
            txtPerfilApellidos.setText(apellidos);
            txtPerfilBiografia.setText(biografia);
            txtPerfilPais.setText(pais);
            txtPerfilCiudad.setText(ciudad);
            txtPerfilDireccion.setText(direccion);
            txtPerfilTlfContacto.setText(tlfContacto);

            if (fotoDePerfil.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.avatardefault)
                        .resize(370, 370)
                        .into(ivImagenPerfil);
                Log.e("ISEMPTY","SE PUSO LA FOTO DEL ISEMPTY POR DEFECTO");
            } else {
                Picasso.with(getApplicationContext())
                        .load(fotoDePerfil)
                        .placeholder(R.drawable.animacioncargar)
                        //.resize(370, 370)//da problemas con imagenes grandes
                        .into(ivImagenPerfil);
                Log.e("PERFIL","SE PUSO FOTO PERFIL");
            }


            Log.e("[[[[[[[[[[[[[[nombreUsuario]]]]]]]]]]]]]", nombre);
            Log.e("[[[[[[[[[[[[[[apellidos]]]]]]]]]]]]]", apellidos);
            Log.e("[[[[[[[[[[[[[[pais]]]]]]]]]]]]]", pais);
            Log.e("[[[[[[[[[[[[[[ciudad]]]]]]]]]]]]]", ciudad);
            Log.e("[[[[[[[[[[[[[[fotoperfil]]]]]]]]]]]]]", fotoDePerfil);
            Log.e("[[[[[[[[[[[[[[UID]]]]]]]]]]]]]", uid);
            Log.e("[[[     --->INFO DE USUARIO REALIZADA<---     ]]]", "realizada");


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; // FIN EventListener


    public void actualizarPerfil() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String nombre = txtPerfilNombre.getText().toString().trim();
        final String apellidos = txtPerfilApellidos.getText().toString().trim();
        final String biografia = txtPerfilBiografia.getText().toString().trim();
        final String pais = txtPerfilPais.getText().toString().trim();
        final String ciudad = txtPerfilCiudad.getText().toString().trim();
        final String direccion = txtPerfilDireccion.getText().toString().trim();
        final String fotoperfil = fotoDePerfil;
        final String tlfContacto = txtPerfilTlfContacto.getText().toString().trim();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(nombre)) {
            Toast.makeText(this, "No puedes dejar el nombre vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(apellidos)) {
            Toast.makeText(this, "No puedes dejar el apellido vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(tlfContacto)) {
            Toast.makeText(this, "Si quieres subir productos, debes introducir un teléfono de contacto, en caso de no necesitarlo, introducir cualquier otro método de contacto.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(apellidos) && !TextUtils.isEmpty(tlfContacto)) {

            progressDialog.setMessage("Actualizando tu perfil en linea...");
            progressDialog.show();


            Usuarios usuario = new Usuarios(email, uid, nombre, apellidos, fotoperfil, biografia, pais, ciudad, direccion, tlfContacto);

            usuariosDB.child("UsuariosRegistrados").child(uid).setValue(usuario);
            // FIN LO GUARDA EN DATABASE

            progressDialog.dismiss();

            Toast.makeText(MiPerfilActivity.this, "Perfil actualizado correctamente :)", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(MiPerfilActivity.this, InterfazPrincipalActivity.class));


        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            imageUri = data.getData();


            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .placeholder(R.drawable.avatardefault)
                    .into(ivImagenPerfil);

            // Creamos una carpeta en el storage (hijo) y guardamos el uri EL CUAL ES LA FOTO
            //filePath = myFirebaseStorage.child("FotosDePerfil").child(uid).child(imageUri.getLastPathSegment());
            filePath = myFirebaseStorage.child("InfoDePerfilUsuarios").child(uid).child(imageUri.getLastPathSegment() + "avatar");

            //Subimos la foto
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(MiPerfilActivity.this, "Se subió exitosamente la fotografía.", Toast.LENGTH_SHORT).show();

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            fotoDePerfil = uri.toString();
                            continuar = true;
                            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
                        }
                    });


                }
            });


        } else {
            continuar = true;
            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent(MiPerfilActivity.this, MenuNavegacion.class);
        startActivity(intent);
        finish();


    }//onBackPressed

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }//onStart


}
