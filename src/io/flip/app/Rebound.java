package io.flip.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public class Rebound extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       
        PreferenceManager.setDefaultValues(this, R.xml.prefs, false);
        
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        String urlTemplate = sharedPref.getString("prefs_url", "");
        
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String action = intent.getAction();
        
        if (action.equals(Intent.ACTION_SEND)) {

        	String linkUrl = extras.getString("android.intent.extra.TEXT");
			String linkTitle = extras.getString("android.intent.extra.SUBJECT");
			
            if (linkTitle == null) {
            	linkTitle = linkUrl;
            }
            
            String url = urlTemplate;
            
            if(url.indexOf("%u") != -1){
            	url = url.replace("%u", Uri.encode(linkUrl));
            }
            
            if(url.indexOf("%t") != -1){
            	url = url.replace("%t", Uri.encode(linkTitle));
            }

			Uri newUrl = Uri.parse(url);

			Intent i = new Intent(Intent.ACTION_VIEW);
			
			i.setData(newUrl);

			startActivity(Intent.createChooser(i, null));
			
			this.finish();
            
	    } else {
	    	
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new Prefs())
	                .commit();
	    }

    }


	public static class Prefs extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        addPreferencesFromResource(R.xml.prefs);
	    }
	}

	
	
}


