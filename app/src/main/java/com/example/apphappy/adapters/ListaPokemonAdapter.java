package com.example.apphappy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apphappy.R;
import com.example.apphappy.models.Pokemon;

import java.util.ArrayList;

public class ListaPokemonAdapter extends RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>{

    private ArrayList<Pokemon> dataset;

    private Context context;



    public ListaPokemonAdapter(Context context) {
        this.context=context;
        dataset=new ArrayList<>();
    }

    @NonNull
    @Override
    //este se encarga de la vista
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon,parent,false);
        return new ViewHolder(view);
    }
    //este me trae los datos
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Pokemon p=dataset.get(position);
        holder.nombreTextView.setText(p.getName());

        //asi obtendremos la imagen
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+p.getNumber()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoImageView);


    }
    //este es el tamaño
    @Override
    public int getItemCount() {
        return dataset.size();
    }

    //este metodo se creo en el FiltersFragment
    public void adicionarListaPokemon(ArrayList<Pokemon> listapokemon) {

        dataset.addAll(listapokemon);
        notifyDataSetChanged();




    }

    //este se encarga de declararlos
    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView fotoImageView; //esto se trajo de item_pokemon.xml
        private TextView nombreTextView;
        public ViewHolder(View itemView){
            super(itemView);

            fotoImageView=itemView.findViewById(R.id.fotoImageView);
            nombreTextView=itemView.findViewById(R.id.nombreTextView);


        }
    }


}
