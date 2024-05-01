package com.example.arbeitszeitenerfassung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.viewmodel.CreationExtras;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RouteListingPreference;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class EinstellungenActivity extends AppCompatActivity {

    //private Button btn_SaveAndBack;
    public EditText txt_fruehschichtspause;
    private EditText txt_spaetschichtspause;
    private EditText txt_nachtschichtspause;
    private Button btn_getArbeitsstunden;
    public EditText txt_Monatseingabe;
    public EditText txt_Jahreingabe;
    private TextView txt_gearbeitetezeiten;
    private LinearLayout Pauseneingabenlayout;
    private LinearLayout Stundenlohneingabelayout;
    private LinearLayout Arbeitsstundenberechnungslayout;
    private TextView txt_gearbeitetezeiten_frueschicht;
    private TextView txt_gearbeitetezeiten_spaetschicht;
    private TextView txt_gearbeitetezeiten_nachtschicht;

    private ArrayList<String> Arbeitsstundenliste;
    private ArrayList<String> Arbeitsstundenliste_Fruehschicht;
    private ArrayList<String> Arbeitsstundenliste_Spaetschicht;
    private ArrayList<String> Arbeitsstundenliste_Nachtschicht;


    private static final String PREFS_NAME = "MyPrefs";
    private static final String EDIT_TEXT_VALUE_KEY = "editTextValue";

    @SuppressLint("MissingInflatedId")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_einstellungen);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));
        getSupportActionBar().setTitle("Einstellungen");

        txt_fruehschichtspause = findViewById(R.id.txt_fruehschichtspause);
        txt_spaetschichtspause = findViewById(R.id.txt_spaetschichtspause);
        txt_nachtschichtspause = findViewById(R.id.txt_nachtschichtspause);
        btn_getArbeitsstunden = findViewById(R.id.btn_getArbeitsstunden);
        txt_Monatseingabe = findViewById(R.id.Monatseingabe);
        txt_Jahreingabe = findViewById(R.id.Jahreingabe);
        txt_gearbeitetezeiten = findViewById(R.id.txt_gearbeitetezeiten);
        Pauseneingabenlayout = findViewById(R.id.Pauseneingabenlayout);
        Stundenlohneingabelayout = findViewById(R.id.Stundenlohneingabelayout);
        Arbeitsstundenberechnungslayout = findViewById(R.id.Arbeitsstundenberechnungslayout);
        txt_gearbeitetezeiten_frueschicht = findViewById(R.id.txt_gearbeitetezeiten_frueschicht);
        txt_gearbeitetezeiten_spaetschicht = findViewById(R.id.txt_gearbeitetezeiten_spaetschicht);
        txt_gearbeitetezeiten_nachtschicht = findViewById(R.id.txt_gearbeitetezeiten_nachtschicht);

        loadPausezeiten();
        boolean Buttonistklickable = getIntent().getBooleanExtra("Buttonistklickable", false);

        /*btn_SaveAndBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Fruehschichtpausentext = txt_fruehschichtspause.getText().toString();
                String Spaetschichtpausentext = txt_spaetschichtspause.getText().toString();
                String Nachtschichtpausentext = txt_nachtschichtspause.getText().toString();

                if (!TextUtils.isEmpty(Fruehschichtpausentext) || !TextUtils.isEmpty(Spaetschichtpausentext) || !TextUtils.isEmpty(Nachtschichtpausentext)) {

                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("Fruehschichtpausentext", Fruehschichtpausentext);
                    editor.putString("Spaetschichtpausentext", Spaetschichtpausentext);
                    editor.putString("Nachtschichtpausentext", Nachtschichtpausentext);

                    editor.apply();
                }


                Intent intent = new Intent(EinstellungenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });*/
        if (Buttonistklickable == true) {
            Pauseneingabenlayout.setVisibility(View.GONE);

            btn_getArbeitsstunden.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!txt_Monatseingabe.getText().toString().isEmpty() && !txt_Jahreingabe.getText().toString().isEmpty()) {
                        ArrayList<Arbeitszeit> liste = MainActivity.zeitList;

                        SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("MM.yyyy");
                        String dateextrahiert = null;
                        if (txt_Monatseingabe.getText().toString().length() == 2) {
                            dateextrahiert = txt_Monatseingabe.getText().toString() + "." + txt_Jahreingabe.getText().toString();

                        } else {
                            dateextrahiert = "0" + txt_Monatseingabe.getText().toString() + "." + txt_Jahreingabe.getText().toString();

                        }
                        Arbeitsstundenliste = new ArrayList<>();
                        Arbeitsstundenliste_Fruehschicht = new ArrayList<>();
                        Arbeitsstundenliste_Spaetschicht = new ArrayList<>();
                        Arbeitsstundenliste_Nachtschicht = new ArrayList<>();


                        for (Arbeitszeit arbeitszeit : liste) {


                            try {
                                String datum1 = arbeitszeit.getDatum().toString();
                                Date date1 = sdf1.parse(datum1);
                                String monatJahr1 = sdf2.format(date1);
                                Date date2 = sdf2.parse(dateextrahiert);
                                String monatJahr2 = sdf2.format(date2);

                                if (monatJahr1.equals(monatJahr2)) {
                                    Arbeitsstundenliste.add(arbeitszeit.getTagesarbeitDauer());
                                }

                                if (arbeitszeit.getArbeitsschicht().toString().equals("FS")) {
                                    Arbeitsstundenliste_Fruehschicht.add(arbeitszeit.getTagesarbeitDauer());

                                }
                                if (arbeitszeit.getArbeitsschicht().toString().equals("SS")) {
                                    Arbeitsstundenliste_Spaetschicht.add(arbeitszeit.getTagesarbeitDauer());

                                }
                                if (arbeitszeit.getArbeitsschicht().toString().equals("NS")) {
                                    Arbeitsstundenliste_Nachtschicht.add(arbeitszeit.getTagesarbeitDauer());

                                }


                            } catch (ParseException e) {
                                Toast.makeText(EinstellungenActivity.this, "Das Parsen ist schiefgelaufen", Toast.LENGTH_SHORT).show();
                            }


                        }
                        // hier werden die Arbeitszeiten in eine Liste hinzugefügt und addiert dann werden die angezeigt
                        String gearbeitete_Stunden = rechneStunden(Arbeitsstundenliste);
                        String gearbeiteteStunden_Fruehschicht = rechneStunden(Arbeitsstundenliste_Fruehschicht);
                        String gearbeiteteStunden_Spaetschicht = rechneStunden(Arbeitsstundenliste_Spaetschicht);
                        String gearbeiteteStunden_Nachtschicht = rechneStunden(Arbeitsstundenliste_Nachtschicht);


                        txt_gearbeitetezeiten.setText(gearbeitete_Stunden);
                        txt_gearbeitetezeiten_frueschicht.setText(gearbeiteteStunden_Fruehschicht);
                        txt_gearbeitetezeiten_spaetschicht.setText(gearbeiteteStunden_Spaetschicht);
                        txt_gearbeitetezeiten_nachtschicht.setText(gearbeiteteStunden_Nachtschicht);


                    }


                    Toast.makeText(EinstellungenActivity.this, "Button wurde geklickt", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            Stundenlohneingabelayout.setVisibility(View.GONE);
            Arbeitsstundenberechnungslayout.setVisibility(View.GONE);

        }


    }

    private String rechneStunden(ArrayList<String> arbeitsstundenliste) {
        String Summe = null;

        // Summe der Stunden und Minuten initialisieren
        int summeStunden = 0;
        int summeMinuten = 0;

        // Durchlaufe die Arbeitsstundenliste
        for (String zeit : arbeitsstundenliste) {
            // Teile die Zeit in Stunden und Minuten auf
            String[] teile = zeit.split(":");
            int stunden = Integer.parseInt(teile[0]);
            int minuten = Integer.parseInt(teile[1]);

            // Addiere die Stunden und Minuten zur Summe
            summeStunden += stunden;
            summeMinuten += minuten;

            // Überprüfe, ob die Summe der Minuten größer als 59 ist
            if (summeMinuten >= 60) {
                // Füge eine Stunde hinzu
                summeStunden++;
                // Setze die Minuten auf den Restwert
                summeMinuten = summeMinuten % 60;
            }
        }
        Summe = String.valueOf(summeStunden) + " Stunden : " + String.valueOf(summeMinuten) + " Minuten";
        return Summe;
    }

    public void loadPausezeiten() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String FruehschichtwertUebergabe = sharedPreferences.getString("Fruehschichtpausentext", "");
        txt_fruehschichtspause.setText(FruehschichtwertUebergabe);

        String SpaetschichtwertUebergabe = sharedPreferences.getString("Spaetschichtpausentext", "");
        txt_spaetschichtspause.setText(SpaetschichtwertUebergabe);

        String NachtschichtwertUebergabe = sharedPreferences.getString("Nachtschichtpausentext", "");
        txt_nachtschichtspause.setText(NachtschichtwertUebergabe);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settingsmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ic_save) {
            String Fruehschichtpausentext = txt_fruehschichtspause.getText().toString();
            String Spaetschichtpausentext = txt_spaetschichtspause.getText().toString();
            String Nachtschichtpausentext = txt_nachtschichtspause.getText().toString();

            if (!TextUtils.isEmpty(Fruehschichtpausentext) || !TextUtils.isEmpty(Spaetschichtpausentext) || !TextUtils.isEmpty(Nachtschichtpausentext)) {

                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("Fruehschichtpausentext", Fruehschichtpausentext);
                editor.putString("Spaetschichtpausentext", Spaetschichtpausentext);
                editor.putString("Nachtschichtpausentext", Nachtschichtpausentext);

                editor.apply();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}