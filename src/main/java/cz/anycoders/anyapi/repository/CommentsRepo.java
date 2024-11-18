package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentsRepo extends JpaRepository<Comment, Integer> {
}

