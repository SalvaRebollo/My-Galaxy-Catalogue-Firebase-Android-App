package com.ceviche.sareb.salvisapp.Adaptadores;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ceviche.sareb.salvisapp.Clases.Novedades;
import com.ceviche.sareb.salvisapp.Clases.UsuarioProductosItemListClass;
import com.ceviche.sareb.salvisapp.EditarProducto;
import com.ceviche.sareb.salvisapp.InterfazPrincipalActivity;
import com.ceviche.sareb.salvisapp.MenuNavegacion;
import com.ceviche.sareb.salvisapp.MisProductosActivity;
import com.ceviche.sareb.salvisapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdaptadorListNovedades extends RecyclerView.Adapter<AdaptadorListNovedades.ViewHolderNovedades> {

    ArrayList<Novedades> listaNovedades;
    Context mainContext;
    RecyclerView.Adapter adapter;

    public AdaptadorListNovedades(ArrayList<Novedades> listaNovedades, Context mainContext) {
        this.listaNovedades = listaNovedades;
        this.mainContext = mainContext;
        this.adapter = this;
    }

    @Override
    public ViewHolderNovedades onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_novedades, null, false);

        return new ViewHolderNovedades(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNovedades viewHolderNovedades, final int position) {


        viewHolderNovedades.ilTextoCabecera.setText(Html.fromHtml("<b>" + listaNovedades.get(position).getCabecera() + "</b>") );
        viewHolderNovedades.ilTextoCuerpo.setText(Html.fromHtml("<b>Detalles: </b>" + listaNovedades.get(position).getCuerpo()) );



    }

    @Override
    public int getItemCount() {
        return listaNovedades.size();
    }

    public class ViewHolderNovedades extends RecyclerView.ViewHolder {

        TextView ilTextoCabecera, ilTextoCuerpo;

        LinearLayout listaItem;


        public ViewHolderNovedades(@NonNull View itemView) {
            super(itemView);

            ilTextoCabecera = (TextView) itemView.findViewById(R.id.ilNovedadesCabecera);
            ilTextoCuerpo = (TextView) itemView.findViewById(R.id.ilNovedadesCuerpo);

            listaItem = (LinearLayout) itemView.findViewById(R.id.listaItemNovedades);


        }
    }


}
