package org.rubengic.micompra;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ListaItems extends RecyclerView.Adapter<ListaItems.ViewHolder> {

    private String[] localDataSet;

    public ListaItems(String[] dataSet){
        localDataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_row_item, parent, false);
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.getTextView().setText(localDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.t_name);
        }

        public TextView getTextView() {
            return textView;
        }
    }

}
