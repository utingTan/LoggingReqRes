package com.tcb.otp.gw.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemoryTrackingJob {
	private static Logger logger = LogManager.getLogger(MemoryTrackingJob.class);
	
	@Scheduled(cron = "*/10 * * * * *") // 每 10sec執行一次
	public String memoryTracking_10Sec() {
		Runtime rt = Runtime.getRuntime();
		long usedMB = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
		logger.info("memoryTracking_10Sec() - usageMemory[{}]", usedMB);
		return null;
	}
}
