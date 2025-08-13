package com.example.quizforkids;


import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class AttemptAdapter extends RecyclerView.Adapter<AttemptAdapter.ViewHolder> {
    private List<QuizAttempt> attempts;

    public AttemptAdapter(List<QuizAttempt> attempts) {
        this.attempts = attempts;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ViewHolder(TextView v) {
            super(v);
            textView = v;
        }
    }

    @Override
    public AttemptAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView v = new TextView(parent.getContext());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        QuizAttempt attempt = attempts.get(position);
        holder.textView.setText(
                attempt.getQuizArea() + " area - attempt started on " +
                        attempt.getDateTime() + " – points earned " + attempt.getPoints());
    }

    @Override
    public int getItemCount() {
        return attempts.size();
    }

    public void updateList(List<QuizAttempt> newList) {
        attempts = newList;
        notifyDataSetChanged();
    }
}
