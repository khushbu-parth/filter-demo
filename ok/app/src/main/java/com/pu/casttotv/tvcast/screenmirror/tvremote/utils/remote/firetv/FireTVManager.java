package com.pu.casttotv.tvcast.screenmirror.tvremote.utils.remote.firetv;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import com.pu.casttotv.tvcast.screenmirror.tvremote.R;
import com.tananaev.adblib.AdbConnection;
import com.tananaev.adblib.AdbCrypto;
import com.tananaev.adblib.AdbStream;
import com.pu.casttotv.tvcast.screenmirror.tvremote.model.MessageEvent;
import java.io.Closeable;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import org.greenrobot.eventbus.EventBus;

public class FireTVManager implements Closeable {
    public static String STRING_CHARSET_NAME = "UTF-8";
    public static ArrayList<String> myArray = new ArrayList<>();
    public static ArrayList<String> myArrayList = new ArrayList<>();
    public static StringBuilder myBuilder = new StringBuilder();
    String[] activities = {"com.netflix.ninja/.MainActivity", "com.amazon.firetv.youtube/dev.cobalt.app.MainActivity", "com.hulu.plus/.SplashActivity", "com.disney.disneyplus/com.bamtechmedia.dominguez.main.MainActivity", "com.hbo.hbonow/com.hbo.go.LaunchActivity", "com.tubitv.ott/com.tubitv.live.ChannelsSyncUpActivity", "com.esaba.downloader/.MainActivity", "com.tiktok.tv/com.ss.android.ugc.aweme.tv.feed.MainTvActivity", "com.peacock.peacockfiretv/com.peacock.peacocktv.AmazonMainActivity", "com.sling/.livetv.LiveTvSyncActivity", "com.starz.starzplay.firetv/com.starz.amznfiretv.SplashActivity", "tv.pluto.android/.EntryPoint", "com.cbsnews.ott/.controllers.activities.MainActivity", "com.plexapp.android/com.plexapp.plex.activities.SplashActivity", "com.spotify.tv.android/.SpotifyTVActivity", "com.philo.philo/.tif.TvSetupActivity", "in.startv.hotstar/.ui.splash.TVSplashActivity", "com.apple.atve.amazon.appletv/.MainActivity", "com.espn.gtv/com.espn.androidtv.ui.LoadingActivity", "com.amazon.cloud9/.browsing.BrowserActivity", "com.amazon.firetv.youtube.tv/dev.cobalt.app.MainActivity", "com.amazon.firebat/.deeplink.DeepLinkRoutingActivity", "com.crunchyroll.crunchyroid/.MainActivity", "com.future.moviesByFawesomeAndroidTV/.SplashActivity", "com.lionsgateplay.videoapp/com.parsifal.starz.ui.features.splash.SplashActivity", "com.onemainstream.sonyliv.android/com.sonyliv.ui.splash.SplashActivity", "com.nbaimd.gametime.nba2011.amazon/com.neulion.MainActivity", "com.mxtech.videoplayer.ad/.ActivityWelcomeMX", "com.discoveryplus.tv.fire/.DPlusFireTvActivity", "com.balaji.alt/com.balaji.play.tv.views.GhostActivity", "com.redbull.rbtv/com.redbull.launch.SplashActivity", "com.zee5.amazon/com.zee5.live.rich.RichTvInputSetupActivity", "com.future.HappyKids/.SplashActivity", "tv.twitch.android.viewer/tv.twitch.android.apps.TwitchActivity", "com.amazon.rialto.cordova.webapp.webappe9d3fd9a69254a278fefa093292e194f/.MainActivity", "com.jio.media.stb.ondemand/com.jio.media.stb.jioondemand.ui.splash.SplashActivity", "com.rma.netpulsetv/.ui.SplashActivity", "com.irondragonproductions.idtvflix/com.irondragonproductions.irondragontv.LaunchActivity", "eu.bandainamcoent.pacman256/com.unity3d.player.UnityPlayerNativeActivity", "com.amazon.rialto.cordova.webapp.webapp490adaf842ab464a8f4f19c7a054d17c/.MainActivity"};
    private Activity activity;
    private AdbConnection adbConnection;
    int[] appImages = {R.drawable.netflix, R.drawable.youtube, R.drawable.hulu, R.drawable.disneyplus, R.drawable.hbomax, R.drawable.tubi, R.drawable.downloader, R.drawable.tiktok_tv, R.drawable.peacock, R.drawable.sling_tv, R.drawable.starz, R.drawable.plutotv, R.drawable.cbs_news, R.drawable.plex, R.drawable.spotify, R.drawable.philo, R.drawable.hotstar, R.drawable.appletv, R.drawable.espn, R.drawable.amazon_silk, R.drawable.youtube_tv, R.drawable.primevideo, R.drawable.crunchyroll, R.drawable.fawsome, R.drawable.lionsgate, R.drawable.sonyliv, R.drawable.nba, R.drawable.mxplayer, R.drawable.discovery, R.drawable.altbalaji, R.drawable.redbull, R.drawable.zee, R.drawable.happykids, R.drawable.twitch, R.drawable.beetv, R.drawable.jiocinema, R.drawable.internetspeedtest, R.drawable.irondragontv, R.drawable.pacman, R.drawable.freemoviepluus};
    String[] appName = {"Netflix", "YouTube", "Hulu", "Disney+", "HBO Max", "Tubi - Watch Free Movies & TV Shows", "Downloader", "TikTok for TV", "Peacock TV", "Sling TV", "STARZ", "Pluto TV", "CBS News", "Plex", "Spotify - Music and Podcasts", "Philo: Live & On-Demand TV", "Hotstar", "Apple TV", "ESPN for Fire TV", "Amazon Silk - Web Browser", "YouTube TV", "Prime Video", "Crunchyroll", "Fawesome - Free Awesome TV & Movies", "Lionsgate Play", "SonyLIV", "NBA on Fire TV", "MX Player", "Discovery Plus", "ALTBalaji", "Red Bull TV", "ZEE5", "HappyKids", "Twitch", "BEE TV Network", "JioCinema: Movies TV Originals", "Internet Speed Test App", "Iron Dragon TV", "PAC-MAN 256 - Endless Arcade Maze", "Free Movies Plus"};
    private boolean arrayFull = false;
    private ArrayList<ChannelFireTVDto> channelFireTVDtoArrayList = new ArrayList<>();
    private StringBuilder commandBuffer = new StringBuilder();
    private LinkedBlockingQueue<byte[]> commandQueue = new LinkedBlockingQueue<>();
    int installedChannelCounter = 0;
    String[] packageName = {"com.netflix.ninja", "com.amazon.firetv.youtube", "com.hulu.plus", "com.disney.disneyplus", "com.hbo.hbonow", "com.tubitv.ott", "com.esaba.downloader", "com.tiktok.tv", "com.peacock.peacockfiretv", "com.sling", "com.starz.starzplay.firetv", "tv.pluto.android", "com.cbs.ott", "com.plexapp.android", "com.spotify.tv.android", "com.philo.philo", "in.startv.hotstar", "com.apple.atve.amazon.appletv", "com.espn.gtv", "com.amazon.cloud9", "com.amazon.firetv.youtube.tv", "com.amazon.firebat", "com.crunchyroll.crunchyroid", "com.future.moviesByFawesomeAndroidTV", "com.lionsgateplay.videoapp", "com.onemainstream.sonyliv.android", "com.nbaimd.gametime.nba2011.amazon", "com.mxtech.videoplayer.ad", "com.discoveryplus.tv.fire", "com.balaji.alt", "com.redbull.rbtv", "com.zee5.amazon", "com.future.HappyKids", "tv.twitch.android.viewer", "com.amazon.rialto.cordova.webapp.webappe9d3fd9a69254a278fefa093292e194f", "com.jio.media.stb.ondemand", "com.rma.netpulsetv", "com.irondragonproductions.idtvflix", "eu.bandainamcoent.pacman256", "com.amazon.rialto.cordova.webapp.webapp490adaf842ab464a8f4f19c7a054d17c"};
    private AdbStream shellStream;
    private UpdateDataListener updateDataListener;

