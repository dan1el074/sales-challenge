package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDto;
import com.devsuperior.dsmeta.dto.SaleSummaryDto;
import com.devsuperior.dsmeta.projection.SaleReportProjection;
import com.devsuperior.dsmeta.projection.SaleSummaryProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDto> searchSaleReport(String minDate, String maxDate, String sellerName, Pageable pageable) {
		LocalDate finalDate = validMaxDate(maxDate);
		LocalDate initialDate = validMinDate(minDate, finalDate);
		Page<SaleReportProjection> projection = repository.searchSaleReport(initialDate, finalDate, sellerName, pageable);
		return projection.map(SaleReportDto::new);
	}

	public Page<SaleSummaryDto> searchSaleSummary(String minDate, String maxDate, Pageable pageable) {
		LocalDate finalDate = validMaxDate(maxDate);
		LocalDate initialDate = validMinDate(minDate, finalDate);
		Page<SaleSummaryProjection> projection = repository.searchSaleSummary(initialDate, finalDate, pageable);
		return projection.map(SaleSummaryDto::new);
	}

	private LocalDate validMaxDate(String maxDate) {
		if (maxDate.isEmpty()) {
			return LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
		}
		return LocalDate.parse(maxDate);
	}

	private LocalDate validMinDate(String minDate, LocalDate maxDate) {
		if (minDate.isEmpty()) {
			return maxDate.minusYears(1L);
		}
		return LocalDate.parse(minDate);
	}
}
