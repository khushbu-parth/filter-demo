package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.colorcallscreen.colorphone.callscreen.calltheme.adapter.ThemeContactAdapter;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.ThemeContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.j256.ormlite.field.FieldType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityThemeContactList extends AppCompatActivity implements SearchView.OnQueryTextListener {
    public static final String CONTACTS = "_contact_lits";
    AdLoad adLoad;
    private ThemeContactAdapter adapter;
    private List<ThemeContactModel> allContacts;
    private Button buttonSet;
    RecyclerView rvContacts;
    private SearchView searchView;
    private List<ThemeContactModel> selectedContacts;
    private List<String> selectedNumbers = new ArrayList();
    private List<String> selectedNames = new ArrayList();
    private boolean shouldExit = false;

    @Override
    public boolean onQueryTextSubmit(String str) {
        return false;
    }

    public void findNumber() {
        for (ThemeContactModel themeContactModel : this.selectedContacts) {
            this.selectedNames.add(themeContactModel.getName());
            Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id = ?", new String[]{themeContactModel.getId()}, null);
            while (query.moveToNext()) {
                String string = query.getString(query.getColumnIndex("data1"));
                this.selectedNumbers.add(string);
                Log.d("_TEST", "findNumber: " + string);
            }
        }
    }

    public List<ThemeContactModel> loadContacts(Context context) {
        if (ContextCompat.checkSelfPermission(this, BoloPermission.READ_CONTACTS) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{BoloPermission.READ_CONTACTS, BoloPermission.WRITE_CONTACTS}, 1);
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        Cursor query = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{"display_name", "data1", "has_phone_number", FieldType.FOREIGN_ID_FIELD_SUFFIX, "contact_id"}, null, null, "upper(display_name)");
        if (query.getCount() > 0) {
            while (query.moveToNext()) {
                ThemeContactModel themeContactModel = new ThemeContactModel();
                themeContactModel.setId(query.getString(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX)));
                String string = query.getString(query.getColumnIndex("display_name"));
                String string2 = query.getString(query.getColumnIndex("data1"));
                if (!arrayList.contains(string)) {
                    arrayList.add(string);
                    themeContactModel.setName(string);
                    themeContactModel.setNumber(string2);
                    if (query.getInt(query.getColumnIndex("has_phone_number")) > 0) {
                        themeContactModel.setHasNumber(true);
                    } else {
                        themeContactModel.setHasNumber(false);
                    }
                    if (themeContactModel.getName() != null) {
                        arrayList2.add(themeContactModel);
                    }
                }
            }
            return arrayList2;
        }
        return null;
    }

    @Override 
    public void onBackPressed() {
        if (this.shouldExit) {
            super.onBackPressed();
            return;
        }
        List<ThemeContactModel> list = this.selectedContacts;
        if (list != null) {
            if (list.size() > 0) {
                Toast.makeText(this, getString(R.string.press_again_back), Toast.LENGTH_SHORT).show();
                this.shouldExit = true;
                return;
            }
            return;
        }
        super.onBackPressed();
    }

    public void onContactSelected(List<ThemeContactModel> list) {
        this.selectedContacts = list;
        if (list.size() > 0) {
            this.buttonSet.setEnabled(true);
            this.buttonSet.setVisibility(View.VISIBLE);
            return;
        }
        this.buttonSet.setEnabled(false);
        this.buttonSet.setVisibility(View.GONE);
        this.shouldExit = true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_themecontact_list);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        this.searchView = searchView;

        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        txtSearch.setHint(getResources().getString(R.string.search_contacts));
        txtSearch.setHintTextColor(Color.WHITE);
        txtSearch.setTextColor(Color.WHITE);

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);

        searchIcon.setColorFilter(getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        searchView.setOnQueryTextListener(this);
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityThemeContactList.this.onBackPressed();
            }
        });
        findViewById(R.id.parent).setOnTouchListener(new View.OnTouchListener() {
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    Utility.hideSoftKeyboard(ActivityThemeContactList.this);
                }
                return false;
            }
        });
        String stringExtra = getIntent().getStringExtra("_tn");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvContacts);
        this.rvContacts = recyclerView;
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    Utility.hideSoftKeyboard(ActivityThemeContactList.this);
                }
                return false;
            }
        });
        this.rvContacts.setLayoutManager(new LinearLayoutManager(this));
        List<ThemeContactModel> loadContacts = loadContacts(this);
        this.allContacts = loadContacts;
        if (loadContacts == null) {
            this.allContacts = new ArrayList();
        }
        Collections.sort(this.allContacts, new Comparator<ThemeContactModel>() {
            @Override
            public int compare(ThemeContactModel themeContactModel, ThemeContactModel themeContactModel2) {
                return themeContactModel.getName().compareToIgnoreCase(themeContactModel2.getName());
            }
        });
        ThemeContactAdapter themeContactAdapter = new ThemeContactAdapter(this, this.allContacts, stringExtra);
        this.adapter = themeContactAdapter;
        this.rvContacts.setAdapter(themeContactAdapter);
        Button button = (Button) findViewById(R.id.buttonSet);
        this.buttonSet = button;
        button.setEnabled(false);
        this.buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (ActivityThemeContactList.this.selectedContacts == null || ActivityThemeContactList.this.selectedContacts.size() <= 0) {
                    return;
                }
                ActivityThemeContactList.this.findNumber();
                Intent intent = new Intent();
                intent.putStringArrayListExtra(ActivityThemeContactList.CONTACTS, (ArrayList) ActivityThemeContactList.this.selectedNumbers);
                intent.putStringArrayListExtra("_cm", (ArrayList) ActivityThemeContactList.this.selectedNames);
                ActivityThemeContactList.this.setResult(-1, intent);
                ActivityThemeContactList.this.finish();
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String str) {
        try {
            if (this.allContacts != null) {
                this.adapter.getFilter().filter(str);
            }
        } catch (Exception unused) {
        }
        return false;
    }
}
