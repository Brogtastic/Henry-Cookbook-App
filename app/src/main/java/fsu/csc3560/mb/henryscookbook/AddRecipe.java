package fsu.csc3560.mb.henryscookbook;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddRecipe extends AppCompatActivity {

    EditText recipeTitle, recipeDescription, recipeIngredients, recipeInstructions;
    ImageButton recipeImage;
    Drawable [] userImagesList;
    int length;

    boolean imageSet = false;

    String [] recipeTitlesCont, recipeDescriptionsCont, recipeImagesDrawableCont, recipeIngredientsCont, recipeInstructionsCont;
    int [] recipeImagesCont;

    boolean [] favoritedArrayCont;
    boolean inFavoritesListCont;

    Button addRecipe;

    Drawable recipeImageDrawable;

    private final int GALLERY_REQ_CODE = 1000;


    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        length = intent.getIntExtra("RecipeListLength", 0);
        recipeTitlesCont = intent.getStringArrayExtra("RecipeList");
        recipeDescriptionsCont = intent.getStringArrayExtra("RecipeDescription");
        recipeImagesCont = intent.getIntArrayExtra("RecipeImages");
        recipeImagesDrawableCont = intent.getStringArrayExtra("RecipeImageDrawableArray");
        recipeInstructionsCont = intent.getStringArrayExtra("RecipeInstructions");
        recipeIngredientsCont = intent.getStringArrayExtra("RecipeIngredients");
        favoritedArrayCont = intent.getBooleanArrayExtra("Favorited");
        inFavoritesListCont = intent.getBooleanExtra("InFavoritesList", false);

        userImagesList = new Drawable[length];

        recipeTitle = findViewById(R.id.recipe_title_enter);
        recipeDescription = findViewById(R.id.recipe_description_enter);
        recipeIngredients = findViewById(R.id.recipe_ingredients_enter);
        recipeIngredients.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        recipeInstructions = findViewById(R.id.recipe_instructions_enter);
        recipeInstructions.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        recipeImage = findViewById(R.id.recipe_image_enter);

        addRecipe = findViewById(R.id.add_recipe);

        builder = new AlertDialog.Builder(this);

        addRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((recipeTitle.length() != 0) && (recipeDescription.length() != 0) && (recipeIngredients.length() != 0) && (recipeInstructions.length() != 0) && (imageSet == true)){
                    openRecipeList();
                }
                else{
                    Toast.makeText(v.getContext(), "All fields must be filled in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recipeImage.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View v){
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery, GALLERY_REQ_CODE);
            }
        });

        recipeIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeIngredients.setCursorVisible(true);
            }
        });

        recipeIngredients.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String[] lines = recipeIngredients.getText().toString().split("\\r?\\n"); // split text into an array of lines

                    for (int i = 0; i < lines.length; i++) {
                        String lineString = lines[i].trim(); // remove leading/trailing white space from line
                        String firstTwoChars = "";
                        if (lineString.length() >= 2) {
                            firstTwoChars = lineString.substring(0, 2);
                        }
                        if (!firstTwoChars.equals("• ")) {
                            String newString = "\u2022 " + lineString.substring(0);
                            lines[i] = newString;
                        } else {
                            lines[i] = lineString;
                        }
                    }
                    String newText = TextUtils.join("\n", lines); // join lines back into a single string
                    recipeIngredients.setText(newText); // set text of the EditText to the updated string
                    recipeIngredients.setSelection(newText.length()); // move cursor to end of text
                }
                return false;
            }
        });

        recipeInstructions.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    String[] lines = recipeInstructions.getText().toString().split("\\r?\\n"); // split text into an array of lines

                    for (int i = 0; i < lines.length; i++) {
                        String lineString = lines[i].trim(); // remove leading/trailing white space from line
                        String firstTwoChars = "";
                        String firstThreeChars = "";
                        if (lineString.length() >= 2) {
                            firstTwoChars = lineString.substring(0, 2);
                        }
                        if (lineString.length() >= 3) {
                            firstThreeChars = lineString.substring(0, 3);
                        }
                        if (!firstTwoChars.equals((i+1) + ")") && (i<9)) {
                            String newString = ((i+1) + ") ") + lineString.trim();
                            lines[i] = newString;
                        } else if (firstTwoChars.equals((i+1) + ")")){
                            lines[i] = lineString.trim();
                        } else if (!firstThreeChars.equals((i+1) + ")")) {
                            String newString = ((i + 1) + ") ") + lineString.substring(0).trim();
                            lines[i] = newString;
                        }
                    }
                    String newText = TextUtils.join("\n", lines); // join lines back into a single string
                    recipeInstructions.setText(newText); // set text of the EditText to the updated string
                    recipeInstructions.setSelection(newText.length()); // move cursor to end of text
                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQ_CODE){
                // for gallery
                recipeImage.setImageURI(data.getData());
                recipeImageDrawable = recipeImage.getDrawable();
                userImagesList[length-1] = recipeImageDrawable;
                imageSet = true;
            }
        }
    }

    Drawable getUserImage(int position){
        return userImagesList[position];
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if((recipeTitle.length() == 0) && (recipeDescription.length() == 0) && (recipeIngredients.length() == 0) && (recipeInstructions.length() == 0)) {
                    onBackPressed();
                }
                else{
                    builder.setTitle("Go back?")
                            .setMessage("You will lose your data.")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    onBackPressed();
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void openRecipeList(){
        Intent intent = new Intent(this, RecipeList.class);

        String recipeTitleString = recipeTitle.getText().toString();
        String recipeDescriptionString = recipeDescription.getText().toString();
        String recipeIngredientsString = recipeIngredients.getText().toString();
        String recipeInstructionsString = recipeInstructions.getText().toString();
        ImageView recipeImageView = (ImageView) recipeImage;

        intent.putExtra("NewRecipeTitle", recipeTitleString);
        intent.putExtra("NewRecipeDescription", recipeDescriptionString);
        intent.putExtra("NewRecipeIngredients", recipeIngredientsString);
        intent.putExtra("NewRecipeInstructions", recipeInstructionsString);

        intent.putExtra("RecipeList", recipeTitlesCont);
        intent.putExtra("RecipeDescription", recipeDescriptionsCont);
        intent.putExtra("RecipeImages", recipeImagesCont);
        intent.putExtra("RecipeIngredients", recipeIngredientsCont);
        intent.putExtra("RecipeInstructions", recipeInstructionsCont);
        intent.putExtra("RecipeImagesDrawableString", recipeImagesDrawableCont);
        intent.putExtra("Favorited", favoritedArrayCont);
        intent.putExtra("InFavoritesList", inFavoritesListCont);

        String str1 = new Boolean(inFavoritesListCont).toString();

        Bitmap bitmap = ((BitmapDrawable) recipeImageDrawable).getBitmap();

        String thisFileName = "image" + length + ".png";
        // Create a file object
        File file = new File(this.getFilesDir(), thisFileName);

        try {
            // Create a file output stream
            FileOutputStream fos = new FileOutputStream(file);

            // Compress the bitmap to a PNG format and write it to the output stream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            // Close the output stream
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.putExtra("picname", thisFileName);

        intent.putExtra("NewItem", true);

        startActivity(intent);
    }

}