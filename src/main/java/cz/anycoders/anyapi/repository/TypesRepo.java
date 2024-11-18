package cz.anycoders.anyapi.repository;


import cz.anycoders.anyapi.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypesRepo extends JpaRepository<Type, Long> {
}
