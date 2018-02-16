package fr.galaisen.groomreader;

import fr.galaisen.groomreader.model.*;
import fr.galaisen.groomreader.model.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface GroomService {
    @GET("users/me")
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

    @GET("tickets")
    Call<List<Ticket>> findTicketByLastName(@Query("last_name") String lastName);

    @GET("products")
    Call<List<Product>> getProductsByEvent(@Query("event") String slug);
}
