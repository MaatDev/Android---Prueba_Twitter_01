package com.twitter;



import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SyncTwittActivity extends Activity {
   
	  private Button btnTwLogin;
	    private OnClickListener twitter_auth, twitter_clearauth;

	    private TextView txtTwStatus;
	    private boolean twitter_active = false;
	    
	    private static CommonsHttpOAuthProvider provider =
	    		new CommonsHttpOAuthProvider(
	    				"https://api.twitter.com/oauth/request_token",
	    		"https://api.twitter.com/oauth/access_token",
	    		"https://api.twitter.com/oauth/authorize");
	    		
	    private static CommonsHttpOAuthConsumer consumer =
	    		new CommonsHttpOAuthConsumer(
	    		"ew1ycFdnoNj4UJmJ7TgvA",
	    		"6ZWBDDCXDFDRb3VI6JTEKjKr64wkTDf0aP1B2ZlFc");
	

	     private static String ACCESS_KEY = null;

	     private static String ACCESS_SECRET = null;
	     
	     
	
  
	     @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        btnTwLogin = (Button)findViewById(R.id.btnTwLogin);
        txtTwStatus = (TextView) this.findViewById(R.id.txtTwStatus);
        
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String stored_keys = prefs.getString("KEY", "");
        String stored_secret = prefs.getString("SECRET", "");
        if (!stored_keys.equals("") && !stored_secret.equals("")) {
        twitter_active = true;
        }
        if (twitter_active) {
        	txtTwStatus.setText("Twitter status: sesión iniciada");
        	btnTwLogin.setText("Deautorizar twitter");
        	btnTwLogin.setOnClickListener(twitter_clearauth);
        } else {
        btnTwLogin.setText("Autorizar twitter");
        btnTwLogin.setOnClickListener(twitter_auth);
        }
        

	     twitter_auth = new OnClickListener() {
	    	 @Override
	    	 public void onClick(View v) {
	    	 txtTwStatus.setText("Twitter status: iniciando sesión");
	    	 try {
	    	 String authUrl = provider.retrieveRequestToken(consumer,
	    	 "mdw://twitter");
	    	 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl)));
	    	 }catch (OAuthMessageSignerException e) {
	    	 e.printStackTrace();
	    	 } catch (OAuthNotAuthorizedException e) {
	    	 e.printStackTrace();
	    	 } catch (OAuthExpectationFailedException e) {
	    	 e.printStackTrace();
	    	 } catch (OAuthCommunicationException e) {
	    	 e.printStackTrace();
	    	 }
	    	 }
	     };
		
	    	     
	     twitter_clearauth = new OnClickListener() {
				@Override
				public void onClick(View v) {    	
	    			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	    		    SharedPreferences.Editor editor = prefs.edit();    		    
	    		    editor.putString("KEY", null);
	    		    editor.putString("SECRET", null);
	    		    editor.commit();
	    		    btnTwLogin.setText("Autorizar twitter");
	    		    txtTwStatus.setText("Twitter status: sesi—n no iniciada ");
	    		    btnTwLogin.setOnClickListener(twitter_auth);
				    
				}
			};
				
	        if (twitter_active) {            
	        	txtTwStatus.setText("Twitter status: sesi—n iniciada ");
				btnTwLogin.setText("Deautorizar twitter");
	            btnTwLogin.setOnClickListener(twitter_clearauth);
	        } else {        	
	        	btnTwLogin.setText("Autorizar twitter");
	            btnTwLogin.setOnClickListener(twitter_auth);
	        }        
	    }
    
    
    
        @Override
        public void onResume() {
        	super.onResume();
        	Uri uri = this.getIntent().getData();  
        	if (uri != null && uri.toString().startsWith("mdw://twitter")) {
        	    String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
        	    try {    	    	
        	    	provider.retrieveAccessToken(consumer,verifier);
        			ACCESS_KEY = consumer.getToken();
        			ACCESS_SECRET = consumer.getTokenSecret();
        			
        			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        		    SharedPreferences.Editor editor = prefs.edit();    		    
        		    editor.putString("KEY", ACCESS_KEY);
        		    editor.putString("SECRET", ACCESS_SECRET);
        		    editor.commit();
        		    
        			TextView txtTwStatus = (TextView) this.findViewById(R.id.txtTwStatus);
    	            txtTwStatus.setText("Twitter status: sesi—n iniciada ");
    	            
    				btnTwLogin.setText("Deautorizar twitter");
    	            btnTwLogin.setOnClickListener(twitter_clearauth);

    			} catch (OAuthMessageSignerException e) {
    				e.printStackTrace();
    			} catch (OAuthNotAuthorizedException e) {
    				e.printStackTrace();
    			} catch (OAuthExpectationFailedException e) {
    				e.printStackTrace();
    			} catch (OAuthCommunicationException e) {
    				e.printStackTrace();
    			}        	    
        	}         
}   
}    
   
    
    

  
     