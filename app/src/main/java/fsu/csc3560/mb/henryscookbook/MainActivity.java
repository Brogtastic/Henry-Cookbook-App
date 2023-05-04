package fsu.csc3560.mb.henryscookbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button browseRecipesBtn, quitAppBtn, favoritedRecipesBtn;
    AlertDialog.Builder builder;

    String recipeList[] = {"Banana Bread", "Quiche", "Rosemary Pie"};
    String recipeDescription[] = {"Delicious banana bread, this is top-tier bread.", "If you like eggs in pie form this is really good honestly", "Rosemary Pie doesn\'t technically exist."};

    String recipeIngredients [] = {"\u2022 bananas \n\u2022 bread", "\u2022 Eggs \n\u2022 Pie","\u2022 Rosemary \n\u2022 Pie"};

    String recipeInstructions [] = {"1) Preheat Oven 400 degrees \n2) Toss that bad larry in the oven \n3) Wait 40 minutes \n4) Enjoy!", "1) Put a pan on the stove, low heat \n2) Fill the pan with eggs \n3) Cook till it congeals \n4) Enjoy!", "1) Collect rosemary \n2) cry miserably \n3) Rosemary Pie really sucks"};
    int recipeImages[] = {R.drawable.banana_bread, R.drawable.quiche, R.drawable.pie};

    Drawable recipeImagesDrawable[] = {null, null, null};

    String recipeImagesDrawableString[] = {"blank", "blank", "blank"};

    boolean favoritedArray[] = {false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        builder = new AlertDialog.Builder(this);

        browseRecipesBtn = findViewById(R.id.browse_recipes_btn);
        quitAppBtn = findViewById(R.id.quit_app_btn);
        favoritedRecipesBtn = findViewById(R.id.favorites_btn);

        Intent intent = getIntent();
        if((intent.getStringArrayExtra("RecipeList") != null)) {
            recipeList = intent.getStringArrayExtra("RecipeList");
            recipeDescription = intent.getStringArrayExtra("RecipeDescription");
            recipeImages = intent.getIntArrayExtra("RecipeImages");
            recipeImagesDrawableString = intent.getStringArrayExtra("RecipeImageDrawableArray");
            recipeIngredients = intent.getStringArrayExtra("RecipeIngredients");
            recipeInstructions = intent.getStringArrayExtra("RecipeInstructions");
            favoritedArray = intent.getBooleanArrayExtra("Favorited");
        }

        if (prefs.contains("titles")) {
            getSaved();
        }

        browseRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRecipeList();
            }
        });

        favoritedRecipesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFavoritesList();
            }
        });

        quitAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("CLOSE APP?")
                        //.setMessage("Do you want to close the app?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishAffinity(); // close the entire app
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    public void openRecipeList(){
        Intent intent = new Intent(this, RecipeList.class);
        intent.putExtra("RecipeList", recipeList);
        intent.putExtra("RecipeDescription", recipeDescription);
        intent.putExtra("RecipeImages", recipeImages);
        intent.putExtra("NewItem", false);
        intent.putExtra("RecipeImagesDrawableString", recipeImagesDrawableString);
        intent.putExtra("RecipeIngredients", recipeIngredients);
        intent.putExtra("RecipeInstructions", recipeInstructions);
        intent.putExtra("Favorited", favoritedArray);
        intent.putExtra("InFavoritesList", false);
        startActivity(intent);
    }

    public void openFavoritesList(){
        Intent intent = new Intent(this, RecipeList.class);
        intent.putExtra("RecipeList", recipeList);
        intent.putExtra("RecipeDescription", recipeDescription);
        intent.putExtra("RecipeImages", recipeImages);
        intent.putExtra("NewItem", false);
        intent.putExtra("RecipeImagesDrawableString", recipeImagesDrawableString);
        intent.putExtra("RecipeIngredients", recipeIngredients);
        intent.putExtra("RecipeInstructions", recipeInstructions);
        intent.putExtra("Favorited", favoritedArray);
        intent.putExtra("InFavoritesList", true);
        startActivity(intent);
    }

    private void getSaved(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String myArrayAsString = prefs.getString("titles", "");
        recipeList = myArrayAsString.split("DELIMITER");

        myArrayAsString = prefs.getString("descriptions", "");
        recipeDescription = myArrayAsString.split("DELIMITER");

        myArrayAsString = prefs.getString("ingredients", "");
        recipeIngredients = myArrayAsString.split("DELIMITER");

        myArrayAsString = prefs.getString("instructions", "");
        recipeInstructions = myArrayAsString.split("DELIMITER");

        myArrayAsString = prefs.getString("drawablesString", "");
        recipeImagesDrawableString = myArrayAsString.split("DELIMITER");

        String boolArrayString = prefs.getString("favoritedArray", "");
        String[] boolStrings = boolArrayString.split("DELIMITER");
        boolean[] boolArray = new boolean[boolStrings.length];
        for (int i = 0; i < boolStrings.length; i++) {
            boolArray[i] = Boolean.parseBoolean(boolStrings[i]);
        }
        favoritedArray = boolArray;

        String imagesDeletedString = prefs.getString("imagesDeleted", "");
        String [] imagesDeletedStringArray = imagesDeletedString.split("DELIMITER");
        recipeImages = new int[imagesDeletedStringArray.length];
        for(int i =0; i<imagesDeletedStringArray.length; i++){
            if(imagesDeletedStringArray[i].equals("banana_bread")){
                recipeImages[i] = R.drawable.banana_bread;
            }
            if(imagesDeletedStringArray[i].equals("quiche")){
                recipeImages[i] = R.drawable.quiche;
            }
            if(imagesDeletedStringArray[i].equals("pie")){
                recipeImages[i] = R.drawable.pie;
            }
        }


    }

}