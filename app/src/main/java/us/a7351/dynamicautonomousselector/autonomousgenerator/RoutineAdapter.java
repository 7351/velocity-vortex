package us.a7351.dynamicautonomousselector.autonomousgenerator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    Program program;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        CardView cardView;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }

    }

    public RoutineAdapter () {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (program.routines.size() <= 0) {
            return 0;
        } else {
            return program.routines.size();
        }
    }

}