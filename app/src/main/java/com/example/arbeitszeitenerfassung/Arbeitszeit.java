package com.example.arbeitszeitenerfassung;

import java.time.LocalDate;
import java.time.LocalTime;

public class Arbeitszeit {
    public String getDatum() {
        return Datum;
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

    public void setDatum(String datum) {
        Datum = datum;
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

    public Arbeitszeit(String datum, String arbeitszeitstart, String arbeitszeitende, String tagesarbeitDauer) {
        Datum = datum;
        Arbeitszeitstart = arbeitszeitstart;
        Arbeitszeitende = arbeitszeitende;
        TagesarbeitDauer = tagesarbeitDauer;
    }

    private String Datum;
    private String Arbeitszeitstart;

    private String Arbeitszeitende;

    private String TagesarbeitDauer;


}
