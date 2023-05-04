package fsu.csc3560.mb.henryscookbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ViewRecipe extends AppCompatActivity {
    //Get strings and everything from RecipeList class
    RecipeList recipeList = new RecipeList();
    String[] recipes = recipeList.getRecipeList();
    String[] descriptions = recipeList.getRecipeDescription();
    int[] images = recipeList.getRecipeImages();

    int position;

    String[] ingredients = recipeList.getRecipeIngredients();
    String[] instructions = recipeList.getRecipeInstructions();

    String[] imagesDrawable;

    TextView recipeTitle, recipeDescription, recipeIngredients, recipeInstructions;
    ImageView recipeImage;

    boolean userImage = false;

    boolean [] favoritedArray;

    int [] favoritesPositions;

    Drawable userSelectedImage;

    private Menu myMenu;

    boolean favorited;

    boolean inFavoritesList;
    MenuItem favorite_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        position = intent.getIntExtra("position", -1);
        if(intent.getStringArrayExtra("RecipeList") != null) {
            recipes = intent.getStringArrayExtra("RecipeList");
            descriptions = intent.getStringArrayExtra("RecipeDescription");
            images = intent.getIntArrayExtra("RecipeImages");
            imagesDrawable = intent.getStringArrayExtra("RecipeImageDrawableArray");
            ingredients = intent.getStringArrayExtra("RecipeIngredients");
            instructions = intent.getStringArrayExtra("RecipeInstructions");
            inFavoritesList = intent.getBooleanExtra("InFavoritesList", false);
            favoritesPositions = intent.getIntArrayExtra("FavoritesPositions");

        }

        recipeTitle = findViewById(R.id.recipe_title_view);
        recipeDescription = findViewById(R.id.recipe_description_view);
        recipeImage = findViewById(R.id.recipe_image_view);
        recipeIngredients = findViewById(R.id.recipe_ingredients_view);
        recipeInstructions = findViewById(R.id.recipe_instructions_view);

        if(inFavoritesList == true) {
            position = favoritesPositions[position];
        }

        recipeTitle.setText(recipes[position]);

        if(!(imagesDrawable[position].equals("blank"))) {
            String fileName = imagesDrawable[position];
            File filePath = getFileStreamPath(fileName);
            userSelectedImage = Drawable.createFromPath(filePath.toString());
            userImage = true;
        }

        recipeDescription.setText(descriptions[position]);
        if (userImage == false) {
            recipeImage.setImageResource(images[position]);
        } else {
            recipeImage.setImageDrawable(userSelectedImage);
        }
        recipeIngredients.setText(ingredients[position]);
        recipeInstructions.setText(instructions[position]);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recipe_view_menu, menu);
        myMenu = menu;
        favorite_icon = myMenu.findItem(R.id.item_favorite);

        Intent intent = getIntent();
        favoritedArray = intent.getBooleanArrayExtra("Favorited");

        if(favoritedArray[position] == true){
            favorited = true;
            favorite_icon.setIcon(R.drawable.baseline_favorite_24);
        }
        else{
            favorite_icon.setIcon(R.drawable.baseline_favorite_border_24);
            favorited = false;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        favorite_icon = myMenu.findItem(R.id.item_favorite);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_favorite:
                if(favorited == false) {
                    favorite_icon.setIcon(R.drawable.baseline_favorite_24);
                    Toast.makeText(this, "Added to favorites!", Toast.LENGTH_SHORT).show();
                    favorited = true;
                    favoritedArray[position] = true;
                }
                else{
                    favorite_icon.setIcon(R.drawable.baseline_favorite_border_24);
                    Toast.makeText(this, "Removed from favorites.", Toast.LENGTH_SHORT).show();
                    favorited = false;
                    favoritedArray[position] = false;
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewRecipe.this, RecipeList.class);
        intent.putExtra("position", position);
        intent.putExtra("RecipeList", recipes);
        intent.putExtra("RecipeDescription", descriptions);
        intent.putExtra("RecipeImages", images);
        intent.putExtra("RecipeImagesDrawableString", imagesDrawable);
        intent.putExtra("RecipeIngredients", ingredients);
        intent.putExtra("RecipeInstructions", instructions);
        intent.putExtra("Favorited", favoritedArray);
        intent.putExtra("InFavoritesList", inFavoritesList);
        startActivity(intent);
    }
}