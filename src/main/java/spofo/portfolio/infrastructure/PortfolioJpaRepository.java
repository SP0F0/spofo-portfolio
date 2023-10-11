package spofo.portfolio.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PortfolioJpaRepository extends JpaRepository<PortfolioEntity, Long> {

    //@Query("select p from PortfolioEntity p where p.memberId = :memberId")
    //List<PortfolioEntity> findByMemberId(@Param("memberId") Long memberId);
    List<PortfolioEntity> findByMemberId(Long memberId);

    @Query(value = "select id from member m where m.id = 1", nativeQuery = true)
    Long findmemberId();

    @Query("select p "
            + "from PortfolioEntity p "
            + "inner join fetch p.stockHaveEntities s "
            + "inner join fetch s.tradeLogEntities t "
            + "where p.id = :id")
    Optional<PortfolioEntity> findByIdWithTradeLogs(@Param("id") Long id);

    @Query("select p "
            + "from PortfolioEntity p "
            + "inner join fetch p.stockHaveEntities s "
            + "inner join fetch s.tradeLogEntities t "
            + "where p.memberId = :id")
    List<PortfolioEntity> findByMemberIdWithTradeLogs(@Param("id") Long id);
}
