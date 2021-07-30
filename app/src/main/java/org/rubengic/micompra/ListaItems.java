package org.rubengic.micompra;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * This class used from show the items with recycler view
 */
public class ListaItems extends RecyclerView.Adapter<ListaItems.ViewHolder> {

    private ArrayList<Items> listItems;

    public ListaItems(ArrayList<Items> listItems){//String[] dataSet
        this.listItems = listItems;
    }

    @Override
    /**
     * its used for the structure of each item
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, null, false);
        return new ViewHolder(vi);
    }

    @Override
    /**
     * add data to display
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.name.setText(listItems.get(position).getName());
        holder.price.setText("Precio: "+listItems.get(position).getPrice().toString());
        holder.market.setText(listItems.get(position).getMarket());
        if(listItems.get(position).getImage() != null)
            holder.imagen.setImageBitmap(BitmapFactory.decodeByteArray(listItems.get(position).getImage(), 0, listItems.get(position).getImage().length));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, market;
        ImageView imagen;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            // Define the texts Views
            name = (TextView) view.findViewById(R.id.t_name);
            price = (TextView) view.findViewById(R.id.t2_price);
            market = (TextView) view.findViewById(R.id.t_market);
            imagen = (ImageView) view.findViewById(R.id.iv_image);
        }

        public TextView getName() {
            return name;
        }
    }

}
