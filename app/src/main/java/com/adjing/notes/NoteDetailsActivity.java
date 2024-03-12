package com.adjing.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    String title,content,time,docId;
    TextView editedTime,pageTitle;
    ImageView copyBtn,pasteBtn;
    ImageButton deletenote;
    boolean isEditMode=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setStatusBarColor(Color.TRANSPARENT);


        titleEditText=findViewById(R.id.notes_title_text);
        contentEditText=findViewById(R.id.notes_content_text);
        saveNoteBtn=findViewById(R.id.save_note_btn);
        deletenote=findViewById(R.id.delete_btn);
        copyBtn=findViewById(R.id.copy);
        pasteBtn=findViewById(R.id.paste);
        editedTime=findViewById(R.id.edited_time);
        pageTitle=findViewById(R.id.page_title);


        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        time=getIntent().getStringExtra("time");
        docId=getIntent().getStringExtra("docId");

        // Get clipboard manager object.
        Object clipboardService = getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;

        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        }
        titleEditText.setText(title);
        contentEditText.setText(content);
        editedTime.setText(time);
        if(isEditMode){
            deletenote.setVisibility(View.VISIBLE);
            pageTitle.setText("Edit note");
        }

        saveNoteBtn.setOnClickListener((v)->saveNote());

        deletenote.setOnClickListener((v)-> deleteNoteFromFirebase());


        pasteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clipData = clipboardManager.getPrimaryClip();
                int itemCount = clipData.getItemCount();
                if(itemCount > 0)
                {
                    ClipData.Item item = clipData.getItemAt(0);
                    String text = item.getText().toString();

                    contentEditText.setText(text);
                    Toast.makeText(NoteDetailsActivity.this, "Pasted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srcText = contentEditText.getText().toString();

                ClipData clipData = ClipData.newPlainText("Source Text", srcText);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(NoteDetailsActivity.this, "Copied", Toast.LENGTH_SHORT).show();
            }
        });
    }
    void onEdit(Note note){
        DocumentReference documentReference;
        if(contentEditText.getText().equals("")){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
            editedTime.setText("Edited on "+Utility.timestampToString(note.timestamp));
        }
    }
    void saveNote(){
        String noteTitle=titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        }else{
            documentReference=Utility.getCollectionReferenceForNotes().document();
        }
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this,"Note Saved Successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed to save notes");
                }
            }
        });
    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference;
        documentReference=Utility.getCollectionReferenceForNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(NoteDetailsActivity.this,"Note Deleted Successfully");
                    finish();
                }else{
                    Utility.showToast(NoteDetailsActivity.this,"Failed to delete note");
                }
            }
        });
    }
}