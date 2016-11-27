package com.example.youssefwagih.servantsapplication.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.youssefwagih.servantsapplication.R;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {

        private static final String TAG = DisplayStudentsActivity.class.getSimpleName();
        private TextView mLoggedInStatusTextView;
        private ProgressDialog mAuthProgressDialog;
        private Firebase mFirebaseRef;
        private AuthData mAuthData;
        private Firebase.AuthStateListener mAuthStateListener;
        public static final int RC_GOOGLE_LOGIN = 1;
        private GoogleApiClient mGoogleApiClient;
        private boolean mGoogleIntentInProgress;
        private boolean mGoogleLoginClicked;
        private ConnectionResult mGoogleConnectionResult;
        private SignInButton mGoogleLoginButton;
        private Button mPasswordLoginButton;
        private Button mAnonymousLoginButton;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        /* Load the view and display it */
            setContentView(R.layout.activity_login);

            mGoogleLoginButton = (SignInButton) findViewById(R.id.login_with_google);
            mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGoogleLoginClicked = true;
                    if (!mGoogleApiClient.isConnecting()) {
                        if (mGoogleConnectionResult != null) {
                            resolveSignInError();
                        } else if (mGoogleApiClient.isConnected()) {
                            //getGoogleOAuthTokenAndLogin();
                            Intent intent = new Intent(LoginActivity.this, DisplayStudentsActivity.class);
                            startActivity(intent);
                        } else {
                    /* connect API now */
                            Log.d(TAG, "Trying to connect to Google API");
                            mGoogleApiClient.connect();
                        }
                    }
                }
            });

         /*Setup the Google API object to allow Google+ logins */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .build();

        /* *************************************
         *               PASSWORD              *
         ***************************************/
            mPasswordLoginButton = (Button) findViewById(R.id.login_with_password);
            mPasswordLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginWithPassword();
                }
            });

        /* Load and setup the anonymous login button */
            mAnonymousLoginButton = (Button) findViewById(R.id.login_anonymously);
            mAnonymousLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginAnonymously();
                }
            });

            mLoggedInStatusTextView = (TextView) findViewById(R.id.login_status);

        /* Create the Firebase ref that is used for all authentication with Firebase */
            mFirebaseRef = new Firebase(getResources().getString(R.string.firebase_url));

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
            mAuthProgressDialog = new ProgressDialog(this);
            mAuthProgressDialog.setTitle("Loading");
            mAuthProgressDialog.setMessage("Authenticating with Firebase...");
            mAuthProgressDialog.setCancelable(false);
            mAuthProgressDialog.show();

            mAuthStateListener = new Firebase.AuthStateListener() {
                @Override
                public void onAuthStateChanged(AuthData authData) {
                    mAuthProgressDialog.hide();
                    setAuthenticatedUser(authData);
                }
            };
        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
            mFirebaseRef.addAuthStateListener(mAuthStateListener);
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            // if changing configurations, stop tracking firebase session.
            mFirebaseRef.removeAuthStateListener(mAuthStateListener);
        }

        /**
         * This method fires when any startActivityForResult finishes. The requestCode maps to
         * the value passed into startActivityForResult.
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            Map<String, String> options = new HashMap<String, String>();
            if (requestCode == RC_GOOGLE_LOGIN) {
            /* This was a request by the Google API */
                if (resultCode != RESULT_OK) {
                    mGoogleLoginClicked = false;
                }
                mGoogleIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
            if (this.mAuthData != null) {
                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_logout) {
                logout();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        /**
         * Unauthenticate from Firebase and from providers where necessary.
         */
    private void logout() {
        if (this.mAuthData != null) {
            /* logout of Firebase */
            mFirebaseRef.unauth();
            /* Logout of any of the Frameworks. This step is optional, but ensures the user is not logged into*/
            if (this.mAuthData.getProvider().equals("google")) {
                /* Logout from Google+ */
                if (mGoogleApiClient.isConnected()) {
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                }
            }
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }

    /**
     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
     * necessary parameters depending on the provider)
     */
    private void authWithFirebase(final String provider, Map<String, String> options) {
        if (options.containsKey("error")) {
            showErrorDialog(options.get("error"));
        } else {
            mAuthProgressDialog.show();
            if (provider.equals("twitter")) {
                // if the provider is twitter, we pust pass in additional options, so use the options endpoint
                mFirebaseRef.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
            } else {
                // if the provider is not twitter, we just need to pass in the oauth_token
                mFirebaseRef.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
                Intent intent = new Intent(this, DisplayStudentsActivity.class);
                startActivity(intent);
            }
        }
    }

    /**
     * Once a user is logged in, take the mAuthData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
            mGoogleLoginButton.setVisibility(View.GONE);
            mPasswordLoginButton.setVisibility(View.GONE);
            mAnonymousLoginButton.setVisibility(View.GONE);
            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
            /* show a provider specific status text */
            String name = null;
            if (authData.getProvider().equals("facebook")
                    || authData.getProvider().equals("google")
                    || authData.getProvider().equals("twitter")) {
                name = (String) authData.getProviderData().get("displayName");
            } else if (authData.getProvider().equals("anonymous")
                    || authData.getProvider().equals("password")) {
                name = authData.getUid();
            } else {
                Log.e(TAG, "Invalid provider: " + authData.getProvider());
            }
            if (name != null) {
                mLoggedInStatusTextView.setText("Logged in as " + name + " (" + authData.getProvider() + ")");
            }
        } else {
            /* No authenticated user show all the login buttons */
            mGoogleLoginButton.setVisibility(View.VISIBLE);
            mPasswordLoginButton.setVisibility(View.VISIBLE);
            mAnonymousLoginButton.setVisibility(View.VISIBLE);
            mLoggedInStatusTextView.setVisibility(View.GONE);
        }
        this.mAuthData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }

        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }

    /* ************************************
     *              GOOGLE                *
     **************************************
     */
    /* A helper method to resolve the current ConnectionResult error. */
    private void resolveSignInError() {
        if (mGoogleConnectionResult.hasResolution()) {
            try {
                mGoogleIntentInProgress = true;
                mGoogleConnectionResult.startResolutionForResult(this, RC_GOOGLE_LOGIN);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.
                mGoogleIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

/*
    private void getGoogleOAuthTokenAndLogin() {
        mAuthProgressDialog.show();
        */
/* Get OAuth token in Background *//*

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String errorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);
                    token = GoogleAuthUtil.getToken(LoginActivity.this, Plus.AccountApi.getAccountName(mGoogleApiClient), scope);
                } catch (IOException transientEx) {
                    */
/* Network or server error *//*

                    Log.e(TAG, "Error authenticating with Google: " + transientEx);
                    errorMessage = "Network error: " + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
                    Log.w(TAG, "Recoverable Google OAuth error: " + e.toString());
                    */
/* We probably need to ask for permissions, so start the intent if there is none pending *//*

                    if (!mGoogleIntentInProgress) {
                        mGoogleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_GOOGLE_LOGIN);
                    }
                } catch (GoogleAuthException authEx) {
                    */
/* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. *//*

                    Log.e(TAG, "Error authenticating with Google: " + authEx.getMessage(), authEx);
                    errorMessage = "Error authenticating with Google: " + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                mGoogleLoginClicked = false;
                if (token != null) {
                    */
/* Successfully got OAuth token, now login with Google *//*

                    mFirebaseRef.authWithOAuthToken("google", token, new AuthResultHandler("google"));
                } else if (errorMessage != null) {
                    mAuthProgressDialog.hide();
                    showErrorDialog(errorMessage);
                }
            }
        };
        task.execute();
    }
*/

    @Override
    public void onConnected(final Bundle bundle) {
        /* Connected with Google API, use this to authenticate with Firebase */
        //getGoogleOAuthTokenAndLogin();
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!mGoogleIntentInProgress) {
            /* Store the ConnectionResult so that we can use it later when the user clicks on the Google+ login button */
            mGoogleConnectionResult = result;

            if (mGoogleLoginClicked) {
                /* The user has already clicked login so we attempt to resolve all errors until the user is signed in,
                 * or they cancel. */
                resolveSignInError();
            } else {
                Log.e(TAG, result.toString());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // ignore
    }

    /* ************************************
     *              PASSWORD              *
     **************************************
     */
    public void loginWithPassword() {
        mAuthProgressDialog.show();
        mFirebaseRef.authWithPassword("test@firebaseuser.com", "test1234", new AuthResultHandler("password"));
        Intent intent = new Intent(this, DisplayStudentsActivity.class);
        startActivity(intent);
    }

    /* ************************************
     *             ANONYMOUSLY            *
     **************************************
     */
    private void loginAnonymously() {
        mAuthProgressDialog.show();
        mFirebaseRef.authAnonymously(new AuthResultHandler("anonymous"));
    }
}

