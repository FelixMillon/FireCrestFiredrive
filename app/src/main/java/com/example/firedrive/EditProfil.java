package com.example.firedrive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class EditProfil extends AppCompatActivity implements View.OnClickListener  {



    private Button btValider, btAnnuler;
    private EditText txtEmail, txtMdp, txtNom, txtPrenom, txtTel;
    private String email, idUser, nom, prenom,tel, commandeJson;
    private String newEmail, newNom, newPrenom,newTel,newMdp;
    private static Utilisateur userModif = null;
    public static String result;
    public static Utilisateur getuserModif() {
        return userModif;
    }

    public static void setuserModif(Utilisateur userModif) {
        EditProfil.userModif = userModif;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.email = this.getIntent().getStringExtra("email");
        this.idUser = this.getIntent().getStringExtra("idUser");
        this.nom = this.getIntent().getStringExtra("nom");
        this.prenom = this.getIntent().getStringExtra("prenom");
        this.tel = this.getIntent().getStringExtra("tel");
        this.commandeJson = this.getIntent().getStringExtra("commandeJson");
        setContentView(R.layout.page_edit_profil);
        this.btValider = (Button) findViewById(R.id.idValider);
        this.btAnnuler = (Button) findViewById(R.id.idAnnuler);
        this.txtNom = (EditText) findViewById(R.id.idNom);
        this.txtPrenom = (EditText) findViewById(R.id.idPrenom);
        this.txtTel = (EditText) findViewById(R.id.idTelephone);
        this.txtEmail = (EditText) findViewById(R.id.idEmail);
        this.txtMdp= (EditText) findViewById(R.id.idMdp);
        //rendre bouton cliquable
        this.btAnnuler.setOnClickListener(this);
        this.btValider.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.idValider){
            this.newNom = this.txtNom.getText().toString();
            this.newPrenom = this.txtPrenom.getText().toString();
            this.newTel = this.txtTel.getText().toString();
            this.newEmail = this.txtEmail.getText().toString();
            this.newMdp = this.txtMdp.getText().toString();
            Log.e("mdp : ",this.newMdp);
            if(!this.newMdp.isEmpty())
            {
                try {
                    // Création d'une instance de l'algorithme SHA-256
                    MessageDigest digest = MessageDigest.getInstance("SHA-256");

                    // Conversion de la variable 'toto' en tableau de bytes
                    byte[] encodedHash = digest.digest(this.newMdp.getBytes(StandardCharsets.UTF_8));

                    // Conversion du tableau de bytes en une représentation hexadécimale
                    StringBuilder hexString = new StringBuilder();
                    for (byte b : encodedHash) {
                        String hex = Integer.toHexString(0xff & b);
                        if (hex.length() == 1) {
                            hexString.append('0');
                        }
                        hexString.append(hex);
                        this.newMdp = hexString.toString();
                        Log.e("hash : ",this.newMdp);
                    }

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            Utilisateur unUser = new Utilisateur(Integer.parseInt(idUser),newEmail,newMdp,newNom,newPrenom,newTel);
            EditProfil.EditMonProfil uneEdit = new EditProfil.EditMonProfil();
            uneEdit.execute(unUser);
        }
        if(view.getId() == R.id.idValider){
            this.txtNom.setText("");
            this.txtEmail.setText("");
            this.txtPrenom.setText("");
            this.txtMdp.setText("");
            this.txtTel.setText("");
        }
        if(view.getId() == R.id.idAnnuler){
            Toast.makeText(EditProfil.this, "Modification annulée.", Toast.LENGTH_SHORT).show();
            Intent unIntent = new Intent(EditProfil.this, Profil.class);
            unIntent.putExtra("email", email);
            unIntent.putExtra("idUser", idUser);
            unIntent.putExtra("nom", nom);
            unIntent.putExtra("prenom", prenom);
            unIntent.putExtra("tel", tel);
            unIntent.putExtra("commandeJson", commandeJson);
            EditProfil.this.startActivity(unIntent);
        }
    }

    /* La connexion à la base de données via une tâche asynchrone */
    class EditMonProfil extends AsyncTask<Utilisateur, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Utilisateur... utilisateurs) {
            String resultatJson= "";
            Utilisateur unUser = utilisateurs[0];
            Utilisateur unUser2 = null;
            String idLivreur = String.valueOf(unUser.getIdLivreur());
            String theNewEmail = unUser.getEmail();
            String theNewMdp = unUser.getMdp();
            String theNewNom = unUser.getNom();
            String theNewPrenom = unUser.getPrenom();
            String theNewTel = unUser.getTel();

            if (theNewEmail.isEmpty()) {
                theNewEmail = "null";
            }
            if (theNewMdp.isEmpty()) {
                theNewMdp = "null";
            }
            if (theNewNom.isEmpty()) {
                theNewNom = "null";
            }
            if (theNewPrenom.isEmpty()) {
                theNewPrenom = "null";
            }
            if (theNewTel.isEmpty()) {
                theNewTel = "null";
            }

            String url = "http://"+MainActivity.GetIp()+"/bachelor/projet/AntiGaspi/firedrive/api/update_livreur.php?id_livreur=" + idLivreur + "&email=" + theNewEmail + "&mdp=" + theNewMdp + "&nom=" + theNewNom + "&prenom=" + theNewPrenom + "&tel=" + theNewTel;
            Log.e("URL : ",url);
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
                Log.e("JSON : ",resultatJson);
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
                Log.e("Je suis passé : ","par ici");
                unUser2 = new Utilisateur(
                        unObjetJson.getInt("id_livreur"),
                        unObjetJson.getString("email"),
                        unObjetJson.getString("mdp"),
                        unObjetJson.getString("nom"),
                        unObjetJson.getString("prenom"),
                        unObjetJson.getString("tel"),
                        0,
                        5
                );
                Log.e("id_livreur : ",unUser2.getIdLivreur()+"");
                Log.e("nom : ",unUser2.getNom());
                Log.e("prenom : ",unUser2.getPrenom());
                Log.e("email : ",unUser2.getEmail());
                Log.e("mdp : ",unUser2.getMdp());
                Log.e("tel : ",unUser2.getTel());
            }
            catch (JSONException exp){
                exp.printStackTrace();
                Log.e("Erreur 1 : ","Impossible de se parser le JSON");
            }
            return unUser2;
        }

        @Override
        protected void onPostExecute(Utilisateur unUtilisateur) {
            EditProfil.setuserModif(unUtilisateur);
            if (unUtilisateur == null) {
                Toast.makeText(EditProfil.this, "Il y a eu une erreur lors de la modification", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditProfil.this, "Modification effectuée.", Toast.LENGTH_SHORT).show();
                Intent unIntent = new Intent(EditProfil.this, Profil.class);
                unIntent.putExtra("email", userModif.getEmail());
                unIntent.putExtra("idUser", idUser);
                unIntent.putExtra("nom", userModif.getNom());
                unIntent.putExtra("prenom", userModif.getPrenom());
                unIntent.putExtra("tel", userModif.getTel());
                unIntent.putExtra("commandeJson", commandeJson);
                EditProfil.this.startActivity(unIntent);
            }
        }
    }
}