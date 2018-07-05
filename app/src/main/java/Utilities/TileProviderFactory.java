package Utilities;

/**
 * Created by David on 3/07/2018.
 * https://github.com/shalperin/android-wms/blob/master/WMSDemo/app/src/main/java/us/blauha/wmsdemo/TileProviderFactory.java
 */

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class TileProviderFactory {

    public static WMSTileProvider getOsgeoWmsTileProvider(String layerName) {

        /*final String WMS_FORMAT_STRING =
                "http://www.mrt.tas.gov.au/erdas-iws/ogc/wms/?TRANSPARENT=TRUE&OPACITY=1&layers=" +
                        layerName +
                        // "All_Tasmania_tas_geology25k.ecw" +
                        // "Geotechnical_landslide_slide.ecw" +
                        "&VERSION=1.1.1&SERVICE=WMS&REQUEST=GetMap&STYLES=&format=image/png&BBOX=%f,%f,%f,%f" +
                        "&WIDTH=256&HEIGHT=256&SRS=EPSG:3857";*/

        /*final String WMS_FORMAT_STRING =
                "http://services.thelist.tas.gov.au/arcgis/services/Basemaps/Orthophoto/ImageServer/WMSServer/?TRANSPARENT=TRUE&OPACITY=1&layers=" +
                        "0" +
                        "&VERSION=1.1.1&SERVICE=WMS&REQUEST=GetMap&STYLES=&format=image/png&BBOX=%f,%f,%f,%f" +
                        "&WIDTH=256&HEIGHT=256&SRS=EPSG:3857";*/

        /*final String WMS_FORMAT_STRING =
                "http://services.thelist.tas.gov.au/arcgis/services/Basemaps/Topographic/ImageServer/WMSServer/?TRANSPARENT=TRUE&OPACITY=1&layers=" +
                        "0" +
                        "&VERSION=1.1.1&SERVICE=WMS&REQUEST=GetMap&STYLES=&format=image/png&BBOX=%f,%f,%f,%f" +
                        "&WIDTH=256&HEIGHT=256&SRS=EPSG:3857";*/

        final String WMS_FORMAT_STRING =
                // "https://services.thelist.tas.gov.au/arcgis/rest/services/Basemaps/HillshadeGrey/MapServer/tile/%d/%d/%d";
                // "https://services.thelist.tas.gov.au/arcgis/rest/services/Basemaps/Topographic/ImageServer/tile/%d/%d/%d";
                // "https://services.thelist.tas.gov.au/arcgis/rest/services/Basemaps/Hillshade/MapServer/tile/%d/%d/%d"; // coloured
                "https://services.thelist.tas.gov.au/arcgis/rest/services/Basemaps/Orthophoto/ImageServer/tile/%d/%d/%d";

        WMSTileProvider tileProvider = new WMSTileProvider(256,256) {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                // String s = String.format(Locale.US, WMS_FORMAT_STRING, bbox[MINX], bbox[MINY], bbox[MAXX], bbox[MAXY]);
                // tile testing
                String s = String.format(Locale.US, WMS_FORMAT_STRING, zoom, y, x);
                Log.d("WMSDEMO", s);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                // debug
                Log.i("URL", url.toString());
                return url;
            }
        };
        return tileProvider;
    }
}