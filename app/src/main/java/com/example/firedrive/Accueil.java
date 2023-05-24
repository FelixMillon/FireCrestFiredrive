package com.example.firedrive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Accueil extends AppCompatActivity  implements View.OnClickListener  {

    private ListView listCommande;
    private String email, idUser, nom, prenom,tel, commandeJson;

    public static ArrayList<Commande> GetCommandes() {
        return lesCommandes;
    }
    private static ArrayList<Commande> lesCommandes = new ArrayList<>();

    private static ArrayList<ListCommande> commandeList = new ArrayList<>();
    public static void setLesCommandess(ArrayList<Commande> lesCommandes) {
        Accueil.lesCommandes = lesCommandes;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.email = this.getIntent().getStringExtra("email");
        this.idUser = this.getIntent().getStringExtra("idUser");
        this.nom = this.getIntent().getStringExtra("nom");
        this.prenom = this.getIntent().getStringExtra("prenom");
        this.tel = this.getIntent().getStringExtra("tel");
        setContentView(R.layout.page_accueil);
        this.listCommande= (ListView) findViewById(R.id.idListeInfoLivraison);
        GetCommandes uneTache= new GetCommandes();
        uneTache.execute();
        ArrayList<String> lesCommandesDispo = new ArrayList<String>();
        JSONArray jsonArray = new JSONArray();
        for (Commande obj : lesCommandes) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("idCommande", obj.getIdCommande());
                jsonObject.put("idLivreur", obj.getIdLivreur());
                jsonObject.put("idClient", obj.getIdClient());
                jsonObject.put("dateheureDebut", obj.getDateheureDebut());
                jsonObject.put("dateheureFinEstimee", obj.getDateheureFinEstimee());
                jsonObject.put("dateheureFinReel", obj.getDateheureFinReel());
                jsonObject.put("numrueDepot", obj.getNumrueDepot());
                jsonObject.put("rueDepot", obj.getRueDepot());
                jsonObject.put("villeDepot", obj.getVilleDepot());
                jsonObject.put("cpDepot", obj.getCpDepot());
                jsonObject.put("total", obj.getTotal());
                jsonObject.put("numrueDest", obj.getNumrueDest());
                jsonObject.put("rueDest", obj.getRueDest());
                jsonObject.put("villeDest", obj.getVilleDest());
                jsonObject.put("cpDest", obj.getCpDest());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        commandeJson = jsonArray.toString();


        ListView listView = findViewById(R.id.idListeInfoLivraison); // Remplacez "R.id.listView" par l'ID réel de votre ListView
        MyAdapter adapter = new MyAdapter(this, commandeList); // Remplacez "this" par votre contexte approprié
        listView.setAdapter(adapter);
        ArrayAdapter unAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lesCommandesDispo);
        this.listCommande.setAdapter(unAdapter);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent unIntent = new Intent();
                switch (id) {
                    case R.id.accueil:
                        return true;
                    case R.id.livraison:
                        return true;
                    case R.id.profil:
                        unIntent = new Intent(Accueil.this, Profil.class);
                        unIntent.putExtra("email", email);
                        unIntent.putExtra("idUser", idUser);
                        unIntent.putExtra("nom", nom);
                        unIntent.putExtra("prenom", prenom);
                        unIntent.putExtra("tel", tel);
                        unIntent.putExtra("commandeJson", commandeJson);
                        Accueil.this.startActivity(unIntent);
                        return true;
                    case R.id.notification:
                        unIntent = new Intent(Accueil.this, Notif.class);
                        unIntent.putExtra("email", email);
                        unIntent.putExtra("idUser", idUser);
                        unIntent.putExtra("nom", nom);
                        unIntent.putExtra("prenom", prenom);
                        unIntent.putExtra("tel", tel);
                        unIntent.putExtra("commandeJson", commandeJson);
                        Accueil.this.startActivity(unIntent);
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {

    }
    class GetCommandes extends AsyncTask<Void, Void, ArrayList<Commande>> {

        @Override
        protected ArrayList<Commande> doInBackground(Void... Void) {
            String resultatJson= "";
            ArrayList<Commande> lesCommandesBDD = null;
            lesCommandesBDD = new ArrayList<>();
            Commande uneCommande = null;
            String url="http://"+MainActivity.GetIp()+"/bachelor/projet/AntiGaspi/firedrive/api/lesCommandes.php";
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
                    lesCommandesBDD.add(uneCommande);
                }
            }
            catch (JSONException exp){
                exp.printStackTrace();
                Log.e("Erreur 1 : ","Impossible de se parser le JSON");
            }
            return lesCommandesBDD;
        }
        @Override
        protected void onPostExecute(ArrayList<Commande> lesCommandes){
            Accueil.setLesCommandess(lesCommandes);
            commandeList = new ArrayList<>();
            for(Commande uneCommande : lesCommandes)
            {
                commandeList.add(new ListCommande(uneCommande.getIdCommande(),"Magasin: "+uneCommande.getNumrueDepot()+" "
                        +uneCommande.getRueDepot()+", "
                        +uneCommande.getCpDepot()+" "
                        +uneCommande.getVilleDepot()+"\n"
                        +"Client: "+uneCommande.getNumrueDest()+" "
                        +uneCommande.getRueDest()+", "
                        +uneCommande.getCpDest()+" "
                        +uneCommande.getVilleDest(),2)
                );
            }

            ListView listView = findViewById(R.id.idListeInfoLivraison);
            MyAdapter adapter = new MyAdapter(Accueil.this, commandeList);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ListCommande selectedItem = adapter.getItem(position);
                    if (selectedItem != null) {
                        int itemId = selectedItem.getIdCommande();
                        String itemName = selectedItem.getChaineCommande();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Accueil.this);
                        builder.setTitle("Confirmation");
                        builder.setMessage("Voulez-vous prendre en charge la commande : " + itemName + "?");

                        AlertDialog dialog = builder.create();
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Action lorsque l'utilisateur clique sur le bouton "Oui"
                                // Vous pouvez ajouter votre logique ici
                                Log.e("OUI : ","OUI");
                                String url = "http://"+MainActivity.GetIp()+"/bachelor/projet/AntiGaspi/firedrive/api/Affectation_livraison.php?id_livreur="+idUser+"&id_commande="+itemId; // Remplacez cette URL par l'URL réelle que vous souhaitez contacter
                                ApiConnector.fetchDataFromURL(url, new ApiConnector.OnDataFetchedListener() {
                                    @Override
                                    public void onDataFetched(String data) {
                                        // Traitez le résultat ici
                                        Log.e("JSON : ", data);
                                        try {
                                            JSONArray tabJson = new JSONArray(data);
                                            for(int i = 0; i<tabJson.length();i++){
                                                JSONObject unObjet = tabJson.getJSONObject(i);
                                                Log.e("objet", unObjet.getString("ok"));
                                                if(!unObjet.getString("ok").equals("1")){
                                                    Toast.makeText(Accueil.this, "Une erreur s'est produite lors de l'enregistrement", Toast.LENGTH_SHORT).show();
                                                }else{
                                                    GetCommandes uneTache= new GetCommandes();
                                                    uneTache.execute();
                                                }
                                                dialog.dismiss();
                                            }
                                        }catch (JSONException exp){
                                            Log.e("Erreur enregistrement : ", "Une erreur s'est produite lors de l'enregistrement ");
                                            Toast.makeText(Accueil.this, "Une erreur s'est produite lors de l'enregistrement", Toast.LENGTH_SHORT).show();
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
                                Log.e("NON : ","NON");
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
            });
            listView.setAdapter(adapter);

        }
    }
}