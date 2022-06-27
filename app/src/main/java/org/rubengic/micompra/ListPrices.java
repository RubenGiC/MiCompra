package org.rubengic.micompra;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.rubengic.micompra.Models.Items;
import org.rubengic.micompra.Models.Prices;

import java.util.ArrayList;

public class ListPrices extends RecyclerView.Adapter<ListPrices.ViewHolder> {

    public ArrayList<Prices> listPrices;

    public ListPrices(ArrayList<Prices> listPrices){//String[] dataSet
        this.listPrices = listPrices;
    }

    @Override
    /**
     * its used for the structure of each item
     */
    public ListPrices.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_price, null, false);

        return new ListPrices.ViewHolder(vi);
    }

    @Override
    /**
     * add data to display
     */
    public void onBindViewHolder(ListPrices.ViewHolder holder, int position) {

        holder.price.setText(listPrices.get(position).getPrice().toString()+" €");
        holder.market.setText(listPrices.get(position).getMarket());
        holder.itemView.setId(position);
    }

    @Override
    public int getItemCount() {
        return listPrices.size();
    }

    public Prices getPrice(int position) {
        return listPrices.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView price, market;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            // Define the texts Views
            price = (TextView) view.findViewById(R.id.tv_price);
            market = (TextView) view.findViewById(R.id.tv_market);
        }

        public TextView getMarket() {
            return market;
        }
    }
}
