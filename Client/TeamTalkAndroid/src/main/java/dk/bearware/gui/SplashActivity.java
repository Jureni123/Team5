package dk.bearware.gui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SplashActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mAuth = FirebaseAuth.getInstance();
        
        // Cek apakah user sudah login sebelumnya
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Sudah login -> Langsung ke MainActivity
            startMainActivity();
            return;
        }

        // Belum login -> Tampilkan layar login
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.progress_bar);
        
        // Konfigurasi Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) 
                .requestEmail()
                .build();
                
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.GONE);
        
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (Exception e) {
                // Handle error login (bisa tambah Toast nanti)
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Login BERHASIL!
                    startMainActivity();
                } else {
                    // Login GAGAL
                    progressBar.setVisibility(View.GONE);
                }
            });
    }

    private void startMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish(); // Tutup SplashActivity biar user nggak bisa back ke sini
    }
}
