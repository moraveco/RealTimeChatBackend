package com.moraveco.springboot.repository;

import com.moraveco.springboot.entity.ChatMessage;
import com.moraveco.springboot.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(value = "SELECT * FROM mydb.messages", nativeQuery = true)
    List<ChatMessage> getAllMessages();

    @Query(value = "SELECT * FROM mydb.messages WHERE (sender_uid = :senderUid AND receiver_uid = :receiverUid) OR (sender_uid = :receiverUid AND receiver_uid = :senderUid) ORDER BY timestamp", nativeQuery = true)
    List<ChatMessage> getSecretMessages(
            @Param("senderUid") String senderUid,
            @Param("receiverUid") String receiverUid
    );

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO mydb.messages (id, sender_uid, receiver_uid, content, timestamp) " +
            "VALUES (:id, :senderUid, :receiverUid,  :content, :timestamp)", nativeQuery = true)
    void insertMessage(
            @Param("id") String id,
            @Param("senderUid") String senderUid,
            @Param("receiverUid") String receiverUid,
            @Param("content") String content,
            @Param("timestamp") String timestamp
    );

}
