package com.example.firedrive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class Profil extends AppCompatActivity implements View.OnClickListener  {

    private ListView listCommande;
    private Button btEdit;
    private String email, idUser, nom, prenom,tel, commandeJson, strCommande;
    private TextView txtEmail, txtNom, txtPrenom, txtTel;

    public static ArrayList<Commande> GetMesCommandes() {
        return mesCommandes;
    }

    private static ArrayList<ListCommande> mesCommandeList = new ArrayList<>();
    private static ArrayList<Commande> mesCommandes = new ArrayList<>();
    public static void setMesCommandes(ArrayList<Commande> mesCommandes) {
        Profil.mesCommandes = mesCommandes;
    }
    ArrayList<String> toutesMesCommandes = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.email = this.getIntent().getStringExtra("email");
        this.idUser = this.getIntent().getStringExtra("idUser");
        this.nom = this.getIntent().getStringExtra("nom");
        this.prenom = this.getIntent().getStringExtra("prenom");
        this.tel = this.getIntent().getStringExtra("tel");
        this.commandeJson = this.getIntent().getStringExtra("commandeJson");
        setContentView(R.layout.page_profil);
        this.btEdit = (Button) findViewById(R.id.idEdit);
        this.btEdit.setOnClickListener(this);
        this.listCommande= (ListView) findViewById(R.id.idListeLivraisons);
        this.txtNom=(TextView) findViewById(R.id.idNom);
        this.txtPrenom=(TextView) findViewById(R.id.idPrenom);
        this.txtEmail=(TextView) findViewById(R.id.idEmail);
        this.txtTel=(TextView) findViewById(R.id.idMdp);
        Utilisateur unUser = new Utilisateur(Integer.parseInt(idUser));
        this.txtNom.setText(nom);
        this.txtPrenom.setText(prenom);
        this.txtEmail.setText(email);
        this.txtTel.setText(tel);
        Profil.GetMesCommandes uneRecupCommande = new com.example.firedrive.Profil.GetMesCommandes();
        uneRecupCommande.execute(unUser);
        for(Commande uneCommande : mesCommandes)
        {
            if(uneCommande.getDateheureFinReel() == null){
                strCommande= "En cours: \nMagasin: "+uneCommande.getNumrueDepot()+" "
                        +uneCommande.getRueDepot()+", "
                        +uneCommande.getCpDepot()+" "
                        +uneCommande.getVilleDepot()+"\n"
                        +"Client: "+uneCommande.getNumrueDest()+" "
                        +uneCommande.getRueDest()+", "
                        +uneCommande.getCpDest()+" "
                        +uneCommande.getVilleDest();
            }else{
                strCommande= "Terminé le "+uneCommande.getDateheureFinReel()+": \nMagasin: "+uneCommande.getNumrueDepot()+" "
                        +uneCommande.getRueDepot()+", "
                        +uneCommande.getCpDepot()+" "
                        +uneCommande.getVilleDepot()+"\n"
                        +"Client: "+uneCommande.getNumrueDest()+" "
                        +uneCommande.getRueDest()+", "
                        +uneCommande.getCpDest()+" "
                        +uneCommande.getVilleDest();
            }
            toutesMesCommandes.add(strCommande);
        }
        ListView listView = findViewById(R.id.idListeLivraisons); // Remplacez "R.id.listView" par l'ID réel de votre ListView
        MyAdapter adapter = new MyAdapter(this, mesCommandeList); // Remplacez "this" par votre contexte approprié
        listView.setAdapter(adapter);
        ArrayAdapter unAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, toutesMesCommandes);
        this.listCommande.setAdapter(unAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.accueil:
                        Toast.makeText(Profil.this, "Bouton Accueil cliqué", Toast.LENGTH_SHORT).show();
                        Intent unIntent = new Intent(Profil.this, Accueil.class);
                        unIntent.putExtra("email", email);
                        unIntent.putExtra("idUser", idUser);
                        unIntent.putExtra("nom", nom);
                        unIntent.putExtra("prenom", prenom);
                        unIntent.putExtra("tel", tel);
                        unIntent.putExtra("commandeJson", commandeJson);
                        Profil.this.startActivity(unIntent);
                        return true;
                    case R.id.livraison:
                        return true;
                    case R.id.profil:
                        return true;
                    case R.id.notification:
                        unIntent = new Intent(Profil.this, Notif.class);
                        unIntent.putExtra("email", email);
                        unIntent.putExtra("idUser", idUser);
                        unIntent.putExtra("nom", nom);
                        unIntent.putExtra("prenom", prenom);
                        unIntent.putExtra("tel", tel);
                        unIntent.putExtra("commandeJson", commandeJson);
                        Profil.this.startActivity(unIntent);
                        return true;
                }
                return false;
            }
        });
        //rendre bouton cliquable
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.idEdit){
            Intent unIntent = new Intent(this, EditProfil.class);
            unIntent.putExtra("email", email);
            unIntent.putExtra("idUser", idUser);
            unIntent.putExtra("nom", nom);
            unIntent.putExtra("prenom", prenom);
            unIntent.putExtra("tel", tel);
            unIntent.putExtra("commandeJson", commandeJson);
            this.startActivity(unIntent);
        }
    }
    class GetMesCommandes extends AsyncTask<Utilisateur, Void, ArrayList<Commande>> {

        @Override
        protected ArrayList<Commande> doInBackground(Utilisateur... utilisateurs) {
            String resultatJson= "";
            Utilisateur leLivreur = utilisateurs[0];
            ArrayList<Commande> mesCommandesBDD = new ArrayList<>();
            Commande uneCommande = null;
            String url="http://"+MainActivity.GetIp()+"/bachelor/projet/AntiGaspi/firedrive/api/lesHistoCommandes.php?id_livreur="+leLivreur.getIdLivreur();
            try{
                URL uneUrl = new URL(url);
                HttpURLConnection uneConnexion = (HttpURLConnection)uneUrl.openConnection();
                uneConnexion.setRequestMethod("GET");
                uneConnexion.setDoInput(true);
                uneConnexion.setDoOutput(true);
                //on fixe le timeout
                uneConnexion.setConnectTimeout(20000);
                uneConnexion.connect();

                //lecture des données json et les mettres dans une chaine
                InputStreamReader isr = new InputStreamReader(uneConnexion.getInputStream(),"UTF-8");
                //utiliser un buffer de lecture dans ce fichier
                BufferedReader br = new BufferedReader(isr);
                //Lire dans le buffer ligne par ligne
                StringBuilder sb = new StringBuilder();
                String ligne = "";
                //boucle de lecture des lignes et ajout dans le string builder
                while((ligne=br.readLine())!=null){
                    sb.append(ligne);
                }
                //récupérer le json dans une chaine résultat
                resultatJson = sb.toString();
                Log.e("JSON : ","avantjson");
                Log.e("JSON : ",resultatJson);
                Log.e("JSON : ","apresjson");
                br.close();
                isr.close();
                uneConnexion.disconnect();
            }
            catch (Exception exp){
                exp.printStackTrace();
                Log.e("Erreur 1 : ","Impossible de se connecter");
            }
            try{
                JSONArray tabJson = new JSONArray(resultatJson);
                JSONObject unObjetJson = tabJson.getJSONObject(0);
                for(int i = 0; i<tabJson.length();i++){
                    JSONObject unObjet = tabJson.getJSONObject(i);
                    int id_livreur = unObjet.isNull("id_livreur") ? 0 : unObjet.getInt("id_livreur");
                    uneCommande = new Commande(
                            unObjet.getInt("id_commande"),
                            id_livreur,
                            unObjet.getInt("id_client"),
                            unObjet.getString("dateheure_debut"),
                            unObjet.getString("dateheure_fin_reel"),
                            unObjet.getString("dateheure_fin_estimee"),
                            unObjet.getString("numrue_depot"),
                            unObjet.getString("rue_depot"),
                            unObjet.getString("ville_depot"),
                            unObjet.getString("cp_depot"),
                            Float.parseFloat(unObjet.getString("poids_total")),
                            unObjet.getString("numrue_dest"),
                            unObjet.getString("rue_dest"),
                            unObjet.getString("ville_dest"),
                            unObjet.getString("cp_dest")
                    );
                    Log.e("id commande : ",uneCommande.getIdCommande()+"");
                    Log.e("id client : ",uneCommande.getIdClient()+"");
                    Log.e("poidstotal : ",uneCommande.getTotal()+"");
                    mesCommandesBDD.add(uneCommande);
                }
            }
            catch (JSONException exp){
                exp.printStackTrace();
                Log.e("Erreur 1 : ","Impossible de se parser le JSON");
            }
            return mesCommandesBDD;
        }
        @Override
        protected void onPostExecute(ArrayList<Commande> mesCommandes){
            Profil.setMesCommandes(mesCommandes);
            mesCommandeList = new ArrayList<>();
            ArrayList<String> toutesMesCommandes = new ArrayList<String>();
            for(Commande uneCommande : mesCommandes)
            {
                if(uneCommande.getDateheureFinReel() == "null"){
                    mesCommandeList.add(
                            new ListCommande(
                                    uneCommande.getIdCommande(),
                                    "Magasin: "
                                            +uneCommande.getNumrueDepot()
                                            +" "
                                            +uneCommande.getRueDepot()+", "
                                            +uneCommande.getCpDepot()+" "
                                            +uneCommande.getVilleDepot()+"\n"
                                            +"Client: "+uneCommande.getNumrueDest()+" "
                                            +uneCommande.getRueDest()+", "
                                            +uneCommande.getCpDest()+" "
                                            +uneCommande.getVilleDest(),
                                    0
                            )
                    );
                }else{
                    mesCommandeList.add(
                            new ListCommande(
                                    uneCommande.getIdCommande(),
                                    "Magasin: "
                                            +uneCommande.getNumrueDepot()
                                            +" "
                                            +uneCommande.getRueDepot()+", "
                                            +uneCommande.getCpDepot()+" "
                                            +uneCommande.getVilleDepot()+"\n"
                                            +"Client: "+uneCommande.getNumrueDest()+" "
                                            +uneCommande.getRueDest()+", "
                                            +uneCommande.getCpDest()+" "
                                            +uneCommande.getVilleDest(),
                                    1
                            )
                    );
                }
            }
            ListView listView = findViewById(R.id.idListeLivraisons);
            MyAdapter adapter = new MyAdapter(Profil.this, mesCommandeList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListCommande selectedItem = adapter.getItem(position);
                    if (selectedItem != null) {
                        int itemId = selectedItem.getIdCommande();
                        String itemName = selectedItem.getChaineCommande();
                        int finished = selectedItem.getFinished();
                        if(finished == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Profil.this);
                            builder.setTitle("Confirmation");
                            builder.setMessage("Voulez-vous terminer la commande suivante : " + itemName + "?");

                            AlertDialog dialog = builder.create();
                            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Action lorsque l'utilisateur clique sur le bouton "Oui"
                                    // Vous pouvez ajouter votre logique ici
                                    Log.e("OUI : ", "OUI");
                                    String url = "http://" + MainActivity.GetIp() + "/bachelor/projet/AntiGaspi/firedrive/api/Terminer_livraison.php?id_commande=" + itemId;
                                    ApiConnector.fetchDataFromURL(url, new ApiConnector.OnDataFetchedListener() {
                                        @Override
                                        public void onDataFetched(String data) {
                                            // Traitez le résultat ici
                                            Log.e("JSON : ", data);
                                            try {
                                                JSONArray tabJson = new JSONArray(data);
                                                for (int i = 0; i < tabJson.length(); i++) {
                                                    JSONObject unObjet = tabJson.getJSONObject(i);
                                                    Log.e("objet", unObjet.getString("ok"));
                                                    if (!unObjet.getString("ok").equals("1")) {
                                                        Toast.makeText(Profil.this, "Une erreur s'est produite lors de la cloture", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Utilisateur unUser = new Utilisateur(Integer.parseInt(idUser));
                                                        Profil.GetMesCommandes uneRecupCommande = new com.example.firedrive.Profil.GetMesCommandes();
                                                        uneRecupCommande.execute(unUser);
                                                    }
                                                    dialog.dismiss();
                                                }
                                            } catch (JSONException exp) {
                                                Log.e("Erreur enregistrement : ", "Une erreur s'est produite lors de la cloture");
                                                Toast.makeText(Profil.this, "Une erreur s'est produite lors de la cloture", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Action lorsque l'utilisateur clique sur le bouton "Non"
                                    // Vous pouvez ajouter votre logique ici
                                    Log.e("NON : ", "NON");
                                }
                            });

                            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialogInterface) {
                                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

                                    // Changer la couleur du texte du bouton "Oui" en noir
                                    SpannableString spannableString = new SpannableString("Oui");
                                    spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
                                    positiveButton.setText(spannableString);
                                    Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                                    // Changer la couleur du texte du bouton "NON" en noir
                                    SpannableString spannableString2 = new SpannableString("Non");
                                    spannableString2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
                                    negativeButton.setText(spannableString2);
                                }
                            });

                            dialog.show();
                        }
                    }
                }
            });
            listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
            listView.setDividerHeight(24);

            listView.setAdapter(adapter);
        }
    }
}