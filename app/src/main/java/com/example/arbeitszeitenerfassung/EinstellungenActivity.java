package com.example.arbeitszeitenerfassung;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;


public class EinstellungenActivity extends AppCompatActivity {

    //private Button btn_SaveAndBack;
    public EditText txt_fruehschichtspause;
    private EditText txt_spaetschichtspause;
    private EditText txt_nachtschichtspause;
    private Button btn_getArbeitsstunden;

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
        if(Buttonistklickable == true){
            btn_getArbeitsstunden.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(EinstellungenActivity.this, "Button wurde geklickt", Toast.LENGTH_SHORT).show();

                }
            });
        }


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