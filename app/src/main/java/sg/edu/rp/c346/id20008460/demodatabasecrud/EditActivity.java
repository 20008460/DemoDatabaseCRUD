package sg.edu.rp.c346.id20008460.demodatabasecrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class EditActivity extends AppCompatActivity {

    TextView tvID;
    EditText etContent;
    Button btnUpdate, btnDelete;
    Note data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //initialize the variables with UI here
        btnUpdate = findViewById(R.id.buttonUpdate);
        btnDelete = findViewById(R.id.buttonDelete);
        etContent = findViewById(R.id.etEdit);
        tvID = findViewById(R.id.tvID);

        Intent i = getIntent();
        data = (Note) i.getSerializableExtra("data"); //Must specify what object it is

        tvID.setText("ID: " + data.getId());
        etContent.setText(data.getNoteContent());


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);
                data.setNoteContent(etContent.getText().toString());
                dbh.updateNote(data);
                dbh.close();

                finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbh = new DBHelper(EditActivity.this);
                dbh.deleteNote(data.getId());

                finish();
            }
        });


    }



}
