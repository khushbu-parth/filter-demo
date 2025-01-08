package connect.couplephotosuit.couplephoto.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import connect.couplephotosuit.couplephoto.Adapter.BackgroundAdapter;
import connect.couplephotosuit.couplephoto.Adapter.GridStickerAdapter;
import connect.couplephotosuit.couplephoto.DemoStickerTExt.DemoStickerView;
import connect.couplephotosuit.couplephoto.DemoStickerTExt.StickerImageView;
import connect.couplephotosuit.couplephoto.MyTouch.HorizontalListView;
import connect.couplephotosuit.couplephoto.MyTouch.MultiTouchListener;
import connect.couplephotosuit.couplephoto.R;
import connect.couplephotosuit.couplephoto.Text.TextActivity;
import connect.couplephotosuit.couplephoto.effect.Effects;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_LOAD_IMAGE = 2;
    public static Bitmap bitmap;
    public static Bitmap bmpFinalSave;
    public static Bitmap btmfrm;
    public static Bitmap btnfinalsticker;
    ArrayList<String> Backgroundlist = new ArrayList<>();
    HorizontalScrollView HL_Effact;
    private int IMG_ERS = 8;
    ArrayList<String> Stickers;
    private int TEX_REQ = 2;
    private LinearLayout backgroundll;
    private ImageView beard;
    ImageView bg;
    private ImageView cap;
    private ImageView ef1;
    private ImageView ef10;
    private ImageView ef11;
    private ImageView ef12;
    private ImageView ef14;
    private ImageView ef15;
    private ImageView ef16;
    private ImageView ef17;
    private ImageView ef18;
    private ImageView ef19;
    private ImageView ef2;
    private ImageView ef20;
    private ImageView ef21;
    private ImageView ef22;
    private ImageView ef4;
    private ImageView ef5;
    private ImageView ef6;
    private ImageView ef7;
    private ImageView ef9;
    private ImageView ef_original;
    ImageView effect;
    ImageView getimage;
    private ImageView goggles;
    private GridStickerAdapter gridAdapter;
    HorizontalListView hl_background;
    ImageView imgbackground;
    boolean isFlag = true;
    GridView ivgrid;
    FrameLayout mainframe1;
    ImageView move;
    private DemoStickerView.OnTouchSticker onTouchSticker = new DemoStickerView.OnTouchSticker() {
        public void onTouchedSticker() {
            EditActivity.this.removeBorder();
        }
    };
    ImageView save;
    FrameLayout saveimgfrm;
    private ImageView stckr;

    public StickerImageView sticker;
    ImageView sticker1;
    LinearLayout stickerlayout;
    ArrayList<Integer> stickerviewId = new ArrayList<>();
    ImageView text;
    private ImageView veapon;
    private ImageView vehicle;
    private int view_id = 1;
    private com.facebook.ads.InterstitialAd interstitialAdFB;
    private boolean flag;
    Handler handler = new Handler();
    private com.google.android.gms.ads.InterstitialAd mInterstitialAdMob;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            showFBInterstitial1();
        }
    };

    private void loadFBInterstitialAd() {
        this.interstitialAdFB = new com.facebook.ads.InterstitialAd(this, getResources().getString(R.string.fb_interstitial1));
        this.interstitialAdFB.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {
            }

            @Override
            public void onAdClicked(Ad ad) {
            }

            @Override
            public void onLoggingImpression(Ad ad) {
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                interstitialAdFB.loadAd();
                handler.removeCallbacks(runnable);
                flag = true;
            }

        });
        this.interstitialAdFB.loadAd();
    }

    private void showFBInterstitial1() {
        if (this.interstitialAdFB != null && this.interstitialAdFB.isAdLoaded()) {
            this.interstitialAdFB.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadFBInterstitialAd();
        if (flag == false) {
            handler.postDelayed(runnable, 5000);
        }

    }



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_edit);

        this.mInterstitialAdMob = showAdmobFullAd();
        loadAdmobAd();


        getWindow().setFlags(1024, 1024);
        bind();
        if (OptionActivity.i == 1) {
            this.bg.setVisibility(View.GONE);
            this.move.setVisibility(View.GONE);
        }
        listcap("love");
        this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
        this.ivgrid.setAdapter(this.gridAdapter);
        this.ivgrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    EditActivity.btnfinalsticker = BitmapFactory.decodeStream(EditActivity.this.getAssets().open(EditActivity.this.Stickers.get(i)));
                    EditActivity.this.addsticker();
                    EditActivity.this.stickerlayout.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void listcap(String str) {
        this.Stickers = new ArrayList<>();
        this.Stickers.clear();
        String[] strArr = new String[0];
        try {
            String[] list = getResources().getAssets().list(str);
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    ArrayList<String> arrayList = this.Stickers;
                    arrayList.add(str + "/" + list[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void bind() {
        this.saveimgfrm = (FrameLayout) findViewById(R.id.saveimgfrm);
        this.getimage = (ImageView) findViewById(R.id.getimage);
        if (OptionActivity.i == 0) {
            this.getimage.setImageBitmap(SuitActivity.bmpNext);
        }
        if (OptionActivity.i == 1) {
            this.getimage.setImageBitmap(SuitActivity.bmpNext);
            this.getimage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        this.backgroundll = (LinearLayout) findViewById(R.id.backgroundll);
        this.imgbackground = (ImageView) findViewById(R.id.imgbackground);
        this.mainframe1 = (FrameLayout) findViewById(R.id.mainframe1);
        this.text = (ImageView) findViewById(R.id.text);
        this.text.setOnClickListener(this);
        this.bg = (ImageView) findViewById(R.id.bg);
        this.bg.setOnClickListener(this);
        this.save = (ImageView) findViewById(R.id.save);
        this.save.setOnClickListener(this);
        this.effect = (ImageView) findViewById(R.id.effect);
        this.effect.setOnClickListener(this);
        this.sticker1 = (ImageView) findViewById(R.id.sticker);
        this.sticker1.setOnClickListener(this);
        this.move = (ImageView) findViewById(R.id.move);
        this.move.setOnClickListener(this);
        this.stickerlayout = (LinearLayout) findViewById(R.id.stickerlayout);
        this.stckr = (ImageView) findViewById(R.id.stckr);
        this.stckr.setOnClickListener(this);
        this.cap = (ImageView) findViewById(R.id.cap);
        this.cap.setOnClickListener(this);
        this.goggles = (ImageView) findViewById(R.id.goggles);
        this.goggles.setOnClickListener(this);
        this.beard = (ImageView) findViewById(R.id.beard);
        this.beard.setOnClickListener(this);
        this.vehicle = (ImageView) findViewById(R.id.vehicle);
        this.vehicle.setOnClickListener(this);
        this.veapon = (ImageView) findViewById(R.id.veapon);
        this.veapon.setOnClickListener(this);
        this.saveimgfrm = (FrameLayout) findViewById(R.id.saveimgfrm);
        this.HL_Effact = (HorizontalScrollView) findViewById(R.id.horizontalscroll);
        this.ivgrid = (GridView) findViewById(R.id.ivgrid);
        this.hl_background = (HorizontalListView) findViewById(R.id.hl_background);
        this.saveimgfrm.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                EditActivity.this.removeBorder();
                return false;
            }
        });
        listbackground1("background");
        this.hl_background.setAdapter((ListAdapter) new BackgroundAdapter(this, this.Backgroundlist));
        this.hl_background.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                try {
                    EditActivity.btmfrm = BitmapFactory.decodeStream(EditActivity.this.getAssets().open(String.valueOf(EditActivity.this.Backgroundlist.get(i))));
                    EditActivity.this.imgbackground.setImageBitmap(EditActivity.btmfrm);
                    if (i == 0) {
                        EditActivity.this.imgbackground.setImageBitmap((Bitmap) null);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.ef_original = (ImageView) findViewById(R.id.ef_original);
        this.ef1 = (ImageView) findViewById(R.id.ef1);
        this.ef2 = (ImageView) findViewById(R.id.ef2);
        this.ef4 = (ImageView) findViewById(R.id.ef4);
        this.ef5 = (ImageView) findViewById(R.id.ef5);
        this.ef6 = (ImageView) findViewById(R.id.ef6);
        this.ef7 = (ImageView) findViewById(R.id.ef7);
        this.ef9 = (ImageView) findViewById(R.id.ef9);
        this.ef10 = (ImageView) findViewById(R.id.ef10);
        this.ef11 = (ImageView) findViewById(R.id.ef11);
        this.ef12 = (ImageView) findViewById(R.id.ef12);
        this.ef14 = (ImageView) findViewById(R.id.ef14);
        this.ef15 = (ImageView) findViewById(R.id.ef15);
        this.ef16 = (ImageView) findViewById(R.id.ef16);
        this.ef17 = (ImageView) findViewById(R.id.ef17);
        this.ef18 = (ImageView) findViewById(R.id.ef18);
        this.ef19 = (ImageView) findViewById(R.id.ef19);
        this.ef20 = (ImageView) findViewById(R.id.ef20);
        this.ef21 = (ImageView) findViewById(R.id.ef21);
        this.ef22 = (ImageView) findViewById(R.id.ef22);
        this.ef_original.setOnClickListener(this);
        this.ef1.setOnClickListener(this);
        this.ef2.setOnClickListener(this);
        this.ef4.setOnClickListener(this);
        this.ef5.setOnClickListener(this);
        this.ef6.setOnClickListener(this);
        this.ef7.setOnClickListener(this);
        this.ef9.setOnClickListener(this);
        this.ef10.setOnClickListener(this);
        this.ef11.setOnClickListener(this);
        this.ef12.setOnClickListener(this);
        this.ef14.setOnClickListener(this);
        this.ef15.setOnClickListener(this);
        this.ef16.setOnClickListener(this);
        this.ef17.setOnClickListener(this);
        this.ef18.setOnClickListener(this);
        this.ef19.setOnClickListener(this);
        this.ef20.setOnClickListener(this);
        this.ef21.setOnClickListener(this);
        this.ef22.setOnClickListener(this);
        Effects.applyEffectNone(this.ef_original);
        Effects.applyEffect1(this.ef1);
        Effects.applyEffect2(this.ef2);
        Effects.applyEffect4(this.ef4);
        Effects.applyEffect5(this.ef5);
        Effects.applyEffect6(this.ef6);
        Effects.applyEffect7(this.ef7);
        Effects.applyEffect9(this.ef9);
        Effects.applyEffect10(this.ef10);
        Effects.applyEffect11(this.ef11);
        Effects.applyEffect12(this.ef12);
        Effects.applyEffect14(this.ef14);
        Effects.applyEffect15(this.ef15);
        Effects.applyEffect16(this.ef16);
        Effects.applyEffect17(this.ef17);
        Effects.applyEffect18(this.ef18);
        Effects.applyEffect19(this.ef19);
        Effects.applyEffect20(this.ef20);
        Effects.applyEffect21(this.ef21);
        Effects.applyEffect22(this.ef22);
    }

    private void listbackground1(String str) {
        this.Backgroundlist = new ArrayList<>();
        this.Backgroundlist.clear();
        String[] strArr = new String[0];
        try {
            String[] list = getResources().getAssets().list(str);
            if (list != null) {
                for (int i = 0; i < list.length; i++) {
                    ArrayList<String> arrayList = this.Backgroundlist;
                    arrayList.add(str + "/" + list[i]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.beard /*2131296346*/:
                this.ivgrid.setVisibility(View.VISIBLE);
                listcap("boyassecories");
                this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                this.ivgrid.setAdapter(this.gridAdapter);
                return;
            case R.id.bg /*2131296348*/:
                if (this.backgroundll.getVisibility() == 8) {
                    this.backgroundll.setVisibility(View.VISIBLE);
                    this.stickerlayout.setVisibility(View.GONE);
                    this.HL_Effact.setVisibility(View.GONE);
                    return;
                }
                this.backgroundll.setVisibility(View.GONE);
                this.stickerlayout.setVisibility(View.GONE);
                this.HL_Effact.setVisibility(View.GONE);
                return;
            case R.id.cap /*2131296363*/:
                this.ivgrid.setVisibility(View.VISIBLE);
                listcap("love");
                this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                this.ivgrid.setAdapter(this.gridAdapter);
                return;
            case R.id.goggles /*2131296461*/:
                this.ivgrid.setVisibility(View.VISIBLE);
                listcap("goggles");
                this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                this.ivgrid.setAdapter(this.gridAdapter);
                return;
            case R.id.image_manual /*2131296485*/:
                startActivityForResult(new Intent(this, EraseActivity.class), this.IMG_ERS);
                return;
            case R.id.move /*2131296574*/:
                this.backgroundll.setVisibility(View.VISIBLE);
                this.stickerlayout.setVisibility(View.GONE);
                this.HL_Effact.setVisibility(View.GONE);
                if (this.isFlag) {
                    Toast.makeText(this, "Move Image..", 0).show();
                    this.move.setImageResource(R.drawable.movedone);
                    this.getimage.setOnTouchListener(new MultiTouchListener());
                    this.getimage.setEnabled(true);
                    this.isFlag = !this.isFlag;
                    return;
                }
                Toast.makeText(this, "Move Close..", 0).show();
                this.move.setImageResource(R.drawable.moveclos);
                this.getimage.setOnTouchListener((View.OnTouchListener) null);
                this.getimage.setEnabled(false);
                this.isFlag = !this.isFlag;
                return;
            case R.id.save /*2131296641*/:
                callvisibility();
                removeBorder();
                bmpFinalSave = getbitmap(this.saveimgfrm);
                saveImage(bmpFinalSave);
                startActivity(new Intent(this, ShareActivity.class));
                showAdmobInterstitial();
                return;
            case R.id.text /*2131296709*/:
                this.stickerlayout.setVisibility(View.GONE);
                this.HL_Effact.setVisibility(View.GONE);
                this.backgroundll.setVisibility(View.VISIBLE);
                startActivityForResult(new Intent(this, TextActivity.class), this.TEX_REQ);
                return;
            default:
                switch (id) {
                    case R.id.ef1 /*2131296413*/:
                        Effects.applyEffect1(this.getimage);
                        return;
                    case R.id.ef10 /*2131296414*/:
                        Effects.applyEffect10(this.getimage);
                        return;
                    case R.id.ef11 /*2131296415*/:
                        Effects.applyEffect11(this.getimage);
                        return;
                    case R.id.ef12 /*2131296416*/:
                        Effects.applyEffect12(this.getimage);
                        return;
                    case R.id.ef14 /*2131296417*/:
                        Effects.applyEffect14(this.getimage);
                        return;
                    case R.id.ef15 /*2131296418*/:
                        Effects.applyEffect15(this.getimage);
                        return;
                    case R.id.ef16 /*2131296419*/:
                        Effects.applyEffect16(this.getimage);
                        return;
                    case R.id.ef17 /*2131296420*/:
                        Effects.applyEffect17(this.getimage);
                        return;
                    case R.id.ef18 /*2131296421*/:
                        Effects.applyEffect18(this.getimage);
                        return;
                    case R.id.ef19 /*2131296422*/:
                        Effects.applyEffect19(this.getimage);
                        return;
                    case R.id.ef2 /*2131296423*/:
                        Effects.applyEffect2(this.getimage);
                        return;
                    case R.id.ef20 /*2131296424*/:
                        Effects.applyEffect20(this.getimage);
                        return;
                    case R.id.ef21 /*2131296425*/:
                        Effects.applyEffect21(this.getimage);
                        return;
                    case R.id.ef22 /*2131296426*/:
                        Effects.applyEffect22(this.getimage);
                        return;
                    case R.id.ef4 /*2131296427*/:
                        Effects.applyEffect4(this.getimage);
                        return;
                    case R.id.ef5 /*2131296428*/:
                        Effects.applyEffect5(this.getimage);
                        return;
                    case R.id.ef6 /*2131296429*/:
                        Effects.applyEffect6(this.getimage);
                        return;
                    case R.id.ef7 /*2131296430*/:
                        Effects.applyEffect7(this.getimage);
                        return;
                    case R.id.ef9 /*2131296431*/:
                        Effects.applyEffect9(this.getimage);
                        return;
                    case R.id.ef_original /*2131296432*/:
                        Effects.applyEffectNone(this.getimage);
                        return;
                    case R.id.effect /*2131296433*/:
                        if (this.HL_Effact.getVisibility() == 8) {
                            this.backgroundll.setVisibility(View.GONE);
                            this.stickerlayout.setVisibility(View.GONE);
                            this.HL_Effact.setVisibility(View.VISIBLE);
                            return;
                        }
                        this.backgroundll.setVisibility(View.GONE);
                        this.stickerlayout.setVisibility(View.GONE);
                        this.HL_Effact.setVisibility(View.GONE);
                        return;
                    default:
                        switch (id) {
                            case R.id.stckr /*2131296691*/:
                                this.ivgrid.setVisibility(View.VISIBLE);
                                listcap("emoji");
                                this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                                this.ivgrid.setAdapter(this.gridAdapter);
                                return;
                            case R.id.sticker /*2131296692*/:
                                if (this.stickerlayout.getVisibility() == 8) {
                                    this.backgroundll.setVisibility(View.GONE);
                                    this.stickerlayout.setVisibility(View.VISIBLE);
                                    this.HL_Effact.setVisibility(View.GONE);
                                    return;
                                }
                                this.backgroundll.setVisibility(View.GONE);
                                this.stickerlayout.setVisibility(View.GONE);
                                this.HL_Effact.setVisibility(View.GONE);
                                return;
                            default:
                                switch (id) {
                                    case R.id.veapon /*2131296750*/:
                                        this.ivgrid.setVisibility(View.VISIBLE);
                                        listcap("cake");
                                        this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                                        this.ivgrid.setAdapter(this.gridAdapter);
                                        return;
                                    case R.id.vehicle /*2131296751*/:
                                        this.ivgrid.setVisibility(View.VISIBLE);
                                        listcap("girlassecories");
                                        this.gridAdapter = new GridStickerAdapter(this, this.Stickers);
                                        this.ivgrid.setAdapter(this.gridAdapter);
                                        return;
                                    default:
                                        return;
                                }
                        }
                }
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == this.IMG_ERS) {
            this.getimage.setImageBitmap(SuitActivity.bmpNext);
        }
        if (i == 2 && i2 == -1 && intent != null) {
            String[] strArr = {"_data"};
            Cursor query = getContentResolver().query(intent.getData(), strArr, (String) null, (String[]) null, (String) null);
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex(strArr[0]));
            query.close();
            this.imgbackground.setImageBitmap(BitmapFactory.decodeFile(string));
        }
        if (i == this.TEX_REQ) {
            addText();
        }
    }

    private Bitmap getbitmap(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }


    public void removeBorder() {
        for (int i = 0; i < this.stickerviewId.size(); i++) {
            View findViewById = this.saveimgfrm.findViewById(this.stickerviewId.get(i).intValue());
            if (findViewById instanceof StickerImageView) {
                ((StickerImageView) findViewById).setControlItemsHidden(true);
            }
        }
    }

    private void addText() {
        this.sticker = new StickerImageView(this, this.onTouchSticker);
        this.sticker.setImageBitmap(TextActivity.finalBitmapText);
        this.view_id = new Random().nextInt();
        int i = this.view_id;
        if (i < 0) {
            this.view_id = i - (i * 2);
        }
        this.sticker.setId(this.view_id);
        this.stickerviewId.add(Integer.valueOf(this.view_id));
        this.sticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditActivity.this.sticker.setControlItemsHidden(false);
            }
        });
        this.saveimgfrm.addView(this.sticker);
    }

    public void callvisibility() {
        this.backgroundll.setVisibility(View.GONE);
        this.stickerlayout.setVisibility(View.GONE);
        this.HL_Effact.setVisibility(View.GONE);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }


    public void addsticker() {
        this.sticker = new StickerImageView(this, this.onTouchSticker);
        this.sticker.setImageBitmap(btnfinalsticker);
        this.view_id = new Random().nextInt();
        int i = this.view_id;
        if (i < 0) {
            this.view_id = i - (i * 2);
        }
        this.sticker.setId(this.view_id);
        this.stickerviewId.add(Integer.valueOf(this.view_id));
        this.sticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditActivity.this.sticker.setControlItemsHidden(false);
            }
        });
        this.saveimgfrm.addView(this.sticker);
    }

    private void saveImage(Bitmap bitmap2) {
        Log.v("TAG", "saveImageInCache is called");
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        File file = new File(externalStorageDirectory.getAbsolutePath() + "/" + Glob.Edit_Folder_name);
        file.mkdirs();
        String str = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpeg";
        File file2 = new File(file, str);
        file2.renameTo(file2);
        String str2 = "file://" + externalStorageDirectory.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + str;
        Glob._url = externalStorageDirectory.getAbsolutePath() + "/" + Glob.Edit_Folder_name + "/" + str;
        Log.d("cache uri=", str2);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(str2))));
            MediaScannerConnection.scanFile(this, new String[]{Glob._url}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
                public void onMediaScannerConnected() {
                }

                public void onScanCompleted(String str, Uri uri) {
                    Glob._url = str;
                    Glob.testUri = uri;
                }
            });
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(str2))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private com.google.android.gms.ads.InterstitialAd showAdmobFullAd() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
            }

            public void onAdOpened() {
            }

            public void onAdClosed() {
                loadAdmobAd();
            }
        });
        return interstitialAd;
    }


    public void loadAdmobAd() {
        this.mInterstitialAdMob.loadAd(new AdRequest.Builder().build());
    }

    private void showAdmobInterstitial() {
        com.google.android.gms.ads.InterstitialAd interstitialAd = this.mInterstitialAdMob;
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            this.mInterstitialAdMob.show();
        }
    }


}
