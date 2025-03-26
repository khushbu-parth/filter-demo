package rapidapp.touchbar.freehdvideodownlaoder.videodonwload.appdata.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorGenerator {
    public static final ColorGenerator MATERIAL = create(Arrays.asList(new Integer[]{-1739917, -1023342, -4560696, -6982195, -8812853, -10177034, -11549705, -11677471, -11684180, -8271996, -5319295, -30107, -2825897, -10929, -18611, -6190977, -7297874}));
    private final List<Integer> mColors;
    private final Random mRandom = new Random(System.currentTimeMillis());

    private ColorGenerator(List<Integer> list) {
        this.mColors = list;
    }

    private static ColorGenerator create(List<Integer> list) {
        return new ColorGenerator(list);
    }

    public int getRandomColor() {
        List<Integer> list = this.mColors;
        return list.get(this.mRandom.nextInt(list.size())).intValue();
    }
}
