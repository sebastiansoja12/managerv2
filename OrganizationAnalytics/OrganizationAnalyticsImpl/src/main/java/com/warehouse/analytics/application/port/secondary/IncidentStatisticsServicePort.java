package com.warehouse.analytics.application.port.secondary;

import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.IncidentFigures;

import java.util.List;

public interface IncidentStatisticsServicePort {

    List<IncidentFigures> incidentFigures(final AnalyticsPeriod period);
}
