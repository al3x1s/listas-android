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
import info.tinyservice.listas.model.DestinatarioMail;
import info.tinyservice.listas.model.Listado;
import info.tinyservice.listas.model.ListadoLogger;
import info.tinyservice.listas.model.Personal;
import info.tinyservice.listas.model.Vehicle;
import retrofit2.Response;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
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
                "   raw TEXT, emailTo TEXT \n" +
                ");";
        database.execSQL(query);

        query =  "CREATE TABLE almacenadora (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   nombre TEXT NOT NULL\n" +
                ");";
        database.execSQL(query);

        query = "CREATE TABLE destinatario_mail (\n" +
                "   id INTEGER PRIMARY KEY NOT NULL,\n" +
                "   nombre TEXT NOT NULL,\n" +
                "   email TEXT NOT NULL, \n" +
                "   almacenadora_id INTEGER NOT NULL\n" +
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
        db.execSQL("DROP TABLE IF EXISTS destinatario_mail;");
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

        // Updating Listado logger
        service.getAllListado().enqueue(new WebServiceCallback<List<ListadoLogger>>(context) {
            @Override
            public void onSuccess(Response<List<ListadoLogger>> response) {
                deleteAllListadoLogger();
                for(ListadoLogger lg : response.body()) {
                    insertListado(lg);
                }
            }
        });


        // Updating destinatarios
        service.getAllDestinatarios().enqueue(new WebServiceCallback<List<DestinatarioMail>>(context) {
            @Override
            public void onSuccess(Response<List<DestinatarioMail>> response) {
                deleteAllDestinatarios();
                for(DestinatarioMail dm : response.body()){
                    insertDestinatario(dm);
                }
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

    /**
     * Listado logger handler
     */
    public void insertListado(ListadoLogger lg){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", lg.getId());
        values.put("fecha", lg.getFecha());
        values.put("almacenadora_id", lg.getAlmacenadora_id());
        values.put("raw", lg.getRaw());
        values.put("emailTo", lg.getEmailTo());
        db.insertOrThrow("lista_logger", null, values);
        db.close();
    }

    public ArrayList<ListadoLogger> getAllListados(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ListadoLogger> listadosList = new ArrayList<>();
        String selectQuery = "SELECT id, fecha, almacenadora_id, raw, emailTo FROM lista_logger;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ListadoLogger listado = new ListadoLogger();
                listado.setId(cursor.getInt(0));
                listado.setFecha(cursor.getInt(1));
                listado.setAlmacenadora_id(cursor.getInt(2));
                listado.setRaw(cursor.getString(3));
                listado.setEmailTo(cursor.getString(4));
                listadosList.add(listado);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return listadosList;
    }

    public void deleteAllListadoLogger(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM lista_logger WHERE 1;");
        db.close();
    }


    /**
     * Destinatarios mail handler
     */

    public void insertDestinatario(DestinatarioMail dm){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", dm.getId());
        values.put("nombre", dm.getNombre());
        values.put("email", dm.getEmail());
        values.put("almacenadora_id", dm.getAlmacenadora_id());
        db.insertOrThrow("destinatario_mail", null, values);
        db.close();
    }

    public String[] getAllDestinatarios(){
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> emailTo = new ArrayList<>();
        String selectQuery = "SELECT email FROM destinatario_mail;";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                emailTo.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return emailTo.toArray(new String[0]);
    }

    public void deleteAllDestinatarios(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM destinatario_mail WHERE 1;");
        db.close();
    }


}