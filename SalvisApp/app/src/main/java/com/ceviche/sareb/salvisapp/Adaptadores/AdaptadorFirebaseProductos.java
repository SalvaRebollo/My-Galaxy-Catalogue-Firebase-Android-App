package com.ceviche.sareb.salvisapp.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ceviche.sareb.salvisapp.Clases.UsuarioProductosItemListClass;
import com.ceviche.sareb.salvisapp.R;
import com.ceviche.sareb.salvisapp.VerProducto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AdaptadorFirebaseProductos extends FirebaseRecyclerAdapter<UsuarioProductosItemListClass, AdaptadorFirebaseProductos.ViewHolderProductos> {

    android.content.Context context;

    public AdaptadorFirebaseProductos(Class<UsuarioProductosItemListClass> modelClass, int modelLayout, Class<ViewHolderProductos> viewHolderClass, DatabaseReference ref, Context c) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        context = c;


    }

    @Override
    protected void populateViewHolder(ViewHolderProductos viewHolder, final UsuarioProductosItemListClass model, int position) {
        viewHolder.ilTextoTitulo.setText(model.getTitulo());
        viewHolder.ilTextoDescripcion.setText(model.getDescripcion());
        viewHolder.ilTextoPrecio.setText(Html.fromHtml("<b>Precio: </b>" + model.getPrecio() + "€"));
        viewHolder.ilTextoCategoria.setText(Html.fromHtml("<b>Categoría: </b>" + model.getCategoria()));
        viewHolder.ilTextoEstado.setText(Html.fromHtml("<b>Estado: </b>" + model.getEstado()));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        StorageReference myFirebaseStorage;
        myFirebaseStorage = FirebaseStorage.getInstance().getReference();

        viewHolder.ilTextoUsuario.setText(Html.fromHtml("<b>" + model.getNombreUsuarioCreador() + "</b>"));

        Picasso.with(context)
                .load(model.getimagen())
                .placeholder(R.drawable.productosinfoto)
                .into(viewHolder.ilImagenProducto);

        if (model.getFotoUsuarioCreador().isEmpty()) {
            Picasso.with(context)
                    .load(R.drawable.avatardefault)
                    .placeholder(R.drawable.avatardefault)
                    .into(viewHolder.ilImagenUsuario);
        } else {

            Picasso.with(context)
                    .load(model.getFotoUsuarioCreador())
                    .placeholder(R.drawable.avatardefault)
                    .into(viewHolder.ilImagenUsuario);
        }


        viewHolder.listaItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, VerProducto.class);

                intent.putExtra("titulo", model.getTitulo());
                intent.putExtra("descripcion", model.getDescripcion());
                intent.putExtra("precio", model.getPrecio());
                intent.putExtra("categoria", model.getCategoria());
                intent.putExtra("estado", model.getEstado());
                intent.putExtra("imagenproducto", model.getimagen());
                intent.putExtra("imagenusuario", model.getFotoUsuarioCreador());
                intent.putExtra("usuarioCreadorUid", model.getUsuarioCreadorUid());

                context.startActivity(intent);

                Toast.makeText(context, "Subido por: " + model.getNombreUsuarioCreador() + model.getFotoUsuarioCreador() + model.getimagen(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class ViewHolderProductos extends RecyclerView.ViewHolder {

        TextView ilTextoTitulo, ilTextoDescripcion, ilTextoPrecio, ilTextoEstado, ilTextoCategoria, ilTextoUsuario;
        ImageView ilImagenProducto, ilImagenUsuario;
        LinearLayout listaItem;


        public ViewHolderProductos(View itemView) {
            super(itemView);

            ilTextoTitulo = (TextView) itemView.findViewById(R.id.ilTextoTitulo);
            ilTextoDescripcion = (TextView) itemView.findViewById(R.id.ilTextoDescripcion);
            ilTextoPrecio = (TextView) itemView.findViewById(R.id.ilTextoPrecio);
            ilTextoEstado = (TextView) itemView.findViewById(R.id.ilTextoEstado);
            ilTextoCategoria = (TextView) itemView.findViewById(R.id.ilTextoCategoria);

            ilTextoUsuario = (TextView) itemView.findViewById(R.id.ilTextoUsuario);

            ilImagenProducto = (ImageView) itemView.findViewById(R.id.ilImagenProducto);
            ilImagenUsuario = (ImageView) itemView.findViewById(R.id.ilImagenUsuario);

            listaItem = (LinearLayout) itemView.findViewById(R.id.listaItem);


        }//FIN ViewHolderProductos(view)


    }//FIN class ViewHolderProductos extends RecyclerView


    @Override
    public ViewHolderProductos onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_productos, null, false);

        return new ViewHolderProductos(view);


    }//FIN onCreateViewHolder*/

   /* @Override
    public void onBindViewHolder(ViewHolderProductos holder, int position) {

        String imagenProducto;
        String imagenPerfil

        holder.ilTextoTitulo.setText(listaPrincipal.get(position).getNombre());
        holder.ilTextoDescripcion.setText(listaPrincipal.get(position).getNombre());
        holder.ilTextoPrecio.setText(listaPrincipal.get(position).getNombre());
        holder.ilTextoEstado.setText(listaPrincipal.get(position).getNombre());
        holder.ilTextoCategoria.setText(listaPrincipal.get(position).getNombre());
        holder.ilTextoUsuario.setText(listaPrincipal.get(position).getNombre());

        //holder.imagenProducto.setText(listaPrincipal.get(position).getFotoProducto());
        //holder.ilImagenUsuario.setImageResource(listaPrincipal.get(position).getFotoPerfil());


    } //FIN onBindViewHolder*/



    /*@Override
    public int getItemCount() {
        return listaPrincipal.size();
    }*/

}
