package com.yanncer.fixconvnum.presentation.main

interface Destination {
    var route: String
}

object Home : Destination {
    override var route: String
        get() = "home"
        set(value) {}

}