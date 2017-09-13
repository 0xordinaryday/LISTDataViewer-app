package Utilities;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by david on 28/07/2017.
 *
 */

public class Helper {

    private final static double SEMIMAJORAXIS = 6378137.0;  // WGS84 spheriod semimajor axis
    private final static double RADTODEGREE = 180.0 / Math.PI;
    private final static double DEGREETORAD = Math.PI / 180.0 * 90.0;
    private final static double DEGREE = 0.017453292519943295;

    private Helper() {
        // not called
    }

    public static LatLng fromWebMercatorToGeographic(double mercatorX_lon, double mercatorY_lat) {
        // can return null
        if (Math.abs(mercatorX_lon) < 180 && Math.abs(mercatorY_lat) < 90) {
            return new LatLng(mercatorY_lat, mercatorX_lon);
        }

        if ((Math.abs(mercatorX_lon) > 20037508.3427892) ||
                (Math.abs(mercatorY_lat) > 20037508.3427892)) {
            return null;
        }

        mercatorX_lon = (mercatorX_lon / SEMIMAJORAXIS * RADTODEGREE) -
                ((Math.floor((double) (((mercatorX_lon / SEMIMAJORAXIS * RADTODEGREE)
                        + 180.0) / 360.0)))*360.0);
        mercatorY_lat = (DEGREETORAD - (2.0 * Math.atan(Math.exp((-1.0 * mercatorY_lat)
                / SEMIMAJORAXIS)))) * RADTODEGREE;
        LatLng convertedCoords = new LatLng(mercatorY_lat, mercatorX_lon);
        return convertedCoords;
    }

    public static double[] geographicToWebMercator(double lat, double lon) {
        double[] coordinateArray = new double[2];
        if (Math.abs(lon) > 180 && Math.abs(lat) > 90) {
           coordinateArray[0] = lat;
           coordinateArray[1] = lon;
        } else {

            double east = lon * DEGREE;
            double north = lat * DEGREE;
            double northing = SEMIMAJORAXIS / 2.0 * Math.log((1.0 + Math.sin(north)) /
                    (1.0 - Math.sin(north)));
            double easting = SEMIMAJORAXIS * east;
            coordinateArray[0] = northing;
            coordinateArray[1] = easting;
        }
        return coordinateArray;
    }

    // usage unzip(new File("/sdcard/pictures.zip"), new File("/sdcard"));
    // https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
    public static void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            /* if time should be restored as well
            long time = ze.getTime();
            if (time > 0)
                file.setLastModified(time);
            */
            }
        } finally {
            zis.close();
        }
    }

    public static void reformatKMLFile(File file) {
        try {
            SAXBuilder builder = new SAXBuilder();
            if (file.exists()) {
                Document document = builder.build(file);
                Element root = document.getRootElement();
                Log.i("ROOT attr name", Arrays.toString(root.getChildren().toArray()));
                Element firstTag = root.getChild("Document");
                Log.i("firstTag", root.getChild("Document").toString());
                //firstTag.getAttribute("tag").setValue("file");
                List rootdata = firstTag.getChildren();
                System.out.println(rootdata);
                for(Iterator<Element> i = rootdata.iterator(); i.hasNext(); ) {
                    Element rootitem =i.next();
                    if(rootitem.getName() =="Placemark"){
                        System.out.println(rootitem.getChildren("ExtendedData"));
                        List listdata = rootitem.getChild("ExtendedData").getChild("SchemaData").getChildren();
                        for(Iterator<Element> j = listdata.iterator(); j.hasNext(); ) {
                            Element item =j.next();
                            System.out.println(item);
                            String gidval = item.getText();
                            Element gid = new Element("value").setText(gidval);
                            item.setText("");
                            item.addContent(gid);
                            System.out.println("test");
                        }
                    }
                }
                String des = new XMLOutputter().outputString(document);
                des = des.replaceAll("SimpleData", "Data");
                des = des.replaceAll("</SchemaData>", "");
                des = des.replaceAll("<SchemaData schemaUrl=\"#new_area\">", "");
                System.out.println("String: " + des.replaceAll("SimpleData", "Data"));
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(des);
                fileWriter.close();
            } else {
                System.out.println("File does not exist");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
