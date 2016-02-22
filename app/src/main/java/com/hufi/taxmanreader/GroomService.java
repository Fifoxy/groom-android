package com.hufi.taxmanreader;

import com.hufi.taxmanreader.model.Event;
import com.hufi.taxmanreader.model.Order;
import com.hufi.taxmanreader.model.Product;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GroomService {
    @GET("events")
    Call<List<Event>> getAllEvents();

    @GET("events/{event_slug}")
    Call<Event> getEvent(@Path("event_slug") String event_slug);

    @GET("products/{product_id}")
    Call<Product> getProduct(@Path("product_id") Integer product_id);

    @GET("orders/{order_id}")
    Call<Order> getOrder(@Path("order_id") Integer order_id);
}
