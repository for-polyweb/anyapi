package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatesRepo extends JpaRepository<State, Long> {
}
