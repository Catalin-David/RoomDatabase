package com.example.roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private UniversityDatabase universityDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        universityDatabase = UniversityDatabase.getInstance(this);

//        universityDatabase.studentDao().insert(new Student("Tom", "tom@meiCode.org", "3284231100"));
  //      universityDatabase.studentDao().insert(new Student("Sarah", "sarah@meiCode.org", "2459402031"));
          //universityDatabase.studentDao().deleteByName("Tom");
         // universityDatabase.studentDao().deleteByName("Sarah");

        LiveData<List<Student>> liveStudents = universityDatabase.studentDao().liveStudents();
        liveStudents.observe(this, new Observer<List<Student>>() {
            @Override
            public void onChanged(List<Student> students) {
                String allStudentsTxt = "";
                for(Student s: students){
                    allStudentsTxt += s.get_id() + " : " + s.getName() + '\n';
                }

                textView.setText(allStudentsTxt);
            }
        }); //life-cycle aware

        //universityDatabase.studentDao().insert(new Student("Madalina", "madalina@yahoo.com", "3242388110"));

        //new MimicUpdateDbAsyncTask().execute();
    }

    private class MimicUpdateDbAsyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<Student> students = new ArrayList<>();
            students.add(new Student("Jerry", "jerry@yahoo.com", "25235235344"));
            students.add(new Student("Brian", "brian@yahoo.com", "4332432232"));
            students.add(new Student("Nicole", "cherry@yahoo.com", "2590431199"));

            for (Student s: students){
                try {
                    Thread.sleep(2000);
                    universityDatabase.studentDao().insert(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }
    }
}
