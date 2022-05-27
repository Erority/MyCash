package com.example.mycash.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycash.R;
import com.example.mycash.model.OperationCategory;
import com.example.mycash.model.OperationHistory;
import com.example.mycash.services.OperationHistoryService;
import com.example.mycash.utils.AdapterItemClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OperationCategoryAdapter extends RecyclerView.Adapter<OperationCategoryAdapter.ViewHolder> {

    private List<OperationCategory> operationCategories = new ArrayList<>();
    private AdapterItemClickListener adapterItemClickListener;

    private Context context;

    public OperationCategoryAdapter(Context context, AdapterItemClickListener adapterItemClickListener) {
        this.context = context;
        this.adapterItemClickListener = adapterItemClickListener;
    }


    public void setItems(Collection<OperationCategory> operations) {
        operationCategories.addAll(operations);
        notifyDataSetChanged();
    }

    public void clearItems() {
        operationCategories.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public OperationCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_operation_item, parent, false);

        return new OperationCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindToView(operationCategories.get(position));

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterItemClickListener.onItemClicked(operationCategories.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return operationCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private MutableLiveData<OperationCategory> operationCategory = new MutableLiveData<>();

        OperationHistoryService operationHistoryService = new OperationHistoryService();

        ImageView imageView;
        TextView title;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }

        public void bindToView(OperationCategory operationCategory) {
            title.setText(operationCategory.getTitle());

            AssetManager am = context.getResources().getAssets();
            try {
                InputStream is = am.open(operationCategory.getImage());
                Drawable d = Drawable.createFromStream(is, null);
                imageView.setImageDrawable(d);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}