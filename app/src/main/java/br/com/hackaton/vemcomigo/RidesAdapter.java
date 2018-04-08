package br.com.hackaton.vemcomigo;

import android.content.Context;
import android.content.Intent;
import android.icu.text.StringSearch;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private List<Ride> rides;
    private Ride currentRide;
    private Ride selectedRide;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rider_name)
        public TextView mTextView;

        private View itemView;
        private View.OnClickListener onClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            itemView.setOnClickListener(onClickListener);
        }
    }


    public RidesAdapter(Context context, List<Ride> rides, Ride currentRide) {
        this.rides = rides;
        this.currentRide = currentRide;
        this.context = context;
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
        if (rides.get(position).getStartPoint().getLatitude() != null) {
            if (!rides.get(position).getUserId().equals(currentRide.getUserId())){
                holder.mTextView.setText(rides.get(position).getUserId()+" vai para um local a "+
                        String.valueOf(currentRide.getEndDistance(rides.get(position)))+
                " metros do seu local de destino. ");

                holder.setOnClickListener(getOnClickListener(position));
            }
        }

    }

    @NonNull
    private View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRide = rides.get(position);
                Intent confirmationIntent = new Intent(context, ConfirmationActivity.class);
                context.startActivity(confirmationIntent);
            }
        };
    }

    @Override
    public int getItemCount() {
        return this.rides.size();
    }

    public void setRides(List<Ride> rides) {
        this.rides = rides;
    }

    public void setCurrentRide(Ride currentRide) {
        this.currentRide = currentRide;
    }
}
