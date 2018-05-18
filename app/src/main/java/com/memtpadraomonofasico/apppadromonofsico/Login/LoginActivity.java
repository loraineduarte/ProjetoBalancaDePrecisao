package com.memtpadraomonofasico.apppadromonofsico.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.BancoController;
import com.memtpadraomonofasico.apppadromonofsico.BancoDeDados.CriaBanco;
import com.memtpadraomonofasico.apppadromonofsico.DashboardActivity;
import com.memtpadraomonofasico.apppadromonofsico.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private final CriaBanco banco = new CriaBanco(this);
    @InjectView(R.id.input_email)
    private EditText _emailText;
    @InjectView(R.id.input_password)
    private EditText _passwordText;
    @InjectView(R.id.btn_login)
    private Button _loginButton;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        NoEncryption encryption = new NoEncryption();
        Hawk.init(this).setEncryption(encryption).build();

        ButterKnife.inject(this);

        final BancoController crud = new BancoController(getBaseContext());
        _emailText = findViewById(R.id.input_email);
        _passwordText = findViewById(R.id.input_password);


        _loginButton = findViewById(R.id.btn_login);
        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

    }

    private void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();

        BancoController crud = new BancoController(getBaseContext());
        Cursor cursor = crud.validaLogin(email, password);
        if (cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "Usuário e/ou senha incorretos. ", Toast.LENGTH_LONG).show();

        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this, R.drawable.circular_progress_bar);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Autenticando...");
            progressDialog.show();

            // TODO: Implement your own authentication logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess(email, password);
                            // onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 3000);
        }


    }


    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    private void onLoginSuccess(String email, String senha) {
        _loginButton.setEnabled(true);
        Hawk.put("usuario", email );
        Hawk.put("senha",senha );
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login falhou, favor verificar os dados ou contate o administrador. ", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    private boolean validate() {
        boolean valid = true;


        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        if (email.isEmpty()) {
            _emailText.setError("Coloque um número de matrícula válido");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("Senha incorreta");
            valid = false;
        } else {
            _passwordText.setError(null);
        }


        return valid;
    }
}

