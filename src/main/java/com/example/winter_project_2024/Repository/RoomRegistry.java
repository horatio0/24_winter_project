package com.example.winter_project_2024.Repository;

import com.example.winter_project_2024.Entity.Room;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RoomRegistry {
    private final ConcurrentHashMap<String, Room> rooms = new ConcurrentHashMap<String, Room>();

    public Room getOrCreateRoom(String roomId, String boss){
        if (rooms.get(roomId) == null) {
            Room room = new Room();
            room.setRoomId(roomId);
            room.setBoss(boss);
            room.setGameState(0);
            rooms.put(roomId, room);
        }
        return rooms.get(roomId);
    }

    public Room getRoom(String roomId) throws NullPointerException{
        return rooms.get(roomId);
    }

    public void removeRoom(String roomId){
        rooms.remove(roomId);
    }

    public Set<String> getRooms(){
        Set<String> roomList = new HashSet<>();
        rooms.values().forEach(room -> {
            roomList.add(room.getRoomId());
        });
        return roomList;
    }
}
