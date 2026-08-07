package dev.elias.restaurante.report.controller;

import dev.elias.restaurante.report.dto.ReportSummaryResponse;
import dev.elias.restaurante.report.service.ReportService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(
        "/api/admin/reports"
)
public class AdminReportController {

    private final ReportService service;

    public AdminReportController(
            ReportService service
    ) {
        this.service = service;
    }

    @GetMapping("/summary")
    public ReportSummaryResponse summary(

            @RequestParam
            @DateTimeFormat(
                    iso =
                            DateTimeFormat.ISO.DATE
            )
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(
                    iso =
                            DateTimeFormat.ISO.DATE
            )
            LocalDate endDate
    ) {
        return service.getSummary(
                startDate,
                endDate
        );
    }
}