package br.com.hackaton.vemcomigo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private List<Ride> rides;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ride_name)
        public TextView mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    public RidesAdapter(List<Ride> rides) {
        this.rides = rides;
    }

    @NonNull
    @Override
    public RidesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ride_item_list, parent, false);

        RidesAdapter.ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RidesAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(rides.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return this.rides.size();
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }
}
