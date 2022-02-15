package com.example.fa_vergeldelacruz_835208_android;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.fa_vergeldelacruz_835208_android.databinding.ActivityFavoritePlaceMapBinding;
import com.example.fa_vergeldelacruz_835208_android.entity.FavoritePlace;
import com.example.fa_vergeldelacruz_835208_android.util.FavoritePlaceRoomDB;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddorUpdateFavoritePlaceActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
//    private ActivityAddorUpdateFavoritePlace binding;

    private ActivityFavoritePlaceMapBinding binding;
    private FavoritePlaceRoomDB favoritePlaceRoomDB;
    private boolean isEditing;
    private int id ;

    private Marker marker;
    private Location markerLoc;
    private float distance = 0;
    private String address;
    private double latitude;
    private double longitude;
    private String date;
    private boolean visited;

    private static final String TAG = "AddorUpdateFavoritePlaceActivity";
    private static final int REQUEST_CODE = 1;
    private static final int ZOOM_LEVEL = 14;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private static final int UPDATE_INTERVAL = 5; // 5 seconds
    private static final int FASTEST_INTERVAL = 3; // 3 seconds

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityFavoritePlaceMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        favoritePlaceRoomDB = FavoritePlaceRoomDB.getInstance(this);
        Intent i = getIntent();
        isEditing = i.getBooleanExtra("isEditing",false);
        if (isEditing) {
            id = i.getIntExtra("id",0);
            address = i.getStringExtra("address");
            latitude = i.getDoubleExtra("latitude", 0);
            longitude = i.getDoubleExtra("longitude", 0);
            date = i.getStringExtra("date");
            visited = i.getBooleanExtra("visited", false);
            if (address.isEmpty()) {
                binding.etAddress.setText(date);
            } else {
                binding.etAddress.setText(address);
            }
            binding.etLatitude.setText(String.valueOf(latitude));
            binding.etLongitude.setText(String.valueOf(longitude));
            binding.cbVisited.setChecked(visited);
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // add permissions
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(
                    new String[permissionsToRequest.size()]), REQUEST_CODE);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOrUpdateFavoritePlace();
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMain();
            }
        });
    }
    private void goBackToMain() {
        startActivity(new Intent(this,MainActivity.class));
    }

    private void addOrUpdateFavoritePlace() {
        String address = binding.etAddress.getText().toString().trim();
        String latitude =  binding.etLatitude.getText().toString().trim();
        String longitude = binding.etLongitude.getText().toString().trim();
        boolean visited = binding.cbVisited.isChecked();

        Date finalDate = new Date();
        if (latitude.isEmpty()) {
            binding.etLatitude.setError("latitude field is empty");
            binding.etLatitude.requestFocus();
            return;
        }
        if (latitude.isEmpty()) {
            binding.etLongitude.setError("longitude field is empty");
            binding.etLongitude.requestFocus();
            return;
        }
        if (isEditing) {
            favoritePlaceRoomDB.favoritePlaceDao().updateFavoritePlace(id,
                    address, latitude, longitude, visited);
        } else {
            FavoritePlace favoritePlace = new FavoritePlace(address,finalDate,
                    Double.parseDouble(latitude),Double.parseDouble(longitude),visited);
            favoritePlaceRoomDB.favoritePlaceDao().insertFavoritePlace(favoritePlace);
        }
        startActivity(new Intent(this,MainActivity.class));
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    private List<String> permissionsToRequest(List<String> permissions) {
        ArrayList<String> results = new ArrayList<>();
        for (String perm: permissions) {
            if (!hasPermission(perm))
                results.add(perm);
        }
        return results;
    }
    private boolean hasPermission(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        // this is a proper place to check the google play services availability

        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(AddorUpdateFavoritePlaceActivity.this, "No Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
            Log.i(TAG, "onPostResume: ");
            findLocation();
        }
    }
    @SuppressLint("MissingPermission")
    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.i(TAG,String.format("onSuccess Lat: %s, Lng: %s", location.getLatitude(), location.getLongitude()));
                    }
                }
            });
        }
        startUpdateLocation();
    }
    @SuppressLint("MissingPermission")
    private void startUpdateLocation() {
        Log.d(TAG, "startUpdateLocation: ");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    Log.i(TAG,String.format("onLocationResult1 Lat: %s, Lng: %s", location.getLatitude(), location.getLongitude()));

                    if (markerLoc != null) {
                        distance = location.distanceTo(markerLoc);
                        Log.i(TAG, String.format("onLocationResult distance %s", distance));
                        marker.setSnippet(buildSnippet());
                    }
                }
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                locationCallback, null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (String perm: permissions) {
                if (!hasPermission(perm))
                    permissionsRejected.add(perm);
            }

            if (permissionsRejected.size() > 0 ) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(AddorUpdateFavoritePlaceActivity.this)
                            .setMessage("The location permission is mandatory")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE);
                                    }

                                }
                            }).setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        if (latitude != 0) {
            markerLoc = new Location("Marker");
            markerLoc.setLatitude(latitude);
            markerLoc.setLongitude(longitude);
            LatLng latLng = new LatLng(latitude, longitude);
            String title = address;
            if (title.isEmpty()) {
                title = date;
            }
            String markerSnippet = buildSnippet();

            marker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(title)
                    .draggable(true)
                    .snippet(markerSnippet));
            mMap.moveCamera(newLatLngZoom(latLng, ZOOM_LEVEL));

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDrag(@NonNull Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    latitude = marker.getPosition().latitude;
                    longitude = marker.getPosition().longitude;
                    binding.etLatitude.setText(String.valueOf(latitude));
                    binding.etLongitude.setText(String.valueOf(longitude));
                }

                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {

                }
            });
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                // Return null here, so that getInfoContents() is called next.
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // Inflate the layouts for the info window, title and snippet.
                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                            (FrameLayout) findViewById(R.id.map), false);

                    TextView title = infoWindow.findViewById(R.id.title);
                    title.setText(marker.getTitle());

                    TextView snippet = infoWindow.findViewById(R.id.snippet);
                    snippet.setText(marker.getSnippet());

                    return infoWindow;
                }
            });


        }

        if (!isEditing) {
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng destination) {
                    if (marker != null) {
                        mMap.clear();
                    }
                    MarkerOptions options = new MarkerOptions();
                    options.position(destination);
                    latitude = destination.latitude;
                    longitude = destination.longitude;
                    marker = mMap.addMarker(options);
                    marker.setSnippet(buildSnippet());
                    binding.etLatitude.setText(String.valueOf(latitude));
                    binding.etLongitude.setText(String.valueOf(longitude));
                }
            });
        }
    }

    private String buildSnippet() {
        String markerSnippet = " Latitude: " + latitude;
        markerSnippet = markerSnippet + "\n Longitude: " + longitude;
        markerSnippet = markerSnippet + "\n Visited: " +  (visited == true ? "Yes": "No");
        if (distance > 0) {
            markerSnippet = markerSnippet + "\n Distance: " + distance + " m";
        }
        return markerSnippet;
    }
}