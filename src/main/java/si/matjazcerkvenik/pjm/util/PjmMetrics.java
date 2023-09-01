package si.matjazcerkvenik.pjm.util;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;

public class PjmMetrics {

    public static CollectorRegistry registry = CollectorRegistry.defaultRegistry;

    public static final Gauge pjm_build_info = Gauge.build()
            .name("pjm_build_info")
            .help("PJM meta data, value contains start time")
            .labelNames("app", "runtimeId", "version", "os")
            .register();

    public static final Gauge pjm_memory_total_bytes = Gauge.build()
            .name("pjm_memory_total_bytes")
            .help("Total memory in bytes")
            .register();

    public static final Gauge pjm_memory_free_bytes = Gauge.build()
            .name("pjm_memory_free_bytes")
            .help("Free memory in bytes")
            .register();

    public static final Gauge pjm_memory_max_bytes = Gauge.build()
            .name("pjm_memory_max_bytes")
            .help("Max memory in bytes")
            .register();

    public static final Gauge pjm_available_processors = Gauge.build()
            .name("pjm_available_processors")
            .help("Number of available processors")
            .register();

    public static final Gauge pjm_projects_total = Gauge.build()
            .name("pjm_projects_total")
            .help("All projects")
            .labelNames("projectName")
            .register();

    public static final Gauge pjm_requirements_total = Gauge.build()
            .name("pjm_requirements_total")
            .help("All projects")
            .labelNames("projectName")
            .register();

}
