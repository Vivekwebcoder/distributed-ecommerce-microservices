package com.ecommerce.inventory.repository;

import com.ecommerce.inventory.entity.InventoryAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryAuditLogRepository extends JpaRepository<InventoryAuditLog, Long> {

    List<InventoryAuditLog> findBySkuCode(String skuCode);

    List<InventoryAuditLog> findByReferenceId(String referenceId);
}
