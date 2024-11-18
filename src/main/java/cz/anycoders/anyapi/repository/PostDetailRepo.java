package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.PostDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostDetailRepo extends JpaRepository<PostDetail, Long> {

}
