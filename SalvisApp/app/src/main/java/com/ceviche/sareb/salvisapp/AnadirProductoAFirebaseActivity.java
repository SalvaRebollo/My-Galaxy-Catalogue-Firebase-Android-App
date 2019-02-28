package com.ceviche.sareb.salvisapp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ceviche.sareb.salvisapp.Clases.Productos;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AnadirProductoAFirebaseActivity extends AppCompatActivity {
    private EditText txtTitulo, txtDesc, txtPrecio;
    private Spinner spinCateg,spinEstado;
    private Button btnRegistrar, btnElegirFotoSubir;
    private Intent intent;
    private Uri imageUri;
    private StorageReference filePath;
    private ImageView imgproducto;

    String fotoProducto = "SINFOTO";
    String encodedImage = "";
    boolean continuar = true;

    String nombre, fotoperfil;





    private StorageReference myFirebaseStorage; //STORAGE1:Referencia para usar el Storage, el cual va a ser usado para subir la foto del producto

    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();

    // Segundo: Obtenemos la instancia, en el parentesis va la clase Java "Productos.java" que tenemos creada con los mismos atributos como variables
    private DatabaseReference productosDB = FirebaseDatabase.getInstance().getReference("Productos").child("ProductosRegistrados");; //Primero: Referencia a la base de datos
    private DatabaseReference usuariosDB = FirebaseDatabase.getInstance().getReference("Usuarios").child("UsuariosRegistrados").child(user.getUid());

    String productoid = productosDB.push().getKey(); //Genera la key y la guarda en la id
    String usuarioCreadorUid = user.getUid();

    private static final int GALLERY_INTENT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anadir_productos_a_firebase_activity);

        getSupportActionBar().setTitle("MyGalaxyCatalogue");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher_round) ;
        getSupportActionBar().setSubtitle("Añadir producto");
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");
        //STORAGE2 obtenemos la referencia de storage de firebase e inicializarla.
        myFirebaseStorage = FirebaseStorage.getInstance().getReference();

        Intent outputInterfazPrincipalDatosUsu = getIntent();
        Bundle datos = outputInterfazPrincipalDatosUsu.getExtras();

        if(datos != null){
            nombre = (String) datos.get("nombre");
            fotoperfil = (String) datos.get("fotoperfil");
        }

        System.out.println("*\n*\n*\n*\n*NOMBRE = " + nombre + "*\nFOTOPERFIL:" + fotoperfil + "*\n*\n*\n*");


        /*Vinculamos los items de la vista en xml con las variables Java*/
        /*---> Cajas de texto*/
        txtTitulo=(EditText) findViewById(R.id.txttitulo);
        txtDesc=(EditText) findViewById(R.id.txtdesc);
        txtPrecio=(EditText) findViewById(R.id.txtprecio);
        /*---> Spinners o selects*/
        spinCateg=(Spinner) findViewById(R.id.spincateg);
        spinEstado=(Spinner) findViewById(R.id.spinestado);
        /*Buttons/Botones*/
        btnRegistrar=(Button) findViewById(R.id.btnregistrar);
        btnElegirFotoSubir= (Button) findViewById(R.id.btneleg);
        /*Imagen de producto*/
        imgproducto = (ImageView) findViewById(R.id.imgproducto);
        Bitmap productoSinFotoDecoded;

        // Imagen de producto sin foto que saldrá por defecto al iniciar la actividad de añadir productos
        imgproducto.setImageBitmap(decodeStringBase64toByte(getResources().getString(R.string.img_defecto)));


        /*ESTO ES PARA VER SI SE CONSIGUE LEER LOS DATOS DE FIREBASE Y SE IMPRIMEN EN CONSOLA*/
        /*
        productosDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 1;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Productos elemento = snapshot.getValue(Productos.class);
                    String productoid = elemento.getProductoid();
                    String titulo = elemento.getTitulo();
                    String descripcion = elemento.getDescripcion();
                    String precio = elemento.getPrecio();
                    String categoria = elemento.getCategoria();
                    String estado = elemento.getEstado();
                    String imagen = elemento.getImagen();


                    Log.e("=============== Datos"+ i + " ===============",""+snapshot.getValue());
                    Log.e("[TITULOOOO"+ i + "]=", titulo);
                    Log.e("[DESCRIPCION"+ i + "]=", descripcion);
                    Log.e("[PRECIO"+ i + "]=", precio);
                    Log.e("[CATEGORIA"+ i + "]=", categoria);
                    Log.e("[ESTADO"+ i + "]=", estado);
                    Log.e("[IMAGEN_BASE64"+ i + "]=", imagen);
                    i++;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
        /*FIN LEER LOS DATOS DE FIREBASE Y SE IMPRIMEN EN CONSOLA*/

        /*On Click Listener para que al pulsar el boton haga funcionar el método registrarProducto()*/
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (continuar == true) {
                    registrarProducto();
                } else{
                    Toast.makeText(AnadirProductoAFirebaseActivity.this, "Subiendo fotografía, por favor espere Ó presione 'BOTÓN ATRÁS' para cancelar.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        //STORAGE3 On Click Listener para que al pulsar el boton O LA FOTO elijamos una foto de la Galeria
        btnElegirFotoSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                continuar = false;
                System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT); //SUBE IMAGEN4
            }
        });

        imgproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                continuar = false;
                System.out.println("*\n*\n*\n*\n*CONTINUAR = " + continuar + "*\n*\n*\n*\n*");

                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT); //SUBE IMAGEN4
            }
        });
        // FIN STORAGE3  ///////////

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {

            imageUri = data.getData();




            imgproducto.setImageURI(imageUri);

            // Creamos una carpeta en el storage (hijo) y guardamos el uri EL CUAL ES LA FOTO
            filePath = myFirebaseStorage.child("FotosDeProductos").child(productoid + "_FOTO");

            //Subimos la foto


            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(AnadirProductoAFirebaseActivity.this, "Se subió exitosamente la fotografía.", Toast.LENGTH_SHORT).show();
                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            fotoProducto = uri.toString();

                            continuar = true;
                            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
                        }
                    });
                }
            });



            /*
            // Creamos una carpeta en el storage (hijo) y guardamos el uri EL CUAL ES LA FOTO
            filePath = myFirebaseStorage.child("FotosDeProductos").child(productoid);
            */
            //Subimos la foto
            /*
            filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(AnadirProductoAFirebaseActivity.this, "Se subió exitosamente la fotografía.", Toast.LENGTH_SHORT).show();
                }
            });
            */

            /*
            //Proceso codificación de URI a Base64 ; el proceso ira en este orden URI a Stream, Stream a Bitmap y Bitmap a Base64 en ese orden
            InputStream imageStream = null;
            // Primero lo pasamos a Stream
            try {
                imageStream = getContentResolver().openInputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // Luego lo pasamos de Stream a Bitmap
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            // Y finalmente lo pasamos de Bitmap a String
            encodedImage = encodeImageBase64(selectedImage); //encodeImageBase64 es un metodo creado abajo





            Log.e("[Imagen transformada en Base64:] ",""+ encodedImage);
            //FIN DE Proceso codificación de URI a Base64
        */






        } else{
            continuar = true;
            System.out.println("*\n*\n*\n*\n*CONTINUARimagenSubida = " + continuar + "*\n*\n*\n*\n*");
        }
    }

    /*Vamos a crear un método para registrar los productosDB así que haremos variables para vincular con las de la clase*/
    public void registrarProducto() {

        String categoria=spinCateg.getSelectedItem().toString();
        // Para pasar los items que son spinners a cadena String
        String estado=spinEstado.getSelectedItem().toString();

        String titulo=txtTitulo.getText().toString(); // Para pasar los items que son EdiText a cadena String
        String descripcion = txtDesc.getText().toString();
        String precio=txtPrecio.getText().toString();
        //precio = String.format("%.2f", precio); //Redondear a 2 decimales
        String imagen = encodedImage;



        if (!TextUtils.isEmpty(titulo) && !TextUtils.isEmpty(descripcion) && !TextUtils.isEmpty(precio)){










            Productos producto = new Productos(productoid, usuarioCreadorUid, titulo, descripcion, precio, categoria, estado, fotoProducto, nombre, fotoperfil);

            //
            productosDB.child(productoid).setValue(producto);
            //productosDB.child("ProductosRegistrados").child(id).setValue(producto);
            //Mensaje en pantalla
            Toast.makeText(this,"Producto registrado correctamente",Toast.LENGTH_LONG).show();
            // Reiniciamos actividad para vaciar los campos
            startActivity(new Intent(AnadirProductoAFirebaseActivity.this, InterfazPrincipalActivity.class));
        } else{
            //Mensaje en pantalla
            Toast.makeText(this,"Debe escribir en todos los campos",Toast.LENGTH_LONG).show();
        }
    }

    private String encodeImageBase64(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        return encImage;
    }

    private Bitmap decodeStringBase64toByte(String encodedImage)
    {
        byte[] decodeString = Base64.decode(encodedImage, Base64.NO_WRAP);
        Bitmap decoded = BitmapFactory.decodeByteArray(decodeString,0, decodeString.length);


        return decoded;
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent(AnadirProductoAFirebaseActivity.this,InterfazPrincipalActivity.class);
        startActivity(intent);
        finish();


    }
}
