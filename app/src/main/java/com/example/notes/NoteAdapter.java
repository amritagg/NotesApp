package com.example.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notes.data.NotesEntity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private static final String DATE_FORMAT = "dd MMM, YYYY  HH:mm:ss";
    private List<NotesEntity> mNoteEntities;
    private final Context mContext;
    @SuppressLint("WeekBasedYear")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private final ItemClickListener mItemClickListener;

    public NoteAdapter(Context context, ItemClickListener listener){
        mContext = context;
        mItemClickListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        if(getItemCount() != 0){
            NotesEntity notesEntity = mNoteEntities.get(position);
            String heading = notesEntity.getHeading();
            String body = notesEntity.getBody();
            String date = dateFormat.format(notesEntity.getLastUpdate());
            holder.heading_view.setVisibility(View.VISIBLE);
            holder.body_view.setVisibility(View.VISIBLE);

            if(heading.isEmpty()) holder.heading_view.setVisibility(View.GONE);
            else holder.heading_view.setText(heading);

            if(body.isEmpty()) holder.body_view.setVisibility(View.GONE);
            else holder.body_view.setText(body);

            holder.date_view.setText(date);
        }
    }

    @Override
    public int getItemCount() {
        if(mNoteEntities == null) return 0;
        return mNoteEntities.size();
    }

    public void setTasks(List<NotesEntity> entities){
        mNoteEntities = entities;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView heading_view;
        TextView body_view;
        TextView date_view;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            heading_view = itemView.findViewById(R.id.heading);
            body_view = itemView.findViewById(R.id.body);
            date_view = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = mNoteEntities.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }

}
