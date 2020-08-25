package br.com.technologies.venom.mailclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnIncluir, btnVerificar, btnLimpar, btnEnviarEmail;
    private TextInputLayout layEmail, layListaEmails;
    private TextInputEditText edtEmail, edtListaEmails;
    private CustomLinkedList emailsLinkedList;

    private ProgressBar pbServico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnIncluir = findViewById(R.id.btnIncluir);
        btnLimpar = findViewById(R.id.btnLimpar);
        btnVerificar = findViewById(R.id.btnVerificar);
        btnEnviarEmail = findViewById(R.id.btnEnviarEmail);
        edtEmail = findViewById(R.id.edtEmail);
        edtListaEmails = findViewById(R.id.edtListaEmails);
        layEmail = findViewById(R.id.layEmail);
        layListaEmails = findViewById(R.id.layListaEmails);
        pbServico = findViewById(R.id.pbServico);

        btnVerificar.setOnClickListener(this);
        btnLimpar.setOnClickListener(this);
        btnIncluir.setOnClickListener(this);
        btnEnviarEmail.setOnClickListener(this);

        emailsLinkedList = new CustomLinkedList();
    }

    private boolean validarEmail(final String email) {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnVerificar:
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(
                        "br.com.technologies.venom.servico",
                        "br.com.technologies.venom.servico.PendingIntentService")
                );
                PendingIntent pendingResult = createPendingResult(100, new Intent(), 0);
                intent.putExtra("pendingIntent", pendingResult);
                intent.putExtra("emailsLinkedList", emailsLinkedList.getList().toArray(new String[0]));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    pbServico.setVisibility(View.VISIBLE);
                    startForegroundService(intent);
                } else
                    startService(intent);
                break;
            case R.id.btnIncluir:
                if (validarEmail(edtEmail.getText().toString())) {
                    edtListaEmails.setText(edtListaEmails.getText().toString() + edtEmail.getText().toString() + "\n");
                    emailsLinkedList.add(edtEmail.getText().toString());
                    edtEmail.setText("");
                    layEmail.setError(null);
                } else {
                    Toast.makeText(this, edtEmail.getText().toString() + " não parece ser um e-mail válido!", Toast.LENGTH_SHORT).show();
                    layEmail.setError("Informe um e-mail válido!");
                }
                break;
            case R.id.btnLimpar:
                edtListaEmails.setText("");
                emailsLinkedList = new CustomLinkedList();
                btnEnviarEmail.setVisibility(View.GONE);
                break;
            case R.id.btnEnviarEmail:
                enviarEmail(emailsLinkedList.getList().toArray(new String[0]), "Teste de envio", null);
        }
    }

    public void enviarEmail(String[] endereco, String assunto, Uri anexo) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, endereco);
        intent.putExtra(Intent.EXTRA_SUBJECT, assunto);
        intent.putExtra(Intent.EXTRA_STREAM, anexo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == 200) {
            Toast.makeText(this, "O serviço respondeu", Toast.LENGTH_LONG).show();
            String[] emails = data.getStringArrayExtra("emails");
            emailsLinkedList = new CustomLinkedList();
            for (String email : emails)
                emailsLinkedList.add(email);
            edtListaEmails.setText("");
            for (String email : emailsLinkedList.getList())
                edtListaEmails.setText(edtListaEmails.getText().toString() + email + "\n");

            pbServico.setVisibility(View.GONE);
            btnEnviarEmail.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}