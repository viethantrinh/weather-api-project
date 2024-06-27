package net.branium.location;

import jakarta.transaction.Transactional;
import net.branium.common.Location;
import org.springframework.data.jpa.repository.Modifying;
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
    Optional<Location> findByCode(@Param("code") String code);

    @Modifying
    @Transactional
    @Query("UPDATE Location l SET l.trashed = true WHERE l.code = ?1")
    void deleteByCode(String code);
}
