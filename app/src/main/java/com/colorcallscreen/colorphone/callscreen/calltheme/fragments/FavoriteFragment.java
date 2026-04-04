package com.colorcallscreen.colorphone.callscreen.calltheme.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.FavoriteAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ContactModel_Favorites;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder;
import com.j256.ormlite.field.FieldType;
import java.util.ArrayList;


public class FavoriteFragment extends Fragment implements CallLogDetailActivity.FavoriteUpdateListener {
    AdLoad adLoad;
    private FavoriteAdapter adapterFavorites;
    public CallLogAction callLogAction;
    private ArrayList<ContactModel_Favorites> itemFavorites;
    RecyclerView rvFavList;
    public TextView tvNoFound;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_favorite, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.callLogAction = new CallLogActionBuilder(requireContext());
        this.rvFavList = (RecyclerView) view.findViewById(R.id.rvFavList);
        this.tvNoFound = (TextView) view.findViewById(R.id.tvNoFound);
        favoriteSetChange();
    }

    private void getAllFavorites() {
        FragmentActivity requireActivity = requireActivity();
        Cursor query = requireActivity.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, "starred=?", new String[]{"1"}, "display_name ASC");
        if (query == null) {
            Toast.makeText(requireActivity, "Something went wrong.Please try again", 0).show();
        } else if (query.getCount() > 0) {
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
                String string2 = query.getString(query.getColumnIndex("display_name"));
                String string3 = query.getString(query.getColumnIndex("photo_uri"));
                Cursor query2 = requireActivity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id =?", new String[]{string}, null);
                if (query2.moveToNext()) {
                    String string4 = query2.getString(query2.getColumnIndex("data1"));
                    ContactModel_Favorites contactModel_Favorites = new ContactModel_Favorites();
                    contactModel_Favorites.setId(string);
                    contactModel_Favorites.setName(string2);
                    contactModel_Favorites.setNumber(string4);
                    contactModel_Favorites.setImage(string3);
                    this.itemFavorites.add(contactModel_Favorites);
                    query2.close();
                }
            }
            query.close();
        }
    }

    @Override // com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.FavoriteUpdateListener
    public void favoriteSetChange() {
        this.itemFavorites = new ArrayList<>();
        try {
            if (requireActivity() != null) {
                getAllFavorites();
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(requireActivity(), this.itemFavorites, this);
                this.adapterFavorites = favoriteAdapter;
                this.rvFavList.setAdapter(favoriteAdapter);
                if (this.itemFavorites.size() > 0) {
                    this.tvNoFound.setVisibility(8);
                } else {
                    this.tvNoFound.setVisibility(0);
                }
            }
        } catch (Exception unused) {
        }
    }
}
