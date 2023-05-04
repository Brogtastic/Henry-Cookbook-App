package fsu.csc3560.mb.henryscookbook;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context context;
    String listRecipe[];
    String descriptionRecipe[];
    int listImages[];

    Drawable listImagesDrawable[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String [] recipeList, String [] recipeDescription, int [] images, Drawable [] drawables){
        this.context = ctx;
        this.listRecipe = recipeList;
        this.descriptionRecipe = recipeDescription;
        this.listImages = images;
        this.listImagesDrawable = drawables;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        //The rows will be equal to the count of listFruit
        return listRecipe.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.activity_custom_list_view, null);
        TextView txtView = (TextView) convertView.findViewById(R.id.recipe_name);
        TextView txtView2 = (TextView) convertView.findViewById(R.id.recipe_description);
        ImageView fruitImg = (ImageView) convertView.findViewById(R.id.image_icon);
        ImageView recipeImageDrawable = (ImageView) convertView.findViewById(R.id.image_drawable_icon);

        txtView.setText(listRecipe[position]);
        txtView2.setText(descriptionRecipe[position]);
        fruitImg.setImageResource(listImages[position]);
        recipeImageDrawable.setImageDrawable(listImagesDrawable[position]);
        return convertView;
    }
}
