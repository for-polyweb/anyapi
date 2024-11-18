package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.PostDetail;
import cz.anycoders.anyapi.entity.PostsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepo extends JpaRepository<PostsList, Long> {

    @Query("SELECT " +
            "p " +
            "FROM PostsList p " +
            "LEFT JOIN Rate r ON p.id = r.postId " +
            "GROUP BY p.id " +
            "ORDER BY " +
            "count(DISTINCT r.user.id) DESC," +
            "avg(r.value) DESC"

    )
    List<PostsList> findAllOrdered();
}
