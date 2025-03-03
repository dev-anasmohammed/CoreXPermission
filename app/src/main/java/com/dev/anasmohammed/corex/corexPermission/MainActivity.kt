package com.dev.anasmohammed.corex.corexPermission

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.data.PermissionCategory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()
    }

    private fun requestPermissions() {
        CoreXPermission.init(this)
            .permissionCategory(PermissionCategory.Location.LowAccurateTracking, false)
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    private fun handleResult(
        allGranted: Boolean,
        grantedList: List<Permission>,
        deniedList: List<Permission>
    ) {
        when (allGranted) {
            true -> {
                Log.e(
                    "CoreXPermission",
                    "All Permission Granted , Granted List: $grantedList"
                )

                Toast.makeText(this@MainActivity, "Granted", Toast.LENGTH_LONG).show()
            }

            false -> {
                Log.e(
                    "CoreXPermission",
                    "Some Permissions Denied ,Denied List: $deniedList"
                )

                Toast.makeText(this@MainActivity, "Denied", Toast.LENGTH_LONG).show()
            }
        }
    }
}