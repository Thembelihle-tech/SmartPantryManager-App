package com.example.smartpantry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    public interface Listener{
        void onRecipeClicked(Recipes recipe);
    }
    private final List<Recipes> recipes;
    private final Listener listener;

    public RecipeAdapter(List<Recipes> recipes, Listener listener){
        this.recipes = recipes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        Recipes recipe = recipes.get(position);
        holder.name.setText(recipe.getName());
        holder.subtitle.setText(recipe.getIngredients().size() + " ingredients needed!");
        holder.itemView.setOnClickListener(c -> listener.onRecipeClicked(recipe));
    }

    @Override
    public int getItemCount(){return recipes.size();}
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView name, subtitle;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.textRecipeName);
            subtitle = itemView.findViewById(R.id.textRecipeSubtitle);
        }
    }
}
