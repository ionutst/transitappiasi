package com.transitiasi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.transitiasi.R;
import com.transitiasi.utils.TransportItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anca Todirica on 21-Nov-15.
 */
public class TransportationAdapter extends RecyclerView.Adapter<TransportationAdapter.TransportationViewHolder> {
    private List<TransportItem> transportationNumbers;
    private Context context;

    public TransportationAdapter(Context context, List<TransportItem> transportationNumbers) {
        this.transportationNumbers = transportationNumbers;
        this.context = context;
    }

    @Override
    public TransportationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transportation_item, parent, false);
        return new TransportationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final TransportationViewHolder holder, final int position) {
        final TransportItem item = transportationNumbers.get(position);
        final String transportationNumber = item.getNumber();
        if(item.isSelection()){
            holder.tv_transportation_number.setBackgroundResource(R.drawable.circle_background);
        }else{
            holder.tv_transportation_number.setBackgroundResource(R.drawable.circle_background_empy);
        }
        holder.tv_transportation_number.setText(transportationNumber);
        holder.tv_transportation_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_transportation_number.setBackgroundResource(R.drawable.circle_background);
                for (int i = 0; i < transportationNumbers.size(); i++) {
                    transportationNumbers.get(i).setSelection(false);
                }
                transportationNumbers.get(position).setSelection(true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportationNumbers.size();
    }

    public class TransportationViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_transportation_number)
        TextView tv_transportation_number;

        public TransportationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
