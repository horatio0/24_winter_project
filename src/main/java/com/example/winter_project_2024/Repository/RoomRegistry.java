package com.example.winter_project_2024.Repository;

import com.example.winter_project_2024.Entity.Room;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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
        log.info("정상 추가 됨 : {}", rooms.toString());
        log.info("RoomRegistry 해시코드: {}", System.identityHashCode(this));
        log.info("rooms 해시코드: {}", System.identityHashCode(rooms));
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
        log.info("현재 rooms : {}", rooms.toString());
        log.info("RoomRegistry 해시코드: {}", System.identityHashCode(this));
        log.info("rooms 해시코드: {}", System.identityHashCode(rooms));
        rooms.values().forEach(room -> {
            log.info("getRooms 동작 중 : {}", room);
            roomList.add(room.getRoomId());
        });
        log.info("getRooms 동작 완료 : {}", roomList.toString());
        return roomList;
    }
}