    public interface UpdateDataListener {
        void onSuccess();
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public AdbConnection getAdbConnection() {
        return this.adbConnection;
    }

    public void setListener(UpdateDataListener updateDataListener2) {
        this.updateDataListener = updateDataListener2;
    }

    public ArrayList<ChannelFireTVDto> getChannelFireTVDtoArrayList() {
        return this.channelFireTVDtoArrayList;
    }

    public FireTVManager(Activity activity2) {
        this.activity = activity2;
    }

    public void initData() {
        int i = 0;
        while (true) {
            String[] strArr = this.packageName;
            if (i >= strArr.length) {
                break;
            }
            this.channelFireTVDtoArrayList.add(new ChannelFireTVDto(this.activities[i], this.appName[i], strArr[i], this.appImages[i]));
            i++;
        }
        for (int i2 = 0; i2 < myArrayList.size(); i2++) {
            for (int i3 = 0; i3 < this.channelFireTVDtoArrayList.size(); i3++) {
                if (this.channelFireTVDtoArrayList.get(i3).getPackageName().equals(myArrayList.get(i2))) {
                    ArrayList<ChannelFireTVDto> arrayList = this.channelFireTVDtoArrayList;
                    arrayList.add(0, arrayList.get(i3));
                    this.channelFireTVDtoArrayList.remove(i3 + 1);
                    this.installedChannelCounter++;
                }
            }
        }
        EventBus.getDefault().post(new MessageEvent("firetv"));
    }

    public boolean queueCommand(String str) {
        try {
            this.commandQueue.add(str.getBytes(STRING_CHARSET_NAME));
            return true;
        } catch (UnsupportedEncodingException unused) {
            return false;
        }
    }

