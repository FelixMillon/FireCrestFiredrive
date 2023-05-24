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
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btConnexion, btInscription;

    private static String IP="10.0.221.8";
    public static String GetIp()
    {
        return IP;
    }
    private EditText txtEmail, txtMdp;
    private static Utilisateur userConnecte = null;

    public static Utilisateur getUserConnecte() {
        return userConnecte;
    }

    public static void setUserConnecte(Utilisateur userConnecte) {
        MainActivity.userConnecte = userConnecte;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btConnexion = (Button) findViewById(R.id.idSeConnecter);
        this.btInscription = (Button) findViewById(R.id.idInscription);
        this.txtEmail = (EditText) findViewById(R.id.idEmail);
        this.txtMdp = (EditText) findViewById(R.id.idMdp);

        // rendre bouton cliquable
        this.btConnexion.setOnClickListener(this);
        this.btInscription.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.idSeConnecter) {
            String email = this.txtEmail.getText().toString();
            String mdp = this.txtMdp.getText().toString();
            try {
                // Création d'une instance de l'algorithme SHA-256
                MessageDigest digest = MessageDigest.getInstance("SHA-256");

                // Conversion de la variable 'toto' en tableau de bytes
                byte[] encodedHash = digest.digest(mdp.getBytes(StandardCharsets.UTF_8));

                // Conversion du tableau de bytes en une représentation hexadécimale
                StringBuilder hexString = new StringBuilder();
                for (byte b : encodedHash) {
                    String hex = Integer.toHexString(0xff & b);
                    if (hex.length() == 1) {
                        hexString.append('0');
                    }
                    hexString.append(hex);
                    mdp = hexString.toString();
                    Log.e("hash : ", mdp);
                }

                // Affichage du hachage SHA-256
                System.out.println("Hachage SHA-256 de 'toto': " + hexString.toString());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            Utilisateur unUser = new Utilisateur(email, mdp);
            VerifConnexion uneVerif = new VerifConnexion();
            uneVerif.execute(unUser);
            Log.e("message : ", "je suis passé la");
        }
        if (view.getId() == R.id.idInscription) {
            Intent unIntent = new Intent(this, Inscription.class);
            this.startActivity(unIntent);
        }
    }

    /* La connexion à la base de données via une tâche asynchrone */
    class VerifConnexion extends AsyncTask<Utilisateur, Void, Utilisateur> {

        @Override
        protected Utilisateur doInBackground(Utilisateur... utilisateurs) {
            String resultatJson = "";
            Utilisateur unUser = utilisateurs[0];
            Utilisateur unUser2 = null;
            String url = "http://"+IP+"/bachelor/projet/AntiGaspi/firedrive/api/verifConnexion.php?email=" + unUser.getEmail() + "&mdp=" + unUser.getMdp();
            Log.e("url : ", url);
            try {
                URL uneUrl = new URL(url);
                HttpURLConnection uneConnexion = (HttpURLConnection) uneUrl.openConnection();
                uneConnexion.setRequestMethod("GET");
                uneConnexion.setDoInput(true);
                uneConnexion.setDoOutput(true);
                //on fixe le timeout
                uneConnexion.setConnectTimeout(20000);
                uneConnexion.connect();

                //lecture des données json et les mettres dans une chaine
                InputStreamReader isr = new InputStreamReader(uneConnexion.getInputStream(), "UTF-8");
                //utiliser un buffer de lecture dans ce fichier
                BufferedReader br = new BufferedReader(isr);
                //Lire dans le buffer ligne par ligne
                StringBuilder sb = new StringBuilder();
                String ligne = "";
                Log.e("JSON : ", "coucou");
                //boucle de lecture des lignes et ajout dans le string builder
                while ((ligne = br.readLine()) != null) {
                    sb.append(ligne);
                }
                //récupérer le json dans une chaine résultat
                resultatJson = sb.toString();
                Log.e("JSON : ", resultatJson);
                br.close();
                isr.close();
                uneConnexion.disconnect();
                try {
                    JSONArray tabJson = new JSONArray(resultatJson);
                    JSONObject unObjetJson = tabJson.getJSONObject(0);
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
                    Log.e("Email : ", unUser2.getEmail());
                    Log.e("Mdp : ", unUser2.getMdp());
                } catch (JSONException exp) {
                    exp.printStackTrace();
                    Log.e("Erreur 1 : ", "Impossible de se parser le JSON");
                }
            } catch (Exception exp) {
                exp.printStackTrace();
                Log.e("Erreur 1 : ", "Impossible de se connecter");
            }
            return unUser2;
        }

        @Override
        protected void onPostExecute(Utilisateur unUtilisateur) {
            MainActivity.setUserConnecte(unUtilisateur);

            if (unUtilisateur == null) {
                Toast.makeText(MainActivity.this, "Veuillez vérifier vos identifiants.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Bienvenue " + unUtilisateur.getNom(), Toast.LENGTH_SHORT).show();
                Intent unIntent = new Intent(MainActivity.this, Accueil.class);
                unIntent.putExtra("email", unUtilisateur.getEmail());
                unIntent.putExtra("idUser", unUtilisateur.getIdLivreur() + "");
                unIntent.putExtra("nom", unUtilisateur.getNom());
                unIntent.putExtra("prenom", unUtilisateur.getPrenom() + "");
                unIntent.putExtra("tel", unUtilisateur.getTel() + "");
                MainActivity.this.startActivity(unIntent);
            }
        }
    }
}