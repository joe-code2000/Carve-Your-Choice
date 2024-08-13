package com.example.carveyourchoice.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.MealPlanner.MealPlannerActivity;
import com.example.carveyourchoice.Activities.MealPlanner.SelectMealPlannerActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.IngredientRecyclerAdapter;
import com.example.carveyourchoice.MainActivity;
import com.example.carveyourchoice.Models.Recipe;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity {
    private static Context context;
    private DBHelper DB;
    private Recipe recipe = null;
    private int id;
    private String action, activity_name;
    private RecyclerView ingredientRecyclerView;
    private IngredientRecyclerAdapter ingredientAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        context = getApplicationContext();
        DB = new DBHelper(context);

        if(getIntent().hasExtra("id")){
            id = getIntent().getIntExtra("id",-1);
            recipe = DB.getRecipe(id);
        }

        if (recipe != null){
            initialise(recipe);
        }else{
            Toast.makeText(getApplicationContext(), "Invalid", Toast.LENGTH_SHORT).show();
            finish();
        }

        if(getIntent().hasExtra("delete")){
            action = getIntent().getStringExtra("delete");
        }
        if (getIntent().hasExtra("add")){
            action = getIntent().getStringExtra("add");
        }

        if(getIntent().hasExtra("activity_name")){
            activity_name = getIntent().getStringExtra("activity_name");
        }

        Button go_back = findViewById(R.id.go_back);
        Button selectButton = findViewById(R.id.select);

        go_back.setOnClickListener(v->{
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("id",EditRecipeActivity.getId());
            startActivity(intent);
        });

        if (action != null && recipe != null){
            String[] data = action.split(",");
            String act = data[0];
            if(act.compareTo("delete") == 0){
                selectButton.setText(act+" from "+data[2]);
                JSONObject object = new JSONObject();
                try {
                    object.put("Date",data[1]);
                    object.put("Liar",data[2]);
                    object.put("Id",recipe.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                selectButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), MealPlannerActivity.class);
                    intent.putExtra("delete",object.toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
                });
            }else if(action.compareTo("add") == 0){
                selectButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getApplicationContext(), SelectMealPlannerActivity.class);
                    intent.putExtra("add",recipe.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);

                });
            }
        }

        //Ingredient Adapter and RecyclerView
        ingredientRecyclerView = findViewById(R.id.ingredients);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        ingredientAdapter = new IngredientRecyclerAdapter(context,recipe.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientAdapter);

    }

    private void initialise(Recipe recipe){
        TextView name, diet, ingredient_count, duration, instructions;
        CardView delete_cardView, edit_cardView;
        ImageView image;
        name = findViewById(R.id.name);
        diet = findViewById(R.id.diet);
        image = findViewById(R.id.image);
        delete_cardView = findViewById(R.id.delete_cardView);
        edit_cardView = findViewById(R.id.edit_cardView);
        if(recipe.getUser_id() != 1){
            edit_cardView.setVisibility(View.VISIBLE);
            Button editBtn = findViewById(R.id.edit);
            editBtn.setOnClickListener(v->{
                Intent intent = new Intent(getApplicationContext(),EditRecipeActivity.class);
                intent.putExtra("id",recipe.getId());
                startActivity(intent);
            });

            delete_cardView.setVisibility(View.VISIBLE);
            Button deleteBtn = findViewById(R.id.delete);
            deleteBtn.setOnClickListener(v->{
                DB.deleteRecipe(recipe.getId(),recipe.getUser_id());
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            });
        }
        ingredient_count = findViewById(R.id.ingredient_count);
        duration = findViewById(R.id.duration);
        instructions = findViewById(R.id.instructions);
        name.setText(recipe.getName());
        diet.setText(recipe.getDiet());
        image.setImageDrawable(Drawable.createFromPath(Utilities.loadImage(recipe.getImage(),context).toString()));
        int ingredientSize = recipe.getIngredients().size();
        String ingredientCount = ingredientSize < 2 ? ingredientSize + " Ingredient": ingredientSize + " Ingredients";
        ingredient_count.setText(ingredientCount);
        if(recipe.getDuration() != null){
            duration.setText(recipe.getDuration());
        }
        if (recipe.getInstruction() != null){
            instructions.setText(recipe.getInstruction());
        }
        WebView web = findViewById(R.id.web);

        if(recipe.getVideo() != null && !recipe.getVideo().isEmpty()){
            web.setVisibility(View.VISIBLE);
            web.setWebViewClient(new WebViewClient());
            web.getSettings().setLoadsImagesAutomatically(true);
            web.getSettings().setJavaScriptEnabled(true);
            web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            MyBrowser browser = new MyBrowser();
            browser.shouldOverrideUrlLoading(web,recipe.getVideo());
        }
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

}