    public void createConnection(final String str) {
        new Thread(new Runnable() {
            /* class com.magicapps.casttotv.tv.utils.remote.firetv.FireTVManager.AnonymousClass1 */

            public void run() {
                try {
                    Socket socket = new Socket(str, 5555);
                    AdbCrypto readCryptoConfig = AdbUtils.readCryptoConfig(FireTVManager.this.activity.getFilesDir());
                    if (readCryptoConfig == null) {
                        readCryptoConfig = AdbUtils.writeNewCryptoConfig(FireTVManager.this.activity.getFilesDir());
                    }
                    FireTVManager.this.adbConnection = AdbConnection.create(socket, readCryptoConfig);
                    FireTVManager.this.adbConnection.connect();
                    FireTVManager fireTVManager = FireTVManager.this;
                    fireTVManager.shellStream = fireTVManager.adbConnection.open("shell:");
                    FireTVManager.this.updateDataListener.onSuccess();
                    FireTVManager.this.startReceiveThread();
                    FireTVManager.this.sendLoop();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public void getChannelsList(String str) {
        this.commandBuffer.append(str);
        this.commandBuffer.append('\n');
        queueCommand(this.commandBuffer.toString());
        this.commandBuffer.setLength(0);
        updateDataInArray();
    }

    public void launchChannelFromRV(String str) {
        this.commandBuffer.append(str);
        this.commandBuffer.append('\n');
        queueCommand(this.commandBuffer.toString());
        this.commandBuffer.setLength(0);
    }

    public void disconnectTelevision() {
        this.commandBuffer.append("exit");
        this.commandBuffer.append('\n');
        queueCommand(this.commandBuffer.toString());
        this.commandBuffer.setLength(0);
    }

    private void updateDataInArray() {
        this.activity.runOnUiThread(new Runnable() {
            /* class com.magicapps.casttotv.tv.utils.remote.firetv.FireTVManager.AnonymousClass2 */

            public void run() {
                new Handler().postDelayed(new Runnable() {
                    /* class com.magicapps.casttotv.tv.utils.remote.firetv.FireTVManager.AnonymousClass2.AnonymousClass1 */

                    public void run() {
                        int lastIndexOf;
                        FireTVManager.myArrayList.clear();
                        int i = 0;
                        boolean z = false;
                        int i2 = 0;
                        while (true) {
                            int indexOf = FireTVManager.myBuilder.toString().indexOf("package:", i);
                            lastIndexOf = FireTVManager.myBuilder.toString().lastIndexOf("shell@tank");
                            StringBuilder sb = new StringBuilder();
                            sb.append(FireTVManager.myBuilder.toString());
                            sb.append(" - count - ");
                            sb.append(indexOf);
                            if (indexOf < 0) {
                                break;
                            }
                            int i3 = indexOf + 8;
                            if (z) {
                                FireTVManager.myArrayList.add(FireTVManager.myBuilder.toString().substring(i2, indexOf).trim());
                            }
                            z = true;
                            FireTVManager.myArrayList.toString();
                            i = i3;
                            i2 = i;
                        }
                        if (lastIndexOf != -1) {
                            try {
                                FireTVManager.myArrayList.add(FireTVManager.myBuilder.toString().substring(i2, lastIndexOf).trim());
                            } catch (Exception unused) {
                            }
                        }
                        FireTVManager.myArrayList.add("com.amazon.firebat");
                        FireTVManager.myArrayList.toString();
                        FireTVManager.this.initData();
                    }
                }, 2000);
            }
        });
    }

    public void sendLoop() {
        while (true) {
            try {
                byte[] take = this.commandQueue.take();
                if (this.shellStream.isClosed()) {
                    AdbUtils.safeClose(this);
                    return;
                }
                try {
                    this.shellStream.write(take);
                } catch (IOException | InterruptedException unused) {
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                AdbUtils.safeClose(this);
            }
        }
    }

    public void startReceiveThread() {
        new Thread(new Runnable() {
            /* class com.magicapps.casttotv.tv.utils.remote.firetv.FireTVManager.AnonymousClass3 */

            public void run() {
                while (!FireTVManager.this.shellStream.isClosed()) {
                    try {
                        FireTVManager.this.receivedData(FireTVManager.this.shellStream.read());
                    } catch (IOException | InterruptedException unused) {
                    } catch (Throwable th) {
                        AdbUtils.safeClose(FireTVManager.this);
                        throw th;
                    }
                    AdbUtils.safeClose(FireTVManager.this);
                }
            }
        }).start();
    }

    static {
        new ArrayList();
    }

    public void receivedData(byte[] bArr) {
        if (Build.VERSION.SDK_INT >= 19 && !this.arrayFull) {
            myArray.add(new String(bArr, StandardCharsets.UTF_8));
            myBuilder.append(new String(bArr, StandardCharsets.UTF_8));
        }
        int i = 0;
        for (int i2 = 0; i2 < myArray.size(); i2++) {
            if (myArray.get(i2).contains("shell@tank")) {
                i++;
            }
        }
        if (i == 2) {
            this.arrayFull = true;
        }
    }
}
