package com.example.garbagefinal;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
 * A fragment showing a list of all the garbage items
 * and where to place them.
 * An item will be deleted if the user clicks the item in the list.
 */

public class ListFragment extends Fragment {
    private Button backButton;

    ItemsViewModel garbageDB;
    RecyclerView itemList;

    @Override public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View v= inflater.inflate(R.layout.fragment_list, container, false);
        backButton= v.findViewById(R.id.back_button);
        garbageDB = new ViewModelProvider(requireActivity()).get(ItemsViewModel.class);

        //setting up the recycler view
        itemList = v.findViewById(R.id.listItems);

        itemList.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemAdapter mAdapter= new ItemAdapter();
        itemList.setAdapter(mAdapter);

        garbageDB.getLiveData().observe(getActivity(), itemsDB -> mAdapter.notifyDataSetChanged());

        //Only show List button in Portraitmode
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            garbageDB.getLiveData().observe(getActivity(), db -> mAdapter.notifyDataSetChanged());
            backButton = v.findViewById(R.id.back_button);
            backButton.setOnClickListener(view -> Navigation.findNavController(view).navigate(R.id.action_listFragment_to_uiFragment));
        }



        return v;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView mWhatTextView, mWhereTextView, mNoView;
        private final ImageView itemImage;


        public ItemHolder(View itemView) {
            super(itemView);
            mNoView= itemView.findViewById(R.id.item_no);
            mWhatTextView= itemView.findViewById(R.id.item_what);
            mWhereTextView= itemView.findViewById(R.id.item_where);
            itemImage = itemView.findViewById(R.id.item_image);
            itemView.setOnClickListener(this);
        }

        public void bind(Item item, int position){
            mNoView.setText(" "+position+" ");
            mWhatTextView.setText(item.getWhat());
            mWhereTextView.setText(item.getWhere());
            byte[] temp= item.getPict();
            if (temp != null)
                itemImage.setImageBitmap(item.ConvertByteArrayToBitmap(temp));
        }

        // Deleting an item by clicking it in the list
        @Override
        public void onClick(View v) {
            // Trick from https://stackoverflow.com/questions/5754887/accessing-view-inside-the-linearlayout-with-code
            String what= (String)((TextView)v.findViewById(R.id.item_what)).getText();
            garbageDB.removeItem(what);
            Toast.makeText(getActivity(), "Item deleted.", Toast.LENGTH_LONG).show();
        }
    }

    //ItemAdapter = link between data and layout
    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater= LayoutInflater.from(getActivity());
            View v= layoutInflater.inflate(R.layout.one_row, parent, false);
            return new ItemHolder(v);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item =  garbageDB.getList().get(position);
            holder.bind(item, position);
        }

        @Override
        public int getItemCount(){ return garbageDB.size(); }
    }
}