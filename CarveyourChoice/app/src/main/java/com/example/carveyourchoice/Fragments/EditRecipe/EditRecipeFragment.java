package com.example.carveyourchoice.Fragments.EditRecipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.MyRecipe.AddRecipeActivity;
import com.example.carveyourchoice.Activities.RecipeDetailActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.SelectedIngredientRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;


public class EditRecipeFragment extends Fragment {
    private DBHelper DB;
    private String hourStr, minuteStr;
    private ImageView image;
    private int PICK_IMAGE = 10;
    private EditText editName,editDiet,instruction,video;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);
        DB = new DBHelper(getContext());
        hourStr = "";
        minuteStr = "";

        image = view.findViewById(R.id.image);

        Button add_image, remove_image;
        add_image = view.findViewById(R.id.add_image);
        remove_image = view.findViewById(R.id.remove_image);

        add_image.setOnClickListener(v->{
            openGallery();
        });
        remove_image.setOnClickListener(v->{
            image.setImageResource(R.color.transparent_background);
            EditRecipeActivity.setImageUri(null);
        });

         editName = (EditText)view.findViewById(R.id.name);
         editDiet = (EditText)view.findViewById(R.id.diet);
         instruction = (EditText)view.findViewById(R.id.instruction);
         video = (EditText) view.findViewById(R.id.edit_video);

        //Presets name value depending on the recipe
        if(EditRecipeActivity.getSelectedName() != null){
            editName.setText(EditRecipeActivity.getSelectedName());
        }

        if (EditRecipeActivity.getSelectedDiet() != null){
            editDiet.setText(EditRecipeActivity.getSelectedDiet());
        }

        //Presets instruction value depending on the recipe
        if(EditRecipeActivity.getSelectedInstruction() != null){
            instruction.setText(EditRecipeActivity.getSelectedInstruction());
        }
        //Presets category value depending on the recipe
        if (EditRecipeActivity.getSelectedCategory() != null){
            TextView category = view.findViewById(R.id.category_name);
            category.setText(EditRecipeActivity.getSelectedCategory().getName());
        }
        //Presets video value depending on the recipe
        if(EditRecipeActivity.getSelectedVideo() != null){
            video.setText(EditRecipeActivity.getSelectedVideo());
        }

        //Gets the hour NumberPicker view from the layout
        NumberPicker hour_pricker = view.findViewById(R.id.duration_hour);

        //Gets the minute NumberPicker view from the layout
        NumberPicker minute_pricker = view.findViewById(R.id.duration_minute);

        //Sets the max hour value to 24
        hour_pricker.setMaxValue(24);

        //Sets the max minute value to 60
        minute_pricker.setMaxValue(60);

        //presets the duration of a recipe depending on the recipe's duration
        if (EditRecipeActivity.getSelectedDuration() != null){
            String d = EditRecipeActivity.getSelectedDuration();

            int hour = 0;
            if (d.contains("hour") || d.contains("hours")){
                hour = Integer.parseInt(d.split("hour")[0].trim());
            }
            hour_pricker.setValue(hour);

            if (hour_pricker.getValue() != 0){
                String str = "";
                if (hour_pricker.getValue() == 1){
                    str = hour_pricker.getValue()+" hour";
                }else {
                    str = hour_pricker.getValue()+" hours";
                }
                hourStr = str;
            }

            int minute = 0;
            if (d.contains("minute")){
                if (d.split("minute")[0].contains("hour") || d.split("minute")[0].contains("hour")){
                    minute = Integer.parseInt(d.split("minute")[0].split("hour")[1].replace("s","").trim());
                }else {
                    minute =  Integer.parseInt(d.split("minute")[0].trim());
                }
            }
            minute_pricker.setValue(minute);
            if (minute_pricker.getValue() != 0){
                String str = "";
                if (minute_pricker.getValue() == 1){
                    str = minute_pricker.getValue()+" minute";
                }else {
                    str = minute_pricker.getValue()+" minutes";
                }
                minuteStr = str;
            }
        }

        //Sets the hour duration
        hour_pricker.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (hour_pricker.getValue() != 0){
                String str = "";
                if (hour_pricker.getValue() == 1){
                    str = hour_pricker.getValue()+" hour";
                }else {
                    str = hour_pricker.getValue()+" hours";
                }
                hourStr = str;
            }
        });

        //Sets the minute duration
        minute_pricker.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (minute_pricker.getValue() != 0){
                String str = "";
                if (minute_pricker.getValue() == 1){
                    str = minute_pricker.getValue()+" minute";
                }else {
                    str = minute_pricker.getValue()+" minutes";
                }
                minuteStr = str;
            }
        });

        Button select_categoryBtn = view.findViewById(R.id.select_categoryBtn);

        select_categoryBtn.setOnClickListener(v->{
            persist();
            Navigation.findNavController(view).navigate(R.id.listCategoriesFragment2);
        });

        Button add_ingredientBtn = view.findViewById(R.id.add_ingredientBtn);
        add_ingredientBtn.setOnClickListener(v->{
            persist();
            Navigation.findNavController(view).navigate(R.id.myRecipeSelectIngredientFragment2);
        });

        Button done = view.findViewById(R.id.done);
        done.setOnClickListener(v->{
            int id = EditRecipeActivity.getId();
            String name = editName.getText().toString();
            String diet = editDiet.getText().toString();
            String duration = hourStr+" "+minuteStr;
            Category category = EditRecipeActivity.getSelectedCategory();
            List<Ingredient> ingredients = EditRecipeActivity.getSelected_ingredients();
            if (!name.isEmpty() && !duration.isEmpty()  && category != null && ingredients.size() > 0){
                if (EditRecipeActivity.getImageUri() != null){
                    String image = Utilities.saveImage(getContext(),EditRecipeActivity.getImageUri());
                    DB.updateRecipe(id,name,diet,image,category.getId(),duration,instruction.getText().toString(),video.getText().toString());
                }else {
                    DB.updateRecipe(id,name,diet,category.getId(),duration,instruction.getText().toString(),video.getText().toString());
                }
                if (EditRecipeActivity.getSelected_ingredients().size() > 0){
                    DB.resetRecipeIngredients(EditRecipeActivity.getId());
                }
                EditRecipeActivity.getSelected_ingredients().forEach(ingredient -> {
                    DB.updateRecipeIngredients(EditRecipeActivity.getId(), ingredient.getId());
                });
                resetData();
                Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                intent.putExtra("id",EditRecipeActivity.getId());
                intent.putExtra("add","add");
                startActivity(intent);
            }else {
                if (editName.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Recipe name can not be empty", Toast.LENGTH_SHORT).show();
                }
                if (category != null){
                    Toast.makeText(getContext(), "Category can not be null", Toast.LENGTH_SHORT).show();
                }
                if (duration.isEmpty()){
                    Toast.makeText(getContext(), "Recipe duration can not be empty", Toast.LENGTH_SHORT).show();
                }
                if(ingredients.size() < 1){
                    Toast.makeText(getContext(), "at least one ingredient is required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button go_back = view.findViewById(R.id.go_back);
        go_back.setOnClickListener(v->{
            resetData();
            getActivity().finish();
        });

        EditRecipeActivity.setIngredientRecyclerView(view.findViewById(R.id.ingredients));
        EditRecipeActivity.getIngredientRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL,false));
        EditRecipeActivity.setIngredientAdapter(new SelectedIngredientRecyclerAdapter(getContext(),EditRecipeActivity.getSelected_ingredients(),getActivity().getLocalClassName()));
        EditRecipeActivity.getIngredientRecyclerView().setAdapter(EditRecipeActivity.getIngredientAdapter());

        return view;
    }

    public void persist(){
        EditRecipeActivity.setSelectedName(editName.getText().toString());
        EditRecipeActivity.setSelectedDiet(editDiet.getText().toString());
        EditRecipeActivity.setSelectedDuration(hourStr+" "+minuteStr);
        EditRecipeActivity.setSelectedInstruction(instruction.getText().toString());
        EditRecipeActivity.setSelectedVideo(video.getText().toString());
    }

    public void resetData(){
        EditRecipeActivity.setRecipe(null);
        EditRecipeActivity.setSelectedDiet(null);
        EditRecipeActivity.setSelectedName(null);
        EditRecipeActivity.setSelectedDuration(null);
        EditRecipeActivity.setSelectedInstruction(null);
        EditRecipeActivity.setSelectedCategory(null);
        EditRecipeActivity.getSelected_ingredients().clear();
        EditRecipeActivity.setImageUri(null);
    }

    public void openGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(gallery,"Select image"),PICK_IMAGE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(EditRecipeActivity.getImageUri() != null){
            image.setImageURI(EditRecipeActivity.getImageUri());
        }else{
            if (EditRecipeActivity.getRecipe() != null){
                if(EditRecipeActivity.getImageUri() != null){
                    image.setImageURI(Uri.parse(EditRecipeActivity.getRecipe().getImage()));
                }else{
                    image.setImageURI(Utilities.getImageUri(getContext(),Utilities.loadImage(EditRecipeActivity.getRecipe().getImage(),getContext())));
                }
            }
        }

    }
}