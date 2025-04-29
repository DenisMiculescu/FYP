package com.example.fyp.firebase.services

import android.net.Uri
import com.google.android.gms.tasks.Task

interface StorageService {
    suspend fun uploadFile(uri: Uri, directory: String): Task<Uri>
}
