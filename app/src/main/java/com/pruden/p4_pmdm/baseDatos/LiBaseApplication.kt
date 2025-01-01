package com.pruden.p4_pmdm.baseDatos

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class LiBaseApplication : Application(){
    companion object{
        lateinit var database: LiBaseDatabase
    }

    override fun onCreate(){
        super.onCreate()

       //this.deleteDatabase("PartidasDatabase")

        database = Room.databaseBuilder(
            this,
            LiBaseDatabase::class.java,
            "PartidasDatabase"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            //.fallbackToDestructiveMigration()
            .build()

    }


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS FavoritosEntity (
                idUsuario INTEGER NOT NULL,
                idPartida INTEGER NOT NULL,
                PRIMARY KEY(idUsuario, idPartida),
                FOREIGN KEY(idUsuario) REFERENCES UsuarioEntity(id) ON DELETE CASCADE,
                FOREIGN KEY(idPartida) REFERENCES PartidaEntity(id) ON DELETE CASCADE
            )
            """
            )
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS VisibleEntity (
                idUsuario INTEGER NOT NULL,
                idPartida INTEGER NOT NULL,
                PRIMARY KEY(idUsuario, idPartida),
                FOREIGN KEY(idUsuario) REFERENCES UsuarioEntity(id) ON DELETE CASCADE,
                FOREIGN KEY(idPartida) REFERENCES PartidaEntity(id) ON DELETE CASCADE
            )
            """
            )
        }
    }
}