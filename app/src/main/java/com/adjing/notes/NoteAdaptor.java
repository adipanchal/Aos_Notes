package com.adjing.notes;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.api.Context;

public class NoteAdaptor extends FirestoreRecyclerAdapter<Note,NoteAdaptor.NoteviewHolder> {
    MainActivity context;
    public NoteAdaptor(@NonNull FirestoreRecyclerOptions<Note> options, MainActivity context) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(@NonNull NoteviewHolder holder, int position, @NonNull Note note) {
        holder.title.setText(note.title);
        holder.content.setText(note.content);
        holder.time.setText(Utility.timestampToString(note.timestamp));

        holder.itemView.setOnClickListener((v)->{
            Intent intent=new Intent(context,NoteDetailsActivity.class);
            intent.putExtra("title",note.title);
            intent.putExtra("content",note.content);
            intent.putExtra("time",note.timestamp);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteviewHolder(view);
    }

    class NoteviewHolder extends RecyclerView.ViewHolder{
        TextView title,content,time;

        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.note_title);
            content=itemView.findViewById(R.id.note_content);
            time=itemView.findViewById(R.id.note_time);
        }
    }
}
