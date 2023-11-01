package com.example.customviewdemo.network


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class ServerApplication {
    val scope = CoroutineScope(SupervisorJob())
    val service by lazy { ServerService() }
    val serverRepository by lazy { ServerRepository(scope, service) }
}
