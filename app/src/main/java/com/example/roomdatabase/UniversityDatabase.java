package com.example.roomdatabase;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Student.class}, version = 1)
public abstract class UniversityDatabase extends RoomDatabase {

    public abstract StudentDao studentDao();

    private static UniversityDatabase instace;

    public static synchronized UniversityDatabase getInstance(Context context) { // Thread safe method
        if(instace == null){
            instace = Room.databaseBuilder(context, UniversityDatabase.class, "univerisity_database")
                    .fallbackToDestructiveMigration() // Will delete all the data in order to prevent
                                                     // crashes from migration (database upgrade or downgrade)
                    .allowMainThreadQueries() // Room database doesn't work in main thread, so we need this for our application; now we can
                                             // query our database in the main thread
                    .addMigrations(MIGRATION_1_2)
                    .addCallback(initialCallback)
                    .build();
        }
        return instace;
    }
    // migrating from version 1 of database to version 2 <=> upgrade & downgrade in SQL
    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE students ADD COLUMN grade INTEGER;");
        }
    };

    private static RoomDatabase.Callback initialCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new InitialAsyncTask(instace).execute();
        }
    };

    private static class InitialAsyncTask extends AsyncTask<Void, Void, Void>{

        private StudentDao studentDao;

        public InitialAsyncTask(UniversityDatabase database){
            studentDao = database.studentDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            studentDao.insert(new Student("Meisam", "meisam@meiCode.org", "2938429238"));
            studentDao.insert(new Student("Catalin", "catalin@yahoo.com", "0741583249"));
            return null;
        }
    }
}
