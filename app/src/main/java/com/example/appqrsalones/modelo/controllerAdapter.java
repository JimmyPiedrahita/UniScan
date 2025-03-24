package com.example.appqrsalones.modelo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appqrsalones.R;

import java.util.List;
public class controllerAdapter extends RecyclerView.Adapter<MyViewHolder>{
    private Context context;
    private List<Horario> dataList;
    public controllerAdapter(Context context, List<Horario> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_horario_single, parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Mostrar datos de cada horario
        holder.materia.setText(dataList.get(position).getHorario_materia());
        holder.hora_ini.setText(dataList.get(position).getHorario_hora_inicial());
        holder.hora_fin.setText(dataList.get(position).getHorario_hora_final());
        holder.dia.setText(dataList.get(position).getHorario_dia());
    }
    @Override
    public int getItemCount() {
        if(dataList.isEmpty())return 0;
        return dataList.size();
    }
}
class MyViewHolder extends RecyclerView.ViewHolder{
    TextView materia,hora_ini, hora_fin, dia;
    CardView cardHorario;
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        materia = itemView.findViewById(R.id.txt_materia_card);
        hora_ini = itemView.findViewById(R.id.txt_hora_inicial_card);
        hora_fin = itemView.findViewById(R.id.txt_hora_final_card);
        dia = itemView.findViewById(R.id.txt_dia_card);
        cardHorario = itemView.findViewById(R.id.card_horario);
    }
}