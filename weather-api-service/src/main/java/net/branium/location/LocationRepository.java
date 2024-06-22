package net.branium.location;

import net.branium.common.Location;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {

    @Query("SELECT l FROM Location l WHERE l.trashed = false")
    List<Location> findAllUnTrashed();

    @Query("SELECT l FROM Location l WHERE l.code = :code AND l.trashed = false")
    Location findByCode(@Param("code") String code);
}
