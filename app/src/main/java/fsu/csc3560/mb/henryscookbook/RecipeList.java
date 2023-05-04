package fsu.csc3560.mb.henryscookbook;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

public class RecipeList extends AppCompatActivity {

    String recipeList[] = {"Banana Bread", "Quiche", "Rosemary Pie"};
    String recipeDescription[] = {"Delicious banana bread, this is top-tier bread.", "If you like eggs in pie form this is really good honestly", "Rosemary Pie doesn\'t technically exist."};

    String recipeIngredients [] = {"\u2022 bananas \n\u2022 bread", "\u2022 Eggs \n\u2022 Pie","\u2022 Rosemary \n\u2022 Pie"};

    String recipeInstructions [] = {"1) Preheat Oven 400 degrees \n2) Toss that bad larry in the oven \n3) Wait 40 minutes \n4) Enjoy!", "1) Put a pan on the stove, low heat \n2) Fill the pan with eggs \n3) Cook till it congeals \n4) Enjoy!", "1) Collect rosemary \n2) cry miserably \n3) Rosemary Pie really sucks"};
    int recipeImages[] = {R.drawable.banana_bread, R.drawable.quiche, R.drawable.pie};
    int favoritesPositions[];

    Drawable recipeImagesDrawable[] = {null, null, null};

    String recipeImagesDrawableString[] = {"blank", "blank", "blank"};

    boolean favoritedArray[] = {false, false, false};

    boolean inFavoritesList;

    String newRecipeTitle, newRecipeDescription, newRecipeIngredients, newRecipeInstructions, alertMessageString;
    int newRecipeImage, myDeletingPosition;
    Drawable newRecipeImageDrawable;

    AlertDialog.Builder builder;
    private Menu myMenu;
    public AddRecipe myAddRecipe;

    boolean deleting = false;

    public String[] getRecipeList() {
        return recipeList;
    }
    public String[] getRecipeDescription() {
        return recipeDescription;
    }
    public int[] getRecipeImages() {
        return recipeImages;
    }

    public String[] getRecipeIngredients(){ return recipeIngredients; }

    public String[] getRecipeInstructions(){ return recipeInstructions; }

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        builder = new AlertDialog.Builder(this);

        Intent intent = getIntent();
        if((intent.getStringArrayExtra("RecipeList") != null)){
            recipeList = intent.getStringArrayExtra("RecipeList");
            recipeDescription = intent.getStringArrayExtra("RecipeDescription");
            recipeImages = intent.getIntArrayExtra("RecipeImages");
            recipeImagesDrawableString = intent.getStringArrayExtra("RecipeImagesDrawableString");
            recipeIngredients = intent.getStringArrayExtra("RecipeIngredients");
            recipeInstructions = intent.getStringArrayExtra("RecipeInstructions");
            favoritedArray = intent.getBooleanArrayExtra("Favorited");
            inFavoritesList = intent.getBooleanExtra("InFavoritesList", false);

            recipeImagesDrawable = new Drawable[recipeImagesDrawableString.length];
            for(int i=0; i<recipeImagesDrawableString.length; i++){
                if (recipeImagesDrawableString[i] == null || recipeImagesDrawableString[i].equals("blank")) {
                    recipeImagesDrawable[i] = null;
                }
                else{
                    File filePath = getFileStreamPath(recipeImagesDrawableString[i]);
                    Drawable userSelectedImage1 = Drawable.createFromPath(filePath.toString());
                    recipeImagesDrawable[i] = userSelectedImage1;
                }
            }
        }
        if (intent.getStringExtra("picname") != null) {
            newRecipeTitle = intent.getStringExtra("NewRecipeTitle");
            newRecipeDescription = intent.getStringExtra("NewRecipeDescription");
            newRecipeIngredients = intent.getStringExtra("NewRecipeIngredients");
            newRecipeInstructions = intent.getStringExtra("NewRecipeInstructions");

            String fileName = intent.getStringExtra("picname");
            File filePath = getFileStreamPath(fileName);
            Drawable userSelectedImage = Drawable.createFromPath(filePath.toString());

            String [] newStringArray = new String[recipeImagesDrawableString.length+1];
            for(int i = 0; i<recipeImagesDrawableString.length+1; i++){
                if(i<recipeImagesDrawableString.length) {
                    newStringArray[i] = recipeImagesDrawableString[i];
                }else{
                    newStringArray[i] = fileName;
                }
            }
            recipeImagesDrawableString = newStringArray;

            //Array String of Drawable Whatevers
            Drawable [] newDrawableArray = new Drawable[recipeImagesDrawableString.length];
            for(int i=0; i<recipeImagesDrawableString.length; i++){
                if (recipeImagesDrawableString[i] == null || recipeImagesDrawableString[i].equals("blank")) {
                    newDrawableArray[i] = null;
                    //Toast.makeText(this, (i+1) + recipeImagesDrawableString[i], Toast.LENGTH_SHORT).show();
                }
                else{
                    filePath = getFileStreamPath(recipeImagesDrawableString[i]);
                    Drawable userSelectedImage1 = Drawable.createFromPath(filePath.toString());
                    newDrawableArray[i] = userSelectedImage1;
                }
            }
            recipeImagesDrawable = newDrawableArray;

            boolean [] newFavoritedArray = new boolean[favoritedArray.length +1];
            for(int i=0; i< favoritedArray.length+1; i++){
                if(i < favoritedArray.length) {
                    newFavoritedArray[i] = favoritedArray[i];
                }
                else{
                    if(inFavoritesList == false) {
                        newFavoritedArray[i] = false;
                    }
                    else{
                        newFavoritedArray[i] = true;
                    }
                }
            }
            favoritedArray = newFavoritedArray;

            addItem(newRecipeTitle, newRecipeDescription, newRecipeIngredients, newRecipeInstructions, userSelectedImage);
        }

