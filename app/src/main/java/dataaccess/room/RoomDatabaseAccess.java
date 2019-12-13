package dataaccess.room;

import dataaccess.setup.AppDatabase;

public class RoomDatabaseAccess {
    AppDatabase db;

    public AppDatabase getDb() {
        return db;
    }

    public void setDb(AppDatabase db) {
        this.db = db;
    }
}
