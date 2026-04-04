package com.colorcallscreen.colorphone.callscreen.calltheme.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.block.BlockHelper;
import com.colorcallscreen.colorphone.callscreen.calltheme.BoloApplication;
import com.colorcallscreen.colorphone.callscreen.calltheme.R;
import com.colorcallscreen.colorphone.callscreen.calltheme.activity.CallLogDetailActivity;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogAction;
import com.colorcallscreen.colorphone.callscreen.calltheme.component.calllog_component.CallLogActionBuilder;
import com.colorcallscreen.colorphone.callscreen.calltheme.models.CallLogModel;
import com.colorcallscreen.colorphone.callscreen.calltheme.module.AdLoad;
import com.colorcallscreen.colorphone.callscreen.calltheme.service.images.UsersImageHandler;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.BoloPermission;
import com.colorcallscreen.colorphone.callscreen.calltheme.utils.CallLogUtils;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;
import java.util.Random;


public class CallLogsAdapter extends RecyclerView.Adapter<CallLogsAdapter.AdapterViewHolder> {
    AdLoad adLoad;
    public CallLogAction callLogAction;
    public List<CallLogModel> callLogModels;
    public Activity context;
    private int itemViewLayout;
    PopupWindow mypopupWindow;
    OnRefreshListener onRefreshListener;
    public String query = "";
    public boolean hasSearch = false;
    private int lastExpandedPos = -2;

    
    public interface OnRefreshListener {
        void onRefresh();
    }

    public CallLogsAdapter(Activity activity, List<CallLogModel> list, int i, OnRefreshListener onRefreshListener) {
        this.context = activity;
        this.callLogModels = list;
        this.itemViewLayout = i;
        this.onRefreshListener = onRefreshListener;
        this.callLogAction = new CallLogActionBuilder(activity);
    }

    @Override 
    public int getItemCount() {
        return this.callLogModels.size();
    }

