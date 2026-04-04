package com.colorcallscreen.colorphone.callscreen.calltheme.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.RoomDatabase;

import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogComponent;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.PhoneModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.Utility;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.contact_component.ContactModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.controller.VideoCallController;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.telephonic.ContactsHandler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.FieldType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CallLogDetailActivity extends AppCompatActivity {
    private static final String[] DATA_COLS = {"mimetype", "data1", "contact_id"};
    private static final int REQUEST_ADD_CONTACT = 197;
    AdLoad adLoad;
    VideoCallController.VideoCallApps app;
    LinearLayout bottom;
    private CallLogAction callLogAction;
    private CallLogModel callLogModel;
    private BroadcastReceiver callReceiver;
    ImageView call_btn;
    private ContactModel contactModel;
    ImageView email_btn;
    AppCompatImageView ivBack;
    AppCompatImageView ivDelete;
    AppCompatImageView ivEdit;
    AppCompatImageView ivFavorite;
    AppCompatImageView ivMore;
    AppCompatImageView ivRingtones;
    AppCompatImageView ivShareContact;
    ImageView ivVideoCall;
    CallLogComponent logComponent;
    ImageView message_btn;
    PopupWindow mypopupWindow;
    AppCompatTextView noHistory;
    Uri photoUri;
    private RecyclerView recyclerView;
    private RecyclerView rvHistory;
    Space spacer;
    private TextView txtLetter;
    private TextView txtName;
    AppCompatTextView txtSeeAll;
    AppCompatTextView txtTitle;
    private AppCompatImageView user_img;
    VideoCallController videoCallController;
    boolean isEdited = false;
    boolean isContact = true;
    boolean isFavorite = false;
    String phoneNumber = "";
    String callerName = "";
    long contactId = 0;
    int colorCode = 0;
    long videoCallId = 0;
    List<PhoneModel> contactNumbers = new ArrayList();
    List<String> emailList = new ArrayList();
    List<CallLogModel> arrayList = new ArrayList();
    Uri uri1 = null;

    
    public interface FavoriteUpdateListener {
        void favoriteSetChange();
    }

    @Override
    // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        try {
            if (this.callReceiver != null) {
                LocalBroadcastManager.getInstance(BoloApplication.getApplication()).unregisterReceiver(this.callReceiver);
            }
        } catch (Exception unused) {
        }
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(R.layout.activity_call_log_detail);
        getWindow().setFlags(1024, 1024);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
        if (ContextCompat.checkSelfPermission(this, BoloPermission.PHONE_CALLS) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{BoloPermission.PHONE_CALLS}, 102);
        }
        this.logComponent = new CallLogComponent(BoloApplication.getApplication(), null);
        this.callLogAction = new CallLogActionBuilder(this);
        this.isContact = getIntent().getBooleanExtra("fromContact", false);
        this.contactNumbers = new ArrayList();
        if (this.callReceiver == null) {
            this.callReceiver = new BroadcastReceiver() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equalsIgnoreCase("isUpdated")) {
                        CallLogDetailActivity.this.contactModel = (ContactModel) new Gson().fromJson(CallLogDetailActivity.this.getIntent().getStringExtra("model"), new TypeToken<ContactModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.1.1
                        }.getType());
                        CallLogDetailActivity callLogDetailActivity = CallLogDetailActivity.this;
                        callLogDetailActivity.callerName = callLogDetailActivity.contactModel.getName();
                        CallLogDetailActivity.this.txtName.setText(CallLogDetailActivity.this.callerName);
                    }
                }
            };
        }
        LocalBroadcastManager.getInstance(BoloApplication.getApplication()).registerReceiver(this.callReceiver, new IntentFilter("isUpdated"));
        initViews();
        if (this.isContact) {
            this.ivEdit.setVisibility(0);
            this.contactModel = (ContactModel) new Gson().fromJson(getIntent().getStringExtra("model"), new TypeToken<ContactModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.2
            }.getType());
            this.contactId = CallLogUtils.getContactID(getContentResolver(), this.contactModel.getDisplayNumber());
            this.callerName = this.contactModel.getName();
            this.phoneNumber = this.contactModel.getDisplayNumber();
            this.contactNumbers = this.contactModel.getNumbers();
            this.photoUri = this.logComponent.getUri(this.contactModel.getId());
            this.colorCode = this.contactModel.getColorCode();
        } else {
            this.ivEdit.setVisibility(8);
            CallLogModel callLogModel = (CallLogModel) new Gson().fromJson(getIntent().getStringExtra("model"), new TypeToken<CallLogModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.3
            }.getType());
            this.callLogModel = callLogModel;
            if (callLogModel == null) {
                finish();
                return;
            }
            this.contactId = CallLogUtils.getContactID(getContentResolver(), this.callLogModel.getNumber());
            if (this.callLogModel.getName() != null && this.callLogModel.getName().length() > 0) {
                this.callerName = this.callLogModel.getName();
                this.ivFavorite.setVisibility(0);
                this.bottom.setVisibility(0);
                this.ivVideoCall.setVisibility(8);
                this.spacer.setVisibility(8);
            } else {
                this.ivVideoCall.setVisibility(8);
                this.spacer.setVisibility(8);
                this.ivFavorite.setVisibility(8);
                this.bottom.setVisibility(8);
                this.callerName = "";
            }
            this.phoneNumber = this.callLogModel.getNumber();
            PhoneModel phoneModel = new PhoneModel();
            phoneModel.setCallNumber(this.callLogModel.getNumber());
            phoneModel.setCallType("1");
            this.contactNumbers.add(phoneModel);
            if (this.callLogModel.getImgUri() != null && !this.callLogModel.getImgUri().equals("")) {
                this.photoUri = Uri.parse(this.callLogModel.getImgUri());
            } else {
                this.photoUri = null;
            }
            this.colorCode = this.callLogModel.getColorCode();
        }
        if (this.ivVideoCall.getVisibility() == 0) {
            VideoCallController videoCallController = new VideoCallController(BoloApplication.getApplication());
            this.videoCallController = videoCallController;
            videoCallController.setNumber(this.phoneNumber);
            this.app = VideoCallController.Settings.getApp();
            String contactNameFromNumber = ContactsHandler.contactNameFromNumber(this.phoneNumber, this);
            if (contactNameFromNumber == null || contactNameFromNumber.isEmpty()) {
                this.videoCallId = this.videoCallController.getVideoCallID1(getString(R.string.unknown), this.app.getName()).longValue();
            } else {
                this.videoCallId = this.videoCallController.getVideoCallID1(contactNameFromNumber, VideoCallController.WA_VIDEO_CALL).longValue();
            }
        }
        this.ivBack.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.4
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity.this.onBackPressed();
            }
        });
        this.ivMore.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.5
            @Override 
            public void onClick(View view) {
                View inflate = ((LayoutInflater) CallLogDetailActivity.this.getSystemService("layout_inflater")).inflate(R.layout.menu_clear, (ViewGroup) null);
                AppCompatTextView appCompatTextView = (AppCompatTextView) inflate.findViewById(R.id.txtClearHistory);
                if (BlockHelper.isPhoneNumberInBlockedList(CallLogDetailActivity.this.phoneNumber, CallLogDetailActivity.this)) {
                    appCompatTextView.setText("Unblock contact");
                } else {
                    appCompatTextView.setText("Block contact");
                }
                appCompatTextView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.5.1
                    @Override 
                    public void onClick(View view2) {
                        if (BlockHelper.isPhoneNumberInBlockedList(CallLogDetailActivity.this.phoneNumber, CallLogDetailActivity.this)) {
                            CallLogDetailActivity.this.callLogAction.onUnblock(CallLogDetailActivity.this.phoneNumber);
                        } else {
                            CallLogDetailActivity.this.callLogAction.onBlock(CallLogDetailActivity.this.phoneNumber);
                        }
                        CallLogDetailActivity.this.mypopupWindow.dismiss();
                    }
                });
                CallLogDetailActivity.this.mypopupWindow = new PopupWindow(inflate, -2, -2, true);
                CallLogDetailActivity.this.mypopupWindow.showAsDropDown(view, -20, -15);
            }
        });
        this.ivFavorite.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.6
            @Override 
            public void onClick(View view) {
                if (CallLogDetailActivity.this.isFavorite) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallLogDetailActivity.this);
                    builder.setMessage(R.string.remove_favorite);
                    builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.6.1
                        @Override 
                        public void onClick(DialogInterface dialogInterface, int i) {
                            CallLogDetailActivity.this.isFavorite = false;
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("starred", (Integer) 0);
                            CallLogDetailActivity.this.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, "_id=" + CallLogDetailActivity.this.contactId, null);
                            CallLogDetailActivity.this.ivFavorite.setImageResource(R.drawable.ic_star);
                            if (MainActivity.favoriteFragment != null) {
                                MainActivity.favoriteFragment.favoriteSetChange();
                            }
                        }
                    });
                    builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                    builder.show();
                    return;
                }
                CallLogDetailActivity.this.isFavorite = true;
                ContentValues contentValues = new ContentValues();
                contentValues.put("starred", (Integer) 1);
                CallLogDetailActivity.this.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, contentValues, "_id=" + CallLogDetailActivity.this.contactId, null);
                CallLogDetailActivity.this.ivFavorite.setImageResource(R.drawable.ic_starfill);
                if (MainActivity.favoriteFragment != null) {
                    MainActivity.favoriteFragment.favoriteSetChange();
                }
            }
        });
        this.txtSeeAll.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.7
            @Override 
            public void onClick(View view) {
                if (CallLogDetailActivity.this.arrayList.size() > 0) {
                    CallLogDetailActivity.this.showBottomSheetDialogHistory();
                }
            }
        });
        this.isEdited = false;
        this.ivEdit.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.8
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity callLogDetailActivity = CallLogDetailActivity.this;
                callLogDetailActivity.editContact(callLogDetailActivity, String.valueOf(callLogDetailActivity.contactId));
            }
        });
        this.ivDelete.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.9
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity.this.showBottomSheetDialogDelete();
            }
        });
        this.ivShareContact.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.10
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity.this.shareContact();
            }
        });
        this.ivVideoCall.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.11
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity.this.videoCallController.makeVideoCall(Long.valueOf(CallLogDetailActivity.this.videoCallId), CallLogDetailActivity.this.app);
            }
        });
        this.ivRingtones.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.12
            @Override 
            public void onClick(View view) {
                Uri actualDefaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(CallLogDetailActivity.this, 4);
                Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                intent.putExtra("android.intent.extra.ringtone.TYPE", 1);
                intent.putExtra("android.intent.extra.ringtone.TITLE", "Select Tone");
                intent.putExtra("android.intent.extra.ringtone.EXISTING_URI", actualDefaultRingtoneUri);
                intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", false);
                intent.putExtra("android.intent.extra.ringtone.SHOW_DEFAULT", true);
                CallLogDetailActivity.this.startActivityForResult(intent, RoomDatabase.MAX_BIND_PARAMETER_CNT);
            }
        });
        checkStarredContact();
        this.recyclerView.setAdapter(new MoreNumberAdapter(this.contactNumbers));
        if (this.isContact) {
            loadCallLogsForContact(this.contactModel);
        } else {
            loadCallLogHistory();
        }
        Uri uri = this.photoUri;
        if (uri != null) {
            this.user_img.setImageURI(uri);
            this.txtLetter.setVisibility(8);
        } else {
            this.user_img.setImageResource(this.colorCode);
            this.txtLetter.setVisibility(0);
        }
        loadUserImage();
        if (this.callerName.equals("")) {
            this.txtLetter.setText("#");
            this.txtName.setText(this.phoneNumber);
        } else {
            this.txtName.setText(this.callerName);
            String[] split = this.callerName.split(" ");
            if (split.length > 1) {
                try {
                    str = split[0].substring(0, 1) + split[1].substring(0, 1) + "";
                } catch (Exception unused) {
                    str = split[0].substring(0, 1) + "";
                }
            } else {
                str = split[0].substring(0, 1) + "";
            }
            this.txtLetter.setText(str);
        }
        this.txtTitle.setText(this.txtName.getText().toString());
        this.call_btn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.13
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogDetailActivity.this, BoloPermission.PHONE_CALLS) != 0) {
                    Toast.makeText(CallLogDetailActivity.this, "Call Phone Permision Needed", 0).show();
                }
                try {
                    CallLogDetailActivity.this.callLogAction.onCall(CallLogDetailActivity.this.phoneNumber);
                } catch (Exception unused2) {
                }
            }
        });
        getEmailLists();
        this.email_btn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.14
            @Override 
            public void onClick(View view) {
                if (CallLogDetailActivity.this.emailList.size() > 0) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.EMAIL", new String[]{CallLogDetailActivity.this.emailList.get(0)});
                    intent.putExtra("android.intent.extra.SUBJECT", "Hello " + CallLogDetailActivity.this.callerName);
                    intent.putExtra("android.intent.extra.TEXT", "Hello");
                    intent.setType("message/rfc822");
                    try {
                        CallLogDetailActivity.this.startActivity(Intent.createChooser(intent, "Send email using..."));
                        return;
                    } catch (ActivityNotFoundException unused2) {
                        Toast.makeText(CallLogDetailActivity.this, "No email clients installed.", 0).show();
                        return;
                    }
                }
                Toast.makeText(CallLogDetailActivity.this, "This contact not contains email id.", 0).show();
            }
        });
        this.message_btn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.15
            @Override 
            public void onClick(View view) {
                CallLogDetailActivity.this.callLogAction.onMessage(CallLogDetailActivity.this.phoneNumber);
            }
        });
    }

    private void getEmailLists() {
        this.emailList = new ArrayList();
        Cursor query = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, "contact_id = ?", new String[]{String.valueOf(this.contactId)}, null);
        while (query.moveToNext()) {
            this.emailList.add(query.getString(query.getColumnIndex("data1")));
        }
        query.close();
    }

    private void initViews() {
        this.txtTitle = (AppCompatTextView) findViewById(R.id.txtTitle);
        this.txtSeeAll = (AppCompatTextView) findViewById(R.id.txtSeeAll);
        this.user_img = (AppCompatImageView) findViewById(R.id.user_img);
        this.txtLetter = (TextView) findViewById(R.id.txtLetter);
        this.txtName = (TextView) findViewById(R.id.txtName);
        this.ivBack = (AppCompatImageView) findViewById(R.id.ivBack);
        this.bottom = (LinearLayout) findViewById(R.id.bottom);
        this.noHistory = (AppCompatTextView) findViewById(R.id.noHistory);
        this.ivMore = (AppCompatImageView) findViewById(R.id.ivMore);
        this.email_btn = (ImageView) findViewById(R.id.email_btn);
        this.call_btn = (ImageView) findViewById(R.id.call_btn);
        this.ivVideoCall = (ImageView) findViewById(R.id.ivVideoCall);
        this.spacer = (Space) findViewById(R.id.spacer);
        this.message_btn = (ImageView) findViewById(R.id.message_btn);
        this.ivFavorite = (AppCompatImageView) findViewById(R.id.ivFavorite);
        this.ivEdit = (AppCompatImageView) findViewById(R.id.ivEdit);
        this.ivDelete = (AppCompatImageView) findViewById(R.id.ivDelete);
        this.ivShareContact = (AppCompatImageView) findViewById(R.id.ivShareContact);
        this.ivRingtones = (AppCompatImageView) findViewById(R.id.ivRingtones);
        this.recyclerView = (RecyclerView) findViewById(R.id.numberRcv);
        this.rvHistory = (RecyclerView) findViewById(R.id.rvHistory);
    }

    private void loadUserImage() {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.16
            @Override 
            public void run() {
                Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(CallLogDetailActivity.this.phoneNumber, BoloApplication.getApplication());
                if (userImageForPhoneNumber != null) {
                    if (CallLogDetailActivity.this.isContact) {
                        CallLogDetailActivity.this.contactModel.setUserImagePresentChecked(true);
                        CallLogDetailActivity.this.contactModel.setUserImage(userImageForPhoneNumber);
                    } else {
                        CallLogDetailActivity.this.callLogModel.setUserImagePresentChecked(true);
                        CallLogDetailActivity.this.callLogModel.setUserImage(userImageForPhoneNumber);
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.16.1
                        @Override 
                        public void run() {
                            CallLogDetailActivity.this.txtLetter.setVisibility(8);
                            if (CallLogDetailActivity.this.isContact) {
                                CallLogDetailActivity.this.user_img.setImageBitmap(CallLogDetailActivity.this.contactModel.getUserImage());
                            } else {
                                CallLogDetailActivity.this.user_img.setImageBitmap(CallLogDetailActivity.this.callLogModel.getUserImage());
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void checkStarredContact() {
        Cursor query = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, "starred=?", new String[]{"1"}, "display_name ASC");
        if (query == null) {
            Toast.makeText(this, "Something went wrong.Please try again", 0).show();
        } else if (query.getCount() > 0) {
            while (true) {
                if (!query.moveToNext()) {
                    break;
                }
                String string = query.getString(query.getColumnIndex(FieldType.FOREIGN_ID_FIELD_SUFFIX));
                query.getString(query.getColumnIndex("display_name"));
                query.getString(query.getColumnIndex("photo_uri"));
                Cursor query2 = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, "contact_id =?", new String[]{string}, null);
                if (query2.moveToNext()) {
                    if (query2.getString(query2.getColumnIndex("data1")).equals(this.phoneNumber)) {
                        this.isFavorite = true;
                        this.ivFavorite.setImageResource(R.drawable.ic_starfill);
                        break;
                    }
                    query2.close();
                }
            }
            query.close();
        }
    }

    
    public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHodler> {
        private List<CallLogModel> list;
        int size;

        
        public class HistoryViewHodler extends RecyclerView.ViewHolder {
            private TextView duration;
            private TextView time;
            private ImageView typeIcon;
            private TextView typeText;

            public HistoryViewHodler(View view) {
                super(view);
                this.typeIcon = (ImageView) view.findViewById(R.id.type);
                this.typeText = (TextView) view.findViewById(R.id.type_name);
                this.time = (TextView) view.findViewById(R.id.time);
                this.duration = (TextView) view.findViewById(R.id.duration);
            }
        }

        public HistoryAdapter(List<CallLogModel> list, int i) {
            this.list = list;
            this.size = i;
        }

        @Override 
        public int getItemCount() {
            int size = this.list.size();
            int i = this.size;
            return size > i ? i : this.list.size();
        }

        @Override 
        public void onBindViewHolder(HistoryViewHodler historyViewHodler, int i) {
            CallLogModel callLogModel = this.list.get(i);
            historyViewHodler.typeIcon.setImageResource(CallLogUtils.getCallTypeIcon(callLogModel.getCallType()));
            historyViewHodler.typeText.setText(CallLogUtils.getCallTypeName(callLogModel.getCallType()));
            historyViewHodler.time.setText(CallLogUtils.timeForCalculateForCallDetails(new Date(Long.valueOf(callLogModel.getDate()).longValue())));
            historyViewHodler.duration.setText(CallLogUtils.formatDuration(callLogModel.getDuration()));
        }

        @Override 
        public HistoryViewHodler onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new HistoryViewHodler(LayoutInflater.from(CallLogDetailActivity.this).inflate(R.layout.call_log_details_row, viewGroup, false));
        }
    }

    
    class MoreNumberAdapter extends RecyclerView.Adapter<MoreNumberAdapter.MoreNumberViewHolder> {
        List<PhoneModel> numbers;

        
        public class MoreNumberViewHolder extends RecyclerView.ViewHolder {
            ImageView callBtn;
            ImageView msgBtn;
            TextView number;
            ImageView videCallBtn;
            ImageView whatsAppBtn;

            public MoreNumberViewHolder(View view) {
                super(view);
                this.number = (TextView) view.findViewById(R.id.number);
                this.callBtn = (ImageView) view.findViewById(R.id.call);
                this.msgBtn = (ImageView) view.findViewById(R.id.msg);
                this.whatsAppBtn = (ImageView) view.findViewById(R.id.whatsapp);
                this.videCallBtn = (ImageView) view.findViewById(R.id.video_call);
                if (Utility.hasWhatsappInstalled(CallLogDetailActivity.this)) {
                    this.whatsAppBtn.setVisibility(0);
                } else {
                    this.whatsAppBtn.setVisibility(8);
                }
            }
        }

        public MoreNumberAdapter(List<PhoneModel> list) {
            this.numbers = list;
        }

        @Override 
        public int getItemCount() {
            return this.numbers.size();
        }

        @Override 
        public void onBindViewHolder(MoreNumberViewHolder moreNumberViewHolder, int i) {
            final String callNumber = this.numbers.get(i).getCallNumber();
            moreNumberViewHolder.number.setText(callNumber);
            moreNumberViewHolder.callBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.MoreNumberAdapter.1
                @Override 
                public void onClick(View view) {
                    ContextCompat.checkSelfPermission(CallLogDetailActivity.this, BoloPermission.PHONE_CALLS);
                    try {
                        CallLogDetailActivity.this.callLogAction.onCall(callNumber);
                    } catch (Exception unused) {
                    }
                }
            });
            moreNumberViewHolder.msgBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.MoreNumberAdapter.2
                @Override 
                public void onClick(View view) {
                    CallLogDetailActivity.this.callLogAction.onMessage(callNumber);
                }
            });
            moreNumberViewHolder.whatsAppBtn.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.MoreNumberAdapter.3
                @Override 
                public void onClick(View view) {
                    CallLogDetailActivity.this.callLogAction.sendWhatsAppMsg("", callNumber);
                }
            });
        }

        @Override 
        public MoreNumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MoreNumberViewHolder(LayoutInflater.from(CallLogDetailActivity.this).inflate(R.layout.more_number_row, viewGroup, false));
        }
    }

    public void editContact(Context context, String str) {
        this.isEdited = true;
        Intent intent = new Intent("android.intent.action.EDIT");
        intent.setDataAndType(Uri.parse(ContactsContract.Contacts.CONTENT_URI + "/" + str), "vnd.android.cursor.item/contact");
        context.startActivity(intent);
    }

    public boolean updateNameAndNumber(Context context, String str, String str2, String str3) {
        if (context != null && str != null && !str.trim().isEmpty()) {
            if (str3 != null && str3.trim().isEmpty()) {
                str3 = null;
            }
            if (str3 == null || this.contactId == 0) {
                return false;
            }
            String[] strArr = DATA_COLS;
            String format = String.format("%s = '%s' AND %s = ?", strArr[0], "vnd.android.cursor.item/name", strArr[2]);
            String[] strArr2 = {String.valueOf(this.contactId)};
            ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
            arrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(format, strArr2).withValue("data2", str2).build());
            String format2 = String.format("%s = '%s' AND %s = ?", strArr[0], "vnd.android.cursor.item/phone_v2", strArr[1]);
            strArr2[0] = str;
            arrayList.add(ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI).withSelection(format2, strArr2).withValue(strArr[1], str3).build());
            try {
                for (ContentProviderResult contentProviderResult : context.getContentResolver().applyBatch("com.android.contacts", arrayList)) {
                    Log.d("Update Result", contentProviderResult.toString());
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void loadCallLogHistory() {
        CallLogModel callLogModel = this.callLogModel;
        if (callLogModel == null || callLogModel.getNumber() == null) {
            return;
        }
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.17
            @Override 
            public void run() {
                CallLogDetailActivity callLogDetailActivity = CallLogDetailActivity.this;
                callLogDetailActivity.arrayList = callLogDetailActivity.logComponent.queryForCallLogs(CallLogDetailActivity.this.callLogModel.getNumber(), CallLogUtils.getNumberOfMonthOlderFromToday(3), CallLogUtils.getCurrentDate(), false, -1, 0);
                if (CallLogDetailActivity.this.arrayList == null) {
                    CallLogDetailActivity.this.recyclerView.setVisibility(8);
                } else {
                    new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.17.1
                        @Override 
                        public void run() {
                            if (CallLogDetailActivity.this.arrayList.size() > 0) {
                                CallLogDetailActivity.this.noHistory.setVisibility(8);
                                CallLogDetailActivity.this.rvHistory.setAdapter(new HistoryAdapter(CallLogDetailActivity.this.arrayList, 5));
                                return;
                            }
                            CallLogDetailActivity.this.noHistory.setVisibility(0);
                        }
                    });
                }
            }
        }).start();
    }

    private void loadCallLogsForContact(final ContactModel contactModel) {
        new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.18
            @Override 
            public void run() {
                CallLogDetailActivity.this.arrayList = new ArrayList();
                for (PhoneModel phoneModel : contactModel.getNumbers()) {
                    List<CallLogModel> queryForCallLogs = CallLogDetailActivity.this.logComponent.queryForCallLogs(phoneModel.getCallNumber(), CallLogUtils.getNumberOfMonthOlderFromToday(3), CallLogUtils.getCurrentDate(), false, -1, 0);
                    if (queryForCallLogs != null) {
                        CallLogDetailActivity.this.arrayList.addAll(queryForCallLogs);
                    }
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.18.1
                    @Override 
                    public void run() {
                        if (CallLogDetailActivity.this.arrayList.size() > 0) {
                            CallLogDetailActivity.this.noHistory.setVisibility(8);
                            CallLogDetailActivity.this.rvHistory.setAdapter(new HistoryAdapter(CallLogDetailActivity.this.arrayList, 5));
                            return;
                        }
                        CallLogDetailActivity.this.noHistory.setVisibility(0);
                    }
                });
            }
        }).start();
    }

    
    public void showBottomSheetDialogHistory() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_dialog_callhistory);
        bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        List<CallLogModel> list = this.arrayList;
        ((RecyclerView) bottomSheetDialog.findViewById(R.id.rvHistoryBottom)).setAdapter(new HistoryAdapter(list, list.size()));
        bottomSheetDialog.show();
    }

    
    public void showBottomSheetDialogDelete() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.confirm_block_view);
        ((TextView) bottomSheetDialog.findViewById(R.id.title)).setText("Delete Contact");
        ((TextView) bottomSheetDialog.findViewById(R.id.message)).setText("Are you sure you want to delete contact?");
        ((TextView) bottomSheetDialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.19
            @Override 
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        TextView textView = (TextView) bottomSheetDialog.findViewById(R.id.block);
        textView.setText("Delete");
        textView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.20
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogDetailActivity.this, BoloPermission.READ_CONTACTS) != 0 || ContextCompat.checkSelfPermission(CallLogDetailActivity.this, BoloPermission.WRITE_CONTACTS) != 0) {
                    ActivityCompat.requestPermissions(CallLogDetailActivity.this, new String[]{BoloPermission.READ_CONTACTS, BoloPermission.WRITE_CONTACTS}, 102);
                    return;
                }
                CallLogDetailActivity callLogDetailActivity = CallLogDetailActivity.this;
                callLogDetailActivity.deleteContact(callLogDetailActivity.getContentResolver(), CallLogDetailActivity.this.phoneNumber);
                bottomSheetDialog.dismiss();
                Toast.makeText(CallLogDetailActivity.this, "Contact deleted.", 0).show();
                LocalBroadcastManager.getInstance(CallLogDetailActivity.this).sendBroadcast(new Intent("isDeleted").putExtra("phoneNumber", CallLogDetailActivity.this.phoneNumber));
                CallLogDetailActivity.this.finish();
            }
        });
        bottomSheetDialog.show();
    }

    
    public void shareContact() {
        Cursor query = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, new String[]{"lookup"}, "_id = " + CallLogUtils.getContactID(getContentResolver(), this.phoneNumber), null, null);
        Uri withAppendedPath = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, query.moveToFirst() ? query.getString(0) : "");
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/x-vcard");
        intent.putExtra("android.intent.extra.STREAM", withAppendedPath);
        intent.putExtra("android.intent.extra.SUBJECT", this.callerName);
        startActivity(intent);
    }

    public void deleteContact(ContentResolver contentResolver, String str) {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        arrayList.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI).withSelection("contact_id=?", new String[]{String.valueOf(CallLogUtils.getContactID(contentResolver, str))}).build());
        try {
            contentResolver.applyBatch("com.android.contacts", arrayList);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e2) {
            e2.printStackTrace();
        }
    }

    private boolean checkSystemWritePermission() {
        boolean z;
        if (Build.VERSION.SDK_INT >= 23) {
            z = Settings.System.canWrite(this);
        } else {
            z = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_SETTINGS") == 0;
        }
        if (!z) {
            if (Build.VERSION.SDK_INT >= 23) {
                Intent intent = new Intent("android.settings.action.MANAGE_WRITE_SETTINGS");
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10022);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_SETTINGS"}, 10022);
            }
        }
        return z;
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 10022 && iArr[0] == 0) {
            setTones();
        }
    }


    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 999 && i2 == -1) {
            this.uri1 = (Uri) intent.getParcelableExtra("android.intent.extra.ringtone.PICKED_URI");
            if (checkSystemWritePermission()) {
                setTones();
            }
        }
        if (i == REQUEST_ADD_CONTACT && i2 == -1) {
            this.contactModel = (ContactModel) new Gson().fromJson(getIntent().getStringExtra("model"), new TypeToken<ContactModel>() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity.21
            }.getType());
        }
    }

    @Override 
    public void onBackPressed() {
        if (this.isEdited) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent("isEdited"));
            finish();
            return;
        }
        finish();
    }

    private void setTones() {
        String lastPathSegment = ContactsContract.Contacts.CONTENT_URI.getLastPathSegment();
        Uri withAppendedPath = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(this.contactId));
        ContentValues contentValues = new ContentValues();
        contentValues.put("raw_contact_id", lastPathSegment);
        contentValues.put("custom_ringtone", this.uri1.toString());
        getContentResolver().update(withAppendedPath, contentValues, null, null);
        Toast.makeText(this, "Ringtone assigned to: " + (this.callerName.equals("") ? this.phoneNumber : this.callerName), 0).show();
    }
}
