package com.example.fyp.data.api

import com.example.fyp.data.ReceiptModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ReceiptService {
    @GET(ServiceEndPoints.RECEIPTS_ENDPOINT)
    suspend fun getall(): Response<List<ReceiptModel>>

    @GET(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{id}")
    suspend fun get(@Path("id") id: String): Response<List<ReceiptModel>>

    @DELETE(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{id}")
    suspend fun delete(@Path("id") id: String): ReceiptWrapper

    @POST(ServiceEndPoints.RECEIPTS_ENDPOINT)
    suspend fun post(@Body receipt: ReceiptModel): ReceiptWrapper

    @PUT(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{id}")
    suspend fun put(@Path("id") id: String,
                    @Body receipt: ReceiptModel
    ): ReceiptWrapper
}
