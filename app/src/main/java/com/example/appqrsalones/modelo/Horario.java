package com.example.appqrsalones.modelo;

import android.os.Parcel;
import android.os.Parcelable;
public class Horario implements Parcelable {
    private String horario_dia;
    private String horario_hora_final;
    private String horario_hora_inicial;
    private String horario_id;
    private String horario_materia;
    private String salon_numero;

    public Horario(String horario_dia, String horario_hora_final, String horario_hora_inicial, String horario_id, String horario_materia, String salon_numero) {
        this.horario_dia = horario_dia;
        this.horario_hora_final = horario_hora_final;
        this.horario_hora_inicial = horario_hora_inicial;
        this.horario_id = horario_id;
        this.horario_materia = horario_materia;
        this.salon_numero = salon_numero;
    }
    public Horario(){}

    public String getHorario_dia() {
        return horario_dia;
    }

    public void setHorario_dia(String horario_dia) {
        this.horario_dia = horario_dia;
    }

    public String getHorario_hora_final() {
        return horario_hora_final;
    }

    public void setHorario_hora_final(String horario_hora_final) {
        this.horario_hora_final = horario_hora_final;
    }

    public String getHorario_hora_inicial() {
        return horario_hora_inicial;
    }

    public void setHorario_hora_inicial(String horario_hora_inicial) {
        this.horario_hora_inicial = horario_hora_inicial;
    }

    public String getHorario_id() {
        return horario_id;
    }

    public void setHorario_id(String horario_id) {
        this.horario_id = horario_id;
    }

    public String getHorario_materia() {
        return horario_materia;
    }

    public void setHorario_materia(String horario_materia) {
        this.horario_materia = horario_materia;
    }

    public String getSalon_numero() {
        return salon_numero;
    }

    public void setSalon_numero(String salon_numero) {
        this.salon_numero = salon_numero;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.horario_dia);
        dest.writeString(this.horario_hora_final);
        dest.writeString(this.horario_hora_inicial);
        dest.writeString(this.horario_id);
        dest.writeString(this.horario_materia);
        dest.writeString(this.salon_numero);
    }

    public void readFromParcel(Parcel source) {
        this.horario_dia = source.readString();
        this.horario_hora_final = source.readString();
        this.horario_hora_inicial = source.readString();
        this.horario_id = source.readString();
        this.horario_materia = source.readString();
        this.salon_numero = source.readString();
    }

    protected Horario(Parcel in) {
        this.horario_dia = in.readString();
        this.horario_hora_final = in.readString();
        this.horario_hora_inicial = in.readString();
        this.horario_id = in.readString();
        this.horario_materia = in.readString();
        this.salon_numero = in.readString();
    }

    public static final Parcelable.Creator<Horario> CREATOR = new Parcelable.Creator<Horario>() {
        @Override
        public Horario createFromParcel(Parcel source) {
            return new Horario(source);
        }

        @Override
        public Horario[] newArray(int size) {
            return new Horario[size];
        }
    };
}
