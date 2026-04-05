package com.example.universalmarketplacebe.repository.replyRepository;

import com.example.universalmarketplacebe.model.Reply;
import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
