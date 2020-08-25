package br.com.technologies.venom.servico;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class PendingIntentService extends Service {
    private PendingIntent data;
    private CustomLinkedList emailsCustomLinkedList;
    private String[] emailsList;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent.hasExtra("pendingIntent"))
            data = intent.getParcelableExtra("pendingIntent");


        if (intent.hasExtra("emailsLinkedList")) {
            emailsList = intent.getStringArrayExtra("emailsLinkedList");

            if (emailsList != null) {
                emailsCustomLinkedList = new CustomLinkedList();
                for (String email : emailsList)
                    emailsCustomLinkedList.add(email);

                new RespostaThread().start();
            }
        }

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
                try {
                    emailsCustomLinkedList.print();
                    emailsCustomLinkedList.removeDuplicates();
                    emailsCustomLinkedList.print();
                    Intent result = new Intent();
                    result.putExtra("emails", emailsCustomLinkedList.getList().toArray(new String[0]));
                    data.send(PendingIntentService.this, 200, result);
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
