package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.ImpersonationConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImpersonationConsentReadRepository extends JpaRepository<ImpersonationConsentEntity, Long> {

    Optional<ImpersonationConsentEntity> findFirstByPlatformOwnerIdAndOperatorIdOrderByRequestedAtDesc(Long platformOwnerId,
                                                                                                        Long operatorId);

    List<ImpersonationConsentEntity> findTop50ByOperatorIdOrderByRequestedAtDesc(Long operatorId);
}
