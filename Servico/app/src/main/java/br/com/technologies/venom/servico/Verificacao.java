package br.com.technologies.venom.servico;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.Collator;
import java.util.Comparator;
import java.util.LinkedList;

public class Verificacao extends AsyncTask<LinkedList<String>, Void, LinkedList<String>> {
    Context context;
    LinkedList<String> emailsLinkedList = new LinkedList<>();
    LinkedList<String> emailsOrdenadosLinkedList = new LinkedList<>();
    LinkedList<String> emailsSanitizados = new LinkedList<>();

    Verificacao(Context context){
        this.context = context;
    }

    public void setEmailsLinkedList(LinkedList<String> emailsLinkedList) {
        this.emailsLinkedList = emailsLinkedList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected LinkedList<String> doInBackground(LinkedList<String>... linkedLists) {
        setEmailsLinkedList(linkedLists[0]);
        ordenarLinkedList();
        removerDuplicados();
        imprimir();
        return this.emailsSanitizados;
    }

    @Override
    protected void onPostExecute(LinkedList<String> listaSanitizada) {
        super.onPostExecute(listaSanitizada);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void ordenarLinkedList(){
        this.emailsOrdenadosLinkedList.addAll(emailsLinkedList);
        this.emailsOrdenadosLinkedList.sort( new Comparator<String>(){
            @Override
            public int compare(String o1,String o2){
                return Collator.getInstance().compare(o1,o2);
            }
        });
    }

    public void imprimir(){
        System.out.println("Lista Original");
        for (String email : emailsLinkedList) {
            System.out.println(email);
        }
        System.out.println("**************");
        System.out.println("Lista Ordenada");
        for (String email : emailsOrdenadosLinkedList) {
            System.out.println(email);
        }
        System.out.println("**************");
        System.out.println("Lista Sanitizada");
        for (String email : emailsSanitizados) {
            System.out.println(email);
        }
    }

    public void removerDuplicados() {
        int cont = 0;
        for (String email : emailsOrdenadosLinkedList) {
            if (emailsSanitizados.size() == 0) {
                emailsSanitizados.add(email);
                cont++;
            }else if (cont < emailsOrdenadosLinkedList.size()){
                if (!emailsSanitizados.get(cont-1).equals(email)) {
                    emailsSanitizados.add(email);
                    cont++;
                }
            }
        }
    }
}
