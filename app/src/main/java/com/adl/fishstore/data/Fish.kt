package com.adl.fishstore.data

class Fish(val productName:String, val price:String, val category:String, val image:String,val description:String, val fullDescription:String  ) {
    constructor():this("","","","","","")
}