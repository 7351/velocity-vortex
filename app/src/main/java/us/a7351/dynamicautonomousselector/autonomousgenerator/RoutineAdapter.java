package us.a7351.dynamicautonomousselector.autonomousgenerator;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.a7351.dynamicautonomousselector.R;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private Program program;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {

        CardView cardView;

        public ViewHolder(CardView itemView) {
            super(itemView);
            cardView = itemView;
        }

    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public RoutineAdapter () {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_encoder_drive, parent);
        return new ViewHolder(view);
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