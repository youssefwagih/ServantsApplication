package com.example.youssefwagih.servantsapplication.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.youssefwagih.servantsapplication.R;
/*import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;*/

import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
/*
    */
/* A reference to the Firebase *//*

    private Firebase mFirebaseRef;
    */
/* Data from the authenticated user *//*

    private AuthData mAuthData;

    Button googleSignInButton;
    Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        */
/* Firebase initialization *//*

        Firebase.setAndroidContext(this);
        mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        registerButton = (Button)findViewById(R.id.registerButton);
        googleSignInButton = (Button)findViewById(R.id.googleSignInButton);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginWithPassword();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseRef.createUser("youssef@gmail.com", "youssefwagih", new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }
                    @Override
                    public void onError(FirebaseError firebaseError) {
                        // there was an error
                    }
                });
            }
        });
    }

    */
/* ************************************
     *              PASSWORD              *
     **************************************
     *//*

    public void loginWithPassword() {
        // mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword("test@firebaseuser.com", "test1234", new AuthResultHandler("password"));
    }

    */
/**
     * Utility class for authentication results
     *//*

    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            //mAuthProgressDialog.hide();
            Log.i("usuf", provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            //mAuthProgressDialog.hide();
            //showErrorDialog(firebaseError.toString());
        }

        */
/**
         * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
         *//*

        private void setAuthenticatedUser(AuthData authData) {
            if (authData != null) {
            */
/* Hide all the login buttons *//*

                */
/*
                mFacebookLoginButton.setVisibility(View.GONE);
                mGoogleLoginButton.setVisibility(View.GONE);
                mTwitterLoginButton.setVisibility(View.GONE);
                mPasswordLoginButton.setVisibility(View.GONE);
                mAnonymousLoginButton.setVisibility(View.GONE);
                mLoggedInStatusTextView.setVisibility(View.VISIBLE);
                *//*

            */
/* show a provider specific status text *//*

                String name = null;
                if (authData.getProvider().equals("facebook")
                        || authData.getProvider().equals("google")
                        || authData.getProvider().equals("twitter")) {
                    name = (String) authData.getProviderData().get("displayName");
                } else if (authData.getProvider().equals("anonymous")
                        || authData.getProvider().equals("password")) {
                    name = authData.getUid();
                } else {
                    Log.e("usuf", "Invalid provider: " + authData.getProvider());
                }
                if (name != null) {
                    Log.e("usuf", "Logged in as " + name + " (" + authData.getProvider() + ")");
                }
            } else {

            */
/* No authenticated user show all the login buttons *//*

                */
/*
                mFacebookLoginButton.setVisibility(View.VISIBLE);
                mGoogleLoginButton.setVisibility(View.VISIBLE);
                mTwitterLoginButton.setVisibility(View.VISIBLE);
                mPasswordLoginButton.setVisibility(View.VISIBLE);
                mAnonymousLoginButton.setVisibility(View.VISIBLE);
                mLoggedInStatusTextView.setVisibility(View.GONE);
                *//*

            }
            //this.mAuthData = authData;
        */
/* invalidate options menu to hide/show the logout button *//*

            supportInvalidateOptionsMenu();
        }
*/

//    }

}

