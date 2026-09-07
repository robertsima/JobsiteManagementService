package com.jobsite_management_service.abstraction;

import com.jobsite_management_service.abstraction.enums.ReportOption;
import com.jobsite_management_service.model.dto.ReportResult;

public interface ReportStrategy<T, R> {
    // Generates a structured report based on dynamic criteria
    ReportResult<R> generateReport(ReportSpecification<T> filterCriteria, ReportOption options);
}
