package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepo extends JpaRepository<Rate, Long> {
    Long countByPostId(Long post_id);
}
