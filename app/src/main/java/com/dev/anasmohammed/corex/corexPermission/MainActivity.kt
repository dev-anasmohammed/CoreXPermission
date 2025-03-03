package com.dev.anasmohammed.corex.corexPermission

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dev.anasmohammed.corex.permission.abstraction.dialog.BaseDialogFragment
import com.dev.anasmohammed.corex.permission.core.CoreXPermission
import com.dev.anasmohammed.corex.permission.core.callback.PermissionRequestCallback
import com.dev.anasmohammed.corex.permission.core.models.DialogAttrs
import com.dev.anasmohammed.corex.permission.core.models.Permission
import com.dev.anasmohammed.corex.permission.data.CoreXPermissions
import com.dev.anasmohammed.corex.permission.data.PermissionCategory

class MainActivity : AppCompatActivity(), PermissionRequestCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //request
//        requestPermissionRequestCase1()
        requestPermissionRequestCase2()
        //requestPermissionRequestCase3()
        //requestPermissionRequestCase4()

    }

    /** -------------- How To Request -------------- **/

    /**
     * Request permission by specify predefined categories [PermissionCategory]
     * You can also disable/enable checking of manifest permissions check
     */
    private fun requestPermissionRequestCase1() {
        CoreXPermission.init(this)
            .permissionCategory(PermissionCategory.Location.LowAccurateTracking, true)
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    /**
     * Request permission by specify predefined permissions [CoreXPermissions] Method 1 - vararg
     */
    private fun requestPermissionRequestCase2() {
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    /**
     * Request permission by specify predefined permissions [CoreXPermissions] Method 2 - list
     * You can also disable/enable checking of manifest permissions check
     */
    private fun requestPermissionRequestCase3() {
        CoreXPermission.init(this)
            .permissions(listOf(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio), false)
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    /**
     * You can show explain/rationale dialog before request permissions  showExplainDialogBeforeRequest()
     * You can set if dialog is cancelable or not                        setDialogCancelable()
     * You can set action when negative/cancel button clicked            setDialogNegativeAction{}
     */
    private fun requestPermissionRequestCase4() {
        CoreXPermission.init(this)
            .permissions(listOf(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio), false)
            .showExplainDialogBeforeRequest(true)
            .setDialogCancelable(false)
            .setDialogNegativeAction { }
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    /**
     * You can now Edit the default values of default dialog of Explain Dialog or Go To Settings Dialog
     */
    private fun requestPermissionRequestCase5() {
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .showExplainDialogBeforeRequest(true)
            .setDialogCancelable(false)
            .setDialogNegativeAction { }
            .onExplainPermissionRequest { scope, deniedList ->
                scope.showDefaultDialog(
                    deniedList,
                    DialogAttrs(titleTextColor = R.color.black)
                )

                scope.showDialogFragment(CustomDialog())
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showDefaultDialog(
                    deniedList,
                    DialogAttrs(titleTextColor = R.color.black)
                )
            }
            .request { allGranted, grantedList, deniedList ->
                handleResult(allGranted, grantedList, deniedList)
            }
    }

    /** -------------- How To Handle Request -------------- **/

    private fun requestPermissionCase30() {
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request(object : PermissionRequestCallback {
                override fun onPermissionResult(
                    allGranted: Boolean,
                    grantedList: List<Permission>,
                    deniedList: List<Permission>
                ) {
                    handleResult(allGranted, grantedList, deniedList)
                }
            })
    }

    /**
     * Request permission and handle result with inline interface
     */
    private fun requestPermissionCase00() {
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request(object : PermissionRequestCallback {
                override fun onPermissionResult(
                    allGranted: Boolean,
                    grantedList: List<Permission>,
                    deniedList: List<Permission>
                ) {
                    handleResult(allGranted, grantedList, deniedList)
                }
            })
    }

    /**
     * Request permission and handle result with interface
     */
    private fun requestPermission() {
        CoreXPermission.init(this)
            .permissions(CoreXPermissions.Camera, CoreXPermissions.RecordingAudio)
            .request(this)
    }

    override fun onPermissionResult(
        allGranted: Boolean,
        grantedList: List<Permission>,
        deniedList: List<Permission>
    ) {
        handleResult(allGranted, grantedList, deniedList)
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


class CustomDialog : BaseDialogFragment() {
    override fun getPositiveButton(): View {
        TODO("Not yet implemented")
    }

    override fun getNegativeButton(): View {
        TODO("Not yet implemented")
    }

    override fun getPermissionsToRequest(): List<Permission> {
        TODO("Not yet implemented")
    }
}