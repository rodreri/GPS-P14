package mx.erick.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import mx.erick.gps.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var lm: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = Listener()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            lm.removeUpdates(locationListener)
        } else {
            val criteria = Criteria()
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            var provider = lm.getBestProvider(criteria, true);
            if (provider != null) {
                lm.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, 0, 10.0.toFloat(), locationListener
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lm.removeUpdates(locationListener)
    }

    inner class Listener() : LocationListener {
        override fun onLocationChanged(location: Location) {
            if (location != null) {
                runOnUiThread {
                    binding.textGPS.text = "Localización cambio:\n" +
                            "Latitud: " + location.latitude.toString() +
                            "\nLongitud: " + location.longitude.toString()
                }
                Toast.makeText(
                    binding.root.context, "Localización cambio: Lat:" + location.latitude
                            + " Long:" + location.longitude, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}