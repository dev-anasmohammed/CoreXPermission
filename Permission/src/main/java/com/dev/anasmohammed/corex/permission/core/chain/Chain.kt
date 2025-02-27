package com.dev.anasmohammed.corex.permission.core.chain

import com.dev.anasmohammed.corex.permission.core.PermissionBuilder
import com.dev.anasmohammed.corex.permission.core.handlers.base.BasePermissionHandler

/**
 * Maintain the handler chain of permission request process.
 */
class Chain(private val permissionBuilder: PermissionBuilder) {
    /**
     * Holds the first handler of request process. Permissions request begins here.
     */
    private var headHandler: BasePermissionHandler? = null

    /**
     * Holds the last handler of request process. Permissions request ends here.
     */
    private var tailHandler: BasePermissionHandler? = null

    /**
     * Add a handler into handler chain.
     * @param handler handler to add.
     */
    internal fun addHandlerToChain(handler: BasePermissionHandler) {
        if (headHandler == null) {
            headHandler = handler
        }
        // add handler to the tail
        tailHandler?.next = handler
        tailHandler = handler
    }

    /**
     * Run this handler chain from the first handler.
     */
    internal fun runHandler() {
        headHandler?.request(permissionBuilder)
    }
}