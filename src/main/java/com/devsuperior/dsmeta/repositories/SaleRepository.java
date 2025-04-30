package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    @Query(nativeQuery = true,
            value = "SELECT tb_sales.id, tb_sales.date, tb_sales.amount ,tb_seller.name AS sellerName " +
                    "FROM tb_sales " +
                    "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
                    "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                    "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:sellerName, '%'))",
            countQuery = "SELECT COUNT(tb_sales.id) " +
                    "FROM tb_sales " +
                    "INNER JOIN tb_seller ON tb_sales.seller_id = tb_seller.id " +
                    "WHERE tb_sales.date BETWEEN :minDate AND :maxDate " +
                    "AND UPPER(tb_seller.name) LIKE UPPER(CONCAT('%',:sellerName, '%'))")
    Page<SaleReportProjection> searchSaleReport(LocalDate minDate, LocalDate maxDate, String sellerName, Pageable pageable);

    @Query(nativeQuery = true,
        value = "SELECT s1.name as sellerName, SUM(s2.amount) as total " +
                "FROM tb_seller s1 " +
                "INNER JOIN tb_sales s2 on s1.id = s2.seller_id " +
                "WHERE s2.date BETWEEN :minDate and :maxDate " +
                "group by s1.name",
        countQuery = "SELECT COUNT(s1.id) " +
                "FROM tb_seller s1 " +
                "INNER JOIN tb_sales s2 on s1.id = s2.seller_id " +
                "WHERE s2.date BETWEEN :minDate and :maxDate " +
                "group by s1.name"
    )
    Page<SaleSummaryProjection> searchSaleSummary(LocalDate minDate, LocalDate maxDate, Pageable pageable);
}
