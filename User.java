package com.example.swith.Friend;

import java.util.Map;

public class User {
    public String uid;         // userId -> uid로 변경
    public String name;
    // id, name, nickname 중 하나 or 두개만 남기고 삭제
    public String nickname;
    private int studyTime;      // 공부 시간 (초 단위)
    public int points;          // 점수
    public String status;      // 추가: 사용자의 상태 (예: online, busy, offline)

    public Map<String, Boolean> joinedRooms;
    public User(){}

    public User(String uid, String nickname, Map<String, Boolean> joinedRooms){
        this.uid = uid;
        this.nickname = nickname;
        this.joinedRooms = joinedRooms;
    }

    // getter & setter
    public String getUid() {
        return uid;
    }
    public String getNickname() {return nickname;}
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudyTime() {
        return studyTime;
    }

    public void setStudyTime(int studyTime) {
        this.studyTime = studyTime;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Boolean> getJoinedRooms() {
        return joinedRooms;
    }
}
