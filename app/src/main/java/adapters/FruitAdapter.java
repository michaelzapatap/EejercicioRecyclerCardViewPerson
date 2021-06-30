package adapters;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tramites.eejerciciorecyclercardviewperson.R;

import java.util.List;

import activities.MainActivity;
import models.Fruit;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> fruits;
    private int layout;
    private Activity activity;
    private OnItemClickListener listener;

    public FruitAdapter(List<Fruit> fruits, int layout, Activity activity, OnItemClickListener listener){
        this.fruits = fruits;
        this.layout = layout;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(fruits.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return fruits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView textViewName;
        public TextView textViewDescription;
        public TextView textViewQuantity;
        public ImageView imageViewBackground;

        public ViewHolder(View itemView){
            super(itemView);
            textViewName = (TextView)itemView.findViewById(R.id.textViewName);
            textViewDescription = (TextView)itemView.findViewById(R.id.textViewDescription);
            textViewQuantity = (TextView)itemView.findViewById(R.id.textViewQuantity);
            imageViewBackground = (ImageView)itemView.findViewById(R.id.imageViewBackground);

            itemView.setOnCreateContextMenuListener(this);
        }

        public void bind(final Fruit fruit, final OnItemClickListener listener){

            this.textViewName.setText(fruit.getName());
            this.textViewDescription.setText(fruit.getDescription());
            this.textViewQuantity.setText(fruit.getQuantity() + "");

            if(fruit.getQuantity() == Fruit.LIMIT_QUANTITY){
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.design_default_color_error));
                textViewQuantity.setTypeface(null, Typeface.BOLD);
            } else {
                textViewQuantity.setTextColor(ContextCompat.getColor(activity, R.color.design_default_color_primary));
                textViewQuantity.setTypeface(null, Typeface.NORMAL);
            }

            Picasso.with(activity).load(fruit.getImgBackground()).fit().into(this.imageViewBackground);

            this.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(fruit, getAdapterPosition());
                }
            });

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            Fruit fruitSelected = fruits.get(this.getAdapterPosition());

            contextMenu.setHeaderTitle(fruitSelected.getName());
            contextMenu.setHeaderIcon(fruitSelected.getImgIcon());

            MenuInflater inflater = activity.getMenuInflater();
            inflater.inflate(R.menu.context_menu_fruit, contextMenu);

            for(int i =0; i < contextMenu.size(); i++){
                contextMenu.getItem(i).setOnMenuItemClickListener(this);
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem){

            switch (menuItem.getItemId()){
                case R.id.delete_fruit:
                    fruits.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    return true;
                case R.id.reset_fruit_quantity:
                    fruits.get(getAdapterPosition()).resetQuantity();
                    notifyItemChanged(getAdapterPosition());
                    return true;
                default:
                    return false;
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Fruit fruit, int position);
    }
}
