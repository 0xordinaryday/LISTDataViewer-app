package Utilities;

import android.graphics.Color;

import java.util.HashMap;

/**
 * Created by David on 5/10/2017.
 *
 */

public class ColorMappingHelper {

    private ColorMappingHelper() {
        // never called
    }

    public static HashMap<String, Integer> makePlanningOnlineColorMap(int alphaValue) {
        HashMap<String, Integer> planningOnlineColorMap = new HashMap<>();
        planningOnlineColorMap.put("10.0 General Residential", Color.argb(alphaValue, 255, 0, 0));
        planningOnlineColorMap.put("11.0 Inner Residential", Color.argb(alphaValue, 128, 0, 0));
        planningOnlineColorMap.put("12.0 Low Density Residential", Color.argb(alphaValue, 242, 121, 121));
        planningOnlineColorMap.put("13.0 Rural Living", Color.argb(alphaValue, 255, 191, 202));
        planningOnlineColorMap.put("14.0 Environmental Living", Color.argb(alphaValue, 128, 128, 0));
        planningOnlineColorMap.put("15.0 Urban Mixed Use", Color.argb(alphaValue, 191, 191, 191));
        planningOnlineColorMap.put("16.0 Village", Color.argb(alphaValue, 255, 170, 0));
        planningOnlineColorMap.put("17.0 Community Purpose", Color.argb(alphaValue, 255, 255, 191));
        planningOnlineColorMap.put("18.0 Recreation", Color.argb(alphaValue, 0, 255, 0));
        planningOnlineColorMap.put("19.0 Open Space", Color.argb(alphaValue, 0, 128, 0));
        planningOnlineColorMap.put("20.0 Local Business", Color.argb(alphaValue, 172, 214, 230));
        planningOnlineColorMap.put("21.0 General Business", Color.argb(alphaValue, 69, 109, 230));
        planningOnlineColorMap.put("22.0 Central Business", Color.argb(alphaValue, 0, 0, 204));
        planningOnlineColorMap.put("23.0 Commercial", Color.argb(alphaValue, 131, 109, 242));
        planningOnlineColorMap.put("24.0 Light Industrial", Color.argb(alphaValue, 255, 0, 255));
        planningOnlineColorMap.put("25.0 General Industrial", Color.argb(alphaValue, 128, 0, 117));
        planningOnlineColorMap.put("26.0 Rural Resource", Color.argb(alphaValue, 255, 228, 191));
        planningOnlineColorMap.put("27.0 Significant Agricultural", Color.argb(alphaValue, 204, 133, 61));
        planningOnlineColorMap.put("28.0 Utilities", Color.argb(alphaValue, 255, 255, 0));
        planningOnlineColorMap.put("29.0 Environmental Mangement", Color.argb(alphaValue, 0, 128, 128));
        planningOnlineColorMap.put("30.0 Major Tourism", Color.argb(alphaValue, 230, 230, 255));
        return planningOnlineColorMap;
    }

}
