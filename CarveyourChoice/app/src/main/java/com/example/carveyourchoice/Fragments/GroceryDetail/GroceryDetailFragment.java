package com.example.carveyourchoice.Fragments.GroceryDetail;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carveyourchoice.Activities.Grocery.GroceryActivity;
import com.example.carveyourchoice.Activities.Grocery.GroceryDetailActivity;
import com.example.carveyourchoice.Adapters.RecyclerAdapters.SelectedIngredientRecyclerAdapter;
import com.example.carveyourchoice.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GroceryDetailFragment extends Fragment {
    private RecyclerView recyclerView;
    private static SelectedIngredientRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grocery_detail, container, false);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(GroceryDetailActivity.getGrocery().getName());

        Button arrow_back = (Button)view.findViewById(R.id.go_back);
        Button edit = (Button)view.findViewById(R.id.edit);
        Button grocery_delete = (Button)view.findViewById(R.id.delete);

        grocery_delete.setOnClickListener(v->{
            Intent intent = new Intent(getContext(), GroceryActivity.class);
            intent.putExtra("delete",GroceryDetailActivity.getId());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        });

        edit.setOnClickListener(v->{
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
            bottomSheetDialog.setContentView(R.layout.grocery_name_edit);
            bottomSheetDialog.setCanceledOnTouchOutside(true);

            Dialog dialog = new Dialog(getContext());

            dialog.setContentView(R.layout.grocery_name_edit);
            dialog.setCanceledOnTouchOutside(true);


            EditText editText = dialog.findViewById(R.id.edit_grocery);
            Button changeBtn = dialog.findViewById(R.id.change_btn);
            Button cancelBtn = dialog.findViewById(R.id.cancel_btn);

            dialog.show();

            changeBtn.setOnClickListener(x -> {
                title.setText(editText.getText().toString());
                GroceryDetailActivity.getGrocery().setName(editText.getText().toString());
                dialog.dismiss();
            });
            cancelBtn.setOnClickListener(x->{
                dialog.dismiss();
            });
        });

        arrow_back.setOnClickListener(v -> {
            String data = GroceryDetailActivity.convertDataToStr();
            Intent intent = new Intent(getContext(), GroceryActivity.class);
            intent.putExtra("save",data);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        });

        Button addIngredient = view.findViewById(R.id.add_ingredient_btn);

        addIngredient.setOnClickListener(v ->{
            Navigation.findNavController(view).navigate(R.id.selectIngredientFragment);
        });

        recyclerView = view.findViewById(R.id.grocery_detail_ingredient_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
        adapter = new SelectedIngredientRecyclerAdapter(view.getContext(),GroceryDetailActivity.getIngredients(),getActivity().getLocalClassName());
        recyclerView.setAdapter(adapter);

        return view;
    }

}