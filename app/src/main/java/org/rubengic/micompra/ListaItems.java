package org.rubengic.micompra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, market;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            // Define the texts Views
            name = (TextView) view.findViewById(R.id.t_name);
        }

        public TextView getName() {
            return name;
        }
    }

}
