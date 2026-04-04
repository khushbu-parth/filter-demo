package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.ArrayList;


public class ActivityAddNewContact extends AppCompatActivity {
    Bitmap bitmap;
    ContactModel contactModel;
    AppCompatEditText edtMail;
    AppCompatEditText edtName;
    AppCompatEditText edtNumber;
    AppCompatEditText edtSecPhoneNo;
    CallLogComponent logComponent;
    Spinner spinEmailType;
    Spinner spinNumType;
    Spinner spinSecNumType;
    String title;
    AppCompatTextView txtName;
    AppCompatTextView txtSave;
    AppCompatTextView txtTitle;
    Uri uri;
    AppCompatImageView user_img;
    String[] typePhoneArr = {"Home", "Mobile", "Work"};
    String[] typeEmailArr = {"Home", "Work"};
    String contactNo = "";
    String contact1 = "";
    String contact2 = "";
    ArrayList<PhoneModel> emailList = new ArrayList<>();


    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        boolean z;
        boolean z2;
        super.onCreate(bundle);
        setContentView(R.layout.activity_add_new_contact);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        if (ActivityCompat.checkSelfPermission(this, BoloPermission.READ_CALL_LOG) != 0) {
            return;
        }
        this.logComponent = new CallLogComponent(BoloApplication.getApplication(), null);
        this.contactNo = getIntent().getStringExtra("contactNo");
        findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                ActivityAddNewContact.this.onBackPressed();
            }
        });
        initUI();
        String stringExtra = getIntent().getStringExtra("title");
        this.title = stringExtra;
        this.txtTitle.setText(stringExtra);
        this.user_img.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.PICK");
                intent.setType("image/*");
                ActivityAddNewContact.this.startActivityForResult(Intent.createChooser(intent, "Choose Image"), 100);
            }
        });
        this.edtName.addTextChangedListener(new TextWatcher() {
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                ActivityAddNewContact.this.txtName.setText(editable.toString());
            }
        });
        this.spinNumType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                Toast.makeText(ActivityAddNewContact.this.getApplicationContext(), ActivityAddNewContact.this.typePhoneArr[i], Toast.LENGTH_LONG).show();
            }
        });
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.typePhoneArr);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spinNumType.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinSecNumType.setAdapter((SpinnerAdapter) arrayAdapter);
        this.spinSecNumType.setSelection(1);
        this.spinSecNumType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, 17367048, this.typeEmailArr);
        arrayAdapter2.setDropDownViewResource(17367049);
        this.spinEmailType.setAdapter((SpinnerAdapter) arrayAdapter2);
        this.spinEmailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        if (this.title.equals("Update contact")) {
            ContactModel contactModel = (ContactModel) new Gson().fromJson(getIntent().getStringExtra("contactModel"), new TypeToken<ContactModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.ActivityAddNewContact.7
            }.getType());
            this.contactModel = contactModel;
            getEmailLists(contactModel.getId());
            this.edtName.setText(this.contactModel.getName());
            this.edtNumber.setText(this.contactModel.getDisplayNumber());
            char c = 65535;
            if (this.contactModel.getNumbers().size() > 0) {
                String callNumber = this.contactModel.getNumbers().get(0).getCallNumber();
                this.contact1 = callNumber;
                this.edtNumber.setText(callNumber);
                String callType = this.contactModel.getNumbers().get(0).getCallType();
                callType.hashCode();
                switch (callType.hashCode()) {
                    case 49:
                        if (callType.equals("1")) {
                            z = false;
                            break;
                        }
                        z = true;
                        break;
                    case 50:
                        if (callType.equals("2")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    case 51:
                        if (callType.equals("3")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    default:
                        z = true;
                        break;
                }
                if (!(z)) {
                    this.spinNumType.setSelection(0);
                } else if (z) {
                    this.spinNumType.setSelection(1);
                } else if (z) {
                    this.spinNumType.setSelection(2);
                }
                if (this.contactModel.getNumbers().size() > 1) {
                    String callNumber2 = this.contactModel.getNumbers().get(1).getCallNumber();
                    this.contact2 = callNumber2;
                    this.edtSecPhoneNo.setText(callNumber2);
                    String callType2 = this.contactModel.getNumbers().get(1).getCallType();
                    callType2.hashCode();
                    switch (callType2.hashCode()) {
                        case 49:
                            if (callType2.equals("1")) {
                                z2 = false;
                                break;
                            }
                            z2 = true;
                            break;
                        case 50:
                            if (callType2.equals("2")) {
                                z2 = true;
                                break;
                            }
                            z2 = true;
                            break;
                        case 51:
                            if (callType2.equals("3")) {
                                z2 = true;
                                break;
                            }
                            z2 = true;
                            break;
                        default:
                            z2 = true;
                            break;
                    }
                    if (!(z2)) {
                        this.spinSecNumType.setSelection(0);
                    } else if (z2) {
                        this.spinSecNumType.setSelection(1);
                    } else if (z2) {
                        this.spinSecNumType.setSelection(2);
                    }
                }
            }
            if (this.emailList.size() > 0) {
                this.edtMail.setText(this.emailList.get(0).getCallNumber());
                String callType3 = this.emailList.get(0).getCallType();
                callType3.hashCode();
                switch (callType3.hashCode()) {
                    case 49:
                        if (callType3.equals("1")) {
                            c = 0;
                            break;
                        }
                        break;
                    case 50:
                        if (callType3.equals("2")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 51:
                        if (callType3.equals("3")) {
                            c = 2;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        this.spinEmailType.setSelection(0);
                        break;
                    case 1:
                        this.spinEmailType.setSelection(1);
                        break;
                    case 2:
                        this.spinEmailType.setSelection(2);
                        break;
                }
            }
            this.user_img.setImageBitmap(this.contactModel.getUserImage());
            this.txtSave.setText("Update");
        }
        this.txtSave.setOnClickListener(new View.OnClickListener() {
            @Override 
            public void onClick(View view) {
                if (ActivityAddNewContact.this.edtName.getText().toString().trim().length() > 0) {
                    if (ActivityAddNewContact.this.edtNumber.getText().toString().trim().length() > 0) {
                        ActivityAddNewContact.this.addContact();
                        return;
                    } else if (ActivityAddNewContact.this.edtSecPhoneNo.getText().toString().trim().length() > 0) {
                        ActivityAddNewContact.this.addContact();
                        return;
                    } else {
                        ActivityAddNewContact.this.edtNumber.setError("Enter number");
                        return;
                    }
                }
                ActivityAddNewContact.this.edtName.setError("Enter name");
            }
        });
    }

    private void getEmailLists(String str) {
        this.emailList = new ArrayList<>();
        Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, "contact_id = ?", new String[]{String.valueOf(str)}, null);
        while (query.moveToNext()) {
            String string = query.getString(query.getColumnIndex("data1"));
            String string2 = query.getString(query.getColumnIndex("data2"));
            PhoneModel phoneModel = new PhoneModel();
            phoneModel.setCallNumber(string);
            phoneModel.setCallType(string2);
            this.emailList.add(phoneModel);
        }
        query.close();
    }

    private void initUI() {
        this.txtTitle = (AppCompatTextView) findViewById(R.id.txtTitle);
        this.txtName = (AppCompatTextView) findViewById(R.id.txtName);
        this.user_img = (AppCompatImageView) findViewById(R.id.user_img);
        this.edtName = (AppCompatEditText) findViewById(R.id.edtName);
        AppCompatEditText appCompatEditText = (AppCompatEditText) findViewById(R.id.edtNumber);
        this.edtNumber = appCompatEditText;
        appCompatEditText.setText(this.contactNo);
        this.edtSecPhoneNo = (AppCompatEditText) findViewById(R.id.edtSecPhoneNo);
        this.edtMail = (AppCompatEditText) findViewById(R.id.edtMail);
        this.spinNumType = (Spinner) findViewById(R.id.spinNumType);
        this.spinSecNumType = (Spinner) findViewById(R.id.spinSecNumType);
        this.spinEmailType = (Spinner) findViewById(R.id.spinEmailType);
        this.txtSave = (AppCompatTextView) findViewById(R.id.txtSave);
    }

    
    public void addContact() {
        String trim = this.edtName.getText().toString().trim();
        String trim2 = this.edtNumber.getText().toString().trim();
        String str = this.typePhoneArr[this.spinNumType.getSelectedItemPosition()];
        String trim3 = this.edtSecPhoneNo.getText().toString().trim();
        String str2 = this.typePhoneArr[this.spinSecNumType.getSelectedItemPosition()];
        String trim4 = this.edtMail.getText().toString().trim();
        String str3 = this.typeEmailArr[this.spinEmailType.getSelectedItemPosition()];
        Uri uri = ContactsContract.Data.CONTENT_URI;
        long rawContactId = getRawContactId();
        if (this.title.equals("Update contact")) {
            rawContactId = Long.parseLong(getRawContactId(this.contactModel.getId()));
        }
        long j = rawContactId;
        insertContactDisplayName(uri, j, trim);
        if (!trim2.isEmpty()) {
            insertContactPhoneNumber(uri, j, trim2, str, this.contact1);
        }
        if (!trim3.isEmpty()) {
            insertContactPhoneNumber(uri, j, trim3, str2, this.contact2);
        }
        if (!trim4.isEmpty()) {
            insertContactEmail(uri, j, trim4, str3);
        }
        if (this.bitmap != null) {
            insertContactUrl(j);
        }
        Toast.makeText(this, "Contact added.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("dispName", trim);
        intent.putExtra("model", new Gson().toJson(this.contactModel));
        setResult(-1, intent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("isUpdated").putExtra("model", new Gson().toJson(this.contactModel)));
        finish();
    }

    private void insertContactUrl(long j) {
        if (this.title.equals("Update contact") && this.contactModel.getUserImage() != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String[] strArr = {String.valueOf(j), "vnd.android.cursor.item/photo"};
            ContentValues contentValues = new ContentValues();
            contentValues.put("raw_contact_id", Long.valueOf(j));
            contentValues.put("is_super_primary", (Integer) 1);
            contentValues.put("data15", byteArray);
            contentValues.put("mimetype", "vnd.android.cursor.item/photo");
            getContentResolver().update(ContactsContract.Data.CONTENT_URI, contentValues, "raw_contact_id = ? AND mimetype = ? ", strArr);
            try {
                byteArrayOutputStream.flush();
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        this.bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream2);
        byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("raw_contact_id", Long.valueOf(j));
        contentValues2.put("is_super_primary", (Integer) 1);
        contentValues2.put("data15", byteArray2);
        contentValues2.put("mimetype", "vnd.android.cursor.item/photo");
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues2);
        try {
            byteArrayOutputStream2.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private void insertContactDisplayName(Uri uri, long j, String str) {
        if (this.title.equals("Update contact")) {
            ContentResolver contentResolver = getContentResolver();
            String[] strArr = {this.contactModel.getId(), "vnd.android.cursor.item/name"};
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            arrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection("contact_id = ? AND mimetype = ?", strArr).withValue("data1", str).build());
            try {
                contentResolver.applyBatch("com.android.contacts", arrayList);
                return;
            } catch (OperationApplicationException e) {
                e.printStackTrace();
                return;
            } catch (RemoteException e2) {
                e2.printStackTrace();
                return;
            }
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("raw_contact_id", Long.valueOf(j));
        contentValues.put("mimetype", "vnd.android.cursor.item/name");
        contentValues.put("data2", str);
        getContentResolver().insert(uri, contentValues);
    }

    private void insertContactEmail(Uri uri, long j, String str, String str2) {
        if (this.title.equals("Update contact")) {
            String[] strArr = {String.valueOf(j), "vnd.android.cursor.item/email_v2"};
            if (this.emailList.size() > 0) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("raw_contact_id", Long.valueOf(j));
                contentValues.put("mimetype", "vnd.android.cursor.item/email_v2");
                contentValues.put("data1", str);
                contentValues.put("data2", str2);
                getContentResolver().update(uri, contentValues, "raw_contact_id = ? AND mimetype = ?", strArr);
                return;
            }
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("raw_contact_id", Long.valueOf(j));
            contentValues2.put("mimetype", "vnd.android.cursor.item/email_v2");
            contentValues2.put("data1", str);
            contentValues2.put("data2", str2);
            getContentResolver().insert(uri, contentValues2);
            return;
        }
        ContentValues contentValues3 = new ContentValues();
        contentValues3.put("raw_contact_id", Long.valueOf(j));
        contentValues3.put("mimetype", "vnd.android.cursor.item/email_v2");
        contentValues3.put("data1", str);
        contentValues3.put("data2", str2);
        getContentResolver().insert(uri, contentValues3);
    }

    private long getRawContactId() {
        return ContentUris.parseId(getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, new ContentValues()));
    }

    private void insertContactPhoneNumber(Uri uri, long j, String str, String str2, String str3) {
        int i = 0;
        if (!"Home".equalsIgnoreCase(str2)) {
            if ("Mobile".equalsIgnoreCase(str2)) {
                i = 2;
            } else if ("Work".equalsIgnoreCase(str2)) {
                i = 3;
            }
            if (!this.title.equals("Update contact") && !str3.isEmpty()) {
                ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
                arrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(String.format("%s = '%s' AND %s = ?", "mimetype", "vnd.android.cursor.item/phone_v2", "data1"), new String[]{str3}).withValue("data1", str).build());
                try {
                    for (ContentProviderResult contentProviderResult : getContentResolver().applyBatch("com.android.contacts", arrayList)) {
                        Log.d("Update Result", contentProviderResult.toString());
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("raw_contact_id", Long.valueOf(j));
            contentValues.put("mimetype", "vnd.android.cursor.item/phone_v2");
            contentValues.put("data1", str);
            contentValues.put("data2", Integer.valueOf(i));
            getContentResolver().insert(uri, contentValues);
        }
        i = 1;
        if (!this.title.equals("Update contact")) {
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("raw_contact_id", Long.valueOf(j));
        contentValues2.put("mimetype", "vnd.android.cursor.item/phone_v2");
        contentValues2.put("data1", str);
        contentValues2.put("data2", Integer.valueOf(i));
        getContentResolver().insert(uri, contentValues2);
    }

    public String getRawContactId(String str) {
        Cursor query = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, new String[]{FieldType.FOREIGN_ID_FIELD_SUFFIX}, "contact_id = ?", new String[]{str}, null);
        if (query == null || !query.moveToFirst()) {
            return "";
        }
        String string = query.getString(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
        query.close();
        return string;
    }


    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1) {
            if (i == 100) {
                if (intent != null) {
                    CropImage.activity(intent.getData()).setAspectRatio(1, 1).setGuidelines(CropImageView.Guidelines.ON).start(this);
                }
            } else if (i == 203) {
                CropImage.ActivityResult activityResult = CropImage.getActivityResult(intent);
                if (i2 != -1) {
                    if (i2 == 204) {
                        activityResult.getError();
                        return;
                    }
                    return;
                }
                this.uri = activityResult.getUri();
                File file = new File(this.uri.getPath());
                File externalFilesDir = BoloApplication.getApplication().getExternalFilesDir(".bolo/" + file.getName());
                if (externalFilesDir.exists()) {
                    externalFilesDir.delete();
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    try {
                        if (Files.copy(file.toPath(), externalFilesDir.toPath(), new CopyOption[0]) != null) {
                            if (!externalFilesDir.toString().startsWith("file://")) {
                                this.uri = Uri.parse("file://" + externalFilesDir.toString());
                            } else {
                                this.uri = Uri.parse(externalFilesDir.toString());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Log.println(Log.ASSERT, "uri==", this.uri.getPath());
                Bitmap decodeFile = BitmapFactory.decodeFile(this.uri.getPath());
                this.bitmap = decodeFile;
                this.user_img.setImageBitmap(decodeFile);
            }
        }
    }
}
