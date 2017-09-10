package exnihilum.com.au.listdataviewer;

/*
 * Created by David on 16/05/2017.
 *
 */

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.TypedValue;

class AboutPageUtils {

    static Boolean isAppInstalled(Context context, String appName) {
        PackageManager pm = context.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(appName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }

    static int getThemeAccentColor(Context context) {
        int colorAttr;
        // Get colorAccent defined for AppCompat
        colorAttr = context.getResources().getIdentifier("colorAccent", "attr", context.getPackageName());
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(colorAttr, outValue, true);
        return outValue.data;
    }
}