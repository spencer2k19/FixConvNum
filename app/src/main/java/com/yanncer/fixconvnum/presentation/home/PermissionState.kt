package com.yanncer.fixconvnum.presentation.home

//State for permissions
sealed class PermissionState {
    object Idle : PermissionState()
    object Denied : PermissionState()
    object Granted : PermissionState()
    object Permanently : PermissionState()
}