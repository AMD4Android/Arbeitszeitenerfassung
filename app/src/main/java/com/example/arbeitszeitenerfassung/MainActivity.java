package com.example.arbeitszeitenerfassung;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout.LayoutParams;


import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.Calendar;

import android.widget.*;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Button dateButton;
    private Button addButton;
    private ListView listview2 = null;

    private RadioButton Radiobutton_Frueh;
    private RadioButton Radiobutton_Spaet;
    private RadioButton Radiobutton_Nacht;

    public static ArrayList<Arbeitszeit> zeitList;

    private ArbeitszeitenDAO arbeitszeitDAO;
    private TextView txt_arbeitszeitenbeginn;
    private TextView txt_arbeitszeitenende;
    Arbeitszeit arbeitsZeit_eines_Tages;
    private ListView listview;
    private ArrayAdapter<Arbeitszeit> listAdapter;
    private int selectedItemIndex;
    private String ausgewaehlteDatum;
    private String Arbeitstag;
    private String Arbeitsbeginn;
    private String Arbeitsende;
    private SimpleDateFormat format;
    private Date startTime;
    private Date endTime;

    private Date pauseTime;

    private String Arbeitsschicht;
    private long differenceMillis;
    private long hours;
    private long minutes;
    private String hoursString;
    private String minutesString;
    private boolean teil2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.black));


        Radiobutton_Frueh = findViewById(R.id.Radiobutton_Frueh);
        Radiobutton_Spaet = findViewById(R.id.Radiobutton_Spaet);
        Radiobutton_Nacht = findViewById(R.id.Radiobutton_Nacht);

        dateButton = findViewById(R.id.btn_getDate);
        dateButton.setBackgroundColor(Color.parseColor("#495a5d"));


        listview = findViewById(R.id.listview);
        zeitList = new ArrayList<>();
        arbeitszeitDAO = new ArbeitszeitenDAO(this);
        arbeitszeitDAO.open();
        List<Arbeitszeit> arbeitszeitFromDB = arbeitszeitDAO.getAllTage();
        zeitList.addAll(arbeitszeitFromDB);

        listAdapter = new ListView_Spaltenadapter(this, R.layout.listview_zeitarbeit_spalten, zeitList);
        listAdapter.notifyDataSetChanged();
        listview.setAdapter(listAdapter);


        txt_arbeitszeitenbeginn = findViewById(R.id.txt_arbeitszeitenbeginn);
        txt_arbeitszeitenende = findViewById(R.id.txt_arbeitszeitenende);
        addButton = findViewById(R.id.addButton);

        Arbeitsschicht = "NON";
        // Ein Item in die ListView hinzufügen
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Pauseeingabe = schichtenpausenErmitteln();
                if (Pauseeingabe.equals("Pauseziten wurden nicht angegeben")) {
                    Toast.makeText(MainActivity.this, "Pausezeiten eingegeben?", Toast.LENGTH_SHORT).show();

                } else {
                    Arbeitstag = dateButton.getText().toString().trim();
                    Arbeitsbeginn = txt_arbeitszeitenbeginn.getText().toString().trim();
                    Arbeitsende = txt_arbeitszeitenende.getText().toString().trim();
                    char pruefeArbeitsbeginn = Arbeitsbeginn.charAt(0);
                    char pruefeArbeitsende = Arbeitsende.charAt(0);

                    boolean ArbeitsbeginnisNumericA = Character.isDigit(pruefeArbeitsbeginn);
                    boolean ArbeitsendeisNumericA = Character.isDigit(pruefeArbeitsende);
                    boolean ArbeitstagisCharacter = !Arbeitstag.matches(".*\\d.*");

                    String Pause = "";

                    if (ArbeitstagisCharacter == false && ArbeitsbeginnisNumericA == true && ArbeitsendeisNumericA == true) {
                        teil2 = false;
                        Pause = checkRadiobutton();

                        if (Pause.equals("")) {
                            Toast.makeText(MainActivity.this, "Schicht anklicken!", Toast.LENGTH_SHORT).show();

                        } else {
                            if (Radiobutton_Nacht.isChecked()) {
                                BerechneNachtschichtstunden(teil2);
                            }
                            else{
                                Pause = "00:" + Pause;

                                format = new SimpleDateFormat("HH:mm");

                                // Zeit-Strings in Date-Objekte parsen

                                int[] start = parseTime(Arbeitsbeginn);
                                int[] end = parseTime(Arbeitsende);
                                int[] pause2 = parseTime(Pause);

                                int totalMinutes = calculateMinutes(end) - calculateMinutes(start);
                                int effectiveMinutes = totalMinutes - calculateMinutes(pause2);

                                // Arbeitszeit in Stunden und Minuten umrechnen
                                int hours = effectiveMinutes / 60;
                                int minutes = effectiveMinutes % 60;
                                // Differenz in Stunden und Minuten umrechnen
                                hours = hours;
                                minutes = minutes;
                                // String für die Differenz erstellen
                                if (String.valueOf(hours).length() == 1) {
                                    hoursString = "0" + hours;
                                } else {
                                    hoursString = String.valueOf(hours);
                                }
                                if (String.valueOf(minutes).length() == 1) {
                                    minutesString = "0" + minutes;
                                } else {
                                    minutesString = String.valueOf(minutes);
                                }
                                String Dauer = hoursString + ":" + minutesString;

                                arbeitszeitDAO.addArbeitstag(new Arbeitszeit(Arbeitstag, Arbeitsschicht, Arbeitsbeginn, Arbeitsende, Dauer), teil2);
                            }

                            Toast.makeText(MainActivity.this, "Arbeitszeiten hinzugefügt", Toast.LENGTH_SHORT).show();
                            dateButton.setText("Datum");
                            txt_arbeitszeitenbeginn.setText("Zeit eingeben");
                            txt_arbeitszeitenende.setText("Zeit eingeben");
                        }


                    } else if (ArbeitstagisCharacter == false && ArbeitsbeginnisNumericA == true && ArbeitsendeisNumericA == false) {
                        teil2 = false;

                        arbeitszeitDAO.addArbeitstag(new Arbeitszeit(Arbeitstag, Arbeitsschicht, Arbeitsbeginn, "", ""), teil2);
                        Toast.makeText(MainActivity.this, "Arbeitszeit hinzugefügt", Toast.LENGTH_SHORT).show();
                        dateButton.setText("Datum");
                        txt_arbeitszeitenbeginn.setText("Zeit eingeben");
                        txt_arbeitszeitenende.setText("Zeit eingeben");


                    } else if (ArbeitstagisCharacter == false && ArbeitsbeginnisNumericA == false && ArbeitsendeisNumericA == true) {
                        teil2 = true;
                        Pause = checkRadiobutton();
                        if (Pause.equals("")) {
                            Toast.makeText(MainActivity.this, "Schicht anklicken!", Toast.LENGTH_SHORT).show();

                        } else {
                            Pause = "00:" + Pause;
                            for (int i = 0; i < zeitList.size(); i++) {
                                if (zeitList.get(i).getDatum().contains(ausgewaehlteDatum)) {
                                    Arbeitsbeginn = String.valueOf(zeitList.get(i).getArbeitszeitstart());
                                    break;
                                }
                            }
                            if (Radiobutton_Nacht.isChecked()) {
                                BerechneNachtschichtstunden(teil2);
                            } else {

                                int[] start = parseTime(Arbeitsbeginn);
                                int[] end = parseTime(Arbeitsende);
                                int[] pause2 = parseTime(Pause);

                                int totalMinutes = calculateMinutes(end) - calculateMinutes(start);
                                int effectiveMinutes = totalMinutes - calculateMinutes(pause2);

                                // Arbeitszeit in Stunden und Minuten umrechnen
                                int hours = effectiveMinutes / 60;
                                int minutes = effectiveMinutes % 60;
                                // Differenz in Stunden und Minuten umrechnen
                                hours = hours;
                                minutes = minutes;
                                // String für die Differenz erstellen
                                if (String.valueOf(hours).length() == 1) {
                                    hoursString = "0" + hours;
                                } else {
                                    hoursString = String.valueOf(hours);
                                }
                                if (String.valueOf(minutes).length() == 1) {
                                    minutesString = "0" + minutes;
                                } else {
                                    minutesString = String.valueOf(minutes);
                                }
                                String Dauer = hoursString + ":" + minutesString;
                                arbeitszeitDAO.addArbeitstag(new Arbeitszeit(ausgewaehlteDatum, Arbeitsschicht, Arbeitsbeginn, Arbeitsende, Dauer), teil2);
                            }

                            Toast.makeText(MainActivity.this, "Arbeitszeit hinzugefügt", Toast.LENGTH_SHORT).show();
                            dateButton.setText("Datum");
                            txt_arbeitszeitenbeginn.setText("Zeit eingeben");
                            txt_arbeitszeitenende.setText("Zeit eingeben");
                        }

                    } else {
                        Toast.makeText(MainActivity.this, "Bitte versuchen Sie noch ein mal", Toast.LENGTH_SHORT).show();
                    }
                    zeitList.clear();
                    List<Arbeitszeit> arbeitszeitFromDB = arbeitszeitDAO.getAllTage();
                    zeitList.addAll(arbeitszeitFromDB);
                    listAdapter.notifyDataSetChanged();
                }


            }
        });

        // ListviewItem entfernen
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItemIndex = position;
                for (int i = 0; i < zeitList.size(); i++) {
                    if (i == position) {
                        ausgewaehlteDatum = String.valueOf(zeitList.get(i).getDatum());
                        Arbeitsbeginn = String.valueOf(zeitList.get(i).getArbeitszeitstart());
                        break;
                    }
                }
                return true;
            }


        });


        Radiobutton_Frueh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radiobutton_Frueh.setChecked(true);
                Arbeitsschicht = "FS";
            }
        });
        Radiobutton_Spaet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radiobutton_Spaet.setChecked(true);
                Arbeitsschicht = "SS";

            }
        });
        Radiobutton_Nacht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Radiobutton_Nacht.setChecked(true);
                Arbeitsschicht = "NS";

            }
        });
    }

    public void BerechneNachtschichtstunden(boolean teil) {
        // Annahme: Arbeitsbeginn, Arbeitsende und Pausenzeit im Format "Stunden:Minuten"

        String pausenzeitStr = "00:" + checkRadiobutton();

        // Extrahieren von Stunden und Minuten aus den Strings
        int arbeitsbeginnStunden = Integer.parseInt(Arbeitsbeginn.split(":")[0]);
        int arbeitsbeginnMinuten = Integer.parseInt(Arbeitsbeginn.split(":")[1]);
        int arbeitsendeStunden = Integer.parseInt(txt_arbeitszeitenende.getText().toString().split(":")[0]);
        int arbeitsendeMinuten = Integer.parseInt(txt_arbeitszeitenende.getText().toString().split(":")[1]);
        int pausenzeitStunden = Integer.parseInt(pausenzeitStr.split(":")[0]);
        int pausenzeitMinuten = Integer.parseInt(pausenzeitStr.split(":")[1]);

        // Berechnung der Arbeitszeit in Minuten
        int arbeitszeitInMinuten = ((arbeitsendeStunden + 24 - arbeitsbeginnStunden) % 24) * 60
                + (arbeitsendeMinuten - arbeitsbeginnMinuten);

        // Berechnung der Pausenzeit in Minuten
        int pausenzeitInMinuten = pausenzeitStunden * 60 + pausenzeitMinuten;

        // Berücksichtigung des Pausenabzugs
        int gesamtArbeitszeitInMinuten = arbeitszeitInMinuten - pausenzeitInMinuten;

        // Umrechnung der Gesamtarbeitszeit in Stunden und Minuten
        int gesamtArbeitszeitStunden = gesamtArbeitszeitInMinuten / 60;
        int gesamtArbeitszeitRestMinuten = gesamtArbeitszeitInMinuten % 60;

        // Umwandeln in String
        String gesamtArbeitszeitStunden_String = String.valueOf(gesamtArbeitszeitStunden);
        if(gesamtArbeitszeitStunden_String.length() == 1){
            gesamtArbeitszeitStunden_String = "0"+ gesamtArbeitszeitStunden_String;
        }

        String gesamtArbeitszeitRestMinuten_String = String.valueOf(gesamtArbeitszeitRestMinuten);
        if(gesamtArbeitszeitRestMinuten_String.length() == 1){
            gesamtArbeitszeitRestMinuten_String = "0"+ gesamtArbeitszeitRestMinuten_String;
        }

        // Ausgabe der berechneten Gesamtarbeitszeit
        String Dauer = gesamtArbeitszeitStunden_String + ":" + gesamtArbeitszeitRestMinuten_String;
        arbeitszeitDAO.addArbeitstag(new Arbeitszeit(ausgewaehlteDatum, Arbeitsschicht, Arbeitsbeginn, Arbeitsende, Dauer), teil);

    }

    private static int[] parseTime(String time) {
        String[] parts = time.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return new int[]{hours, minutes};
    }

    private static int calculateMinutes(int[] time) {
        return time[0] * 60 + time[1];
    }

    /*private void ItitialeComboBox() {
        // Spinner und Adapter initialisieren
        Spinner spinner = findViewById(R.id.spinner);
//, android.R.layout.simple_spinner_dropdown_item
        // ArrayAdapter für den Spinner erstellen
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Schichten,
                R.layout.color_spinner
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown);

        // Adapter an den Spinner binden
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
    }*/
    private String checkRadiobutton() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String value = "";

        if (Radiobutton_Frueh.isChecked()) {
            value = sharedPreferences.getString("Fruehschichtpausentext", "");

        } else if (Radiobutton_Spaet.isChecked()) {
            value = sharedPreferences.getString("Spaetschichtpausentext", "");

        } else if (Radiobutton_Nacht.isChecked()) {
            value = sharedPreferences.getString("Nachtschichtpausentext", "");

        }
        return value;
    }

    private String schichtenpausenErmitteln() {

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String value = null;
        String Fruehschichtpause = sharedPreferences.getString("Fruehschichtpausentext", "");
        String Spaetschichtpause = sharedPreferences.getString("Spaetschichtpausentext", "");
        String Nachtschichtpause = sharedPreferences.getString("Nachtschichtpausentext", "");

        if (Fruehschichtpause.equals("") || Spaetschichtpause.equals("") || Nachtschichtpause.equals("")) {
            value = "Pauseziten wurden nicht angegeben";
        } else {
            value = "Pauseziten wurden angegeben";

        }
        return value;
    }

    public void getKalender(View view) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Aktion ausführen, wenn der Benutzer ein Datum auswählt
                        String selectedDate = selectedDay + "." + (selectedMonth + 1) + "." + selectedYear;
                        ausgewaehlteDatum = selectedDate;
                        dateButton.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void showArbeitszeitanfang(View view) {
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the selected time (hourOfDay and minute)
                // For example, display the selected time in a TextView
                String minuten_zweistellig = getTimezweistellig(minute);
                String stunden_zweistellig = getTimezweistellig(hourOfDay);

                TextView textViewTime = findViewById(R.id.txt_arbeitszeitenbeginn);
                LayoutParams params = (LayoutParams) textViewTime.getLayoutParams();

                params.leftMargin = (int) getResources().getDimension(R.dimen.margin_left);
                textViewTime.setLayoutParams(params);

                textViewTime.setText(stunden_zweistellig + ":" + minuten_zweistellig);
            }

            private String getTimezweistellig(int t) {
                String x = "";
                x = String.valueOf(t);
                int laenge = x.length();
                if (laenge < 2) {
                    x = "0" + x;
                }
                return x;
            }
        }, /* Default hour */ 0, /* Default minute */ 0, /* 24-hour format */ true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    public void showArbeitszeitende(View view) {
        // Create a new instance of TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Do something with the selected time (hourOfDay and minute)
                // For example, display the selected time in a TextView
                String minuten_zweistellig = getTimezweistellig(minute);
                String stunden_zweistellig = getTimezweistellig(hourOfDay);

                TextView textViewTime = findViewById(R.id.txt_arbeitszeitenende);
                LayoutParams params = (LayoutParams) textViewTime.getLayoutParams();

                params.leftMargin = (int) getResources().getDimension(R.dimen.margin_left);
                textViewTime.setLayoutParams(params);

                textViewTime.setText(stunden_zweistellig + ":" + minuten_zweistellig);

            }

            private String getTimezweistellig(int t) {
                String x = "";
                x = String.valueOf(t);
                int laenge = x.length();
                if (laenge < 2) {
                    x = "0" + x;
                }
                return x;
            }
        }, /* Default hour */ 0, /* Default minute */ 0, /* 24-hour format */ true);

        // Show the TimePickerDialog
        timePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            boolean Buttonistklickable = false;
            Intent intent = new Intent(MainActivity.this, EinstellungenActivity.class);
            intent.putExtra("Buttonistklickable", false);
            startActivity(intent);

        }
        if (id == R.id.monatsStundenberechnung) {
            boolean Buttonistklickable = true;
            Intent intent = new Intent(MainActivity.this, EinstellungenActivity.class);
            intent.putExtra("Buttonistklickable", true);
            startActivity(intent);

        }
        return true;
    }
}
