package com.dev.anasmohammed.corex.permission.core

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * The start point of the library that init it and return [PermissionMediator]
 * that used to sort permissions between normal , special or remove it.
 */
object CoreXPermission : DefaultLifecycleObserver {

    /**
     * Indicate if dialog is showed or not do don't show it again
     */
    var isDialogShowed: Boolean = false

    /**
     * Indicate if can start the flow of requesting permissions again or not
     * to fix the issue when call the library onResume
     */
    var canRequestAgain: Boolean = true

    /**
     * Indicate if called at on resume or not to handle this case
     */
    var isCalledAtOnResume: Boolean = false

    /** Init CoreXPermission to make everything prepare to work **/
    fun init(activity: FragmentActivity): PermissionMediator {
        isCalledAtOnResume = true
        activity.lifecycle.addObserver(this)
        return PermissionMediator(activity = activity)
    }

    /** Init CoreXPermission to make everything prepare to work **/
    fun init(fragment: Fragment): PermissionMediator {
        isCalledAtOnResume = true
        fragment.requireActivity().lifecycle.addObserver(this)
        return PermissionMediator(fragment = fragment)
    }

    /**
     * This function used to ensure that your request runs once with one dialog and avoid
     * many request with lifecycle changes. Also to fix the issue with call the library on resume
     */
    fun canRequestPermissionNow(): Boolean {
        return canRequestAgain && !isCalledAtOnResume
    }

    /**
     * Override it to handle if permissions called at on resume but you need to
     * surrounded your CoreXPermission call with [canRequestPermissionNow] to avoid issues or crashes.
     * This done because when call library at onResume and go to settings or request permission it fire
     * onRequestCallback twice because activity goes across lifecycle changes i.e onResume , onPause , ....
     */
    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        if (canRequestAgain) {
            isCalledAtOnResume = false
        }
    }
}