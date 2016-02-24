package com.hufi.taxmanreader;

import com.hufi.taxmanreader.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface GroomService {
    @GET("me")
    Call<User> getUser(@Header("Authorization") String authorization);


    @GET("events")
    Call<List<Event>> getAllEvents();

    @GET("events/{event_slug}")
    Call<Event> getEvent(@Path("event_slug") String event_slug);

    @GET("products/{product_id}")
    Call<Product> getProduct(@Path("product_id") Integer product_id);

    @GET("orders/{order_id}")
    Call<Order> getOrder(@Path("order_id") Integer order_id);

    @GET("tickets")
    Call<List<Ticket>> getAllTickets();

    @GET("tickets/{ticket_id}")
    Call<Ticket> getTicket(@Path("ticket_id") Integer ticket_id);

    @POST("tickets/{ticket_id}/usage")
    Call<Ticket> ticketUsage(@Path("ticket_id") Integer ticket_id);
}
