import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pusher.rest.Pusher;

import static spark.Spark.*;

public class HelloWorld {
    private static Pusher pusher() {
        return new Pusher("175898", "e395e0ba85e39a4f0248", "2db1f52b9b7d40b0db03");

    }

    public static void main(String[] args) {
        staticFileLocation("/public");

        post("/channel_webhooks", (req, res) -> {
            System.out.println("Got post REQ");
            JsonParser parser = new JsonParser();
            JsonElement responseData = parser.parse(req.body());
            JsonObject obj = responseData.getAsJsonObject();
            System.out.println("Got channel webhooks " + obj.getAsJsonArray("events").toString());

            return "";
        });


        post("/authenticate", (req, res) -> {

            // in a real application you might take this user ID
            // and check in your database to check it's valid
            // or perform some other test to ensure you want this user subscribing

            String userId = req.queryParams("userId");

            // you also get passed the channel name, so you can check if a specific user
            // is allowed to subscribe to a specific channel
            String socketId = req.queryParams("socket_id");
            String channelName = req.queryParams("channel_name");

            // if the user is valid, you should return the result of pusher.authenticate
            // if at this point we'd decided the user was invalid, we should return HTTP 403 Forbidden
            // and Pusher won't allow this user to be subscribed
            return pusher().authenticate(socketId, channelName);

        });

        post("/message", (req, res) -> {

            JsonParser parser = new JsonParser();
            JsonElement responseData = parser.parse(req.body());
            JsonObject obj = responseData.getAsJsonObject();

            pusher().trigger("private-messages", "new_message", obj);
            return "";
        });
    }
}