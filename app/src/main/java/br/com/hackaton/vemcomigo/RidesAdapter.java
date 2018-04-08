package br.com.hackaton.vemcomigo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.StringSearch;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.Profile;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RidesAdapter extends RecyclerView.Adapter<RidesAdapter.ViewHolder> {

    private List<Ride> rides;
    private Ride currentRide;
    private Ride selectedRide;
    private Context context;
    List<Users> list =  Users.getUsersList();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.rider_name)
        public TextView mTextView;

        @BindView(R.id.mutual_friends)
        public TextView mutualFriends;

        @BindView(R.id.education)
        public TextView education;

        @BindView(R.id.distance)
        public TextView distance;

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

        Users user = getUserInfo(position);

        String distanceStr = String.valueOf( (int) currentRide.getEndDistance(rides.get(position)));
        holder.mTextView.setText(rides.get(position).getUserId());

        holder.distance.setText("Vai para um local a "+ distanceStr +
                " metros do seu local de destino. ");

        holder.mutualFriends.setText(user.getCommonFriends() + " amigos em comum");

        holder.education.setText("Estuda " + user.getCurso());

        holder.setOnClickListener(getOnClickListener(position));

    }

    private Users getUserInfo(int position) {
        for (Users user : list) {
            if (user.getUserId().equals(rides.get(position).getUserId())) {
                return user;
            }

        }
        return list.get(0);
    }

    @NonNull
    private View.OnClickListener getOnClickListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage(R.string.dialog_message)
                        .setTitle(R.string.dialog_title);

                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        selectedRide = rides.get(position);
                        Intent confirmationIntent = new Intent(context, ConfirmationActivity.class);
                        context.startActivity(confirmationIntent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
