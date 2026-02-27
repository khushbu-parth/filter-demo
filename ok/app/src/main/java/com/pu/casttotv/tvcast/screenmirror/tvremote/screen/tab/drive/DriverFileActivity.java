package com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.drive;

import android.accounts.Account;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.pu.casttotv.tvcast.screenmirror.tvremote.base.BaseActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.ManagerDataPlay;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVConnectUtils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.controllers.TVType;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.ConnectActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.connect.DialogDisconnect;
import com.pu.casttotv.tvcast.screenmirror.tvremote.screen.tab.playcast.PlayCastActivity;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.SharedPrefsUtil;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.Utils;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.resize.CastPhotoOnlineError;
import com.pu.casttotv.tvcast.screenmirror.tvremote.utils.resize.PhotoUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kotlin.Unit;

@SuppressLint("WrongConstant")
public class DriverFileActivity extends BaseActivity {
    private FileAdapter adapter;
    private Button btn_login;
    DetailDriverFragment detailDriverFragment;
    private FrameLayout frame_content;
    private ImageView imvConnect;
    private List<GoogleDriveItem> list;
    private LinearLayout llBack;
    private LinearLayout llConnect;
    private LinearLayout llLogin;
    private LinearLayout llLogout;
    private DriveServiceHelper mDriveServiceHelper;
    private ViewGroup main_ads_native;
    private ProgressBar progress;
    private ProgressBar progressDetail;
    private RecyclerView rcvList;
    private TextView tvTitleTab;
    private String TAG = "DriverFileActivity";
    List<GoogleDriveItem> listNew = new ArrayList();
    List<GoogleDriveItem> listImage = new ArrayList();

    public void lambda$handleSignInResult$1(Exception exc) {
    }

    public void lambda$openFileFromFilePicker$3(Exception exc) {
    }

    public void lambda$query$5(Exception exc) {
    }

