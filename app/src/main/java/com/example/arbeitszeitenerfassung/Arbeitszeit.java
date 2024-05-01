package com.example.arbeitszeitenerfassung;

import java.time.LocalDate;
import java.time.LocalTime;

public class Arbeitszeit {
    public String getDatum() {
        return Datum;
    }
    public String getArbeitsschicht() {
        return Schicht;
    }

    public String getArbeitszeitstart() {
        return Arbeitszeitstart;
    }

    public String getArbeitszeitende() {
        return Arbeitszeitende;
    }

    public String getTagesarbeitDauer() {
        return TagesarbeitDauer;
    }



    public void setArbeitszeitstart(String arbeitszeitstart) {
        Arbeitszeitstart = arbeitszeitstart;
    }

    public void setArbeitszeitende(String arbeitszeitende) {
        Arbeitszeitende = arbeitszeitende;
    }

    public void setTagesarbeitDauer(String tagesarbeitDauer) {
        TagesarbeitDauer = tagesarbeitDauer;
    }
    public void setDatum(String datum) {
        Datum = datum;
    }
    public void setSchicht(String schicht) {
        Schicht = schicht;
    }
    public Arbeitszeit(String datum,String schicht, String arbeitszeitstart, String arbeitszeitende, String tagesarbeitDauer) {
        Datum = datum;
        Schicht = schicht;
        Arbeitszeitstart = arbeitszeitstart;
        Arbeitszeitende = arbeitszeitende;
        TagesarbeitDauer = tagesarbeitDauer;
    }

    private String Datum;

    private String Schicht;
    private String Arbeitszeitstart;

    private String Arbeitszeitende;

    private String TagesarbeitDauer;


}
