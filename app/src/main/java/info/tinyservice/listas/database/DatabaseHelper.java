package info.tinyservice.listas.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.tinyservice.listas.MainApplication;
import info.tinyservice.listas.WebService;
import info.tinyservice.listas.WebServiceCallback;
import info.tinyservice.listas.model.Personal;
import info.tinyservice.listas.model.Vehicle;
import retrofit2.Response;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "listados.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        createTable(database); // Crear la tabla "gasto"
    }

    /**
     * Crear tabla en la base de datos
     *
     * @param database Instancia de la base de datos
     */
    private void createTable(SQLiteDatabase database) {
        String query = "CREATE TABLE personal (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   nombre TEXT NOT NULL,\n" +
                "   documento TEXT NOT NULL,\n" +
                "   tipo_documento TEXT NOT NULL,\n" +
                "   tipo_personal_id INTEGER NOT NULL\n" +
                ");";
        database.execSQL(query);

        query = "CREATE TABLE tipo_personal (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   nombre TEXT NOT NULL\n" +
                ");";
        database.execSQL(query);

        query =    "CREATE TABLE vehiculo (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   placa TEXT NOT NULL,\n" +
                "   remolque TEXT\n" +
                ");";
        database.execSQL(query);

        query = "CREATE TABLE lista_logger (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   fecha INTEGER NOT NULL,\n" +
                "   almacenadora_id INTEGER NOT NULL, \n" +
                "   raw TEXT \n" +
                ");";
        database.execSQL(query);

        query =  "CREATE TABLE almacenadora (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   nombre TEXT NOT NULL\n" +
                ");";
        database.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS personal;");
        db.execSQL("DROP TABLE IF EXISTS tipo_personal;");
        db.execSQL("DROP TABLE IF EXISTS vehiculo;");
        db.execSQL("DROP TABLE IF EXISTS lista_logger;");
        db.execSQL("DROP TABLE IF EXISTS almacenadora;");
        onCreate(db);
    }

    public void syncSQLiteMySQL(final Context context){
        final MainApplication application = (MainApplication) context;
        final WebService service = application.getService();

        // Updating personal
        service.getAllPersonal().enqueue(new WebServiceCallback<List<Personal>>(context) {
            @Override
            public void onSuccess(Response<List<Personal>> response) {
                deleteAllPersonal();
                for(Personal p : response.body()){
                    insertPersonal(p);
                }
                Toast.makeText(context, "personal actualizado", Toast.LENGTH_SHORT).show();
            }
        });

        //Updating vehicles
        service.getAllVehicles().enqueue(new WebServiceCallback<List<Vehicle>>(context) {
            @Override
            public void onSuccess(Response<List<Vehicle>> response) {
                deleteAllVehicles();
                for(Vehicle v : response.body()){
                    insertVehicle(v);
                }
                Toast.makeText(context, "vehiculos actualizados", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Personal Handler
     */

    public void insertPersonal(Personal p){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", p.getId());
        values.put("nombre", p.getNombre());
        values.put("documento", p.getDocumento());
        values.put("tipo_documento", p.getTipo_documento());
        values.put("tipo_personal_id", p.getTipo_personal_id());
        db.insertOrThrow("personal", null, values);
        db.close();
    }

    public ArrayList<Personal> getAllPersonal(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Personal> personalList = new ArrayList<Personal>();
        String selectQuery = "SELECT id, nombre, documento, tipo_documento, tipo_personal_id FROM personal;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Personal personal = new Personal();
                personal.setId(cursor.getInt(0));
                personal.setNombre(cursor.getString(1));
                personal.setDocumento(cursor.getString(2));
                personal.setTipo_documento(cursor.getString(3));
                personal.setTipo_personal_id(cursor.getInt(4));
                personalList.add(personal);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return personalList;
    }

    public void deleteAllPersonal(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM personal WHERE 1;");
        db.close();
    }


    /**
     * Vehicle handler
     */

    public void insertVehicle(Vehicle v){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", v.getId());
        values.put("placa", v.getPlaca());
        values.put("remolque", v.getRemolque());
        db.insertOrThrow("vehiculo", null, values);
        db.close();
    }

    public ArrayList<Vehicle> getAllVehicle(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        String selectQuery = "SELECT id, placa, remolque FROM vehiculo;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Vehicle vehicle = new Vehicle();
                vehicle.setId(cursor.getInt(0));
                vehicle.setPlaca(cursor.getString(1));
                vehicle.setRemolque(cursor.getString(2));
                vehicleList.add(vehicle);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return vehicleList;
    }

    public void deleteAllVehicles(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM vehiculo WHERE 1;");
        db.close();
    }
}