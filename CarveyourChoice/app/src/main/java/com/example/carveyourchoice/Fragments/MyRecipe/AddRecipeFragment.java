package com.example.carveyourchoice.Fragments.MyRecipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carveyourchoice.Activities.EditRecipeActivity;
import com.example.carveyourchoice.Activities.MyRecipe.AddRecipeActivity;
import com.example.carveyourchoice.Activities.MyRecipe.MyRecipeActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.SelectedIngredientRecyclerAdapter;
import com.example.carveyourchoice.Models.Category;
import com.example.carveyourchoice.Models.Ingredient;
import com.example.carveyourchoice.R;
import com.example.carveyourchoice.Utilities.DBHelper;
import com.example.carveyourchoice.Utilities.Utilities;

import java.util.List;
import java.util.stream.Collectors;

public class AddRecipeFragment extends Fragment {
    private DBHelper DB;
    private String hourStr, minuteStr;
    private ImageView image;
    private int PICK_IMAGE = 10;
    private EditText editName, editDiet, instruction, video;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_recipe, container, false);
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
            AddRecipeActivity.setImageUri(null);
        });

        editName = (EditText)view.findViewById(R.id.name);
        editDiet = (EditText)view.findViewById(R.id.diet);
        instruction = (EditText)view.findViewById(R.id.instruction);
        video = (EditText) view.findViewById(R.id.edit_video);

        if(AddRecipeActivity.getSelectedName() != null){
            editName.setText(AddRecipeActivity.getSelectedName());
        }

        if (AddRecipeActivity.getSelectedDiet() != null){
            editDiet.setText(AddRecipeActivity.getSelectedDiet());
        }

        if(AddRecipeActivity.getSelectedInstruction() != null){
            instruction.setText(AddRecipeActivity.getSelectedInstruction());
        }

        if (AddRecipeActivity.getSelectedCategory() != null){
            TextView category = view.findViewById(R.id.category_name);
            category.setText(AddRecipeActivity.getSelectedCategory().getName());
        }

        if(AddRecipeActivity.getSelectedVideo() != null){
            video.setText(AddRecipeActivity.getSelectedVideo());
        }

        NumberPicker hour_pricker = view.findViewById(R.id.duration_hour);
        NumberPicker minute_pricker = view.findViewById(R.id.duration_minute);

        hour_pricker.setMaxValue(24);
        minute_pricker.setMaxValue(60);

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
            Navigation.findNavController(view).navigate(R.id.listCategoriesFragment);
        });

        Button add_ingredientBtn = view.findViewById(R.id.add_ingredientBtn);
        add_ingredientBtn.setOnClickListener(v->{
            persist();
            Navigation.findNavController(view).navigate(R.id.myRecipeSelectIngredientFragment);
        });


        Button done = view.findViewById(R.id.done);
        done.setOnClickListener(v->{
            String name = editName.getText().toString();
            String diet = editDiet.getText().toString();
            String duration = hourStr+" "+minuteStr;
            Category category = AddRecipeActivity.getSelectedCategory();
            List<Ingredient> ingredients = AddRecipeActivity.getSelected_ingredients();
            if (!name.isEmpty() && !duration.isEmpty()  && category != null && ingredients.size() > 0 && category != null){
                if (AddRecipeActivity.getImageUri() != null){
                    String image = Utilities.saveImage(getContext(),AddRecipeActivity.getImageUri());
                    DB.insertRecipe(2,name,diet,image,category.getId(),duration,instruction.getText().toString(),video.getText().toString());
                }else {
                    DB.insertRecipe(2,name,diet,category.getId(),duration,instruction.getText().toString(),video.getText().toString());
                }
                List<Integer> ids = DB.getRecipes().stream().map(x->x.getId()).collect(Collectors.toList());
                AddRecipeActivity.getSelected_ingredients().forEach(ingredient -> {
                    DB.insertRecipeIngredient(ids.get(ids.size()-1), ingredient.getId());
                });
                reset();
                Intent intent = new Intent(getContext(), MyRecipeActivity.class);
                startActivity(intent);
            }else{
                if (name.isEmpty()){
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
            reset();
            Intent intent = new Intent(getContext(),MyRecipeActivity.class);
            startActivity(intent);
        });

        AddRecipeActivity.setIngredientRecyclerView(view.findViewById(R.id.ingredients));
        AddRecipeActivity.getIngredientRecyclerView().setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        AddRecipeActivity.setIngredientAdapter(new SelectedIngredientRecyclerAdapter(getContext(),AddRecipeActivity.getSelected_ingredients(),getActivity().getLocalClassName()));
        AddRecipeActivity.getIngredientRecyclerView().setAdapter(AddRecipeActivity.getIngredientAdapter());

        return view;
    }
    public void persist(){
        AddRecipeActivity.setSelectedName(editName.getText().toString());
        AddRecipeActivity.setSelectedDiet(editDiet.getText().toString());
        AddRecipeActivity.setSelectedDuration(hourStr+" "+minuteStr);
        AddRecipeActivity.setSelectedInstruction(instruction.getText().toString());
        AddRecipeActivity.setSelectedVideo(video.getText().toString());
    }
    public void reset(){
        AddRecipeActivity.setSelectedName(null);
        AddRecipeActivity.setSelectedDiet(null);
        AddRecipeActivity.setSelectedDuration(null);
        AddRecipeActivity.setSelectedInstruction(null);
        AddRecipeActivity.setSelectedCategory(null);
        AddRecipeActivity.getSelected_ingredients().clear();
        AddRecipeActivity.setImageUri(null);
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
        if(AddRecipeActivity.getImageUri() != null){
            image.setImageURI(AddRecipeActivity.getImageUri());
        }
    }
}