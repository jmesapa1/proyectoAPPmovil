package com.example.julian.proyecto1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ruta extends AppCompatActivity {

    Context mContext;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    EditText nombreRuta;
    Button botonEmpezar,botonTerminar;
    int contador=1;
    String horaInicio,horaFinal;
    Boolean flagTermino=false;
    LocationListener locationListener;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ruta);
        mContext = this;
        locationManager = (LocationManager) getSystemService(ruta.this.LOCATION_SERVICE);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();


        nombreRuta = (EditText) findViewById(R.id.input_nombreRuta);
        botonEmpezar = (Button) findViewById(R.id.btn_empezar);
        botonTerminar = (Button) findViewById(R.id.btn_terminar);
        botonTerminar.setVisibility(View.INVISIBLE);
        botonEmpezar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {



                empezarRuta();
                listener();
                v.setVisibility(View.INVISIBLE);
                nombreRuta.setVisibility(View.INVISIBLE);
                botonTerminar.setVisibility(View.VISIBLE);







            }
        });

        botonTerminar.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {




                v.setVisibility(View.INVISIBLE);
                nombreRuta.setVisibility(View.VISIBLE);
                botonEmpezar.setVisibility(View.VISIBLE);








            }
        });




    }



public void listener(){
    if (ActivityCompat.checkSelfPermission(ruta.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ruta.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling

        return;
    }


    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
     locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            contador=contador+1;

            String latitud = new Double(location.getLatitude()).toString();
            String longitud = new Double(location.getLongitude()).toString();
            guardar(latitud,longitud);




        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,15000,0,locationListener);

}



public void guardar(String latitud,String longitud){

    String usuario = user.getEmail().replace('@', '0');
    usuario = usuario.replace('.', '0');
   String numero = new Integer(contador).toString();


    DatabaseReference myRef = database.getReference("Usuarios/" + usuario );

    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("coordenadas").child(numero).child("latitud").setValue(latitud);
    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("coordenadas").child(numero).child("longitud").setValue(longitud);



}

public void empezarRuta() {
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    horaInicio = df.format(calendar.getTime());
    String usuario = user.getEmail().replace('@', '0');
    usuario = usuario.replace('.', '0');


    DatabaseReference myRef = database.getReference("Usuarios/" + usuario );

    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("nombre").setValue(nombreRuta.getText().toString());
    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("hora inicio").setValue(horaInicio);

}

public void terminar(){
    DateFormat df = new SimpleDateFormat("HH:mm:ss");
    Calendar calendar = Calendar.getInstance();

    horaFinal = df.format(calendar.getTime());
    String usuario = user.getEmail().replace('@', '0');
    usuario = usuario.replace('.', '0');


    DatabaseReference myRef = database.getReference("Usuarios/" + usuario );

    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("nombre").setValue(nombreRuta.getText().toString());
    myRef.child("Rutas").child(nombreRuta.getText().toString()).child("hora fin").setValue(horaFinal);
    locationManager.removeUpdates(locationListener);
    nombreRuta.setText("");
}

}
