package com.example.appqrsalones.modelo;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.appqrsalones.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
public class controllerAdapterDelete extends RecyclerView.Adapter<MyViewHolderDelete>{
    private Context context;
    private List<Horario> dataList;
    private FirebaseFirestore firestore;
    public controllerAdapterDelete(Context context, List<Horario> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolderDelete onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_horario_single_delete, parent,false);
        return new MyViewHolderDelete(view);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderDelete holder, int position) {
        //Mostrar datos de cada horario
        holder.materia.setText(dataList.get(position).getHorario_materia());
        holder.hora_ini.setText(dataList.get(position).getHorario_hora_inicial());
        holder.hora_fin.setText(dataList.get(position).getHorario_hora_final());
        holder.dia.setText(dataList.get(position).getHorario_dia());
        holder.eliminar.setOnClickListener(v -> {
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("horarios").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null){
                        for (DocumentSnapshot document : querySnapshot.getDocuments()){
                            if(document.getString("horario_id").equals(dataList.get(position).getHorario_id())){
                                document.getReference().delete().addOnCompleteListener(deleteTask -> {
                                    if (deleteTask.isSuccessful()) {
                                        dataList.remove(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Horario Borrado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "Error al borrar el horario", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }
            });
        });
    }
    @Override
    public int getItemCount() {
        if(dataList.isEmpty())return 0;
        return dataList.size();
    }
}
class MyViewHolderDelete extends RecyclerView.ViewHolder{
    TextView materia,hora_ini, hora_fin, dia;
    CardView cardHorario;
    Button eliminar;
    public MyViewHolderDelete(@NonNull View itemView) {
        super(itemView);
        materia = itemView.findViewById(R.id.txt_materia_card);
        hora_ini = itemView.findViewById(R.id.txt_hora_inicial_card);
        hora_fin = itemView.findViewById(R.id.txt_hora_final_card);
        dia = itemView.findViewById(R.id.txt_dia_card);
        cardHorario = itemView.findViewById(R.id.card_horario);
        eliminar = itemView.findViewById(R.id.btn_eliminar_horario);
    }
}
