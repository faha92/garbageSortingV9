package com.example.garbagefinal;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

/*
 * A fragment containing the UI
 * for the app with options to:
 *  -  enter a word and search for where to place that item,
 *  -  enter a what+where and add that to the database.
 */

public class UIFragment extends Fragment {
    private final static String ImageIntent= "android.media.action.IMAGE_CAPTURE";
    private final static int IMAGE_REQUEST= 2;
    private static final String TAG = "UIFragment";
    private TextView alert;
    private TextView itemWhat;
    private TextView itemWhere;
    private Button itemListButton;
    private Button addItemButton;
    private Button whereButton;
    private Button takePictureButton;
    private Button reset;
    private ImageView image;
    private Bitmap imageBitmap;

    //model: database of items
    private ItemsViewModel garbageDB;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v= inflater.inflate(R.layout.fragment_ui, container, false);

        //text fields and buttons
        alert = v.findViewById(R.id.alert);
        itemWhat = v.findViewById(R.id.what_text);
        itemWhere = v.findViewById(R.id.where_text);
        whereButton = v.findViewById(R.id.search_button);
        itemListButton = v.findViewById(R.id.itemList_button);
        addItemButton = v.findViewById(R.id.add_button);
        image= v.findViewById(R.id.photo_view);
        takePictureButton= v.findViewById(R.id.take_photo_button);
        reset = v.findViewById(R.id.reset);


        // Shared data
        garbageDB = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);

        //Only show List button in Portraitmode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            itemListButton = v.findViewById(R.id.itemList_button);
            itemListButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_uiFragment_to_listFragment));
        }

        whereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = "";
                input = itemWhat.getText().toString();
                itemWhere.setText(input + " should be placed in: " + garbageDB.getWhere(input));
            }
        });

       reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                itemWhat.setText("");
                itemWhere.setText("");
            }});

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((itemWhat.getText().length() > 0) && (itemWhere.getText().length() > 0)) {
                    Item newItem= new Item(
                            itemWhat.getText().toString(), itemWhere.getText().toString());
                    alert.setText(itemWhat.getText().toString()+" was successfully added to "+itemWhere.getText().toString());
                    if (imageBitmap != null)  {
                        Log.d(TAG, "onClick: image is not null processing");
                        newItem.setPict(Item.scaleAndConvert(imageBitmap));
                    }
                    garbageDB.addItem(newItem);
                    itemWhat.setText("");
                    itemWhere.setText("");
                    image.setVisibility(View.INVISIBLE);
                    imageBitmap= null;
                }
            }
        });

        //Handling pictures of things
        takePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageIntent);
                startActivityForResult(intent, IMAGE_REQUEST);
            }
        });

        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_REQUEST) {
                Bundle extras= intent.getExtras();
                imageBitmap= (Bitmap) extras.get("data");
                image.setVisibility(View.VISIBLE);
                image.setImageBitmap(imageBitmap);
            }
        }
    }
}
