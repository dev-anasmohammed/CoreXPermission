package com.dev.anasmohammed.corex.corexPermission

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.callback.PermissionRequestCallback
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.data.PermissionCategory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CoreXPermission.init(this)
            .permissionCategory(PermissionCategory.Notification)
            .request(object : PermissionRequestCallback {
                override fun onPermissionResult(
                    allGranted: Boolean,
                    grantedList: List<Permission>,
                    deniedList: List<Permission>
                ) {

                }
            })
    }
}