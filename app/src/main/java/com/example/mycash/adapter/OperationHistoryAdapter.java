package com.example.mycash.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycash.R;
import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.services.OperationHistoryService;
import com.example.mycash.utils.HistoryAdapterItemClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OperationHistoryAdapter extends RecyclerView.Adapter<OperationHistoryAdapter.ViewHolder> {

    private List<OperationHistory> operationHistories = new ArrayList<>();

    private   HistoryAdapterItemClickListener historyAdapterItemClickListener;
    private Context context;

    public OperationHistoryAdapter(Context context, HistoryAdapterItemClickListener historyAdapterItemClickListener){
        this.context = context;
        this.historyAdapterItemClickListener = historyAdapterItemClickListener;
    }

    public void setItems(Collection<OperationHistory> operations) {
        operationHistories.addAll(operations);
        notifyDataSetChanged();
    }

    public void clearItems() {
        operationHistories.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expenses_layout_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindToView(operationHistories.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyAdapterItemClickListener.onItemClicked(operationHistories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return operationHistories.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {

        private MutableLiveData<OperationCategory> operationCategory = new MutableLiveData<>();

        OperationHistoryService operationHistoryService = new OperationHistoryService();

        ImageView imageView;
        TextView title, cost;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            title = itemView.findViewById(R.id.textViewTitle);
            cost = itemView.findViewById(R.id.textViewCost);
            imageView = itemView.findViewById(R.id.imageView);
            cardView = itemView.findViewById(R.id.cardView);
        }

        public void bindToView(OperationHistory operationHistory){
            title.setText(operationHistory.getOperationCategory().getTitle());
            StringBuilder sb = new StringBuilder();
            sb.append(operationHistory.getAmount());
            cost.setText(sb.toString());

            AssetManager am = context.getResources().getAssets();
            try {
                InputStream is = am.open(operationHistory.getOperationCategory().getImage());
                Drawable d = Drawable.createFromStream(is, null);
                imageView.setImageDrawable(d);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
