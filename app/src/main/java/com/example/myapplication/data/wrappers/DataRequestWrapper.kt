package com.example.myapplication.data.wrappers

class DataRequestWrapper<T,Boolean,E:Exception> (
    var data:T?=null,
    var state: kotlin.String?=null,
    var exception:E?=null
)