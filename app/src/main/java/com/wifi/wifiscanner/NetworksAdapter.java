package com.wifi.wifiscanner;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NetworksAdapter extends RecyclerView.Adapter<NetworkHolder> {

    private Report report;

    public NetworksAdapter(Report report) {
        this.report = report;
    }

    @NonNull
    @Override
    public NetworkHolder onCreateViewHolder(@NonNull ViewGroup recycler, int i) {
        LayoutInflater inflater = LayoutInflater.from(recycler.getContext());
        View view = inflater.inflate(R.layout.network_info, recycler, false);
        return new NetworkHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetworkHolder networkHolder, int i) {
        Result networkInfo = this.report.getResults().get(i);
        networkHolder.bind(networkInfo);
    }

    @Override
    public int getItemCount() {
        return this.report.getResults().size();
    }


}
