package restopoly.util;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import restopoly.client.RESTopoly;
import restopoly.resources.Event;

import java.io.IOException;


@WebSocket
public class PlayerWebSocket {
    public Session session;

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        System.out.println("connected");
        this.session = session;

        SocketList.getInstance().join(session.getRemoteAddress().toString(), this);
        session.getRemote().sendString(session.getRemoteAddress().toString());
    }

    @OnWebSocketMessage
    public void onText(String message) throws IOException {
        if(message.equals("/player")){
            session.getRemote().sendString(new Gson().toJson(RESTopoly.getInstance().getPLAYER()));
        }
        if(message.equals("/player/turn")){
            RESTopoly.getInstance().setTurn(true);
        }
        if(message.startsWith("/player/event/")){
            System.out.println(message.substring(14));
            Event event = new Gson().fromJson(message.substring(14), Event.class);
            RESTopoly.getInstance().addEvent(event);
        }
        if(message.matches("/(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}):(\\d{1,5})")){
            RESTopoly.getInstance().setPLAYERADDRESS(message);
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        SocketList.getInstance().part(session.getRemoteAddress().toString());
    }
}


