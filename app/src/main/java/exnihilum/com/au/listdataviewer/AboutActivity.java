package exnihilum.com.au.listdataviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by David on 15/05/2017.
 * https://github.com/medyo/android-about-page
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // find all the views
        LinearLayout emailLayout = (LinearLayout) findViewById(R.id.email_view);
        LinearLayout websiteLayout = (LinearLayout) findViewById(R.id.website_view);

        // global onclick listener
        // define 'global' Onclick listener for all views
        final View.OnClickListener mGlobal_OnClickListener = new View.OnClickListener() {
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.email_view:
                        String email = "exnihilum_tasmania@gmail.com";
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setType("message/rfc822");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        startActivity(emailIntent);
                        break;
                    case R.id.website_view:
                        String url = "https://exnihilum.com.au/";
                        Uri webUri = Uri.parse(url);
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
                        startActivity(browserIntent);
                        break;
                }
            }
        };

        // set listeners
        emailLayout.setOnClickListener(mGlobal_OnClickListener);
        websiteLayout.setOnClickListener(mGlobal_OnClickListener);

    }

}