    @Override 
    public AdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AdapterViewHolder(LayoutInflater.from(this.context).inflate(this.itemViewLayout, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(final AdapterViewHolder adapterViewHolder, final int i) {
        String str;
        String lowerCase;
        CharSequence text;
        final CallLogModel callLogModel = this.callLogModels.get(i);
        if (callLogModel.getColorCode() == -1) {
            callLogModel.setColorCode(CallLogUtils.colors[new Random().nextInt(CallLogUtils.colors.length)]);
        }
        boolean z = true;
        if (callLogModel.getName() != null && callLogModel.getName().length() > 0) {
            adapterViewHolder.name.setText(callLogModel.getDisplayName());
            String[] split = callLogModel.getName().trim().split(" ");
            if (split.length > 1) {
                try {
                    str = split[0].substring(0, 1) + split[1].substring(0, 1) + "";
                } catch (Exception unused) {
                    str = split[0].substring(0, 1) + "";
                }
            } else {
                str = split[0].substring(0, 1) + "";
            }
        } else {
            String number = callLogModel.getNumber();
            if (callLogModel.getSameNumberCount() > 1) {
                number = number + " (" + callLogModel.getSameNumberCount() + ")";
            }
            adapterViewHolder.name.setText(number);
            str = "#";
        }
        if (callLogModel.getUserImage() != null) {
            Glide.with(BoloApplication.getApplication()).load(callLogModel.getUserImage()).thumbnail(0.1f).into(adapterViewHolder.userImg);
            adapterViewHolder.letter.setVisibility(8);
        } else {
            adapterViewHolder.userImg.setImageBitmap(null);
            adapterViewHolder.letter.setVisibility(0);
            adapterViewHolder.userImg.setImageResource(callLogModel.getColorCode());
            adapterViewHolder.letter.setText(str);
            if (!callLogModel.isUserImagePresentChecked()) {
                new Thread(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.1
                    @Override 
                    public void run() {
                        Bitmap userImageForPhoneNumber = UsersImageHandler.userImageForPhoneNumber(callLogModel.getNumber(), CallLogsAdapter.this.context);
                        callLogModel.setUserImagePresentChecked(true);
                        if (userImageForPhoneNumber != null) {
                            callLogModel.setUserImage(userImageForPhoneNumber);
                            new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.1.1
                                @Override 
                                public void run() {
                                    Glide.with(BoloApplication.getApplication()).load(callLogModel.getUserImage()).thumbnail(0.1f).into(adapterViewHolder.userImg);
                                    adapterViewHolder.letter.setVisibility(8);
                                }
                            });
                        }
                    }
                }).start();
            }
        }
        adapterViewHolder.number.setText(callLogModel.getNumber());
        adapterViewHolder.time.setText(CallLogUtils.calculateTiming(new Date(Long.valueOf(callLogModel.getDate()).longValue())));
        adapterViewHolder.type.setImageResource(CallLogUtils.getCallTypeIcon(callLogModel.getCallType()));
        int i2 = this.lastExpandedPos;
        if (i2 != -1 && i2 == i) {
            adapterViewHolder.number.setVisibility(8);
        }
        try {
            if (this.query != null) {
                if (this.hasSearch && !callLogModel.getNumber().isEmpty() && callLogModel.getNumber().contains(this.query)) {
                    lowerCase = callLogModel.getNumber().toLowerCase();
                } else {
                    lowerCase = callLogModel.getName().toLowerCase();
                    z = false;
                }
                if (lowerCase.contains(this.query)) {
                    int indexOf = lowerCase.indexOf(this.query);
                    int length = this.query.length() + indexOf;
                    if (z) {
                        text = adapterViewHolder.number.getText();
                    } else {
                        text = adapterViewHolder.name.getText();
                    }
                    Spannable newSpannable = Spannable.Factory.getInstance().newSpannable(text);
                    newSpannable.setSpan(new ForegroundColorSpan((int) SupportMenu.CATEGORY_MASK), indexOf, length, 33);
                    if (z) {
                        adapterViewHolder.number.setText(newSpannable, TextView.BufferType.SPANNABLE);
                    } else {
                        adapterViewHolder.name.setText(newSpannable, TextView.BufferType.SPANNABLE);
                    }
                }
            }
        } catch (Exception unused2) {
        }
        adapterViewHolder.optionIcon.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.2
            @Override 
            public void onClick(View view) {
                CallLogsAdapter.this.showPopup(callLogModel);
                CallLogsAdapter.this.mypopupWindow.showAsDropDown(view, -20, -30);
            }
        });
        adapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.3
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogsAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(CallLogsAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    CallLogsAdapter.this.callLogAction.onCall(callLogModel.getNumber());
                } catch (Exception unused3) {
                }
            }
        });
        adapterViewHolder.userImg.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.4
            @Override 
            public void onClick(View view) {
                final Intent intent = new Intent(CallLogsAdapter.this.context, CallLogDetailActivity.class);
                CallLogsAdapter.this.callLogModels.get(i).setUserImage(null);
                CallLogsAdapter.this.callLogModels.get(i).setUserImagePresentChecked(false);
                intent.putExtra("model", new Gson().toJson(CallLogsAdapter.this.callLogModels.get(i)));
                intent.putExtra("fromContact", false);
                CallLogsAdapter.this.context.startActivity(intent);
            }
        });
    }

    public void setCallLogModel(List<CallLogModel> list) {
        this.callLogModels = list;
        notifyDataSetChanged();
    }

    
    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        public TextView duration;
        public View frame;
        public TextView letter;
        public TextView name;
        public TextView number;
        public ImageView optionIcon;
        public TextView time;
        public ImageView type;
        public AppCompatImageView userImg;

        public AdapterViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.name);
            this.number = (TextView) view.findViewById(R.id.number);
            this.time = (TextView) view.findViewById(R.id.time);
            this.duration = (TextView) view.findViewById(R.id.duration);
            this.type = (ImageView) view.findViewById(R.id.type);
            this.userImg = (AppCompatImageView) view.findViewById(R.id.user_img);
            this.letter = (TextView) view.findViewById(R.id.letter);
            this.optionIcon = (ImageView) view.findViewById(R.id.optionIcon);
            this.frame = view.findViewById(R.id.frame);
            this.number.setVisibility(8);
        }
    }

    public void showPopup(final CallLogModel callLogModel) {
        View inflate = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.menu_more, (ViewGroup) null);
        ((ImageView) inflate.findViewById(R.id.ivCall)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.5
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogsAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(CallLogsAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    CallLogsAdapter.this.callLogAction.onCall(callLogModel.getNumber());
                } catch (Exception unused) {
                }
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((ImageView) inflate.findViewById(R.id.ivMsg)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.6
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogsAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(CallLogsAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                try {
                    CallLogsAdapter.this.callLogAction.onMessage(callLogModel.getNumber());
                } catch (Exception unused) {
                }
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        AppCompatTextView appCompatTextView = (AppCompatTextView) inflate.findViewById(R.id.txtAddContact);
        if (callLogModel.getName() != null && callLogModel.getName().length() > 0) {
            appCompatTextView.setVisibility(8);
        } else {
            appCompatTextView.setVisibility(0);
        }
        appCompatTextView.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.7
            @Override 
            public void onClick(View view) {
                String number = callLogModel.getNumber();
                Intent intent = new Intent();
                intent.setAction("android.intent.action.INSERT");
                intent.setType("vnd.android.cursor.dir/raw_contact");
                intent.putExtra("phone", number);
                CallLogsAdapter.this.context.startActivity(intent);
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtCallDetails)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.8
            @Override 
            public void onClick(View view) {
                final Intent intent = new Intent(CallLogsAdapter.this.context, CallLogDetailActivity.class);
                callLogModel.setUserImage(null);
                callLogModel.setUserImagePresentChecked(false);
                intent.putExtra("model", new Gson().toJson(callLogModel));
                intent.putExtra("fromContact", false);
                CallLogsAdapter.this.context.startActivity(intent);
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtCopy)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.9
            @Override 
            public void onClick(View view) {
                ((ClipboardManager) CallLogsAdapter.this.context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("label", callLogModel.getNumber()));
                Toast.makeText(CallLogsAdapter.this.context, "Number copied.", 0).show();
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        ((AppCompatTextView) inflate.findViewById(R.id.txtDelete)).setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.10
            @Override 
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CallLogsAdapter.this.context, R.style.MyAlertDialogTheme2);
                builder.setMessage(R.string.history_removed);
                builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.10.1
                    @Override 
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CallLogsAdapter.this.context.getContentResolver().delete(CallLog.Calls.CONTENT_URI, "_id = ? ", new String[]{callLogModel.getCallId()});
                        CallLogsAdapter.this.callLogModels.remove(callLogModel);
                        CallLogsAdapter.this.onRefreshListener.onRefresh();
                        CallLogsAdapter.this.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
                builder.show();
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) inflate.findViewById(R.id.txtBlock);
        if (BlockHelper.isPhoneNumberInBlockedList(callLogModel.getNumber(), this.context)) {
            appCompatTextView2.setText(this.context.getResources().getString(R.string.unblock));
        } else {
            appCompatTextView2.setText(this.context.getResources().getString(R.string.block));
        }
        appCompatTextView2.setOnClickListener(new View.OnClickListener() { // from class: com.colorcallscreen.colorphone.callscreen.calltheme.adapter.CallLogsAdapter.11
            @Override 
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(CallLogsAdapter.this.context, BoloPermission.PHONE_CALLS) != 0) {
                    ActivityCompat.requestPermissions(CallLogsAdapter.this.context, new String[]{BoloPermission.PHONE_CALLS}, 111);
                }
                if (BlockHelper.isPhoneNumberInBlockedList(callLogModel.getNumber(), CallLogsAdapter.this.context)) {
                    CallLogsAdapter.this.callLogAction.onUnblock(callLogModel.getNumber());
                } else {
                    CallLogsAdapter.this.callLogAction.onBlock(callLogModel.getNumber());
                }
                CallLogsAdapter.this.mypopupWindow.dismiss();
            }
        });
        this.mypopupWindow = new PopupWindow(inflate, -2, -2, true);
    }
}
