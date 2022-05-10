/**
 * 
 */
package com.trade.store.cron;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.trade.store.entity.TradeStore;
import com.trade.store.repository.TradeStoreRepository;

/**
 * @author RogStrix
 *
 */
@Component
public class ExpiryUpdateCron {
	public static final org.slf4j.Logger logger= LoggerFactory.getLogger(ExpiryUpdateCron.class);
	
	@Autowired
	private TradeStoreRepository tradeStoreRepository;
	
	//method to update the expiry flag
	@Scheduled(cron ="00 01 00 ? * *", zone = "IST" )
	public void updateExpiry() {
		logger.info("IN cron");
		List<TradeStore> allTradeList = tradeStoreRepository.findAll();
		if(!allTradeList.isEmpty()) {
			allTradeList.forEach(trade -> {
				if(trade.getMaturityDate().isBefore(LocalDate.now())) {
					logger.info("Inside if of lambda");
					Optional<TradeStore> tradeStore = tradeStoreRepository.findById(trade.getId());
					tradeStore.get().setExpired("Y");
					tradeStoreRepository.save(tradeStore.get());
				}
					
			});
		}
	}

}
