package com.adl.fishstore.repo

import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

sealed class FishResponse
data class OnSuccess(val querySnapshot: QuerySnapshot?):FishResponse()
data class OnFailure(val exeception: FirebaseFirestoreException?):FishResponse()
