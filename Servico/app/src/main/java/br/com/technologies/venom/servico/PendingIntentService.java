package br.com.technologies.venom.servico;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PendingIntentService extends Service {

    private PendingIntent data;
    private LinkedList<String> emailsLinkedList, emailsSanitizadosLinkedList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        data = intent.getParcelableExtra("pendingIntent");

        emailsLinkedList = new LinkedList<>();
        emailsSanitizadosLinkedList = new LinkedList<>();

        for(String email : (List<String>) intent.getSerializableExtra("emailsLinkedList")){
            emailsLinkedList.add(email);
        }

        Verificacao verificacao = (Verificacao) new Verificacao(getApplicationContext()).execute(emailsLinkedList);

        try {
            emailsSanitizadosLinkedList.addAll(verificacao.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new RespostaThread().start();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class RespostaThread extends Thread {
        @Override
        public void run() {
            if (!isInterrupted()) {


                Intent result = new Intent();
                result.putExtra("emails", emailsSanitizadosLinkedList);
                try {
                    data.send(PendingIntentService.this,200,result);
                } catch (PendingIntent.CanceledException e) {

                    e.printStackTrace();
                }
            }
        }
    }
}
