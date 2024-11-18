package cz.anycoders.anyapi.repository;

import cz.anycoders.anyapi.entity.APIError;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ErrorsRepo extends JpaRepository<APIError, Integer> {
}

