package com.example.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {
    public interface Listener{
        void onEdit(Ingredients item);
        void onDelete(Ingredients item);
    }

    private final List<Ingredients> items;
    private final Listener listener;

    public PantryAdapter(List<Ingredients> items, Listener listener){
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Ingredients item = items.get(position);
        holder.name.setText(item.getName());
        holder.details.setText(item.getQuantity() + " " + item.getUnit() +
                (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()
                ? " . expires " + item.getExpiryDate() : ""));

        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {return items.size();}

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, details;
        View deleteButton;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.textIngredientName);
            details = itemView.findViewById(R.id.textIngredientDetails);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