        inFavoritesList = intent.getBooleanExtra("InFavoritesList", false);

        if (inFavoritesList == false) {
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), recipeList, recipeDescription, recipeImages, recipeImagesDrawable);
            listView.setAdapter(customBaseAdapter);
        }
        else{
            makeFavoritesList(false);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(deleting == false) {
                    String selectedRecipeName = recipeList[position];
                    String selectedRecipeDescription = recipeDescription[position];
                    int selectedRecipeImage = recipeImages[position];
                    openViewRecipe(position);
                }
                else{
                    if(inFavoritesList == false){
                        myDeletingPosition = position;
                        alertMessageString = "You cannot undo this action.";
                    }
                    else{
                        myDeletingPosition = favoritesPositions[position];
                        alertMessageString = "Deleting " + recipeList[myDeletingPosition] + " here will also delete it from Browse Recipes List.";
                    }
                    builder.setTitle("Delete " + recipeList[myDeletingPosition] + "?")
                            .setMessage(alertMessageString)
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(myDeletingPosition);
                                    dialogInterface.cancel();
                                    MenuItem delete_icon = myMenu.findItem(R.id.item_delete);
                                    delete_icon.setIcon(R.drawable.baseline_delete_outline_24);
                                    noLongerDeletingImages();
                                    deleting = false;
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    MenuItem delete_icon = myMenu.findItem(R.id.item_delete);
                                    delete_icon.setIcon(R.drawable.baseline_delete_outline_24);
                                    noLongerDeletingImages();
                                    deleting = false;
                                }
                            })
                            .show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.overflow_menu, menu);
        myMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public int returnRecipeListLength(){
        return recipeList.length;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_add:
                openAddRecipe();
                break;
            case R.id.item_delete:
                MenuItem delete_icon = myMenu.findItem(R.id.item_delete);
                if(deleting == false){
                    delete_icon.setIcon(R.drawable.baseline_delete_24);
                    deleting = true;
                    deletingImages();
                    Toast.makeText(this, "Select something to delete...", Toast.LENGTH_SHORT).show();
                }
                else{
                    delete_icon.setIcon(R.drawable.baseline_delete_outline_24);
                    noLongerDeletingImages();
                    deleting = false;
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("RecipeList", recipeList);
        intent.putExtra("RecipeDescription", recipeDescription);
        intent.putExtra("RecipeImages", recipeImages);
        intent.putExtra("RecipeImageDrawableArray", recipeImagesDrawableString);
        intent.putExtra("RecipeIngredients", recipeIngredients);
        intent.putExtra("RecipeInstructions", recipeInstructions);
        intent.putExtra("Favorited", favoritedArray);
        saveState();
        startActivity(intent);
    }

    public void openAddRecipe(){
        Intent intent = new Intent(this, AddRecipe.class);
        intent.putExtra("RecipeList", recipeList);
        intent.putExtra("RecipeDescription", recipeDescription);
        intent.putExtra("RecipeImages", recipeImages);
        intent.putExtra("RecipeImageDrawableArray", recipeImagesDrawableString);
        intent.putExtra("RecipeListLength", recipeList.length);
        intent.putExtra("RecipeIngredients", recipeIngredients);
        intent.putExtra("RecipeInstructions", recipeInstructions);
        intent.putExtra("Favorited", favoritedArray);
        intent.putExtra("InFavoritesList", inFavoritesList);
        saveState();
        startActivity(intent);
    }

    public void openViewRecipe(int position){
        Intent intent = new Intent(RecipeList.this, ViewRecipe.class);
        intent.putExtra("position", position);
        intent.putExtra("RecipeList", recipeList);
        intent.putExtra("RecipeDescription", recipeDescription);
        intent.putExtra("RecipeImages", recipeImages);
        intent.putExtra("RecipeImageDrawableArray", recipeImagesDrawableString);
        intent.putExtra("RecipeIngredients", recipeIngredients);
        intent.putExtra("RecipeInstructions", recipeInstructions);
        intent.putExtra("Favorited", favoritedArray);
        intent.putExtra("InFavoritesList", inFavoritesList);
        intent.putExtra("FavoritesPositions", favoritesPositions);
        saveState();
        startActivity(intent);
    }

    private void deleteItem(int position){
        String newRecipeList[] = new String[recipeList.length-1];
        String newRecipeDescription[] = new String[recipeDescription.length - 1];
        int newRecipeImages[] = new int[recipeImages.length-1];
        Drawable newRecipeImageViews [] = new Drawable[recipeImagesDrawable.length-1];
        String newRecipeImageDrawablesString [] = new String[recipeImagesDrawableString.length-1];
        String newRecipeIngredients [] = new String[recipeIngredients.length - 1];
        String newRecipeInstructions [] = new String[recipeInstructions.length - 1];
        boolean newFavoritedArray [] = new boolean[favoritedArray.length - 1];

        String whatWasDeleted = recipeList[position];

        for(int i=0, k=0;i<recipeList.length;i++){
            if(i!=position){
                newRecipeList[k]=recipeList[i];
                newRecipeDescription[k]=recipeDescription[i];
                newRecipeImages[k]=recipeImages[i];
                newRecipeImageViews[k] = recipeImagesDrawable[i];
                newRecipeImageDrawablesString[k] = recipeImagesDrawableString[i];
                newRecipeIngredients[k] = recipeIngredients[i];
                newRecipeInstructions[k] = recipeInstructions[i];
                newFavoritedArray[k] = favoritedArray[i];
                k++;
            }
        }

        recipeList = newRecipeList;
        recipeDescription = newRecipeDescription;
        recipeImages = newRecipeImages;
        recipeImagesDrawable = newRecipeImageViews;
        recipeImagesDrawableString = newRecipeImageDrawablesString;
        recipeIngredients = newRecipeIngredients;
        recipeInstructions = newRecipeInstructions;
        favoritedArray = newFavoritedArray;

        listView = (ListView) findViewById(R.id.recipe_list_view);
        CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), recipeList, recipeDescription, recipeImages, recipeImagesDrawable);
        listView.setAdapter(customBaseAdapter);

        Toast.makeText(this, whatWasDeleted + " was deleted.", Toast.LENGTH_SHORT).show();
        saveState();
    }

    private void addItem(String myNewRecipeTitle, String myNewRecipeDescription, String myNewRecipeIngredients, String myNewRecipeInstructions, Drawable myNewRecipeImage){
        String newRecipeList[] = new String[recipeList.length+1];
        String newRecipeDescription[] = new String[recipeDescription.length+1];
        int newRecipeImages[] = new int[recipeImages.length+1];
        Drawable newRecipeImageViews [] = new Drawable[recipeImagesDrawable.length+1];
        String newRecipeIngredients [] = new String[recipeIngredients.length + 1];
        String newRecipeInstructions [] = new String[recipeInstructions.length +1];


        for(int i=0;i<recipeList.length+1;i++){
            if(i<recipeList.length) {
                newRecipeList[i] = recipeList[i];
                newRecipeDescription[i] = recipeDescription[i];
                newRecipeImages[i] = recipeImages[i];
                newRecipeImageViews[i] = recipeImagesDrawable[i];
                newRecipeIngredients[i] = recipeIngredients[i];
                newRecipeInstructions[i] = recipeInstructions[i];
            }
            else{
                newRecipeList[i] = myNewRecipeTitle;
                newRecipeDescription[i] = myNewRecipeDescription;
                newRecipeImages[i] = android.R.color.transparent;
                newRecipeImageViews[i] = myNewRecipeImage;
                newRecipeIngredients[i] = myNewRecipeIngredients;
                newRecipeInstructions[i] = myNewRecipeInstructions;
            }
        }

        recipeList = newRecipeList;
        recipeDescription = newRecipeDescription;
        recipeImages = newRecipeImages;
        recipeImagesDrawable = newRecipeImageViews;
        recipeInstructions = newRecipeInstructions;
        recipeIngredients = newRecipeIngredients;

        if(inFavoritesList == false) {
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), recipeList, recipeDescription, recipeImages, recipeImagesDrawable);
            listView.setAdapter(customBaseAdapter);
        }
        else{
            makeFavoritesList(false);
        }

        Toast.makeText(this, recipeList[recipeList.length-1] + " was added!", Toast.LENGTH_SHORT).show();
        saveState();
    }

    private void deletingImages(){
        int deletingRecipeImages[] = new int[recipeImages.length];
        for (int i=0; i<recipeList.length; i++){
            deletingRecipeImages[i] = R.drawable.minus;
        }
        if(inFavoritesList == false) {
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), recipeList, recipeDescription, deletingRecipeImages, recipeImagesDrawable);
            listView.setAdapter(customBaseAdapter);
        }
        else{
            makeFavoritesList(true);
        }
    }

    private void noLongerDeletingImages(){
        if(inFavoritesList == false) {
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), recipeList, recipeDescription, recipeImages, recipeImagesDrawable);
            listView.setAdapter(customBaseAdapter);
        }
        else{
            makeFavoritesList(false);
        }
    }

    private void makeFavoritesList(boolean deleting){
        int favoriteListLength=0;
        for(int i = 0; i<favoritedArray.length; i++){
            if(favoritedArray[i] == true){
                favoriteListLength += 1;
            }
        }

        String newRecipeList[] = new String[favoriteListLength];
        String newRecipeDescription[] = new String[favoriteListLength];
        int newRecipeImages[] = new int[favoriteListLength];
        Drawable newRecipeImageViews [] = new Drawable[favoriteListLength];
        String newRecipeIngredients [] = new String[favoriteListLength];
        String newRecipeInstructions [] = new String[favoriteListLength];
        int newFavoritesPositions[] = new int[favoriteListLength];

        for(int i = 0, k=0; i<recipeList.length; i++){
            if(favoritedArray[i] == true){
                newRecipeList[k] = recipeList[i];
                newRecipeDescription[k] = recipeDescription[i];
                newRecipeImages[k] = recipeImages[i];
                newRecipeImageViews[k] = recipeImagesDrawable[i];
                newRecipeIngredients[k] = recipeIngredients[i];
                newRecipeInstructions[k] = recipeInstructions[i];

                newFavoritesPositions[k] = i;

                k++;
            }
        }

        favoritesPositions = newFavoritesPositions;

        if(deleting == false) {
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), newRecipeList, newRecipeDescription, newRecipeImages, newRecipeImageViews);
            listView.setAdapter(customBaseAdapter);
        }
        else{
            int deletingRecipeImages[] = new int[favoriteListLength];
            for (int i=0; i<favoriteListLength; i++){
                deletingRecipeImages[i] = R.drawable.minus;
            }
            listView = (ListView) findViewById(R.id.recipe_list_view);
            CustomBaseAdapter customBaseAdapter = new CustomBaseAdapter(getApplicationContext(), newRecipeList, newRecipeDescription, deletingRecipeImages, newRecipeImageViews);
            listView.setAdapter(customBaseAdapter);
        }
        saveState();
    }

    private void saveState(){
        SharedPreferences.Editor editor = getSharedPreferences("MyPrefs", MODE_PRIVATE).edit();

        String savedRecipeList = TextUtils.join("DELIMITER", recipeList);
        editor.putString("titles", savedRecipeList);

        String savedRecipeDescription = TextUtils.join("DELIMITER", recipeDescription);
        editor.putString("descriptions", savedRecipeDescription);

        String savedRecipeIngredients = TextUtils.join("DELIMITER", recipeIngredients);
        editor.putString("ingredients", savedRecipeIngredients);

        String savedRecipeInstructions = TextUtils.join("DELIMITER", recipeInstructions);
        editor.putString("instructions", savedRecipeInstructions);

        String savedRecipeImagesDrawableString = TextUtils.join("DELIMITER", recipeImagesDrawableString);
        editor.putString("drawablesString", savedRecipeImagesDrawableString);

        String savedFavoritedArray = Arrays.toString(favoritedArray)
                .replace(", ", "DELIMITER")
                .replaceAll("[\\[\\]]", "");
        editor.putString("favoritedArray", savedFavoritedArray);


        String [] savedImagesDeleted = new String[recipeImages.length];
        for(int i=0; i<recipeImages.length; i++){
            if(recipeImages[i] == R.drawable.banana_bread){
                savedImagesDeleted[i] = "banana_bread";
            }
            if(recipeImages[i] == R.drawable.quiche) {
                savedImagesDeleted[i] = "quiche";
            }
            if(recipeImages[i] == R.drawable.pie) {
                savedImagesDeleted[i] = "pie";
            }
        }
        String savedImagesDeleted1 = TextUtils.join("DELIMITER", savedImagesDeleted);
        editor.putString("imagesDeleted", savedImagesDeleted1);

        editor.apply();
    }
}