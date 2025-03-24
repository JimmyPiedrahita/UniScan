package com.example.appqrsalones.modelo;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appqrsalones.R;
import com.example.appqrsalones.activityInformacionSalon;

import java.util.List;

public class controllerAdapterSalon extends RecyclerView.Adapter<MyViewHolderSalon> {
    private Context context;
    private List<Salon> dataList;
    public controllerAdapterSalon(Context context, List<Salon> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolderSalon onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_salon_single, parent,false);
        return new MyViewHolderSalon(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderSalon holder, int position) {
        holder.sede.setText(dataList.get(position).getSalon_sede());
        holder.numero.setText(dataList.get(position).getSalon_numero());
        holder.cardHorario.setOnClickListener(v -> {
            Intent intent = new Intent(context, activityInformacionSalon.class);
            intent.putExtra("numero", dataList.get(position).getSalon_numero());
            intent.putExtra("sede", dataList.get(position).getSalon_sede());
            intent.putExtra("codigo", dataList.get(position).getCodigo_qr());
            context.startActivity(intent);


        });
    }

    @Override
    public int getItemCount() {
        if(dataList.isEmpty())return 0;
        return dataList.size();
    }
}
class MyViewHolderSalon extends RecyclerView.ViewHolder{
    TextView sede, numero;
    CardView cardHorario;
    public MyViewHolderSalon(@NonNull View itemView) {
        super(itemView);
        sede = itemView.findViewById(R.id.txt_sede_card);
        numero = itemView.findViewById(R.id.txt_numero_card);
        cardHorario = itemView.findViewById(R.id.card_salon);
    }
}
