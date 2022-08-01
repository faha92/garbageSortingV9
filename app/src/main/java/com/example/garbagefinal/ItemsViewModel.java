package com.example.garbagefinal;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;


public class ItemsViewModel extends ViewModel {
    private static MutableLiveData<Database> items;

    public void initialize(Context context){
        items.getValue().initialize(context);
    }

    public ItemsViewModel() {
        items= new MutableLiveData<>(); //nyt object
        items.setValue(new Database()); //objektet fyldes med vores database (gemmer itemsDB objekt i den)
    }

    public MutableLiveData<Database> getLiveData() { return items; }

    public String getWhere(String what){
        String where ="";
        Database temp= items.getValue();
        where = temp.getWhere(what);
        return where;
    }

    public void addItem(String what, String where){
        Database temp= items.getValue();
        temp.addItem(what, where);
        items.setValue(temp);
    }

    public void addItem(Item newItem) {
        Log.d("ItemsViewModel", "addItem: starts");
        Database temp= items.getValue();
        temp.addItem(newItem);
        items.setValue(temp);
    }

    public List<Item> getList() {
        return items.getValue().getValues();
    }

    public int size() { return items.getValue().size();}

    public void removeItem(String what){
        //what are we doing below? accessing the shared database and removing the item
        Database temp = items.getValue();
        temp.removeItem(what);
        items.setValue(temp);
    }

}
