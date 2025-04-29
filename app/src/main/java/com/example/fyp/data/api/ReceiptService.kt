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


    @GET(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{email}")
    suspend fun getall(
        @Path("email") email: String)
            : Response<List<ReceiptModel>>

    @GET(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{email}" + "/{id}")
    suspend fun get(@Path("email") email: String,
                    @Path("id") id: String): Response<List<ReceiptModel>>

    @DELETE(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{email}" + "/{id}")
    suspend fun delete(@Path("email") email: String,
                       @Path("id") id: String): Response<Unit>

    @POST(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{email}")
    suspend fun post(@Path("email") email: String,
                     @Body receipt: ReceiptModel): ReceiptWrapper

    @PUT(ServiceEndPoints.RECEIPTS_ENDPOINT + "/{email}" + "/{id}")
    suspend fun put(@Path("email") email: String,
                    @Path("id") id: String,
                    @Body receipt: ReceiptModel
    ): ReceiptWrapper
}

