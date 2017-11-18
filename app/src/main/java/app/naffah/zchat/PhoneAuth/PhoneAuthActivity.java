package app.naffah.zchat.PhoneAuth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import app.naffah.zchat.R;

public class PhoneAuthActivity extends AppCompatActivity {

    private static final String TAG = "PhoneLogin";
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacksVerification;
    private FirebaseAuth mAuth;

    private TextView textViewFirst, textViewSecond;
    private EditText editTextFirst, editTextSecond;
    private Button buttonFirst, buttonSecond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number_auth);

        editTextFirst = (EditText) findViewById(R.id.phoneNumberEditText);
        buttonFirst = (Button) findViewById(R.id.sendCodeBtn);
        textViewFirst = (TextView)findViewById(R.id.textViewOne);
        editTextSecond = (EditText) findViewById(R.id.enterCodeEditText);
        buttonSecond = (Button)findViewById(R.id.codeVerifyBtn);
        textViewSecond = (TextView)findViewById(R.id.textViewSecond);
        mAuth = FirebaseAuth.getInstance();
        mCallbacksVerification = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // Log.d(TAG, "onVerificationCompleted:" + credential);
                mVerificationInProgress = false;
                Toast.makeText(PhoneAuthActivity.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(PhoneAuthActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneAuthActivity.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(PhoneAuthActivity.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                editTextFirst.setVisibility(View.GONE);
                buttonFirst.setVisibility(View.GONE);
                textViewFirst.setVisibility(View.GONE);
                textViewSecond.setVisibility(View.VISIBLE);
                editTextSecond.setVisibility(View.VISIBLE);
                buttonSecond.setVisibility(View.VISIBLE);
                // ...
            }
        };

        buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        editTextFirst.getText().toString(),
                        60,
                        java.util.concurrent.TimeUnit.SECONDS,
                        PhoneAuthActivity.this,
                        mCallbacksVerification);
            }
        });

        buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, editTextSecond.getText().toString());
                // [END verify_with_code]
                signInWithPhoneAuthCredential(credential);
            }
        });


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "signInWithCredential:success");
                            startActivity(new Intent(PhoneAuthActivity.this, PhoneAuthToTabView.class));
                            Toast.makeText(PhoneAuthActivity.this, "Verification Done", Toast.LENGTH_SHORT).show();
                            finish();
                            // ...
                        } else {
                            // Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(PhoneAuthActivity.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}