    public void lambda$queryFolder$7(Exception exc) {
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_driver_file);
        initView();
    }

    public Unit callbackFail() {
        return Unit.INSTANCE;
    }

    public Unit callbackDone() {
        return Unit.INSTANCE;
    }

    public Unit actionCommon() {
        return Unit.INSTANCE;
    }

    private void initView() {
        this.main_ads_native = (ViewGroup) findViewById(R.id.main_ads_native);
        this.imvConnect = (ImageView) findViewById(R.id.imvConnect);
        this.frame_content = (FrameLayout) findViewById(R.id.frame_content);
        this.llConnect = (LinearLayout) findViewById(R.id.llConnect);
        this.llBack = (LinearLayout) findViewById(R.id.llBack);
        this.tvTitleTab = (TextView) findViewById(R.id.tvTitleTab);
        this.llLogin = (LinearLayout) findViewById(R.id.llLogin);
        this.llLogout = (LinearLayout) findViewById(R.id.llLogout);
        this.btn_login = (Button) findViewById(R.id.btn_login);
        this.progress = (ProgressBar) findViewById(R.id.progress);
        this.progressDetail = (ProgressBar) findViewById(R.id.progressDetail);
        this.rcvList = (RecyclerView) findViewById(R.id.rcvList);
        this.tvTitleTab.setText(getText(R.string.menu_google_drive));
        this.llBack.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DriverFileActivity.this.onBackPressed();
            }
        });
        ImageView imageView = this.imvConnect;
        if (imageView != null) {
            imageView.setImageResource(TVConnectUtils.getInstance().isConnected() ? R.drawable.ic_connected_org : R.drawable.ic_not_connect_org);
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(1);
        this.rcvList.setLayoutManager(linearLayoutManager);
        FileAdapter fileAdapter = new FileAdapter(new ArrayList());
        this.adapter = fileAdapter;
        fileAdapter.setListener(new FileAdapter.IItemClick() {
            @Override
            public void clickItem(GoogleDriveItem googleDriveItem, List<GoogleDriveItem> list) {
                DriverFileActivity.this.gotoCast(googleDriveItem, list);
            }
        });
        this.rcvList.setAdapter(this.adapter);
        this.llConnect.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (TVConnectUtils.getInstance().isConnected()) {
                    new DialogDisconnect(DriverFileActivity.this).show();
                } else {
                    DriverFileActivity.this.gotoActivity(ConnectActivity.class);
                }
            }
        });
        this.btn_login.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DriverFileActivity.this.progress.setVisibility(0);
                DriverFileActivity.this.requestSignIn();
            }
        });
        this.llLogout.setOnClickListener(new View.OnClickListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                SharedPrefsUtil.getInstance().setAccount(null);
                DriverFileActivity.this.llLogin.setVisibility(0);
                DriverFileActivity.this.rcvList.setVisibility(8);
                DriverFileActivity.this.frame_content.setVisibility(8);
                DriverFileActivity.this.removeYourFragment();
                DriverFileActivity.this.signOut();
                DriverFileActivity.this.tvTitleTab.setText(DriverFileActivity.this.getString(R.string.menu_google_drive));
            }
        });
        Account account = SharedPrefsUtil.getInstance().getAccount();
        if (account != null) {
            this.progressDetail.setVisibility(0);
            this.llLogin.setVisibility(8);
            this.rcvList.setVisibility(0);
            queryData(account);
        } else {
            this.llLogout.setVisibility(8);
        }
        DriverFileActivity.this.callbackDone();
    }

    public void gotoCast(final GoogleDriveItem googleDriveItem, List<GoogleDriveItem> list) {
        final int i;
        DriverFileActivity.this.actionCommon();
        if (googleDriveItem.isImage()) {
            try {
                if (TVConnectUtils.getInstance().isConnected()) {
                    final Intent intent = new Intent(this, PlayCastActivity.class);
                    if (googleDriveItem.getName().contains("mp4")) {
                        ManagerDataPlay.getInstance().setTypePlay(6);
                    } else {
                        ManagerDataPlay.getInstance().setTypePlay(5);
                        ManagerDataPlay.getInstance().setListPhotoOnl(new ArrayList<>());
                    }
                    String thumbnailLink = googleDriveItem.getThumbnailLink();
                    this.listImage = new ArrayList();
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        if (list.get(i2).isImage()) {
                            this.listImage.add(list.get(i2));
                        }
                    }
                    int i3 = 0;
                    while (true) {
                        if (i3 >= this.listImage.size()) {
                            i = 0;
                            break;
                        } else if (this.listImage.get(i3).getThumbnailLink().equals(thumbnailLink)) {
                            i = i3;
                            break;
                        } else {
                            i3++;
                        }
                    }
                    if (thumbnailLink.contains("s220")) {
                        thumbnailLink = thumbnailLink.replace("s220", "s800");
                    }
                    if (TVType.isRokuTV(TVConnectUtils.getInstance().getConnectableDevice())) {
                        final String str = thumbnailLink;
                        PhotoUtils.Companion.castPhotoOnline(thumbnailLink, this, (DownloadManager) getSystemService("download"), new CastPhotoOnlineError() {
                            @Override
                            public void playAgainOnline(String str2) {
                                ManagerDataPlay.getInstance().pathCast = str2;
                                ManagerDataPlay.getInstance().titleCast = googleDriveItem.getName();
                                ManagerDataPlay.getInstance().thumbCast = str;
                                ManagerDataPlay.getInstance().currentPosCast = i;
                                ManagerDataPlay.getInstance().setListDriver(DriverFileActivity.this.listImage);
                                ManagerDataPlay.getInstance().setTypePlay(6);
                                DriverFileActivity.this.startActivity(intent);
                                Utils.nextScreen(DriverFileActivity.this);
                            }
                        });
                        return;
                    }
                    ManagerDataPlay.getInstance().pathCast = thumbnailLink;
                    ManagerDataPlay.getInstance().titleCast = googleDriveItem.getName();
                    ManagerDataPlay.getInstance().thumbCast = thumbnailLink;
                    ManagerDataPlay.getInstance().currentPosCast = i;
                    ManagerDataPlay.getInstance().setTypePlay(6);
                    ManagerDataPlay.getInstance().setListDriver(this.listImage);
                    startActivity(intent);
                    Utils.nextScreen(this);
                    return;
                }
                gotoActivity(ConnectActivity.class);
                return;
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
        setDetail(googleDriveItem);
    }

    @Override
    public void onResume() {
        super.onResume();
        ImageView imageView = this.imvConnect;
        if (imageView != null) {
            imageView.setImageResource(TVConnectUtils.getInstance().isConnected() ? R.drawable.ic_connected_org : R.drawable.ic_not_connect_org);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        int backStackEntryCount = supportFragmentManager.getBackStackEntryCount();
        if (backStackEntryCount <= 0) {
            onFinish();
            return;
        }
        if (backStackEntryCount > 1) {
            String name = supportFragmentManager.getBackStackEntryAt(backStackEntryCount - 2).getName();
            StringBuilder sb = new StringBuilder();
            sb.append("onBackPressed - title=");
            sb.append(name);
            this.tvTitleTab.setText(name);
        } else {
            this.tvTitleTab.setText("Drive");
        }
        super.onBackPressed();
    }

    public void removeYourFragment() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        for (int i = 0; i < supportFragmentManager.getBackStackEntryCount(); i++) {
            supportFragmentManager.popBackStack();
        }
    }

    public void setDetail(GoogleDriveItem googleDriveItem) {
        this.progressDetail.setVisibility(0);
        FrameLayout frameLayout = this.frame_content;
        if (frameLayout != null && frameLayout.getVisibility() == 8) {
            this.frame_content.setVisibility(0);
        }
        this.tvTitleTab.setText(googleDriveItem.getName());
        this.detailDriverFragment = new DetailDriverFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_content, this.detailDriverFragment).addToBackStack(googleDriveItem.getName()).commit();
        queryFolder(googleDriveItem.getId(), this.detailDriverFragment);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void signOut() {
        GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().requestId().requestScopes(new Scope(DriveScopes.DRIVE_READONLY), new Scope[0]).build()).signOut();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestSignIn() {
        startActivityForResult(GoogleSignIn.getClient((Activity) this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().requestId().requestScopes(new Scope(DriveScopes.DRIVE_READONLY), new Scope[0]).build()).getSignInIntent(), 1);
    }

    private void handleSignInResults(Task<GoogleSignInAccount> task) {
        try {
            task.getResult(ApiException.class).getIdToken();
            GoogleSignInAccount lastSignedInAccount = GoogleSignIn.getLastSignedInAccount(this);
            if (lastSignedInAccount == null) {
                return;
            }
            String str = "";
            if (lastSignedInAccount.getPhotoUrl() != null) {
                str = lastSignedInAccount.getPhotoUrl().toString();
            }
            new Gson().toJson(new CloudAccountDto(lastSignedInAccount.getEmail(), lastSignedInAccount.getId(), lastSignedInAccount.getGivenName(), lastSignedInAccount.getFamilyName(), lastSignedInAccount.getDisplayName(), lastSignedInAccount.getServerAuthCode(), lastSignedInAccount.getIdToken(), str));
        } catch (ApiException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("signInResult:failed code=");
            sb.append(e2.getStatusCode());
        }
    }

    @Override
    // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        Uri data;
        if (i != 1) {
            if (i == 2 && i2 == -1 && intent != null && (data = intent.getData()) != null) {
                openFileFromFilePicker(data);
            }
        } else if (i2 == -1 && intent != null) {
            handleSignInResults(GoogleSignIn.getSignedInAccountFromIntent(intent));
            handleSignInResult(intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    private void handleSignInResult(Intent intent) {
        GoogleSignIn.getSignedInAccountFromIntent(intent).addOnSuccessListener(new OnSuccessListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda4
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                DriverFileActivity.this.lambda$handleSignInResult$0((GoogleSignInAccount) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda3
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                DriverFileActivity.this.lambda$handleSignInResult$1(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lambda$handleSignInResult$0(GoogleSignInAccount googleSignInAccount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Signed in as ");
        sb.append(googleSignInAccount.getEmail());
        SharedPrefsUtil.getInstance().setAccount(googleSignInAccount.getAccount());
        queryData(googleSignInAccount.getAccount());
    }

    private void queryData(Account account) {
        this.llLogout.setVisibility(0);
        GoogleAccountCredential usingOAuth2 = GoogleAccountCredential.usingOAuth2(this, Collections.singleton(DriveScopes.DRIVE_READONLY));
        usingOAuth2.setSelectedAccount(account);
        this.mDriveServiceHelper = new DriveServiceHelper(new Drive.Builder(new NetHttpTransport.Builder().build(), new GsonFactory(), usingOAuth2).build());
        query();
    }

    private void openFileFromFilePicker(Uri uri) {
        if (this.mDriveServiceHelper != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Opening ");
            sb.append(uri.getPath());
            this.mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri).addOnSuccessListener(new OnSuccessListener<Pair<String, String>>() {
                @Override
                public void onSuccess(Pair<String, String> stringStringPair) {
                    DriverFileActivity.lambda$openFileFromFilePicker$2(stringStringPair);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public final void onFailure(Exception exc) {
                    DriverFileActivity.this.lambda$openFileFromFilePicker$3(exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void lambda$openFileFromFilePicker$2(Pair pair) {
        String str = (String) pair.first;
        String str2 = (String) pair.second;
    }

    private void query() {
        this.listNew = new ArrayList();
        if (this.mDriveServiceHelper != null) {
            this.list = new ArrayList();
            this.mDriveServiceHelper.queryFiles().addOnSuccessListener(new OnSuccessListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda5
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    DriverFileActivity.this.lambda$query$4((FileList) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda1
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    DriverFileActivity.this.lambda$query$5(exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lambda$query$4(FileList fileList) {
        for (File file : fileList.getFiles()) {
            if (file.getMimeType().toLowerCase().contains("png") || file.getMimeType().toLowerCase().contains("jpg") || file.getMimeType().toLowerCase().contains("hevc") || file.getMimeType().toLowerCase().contains("mpeg") || file.getMimeType().toLowerCase().contains("jpeg")) {
                if (file.getThumbnailLink() != null) {
                    file.getThumbnailLink();
                }
                file.getName();
                this.list.add(new GoogleDriveItem(file.getId(), file.getName(), file.getMimeType(), file.getThumbnailLink(), file.getWebViewLink(), file.getWebContentLink(), file.getSize(), true));
            } else if (file.getMimeType().contains("folder")) {
                this.list.add(new GoogleDriveItem(file.getId(), file.getName(), file.getMimeType(), file.getThumbnailLink(), file.getWebViewLink(), file.getWebContentLink(), file.getSize(), false));
            }
        }
        this.llLogin.setVisibility(8);
        this.progress.setVisibility(4);
        this.progressDetail.setVisibility(8);
        this.rcvList.setVisibility(0);
        for (int i = 0; i < this.list.size(); i++) {
            if (!this.list.get(i).isImage()) {
                this.listNew.add(this.list.get(i));
            }
        }
        for (int i2 = 0; i2 < this.list.size(); i2++) {
            if (this.list.get(i2).isImage()) {
                this.listNew.add(this.list.get(i2));
            }
        }
        this.adapter.setData(this.listNew);
    }

    private void queryFolder(String str, final DetailDriverFragment detailDriverFragment) {
        this.listNew = new ArrayList();
        if (this.mDriveServiceHelper != null) {
            final ArrayList arrayList = new ArrayList();
            this.mDriveServiceHelper.queryFiles(str).addOnSuccessListener(new OnSuccessListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda6
                @Override // com.google.android.gms.tasks.OnSuccessListener
                public final void onSuccess(Object obj) {
                    DriverFileActivity.this.lambda$queryFolder$6(arrayList, detailDriverFragment, (FileList) obj);
                }
            }).addOnFailureListener(new OnFailureListener() { // from class: com.magicapps.casttotv.tv.screen.tab.drive.DriverFileActivity$$ExternalSyntheticLambda2
                @Override // com.google.android.gms.tasks.OnFailureListener
                public final void onFailure(Exception exc) {
                    DriverFileActivity.this.lambda$queryFolder$7(exc);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void lambda$queryFolder$6(List list, DetailDriverFragment detailDriverFragment, FileList fileList) {
        for (File file : fileList.getFiles()) {
            if (file.getMimeType().toLowerCase().contains("png") || file.getMimeType().toLowerCase().contains("jpg") || file.getMimeType().toLowerCase().contains("hevc") || file.getMimeType().toLowerCase().contains("mpeg") || file.getMimeType().toLowerCase().contains("jpeg")) {
                if (file.getThumbnailLink() != null) {
                    file.getThumbnailLink();
                }
                file.getName();
                list.add(new GoogleDriveItem(file.getId(), file.getName(), file.getMimeType(), file.getThumbnailLink(), file.getWebViewLink(), file.getWebContentLink(), file.getSize(), true));
            } else if (file.getMimeType().contains("folder")) {
                list.add(new GoogleDriveItem(file.getId(), file.getName(), file.getMimeType(), file.getThumbnailLink(), file.getWebViewLink(), file.getWebContentLink(), file.getSize(), false));
            }
        }
        for (int i = 0; i < list.size(); i++) {
            if (!((GoogleDriveItem) list.get(i)).isImage()) {
                this.listNew.add((GoogleDriveItem) list.get(i));
            }
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            if (((GoogleDriveItem) list.get(i2)).isImage()) {
                this.listNew.add((GoogleDriveItem) list.get(i2));
            }
        }
        this.progressDetail.setVisibility(8);
        detailDriverFragment.setData(this.listNew);
    }
}
