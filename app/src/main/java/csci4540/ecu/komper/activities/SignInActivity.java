package csci4540.ecu.komper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import csci4540.ecu.komper.R;

public class SignInActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    // Instance variables
    private SignInButton mSignInButton;

    // Class variables
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartGoogleSignIn();
            }
        });

    }

    private void StartGoogleSignIn() {
        // Request the User's ID and basic profile info with DEFAULT_SIGN_IN option
        // Also request the email of the user,
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // TODO: Thoroughly understand the details of the OAuth 2.0 framework.
                .requestIdToken(getString(R.string.OAuthClientID))
                .requestEmail()
                .build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                // NOTE: Manages the client connection automatically, connecting the client
                // NOTE: in the onStart() method and disconnecting the client in the onStop() method
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (signInResult.isSuccess()) {
                GoogleSignInAccount account = signInResult.getSignInAccount();
                Toast.makeText(this, "Welcome " + account.getDisplayName() + "!", Toast.LENGTH_SHORT).show();
                Log.i(TAG, account.getDisplayName() + " logged in successfully!");
                Log.i(TAG, "ID Token: " + account.getIdToken());

                /* TODO:
                 * Pass the IdToken from account.getDisplayName() to the backend to check
                 * if the user is already in our DB or not. If they are not in the database
                 * send them to the page where they can configure their account (i.e. set up
                 * their username and other user configurations). If they are in the database,
                 * send them to the page where they select from a predefined collection of
                 * grocery lists (or add a new one) and select the stores to search for the best
                 * price of the given grocery list.
                 */
            } else {
                Toast.makeText(this, "Sign In failed!", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to sign in to Google Account!");
                Log.e(TAG, signInResult.getStatus().getStatusMessage());
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "GoogleApiClient failed to connect properly.");
        Log.e(TAG, connectionResult.getErrorMessage());
    }
}
