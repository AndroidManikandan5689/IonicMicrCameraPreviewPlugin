package cordova.plugin.raqmiyat.micrcameraview;

import java.util.HashMap;

public enum RasterYUVFormat {
    YV12(0),
    NV12(1),
    NV21(2),
    YUY2(3),
    YUV_420_888(4);

    private int intValue;
    private static HashMap<Integer, RasterYUVFormat> mappings;

    private static HashMap<Integer, RasterYUVFormat> getMappings() {
        if (mappings == null) {
            Class var0 = RasterYUVFormat.class;
            synchronized(RasterYUVFormat.class) {
                if (mappings == null) {
                    mappings = new HashMap();
                }
            }
        }

        return mappings;
    }

    private RasterYUVFormat(int value) {
        this.intValue = value;
        getMappings().put(value, this);
    }

    public int getValue() {
        return this.intValue;
    }

    public static RasterYUVFormat forValue(int value) {
        return (RasterYUVFormat)getMappings().get(value);
    }
}
