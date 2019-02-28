package com.ceviche.sareb.salvisapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ceviche.sareb.salvisapp.Clases.Productos;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditarProducto extends AppCompatActivity {

    String idProductoAEditar;
    String nuevaFotoProducto = "SINFOTO";
    DatabaseReference usuariosFirebase;
    DatabaseReference productosFirebase;

    ///////////////////////


    boolean continuar = true;

    Button btnEditarProducto, btnEleg;
    TextView editTxtTitulo, editTxtDescripcion, editTxtPrecio;
    Spinner editSpinEstado, editSpinCategoria;
    ImageView editIvImagenProducto;

    private ProgressDialog progressDialog;
    private DatabaseReference firebaseAuth;
    private DatabaseReference productosDB;
    private Uri imageUri;
    private StorageReference filePath;
    private StorageReference myFirebaseStorage;

    String productoid, usuarioCreadorUid, titulo, descripcion, precio, categoria, estado, imagen, nombreUsuarioCreador, fotoUsuarioCreador;

    String email, nombre, apellidos, biografia, pais, ciudad, direccion, fotoDePerfil, uid, tlfContacto = "";
    private static final int GALLERY_INTENT = 1;
    /////////////////////////////////////////////////////////////////



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);


        Intent outputEditarProductoId = getIntent();
        Bundle datos = outputEditarProductoId.getExtras();


        if (datos != null) {
            idProductoAEditar = (String) datos.get("idProductoAEditar");

            System.out.println("*\n*\n*\n*\n***idProductoAEditar: " + idProductoAEditar + "*\n*\n*\n*\n***");

        } else {
            idProductoAEditar = "ERROR, NO SE PUDO OBTENER EL ID DEL PRODUCTO ELEGIDO";
            System.out.println("*\n*\n*\n*\n***idProductoAEditar: " + idProductoAEditar + "*\n*\n*\n*\n***");
        }





        //////////////////////////////////////////////////////////////////////////////////////////////////

        getSupportActionBar().setTitle("MyGalaxyCatalogue");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round) ;
        getSupportActionBar().setSubtitle("Mi Perfil");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

        editTxtTitulo = (TextView) findViewById(R.id.edittxttitulo);
        editTxtDescripcion = (TextView) findViewById(R.id.edittxtdesc);
        editTxtPrecio = (TextView) findViewById(R.id.edittxtprecio);
        editSpinEstado = (Spinner) findViewById(R.id.editspinestado);
        editSpinCategoria = (Spinner) findViewById(R.id.editspincateg);
        editIvImagenProducto = (ImageView) findViewById(R.id.editimgproducto);

        btnEditarProducto = (Button) findViewById(R.id.editbtneditarproducto);
        btnEleg = (Button) findViewById(R.id.editbtneleg);
        progressDialog = new ProgressDialog(this);

        //Declaramos un objeto FirebaseUser llamado user para obtener de el por ejemplo su Uid a posteriori con diferentes métodos. Tambien vamos a configuar el acceso anónimo sin usuario registrado
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseDatabase.getInstance().getReference();

        //STORAGE2 obtenemos la referencia de storage de firebase e inicializarla.
        myFirebaseStorage = FirebaseStorage.getInstance().getReference();
        /* Consulta Firebase: SELECT * FROM Usuarios/UsuariosRegistrados WHERE ChildUid = user.getUid();Referencia a la base de datos a la tabla de nuestros usuarios*/
        productosFirebase = FirebaseDatabase.getInstance().getReference("Productos").child("ProductosRegistrados").child(idProductoAEditar);
        //////// Lo añado al Listener "valueEventListener" creado debajo del OnCreate. Con addListenerForSingleValueEvent lo que hago es obtener los datos del listener 1 VEZ y no estoy continuamente a la escucha para actualizar el dato
        productosFirebase.addListenerForSingleValueEvent(valueEventListener);

        //FirebaseDatabase.getInstance().getReference("Usuarios").child("UsuariosRegistrados");



        //ivImagenPerfil.setImageResource(R.mipmap.imagenperfilpordefecto);
        Picasso.with(getApplicationContext())
                .load(R.drawable.productosinfoto)
                //.load(R.mipmap.imagenperfilpordefecto)
                .placeholder(R.drawable.avatardefault)
                .into(editIvImagenProducto);
        Log.e("WE","SE PUSO LA FOTO POR DEFECTO");




        /*On Click Listener BOTON ACTUALIZAR PERFIL */
        btnEditarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (continuar == true) {
                    editarProducto();
                } else {
                    Toast.makeText(EditarProducto.this, "Subiendo fotografía, por favor espere Ó presione 'BOTÓN ATRÁS' para cancelar.", Toast.LENGTH_SHORT).show();
                }


            }
        });
        /*FIN On Click Listener*/

        //STORAGE3 On Click Listener para que al pulsar el boton O LA FOTO elijamos una foto de la Galeria
        /*On Click Listener IMAGEN PERFIL */
        editIvImagenProducto.setOnClickListener(new View.OnClickListener() {
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


        btnEleg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                continuar = false;
                System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT); //SUBE IMAGEN4
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////















    } //OnCreate



    /////////////////////////////////////////////////////////////////////////////////////////////












    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Productos elemento = dataSnapshot.getValue(Productos.class);

            productoid = elemento.getProductoid();
            usuarioCreadorUid = elemento.getUsuarioCreadorUid();
            titulo = elemento.getTitulo();
            descripcion = elemento.getDescripcion();
            precio = elemento.getPrecio();
            categoria = elemento.getCategoria();
            estado = elemento.getEstado();
            imagen = elemento.getImagen();
            nombreUsuarioCreador = elemento.getNombreUsuarioCreador();
            fotoUsuarioCreador = elemento.getFotoUsuarioCreador();

            editTxtTitulo.setText(titulo);
            editTxtDescripcion.setText(descripcion);
            editTxtPrecio.setText(precio);




            if (estado.equals("Bien")){
                editSpinEstado.setSelection(1);
            } else if (estado.equals("Regular/Bastante usado")){
                editSpinEstado.setSelection(0);
            } else if (estado.equals("Mal estado")){
                editSpinEstado.setSelection(2);
            } else{
                editSpinEstado.setSelection(0);
            }

            if (categoria.equals("Objetos")){
                editSpinCategoria.setSelection(0);
            } else if (categoria.equals("Servicios")){
                editSpinCategoria.setSelection(1);
            } else if (categoria.equals("Otros")){
                editSpinCategoria.setSelection(2);
            } else{
                editSpinCategoria.setSelection(0);
            }


            //editSpinCategoria.setAdapter(categoria);
            //editSpinEstado.setText(estado);


            if (imagen.isEmpty()) {
                Picasso.with(getApplicationContext())
                        .load(R.drawable.productosinfoto)
                        .resize(370, 370)
                        .into(editIvImagenProducto);
                Log.e("ISEMPTY","SE PUSO LA FOTO DEL ISEMPTY POR DEFECTO");
            } else {
                Picasso.with(getApplicationContext())
                        .load(imagen)
                        .placeholder(R.drawable.productosinfoto)
                        //.resize(370, 370)//da problemas con imagenes grandes
                        .into(editIvImagenProducto);
                Log.e("PRODUCTO","SE PUSO FOTO DEL PRODUCTO");
            }




            Log.e("[[[[[[[[[[[[[[productoid]]]]]]]]]]]]]", productoid);
            Log.e("[[[[[[[[[[[[[[usuarioCreadorUid]]]]]]]]]]]]]", usuarioCreadorUid);
            Log.e("[[[[[[[[[[[[[[titulo]]]]]]]]]]]]]", titulo);
            Log.e("[[[[[[[[[[[[[[DESCRIPCION]]]]]]]]]]]]]", descripcion);
            Log.e("[[[[[[[[[[[[[[precio]]]]]]]]]]]]]", precio);
            Log.e("[[[[[[[[[[[[[[categoria]]]]]]]]]]]]]", categoria);
            Log.e("[[[[[[[[[[[[[[estado]]]]]]]]]]]]]", estado);
            Log.e("[[[[[[[[[[[[[[imagenproducto]]]]]]]]]]]]]", imagen);
            Log.e("[[[[[[[[[[[[[[imagenusuariocreador]]]]]]]]]]]]]", fotoUsuarioCreador);
            Log.e("[[[[[[[[[[[[[[nombreusuariocreador]]]]]]]]]]]]]", nombreUsuarioCreador);
            Log.e("[[[     --->INFO DE PRODUCTO REALIZADA<---     ]]]", "realizada");


            usuariosFirebase = FirebaseDatabase.getInstance().getReference("Usuarios").child("UsuariosRegistrados").child(elemento.getUsuarioCreadorUid());
            usuariosFirebase.addListenerForSingleValueEvent(valueEventListenerUsuarios);

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; // FIN EventListener


    ValueEventListener valueEventListenerUsuarios = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Usuarios elemento = dataSnapshot.getValue(Usuarios.class);

            nombre = elemento.getNombre();
            fotoDePerfil = elemento.getFotoPerfil();


            Log.e("[[[[[[[[[[[[[[nombreUsuario]]]]]]]]]]]]]", nombre);
            Log.e("[[[[[[[[[[[[[[fotoperfil]]]]]]]]]]]]]", fotoDePerfil);
            Log.e("[[[     --->INFO DE USUARIO PARA ACTUALIZAR REALIZADA<---     ]]]", "realizada");


        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }; // FIN EventListenerUsuarios










    public void editarProducto() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String titulO = editTxtTitulo.getText().toString().trim();
        final String descripcioN = editTxtDescripcion.getText().toString().trim();
        final String preciO = editTxtPrecio.getText().toString().trim();
        final String imageN;

        if (nuevaFotoProducto.equals("SINFOTO")){
            imageN = imagen;
        } else{
            imageN = nuevaFotoProducto;
        }


        final String categoriA = editSpinCategoria.getSelectedItem().toString();
        final String estadO = editSpinEstado.getSelectedItem().toString();

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(titulO)) {
            Toast.makeText(this, "No puedes dejar el título vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(descripcioN)) {
            Toast.makeText(this, "No puedes dejar la descripción vacía", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(preciO)) {
            Toast.makeText(this, "No puedes dejar el precio vacío", Toast.LENGTH_LONG).show();
            return;
        }

        if (!TextUtils.isEmpty(titulO) && !TextUtils.isEmpty(descripcioN) && !TextUtils.isEmpty(preciO)) {

            progressDialog.setMessage("Actualizando tu perfil en linea...");
            progressDialog.show();


            Productos producto = new Productos(idProductoAEditar, usuarioCreadorUid, titulO, descripcioN, preciO, categoriA, estadO, imageN, nombre, fotoDePerfil);

            //
            productosFirebase.setValue(producto);
            //productosDB.child("ProductosRegistrados").child(id).setValue(producto);
            //Mensaje en pantalla
            Toast.makeText(this,"Producto editado correctamente",Toast.LENGTH_LONG).show();
            // Reiniciamos actividad para vaciar los campos
            startActivity(new Intent(EditarProducto.this, MenuNavegacion.class));


            progressDialog.dismiss();




        }


    }










    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            imageUri = data.getData();

            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .placeholder(R.drawable.productosinfoto)
                    .into(editIvImagenProducto);

            // Creamos una carpeta en el storage (hijo) y guardamos el uri EL CUAL ES LA FOTO
            SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss");
            String format = s.format(new Date());
            filePath = myFirebaseStorage.child("FotosDeProductos").child(idProductoAEditar + format + "_FOTO");

            //Subimos la foto


            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(EditarProducto.this, "Se subió exitosamente la fotografía.", Toast.LENGTH_SHORT).show();

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            nuevaFotoProducto = uri.toString();

                            continuar = true;
                            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
                        }
                    });// getDownloadUrl()
                }
            }); //putFile()


        } else {
            continuar = true;
            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
        }

    }






















































    /////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Estás seguro de cancelar la edición del elemento seleccionado?")
                .setMessage("Se perderán los datos no guardados.")
                .setCancelable(false)
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(EditarProducto.this, MenuNavegacion.class);

                        startActivity(intent);

                    }
                })
                .setNegativeButton("Continuar editando", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    } //OnBackPressed*/

}
