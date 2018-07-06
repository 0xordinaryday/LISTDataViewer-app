package Utilities;

/**
 * Created by David on 3/07/2018.
 * https://github.com/shalperin/android-wms/blob/master/WMSDemo/app/src/main/java/us/blauha/wmsdemo/TileProviderFactory.java
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

public class TileProviderFactory {

    private final static String[] MRTbase = {"Geology, 25k Scale", "Geology, 250k Scale",
            "MRT Landslide Susceptibility", "MRT Landslide Geomorphology"};

    private final static String[] LISTbase = {"State Orthophoto Composite", "Hillshade Grey",
            "Hillshade Coloured", "LIST Topographic"};

    private static HashMap<String, String> layerMap;
    private static boolean isLISTbase = false;
    private static boolean isMRTbase = false;

    public static WMSTileProvider getOsgeoWmsTileProvider(String layerName) {

        layerMap = new HashMap<String, String>();
        layerMap.put("Geology, 25k Scale", "All_Tasmania_tas_geology25k.ecw");
        layerMap.put("Geology, 250k Scale", "All_Tasmania_tas_geology250K.ecw");
        layerMap.put("MRT Landslide Susceptibility", "Geotechnical_landslide_slide.ecw");
        layerMap.put("MRT Landslide Geomorphology", "Geotechnical_landslide_geomorphology.ecw");
        layerMap.put("State Orthophoto Composite", "Orthophoto/ImageServer");
        layerMap.put("Hillshade Grey", "HillshadeGrey/MapServer");
        layerMap.put("Hillshade Coloured", "Hillshade/MapServer");
        layerMap.put("LIST Topographic", "Topographic/ImageServer");

        for (String str:MRTbase) {
            if (str.equals(layerName)) {
                isMRTbase = true;
                isLISTbase = false;
            }
        }
        for (String str:LISTbase) {
            if (str.equals(layerName)) {
                isMRTbase = false;
                isLISTbase = true;
            }
        }

        String WMS_FORMAT_STRING;

        if (!isLISTbase && isMRTbase) {
            WMS_FORMAT_STRING =
            "http://www.mrt.tas.gov.au/erdas-iws/ogc/wms/?TRANSPARENT=TRUE&OPACITY=1&layers=" +
                    layerMap.get(layerName) +
                    "&VERSION=1.1.1&SERVICE=WMS&REQUEST=GetMap&STYLES=&format=image/png&BBOX=%f,%f,%f,%f" +
                    "&WIDTH=256&HEIGHT=256&SRS=EPSG:3857";
        } else if (!isMRTbase && isLISTbase) {
            WMS_FORMAT_STRING =
            "https://services.thelist.tas.gov.au/arcgis/rest/services/Basemaps/" +
                    layerMap.get(layerName) +
                    "/tile/%d/%d/%d";
        } else {
            WMS_FORMAT_STRING = "";
        }

        WMSTileProvider tileProvider = new WMSTileProvider(256,256) {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                String s;
                if (!isLISTbase && isMRTbase) {
                    s = String.format(Locale.US, WMS_FORMAT_STRING, bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY]);
                } else if (!isMRTbase && isLISTbase) {
                    s = String.format(Locale.US, WMS_FORMAT_STRING, zoom, y, x);
                } else {
                    s = "";
                }
                URL url;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }
}