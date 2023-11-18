package com.pkt.emarkit.api

import com.pkt.emarkit.api.api_models.*
import com.pkt.emarkit.utils.preferences.PrefsSessionManagement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


interface ApiService {
 @POST("login")
 fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

 @POST("delivery/list")
 fun getPendingDelivery(@Body pendingDeliveryRequest: PendingDeliveryRequest) : Call<PendingDeliveryResponse>

 @GET("delivery/dashboard")
 fun getDashboard():Call<DashboardResponse>

 @POST("delivery/cancel")
 fun cancleDelivery(@Body cancelrequest: cancelRequest):Call<cancelResponse>

 @POST("delivery/confirm")
 fun confirmDelivery(@Body confirmdeliveryRequest:confirmDeliveryRequest):Call<confirmDeliveryResponse>

 @POST("delivery/cashbook-list")
 fun cashBookList(@Body cashBookRequest: CashBookRequest):Call<CashBookResponse>

 @POST("delivery/stock-summary")
 fun stockSummary(@Body stockSummaryRequest: StockSummaryRequest):Call<StockSummary>

 @POST("delivery/arealist")
 fun areaList():Call<AreaListResponse>
}