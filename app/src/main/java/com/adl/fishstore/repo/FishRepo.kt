package com.adl.fishstore.repo

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FishRepo {
    private val firestore = FirebaseFirestore.getInstance()


    fun getFishDetails() = callbackFlow<FishResponse> {

        val collection = firestore.collection("Product")
        val snapshotListener = collection.addSnapshotListener{value,error ->
            run {

                Log.d("Error", error.toString())
                val response = if (error == null) {
                    OnSuccess(value)
                } else {
                    OnFailure(error)
                }

                trySend(response).isSuccess
            }


        }
        awaitClose{
            snapshotListener.remove()
        }

    }
}