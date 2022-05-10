package com.trade.store.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trade.store.entity.TradeStore;
import com.trade.store.exception.TradeStoreException;
import com.trade.store.repository.TradeStoreRepository;

@Service
public class TradeStoreService {
	public static final Logger logger = LoggerFactory.getLogger(TradeStoreService.class);
	@Autowired
	private TradeStoreRepository tradeStoreRepository;
//	private ModelMapper mapper=new ModelMapper();
	

	//method to add trade
	public String addTrade(TradeStore tradeStore) throws TradeStoreException {
		Boolean flag = checkMaturityDate(tradeStore.getMaturityDate());
		try {
		if (flag) {
			List<TradeStore> tradeStoreList=tradeStoreRepository.findByTradeId(tradeStore.getTradeId());
			if(!tradeStoreList.isEmpty()) {
			Integer maxVersion=tradeStoreList.stream().map(TradeStore::getVersion).max(Comparator.comparing(Integer::valueOf)).get();
			if(maxVersion!=null) {
				logger.info("Version for "+ tradeStore.getTradeId()+ " is greater");
				//for a greater version
				if(tradeStore.getVersion()>maxVersion) {
					tradeStore.setCreatedDate(LocalDate.now());
					tradeStore.setExpired("N");
					tradeStoreRepository.save(tradeStore);	
				}
				//to overwrite the max version
				else if (tradeStore.getVersion()==maxVersion) {
					logger.info("Overriding for trade"+tradeStore.getTradeId()+" version"+tradeStore.getVersion());
					TradeStore existingTradeStore=tradeStoreRepository.findByTradeIdAndVersion(tradeStore.getTradeId(),tradeStore.getVersion()).get();
					existingTradeStore.setBookId(tradeStore.getBookId());
					existingTradeStore.setCounterPartyId(tradeStore.getCounterPartyId());
					existingTradeStore.setMaturityDate(tradeStore.getMaturityDate());
					existingTradeStore.setTradeId(tradeStore.getTradeId());
					existingTradeStore.setVersion(tradeStore.getVersion());
					tradeStoreRepository.save(existingTradeStore);	
				}else throw new TradeStoreException("Cannot Accept Lower Version");
			}
			}
			//adding new trade
			else {
				logger.debug("Adding new Trade");
				tradeStore.setCreatedDate(LocalDate.now());
				tradeStore.setExpired("N");
				tradeStoreRepository.save(tradeStore);	
			}
		
			 return new String("Trade added Succesfully!!");
		}else {
			logger.info("Maturity date has passed");
			return new String("Maturity date has passed!!");
		}
		}
		catch (TradeStoreException e) {
			// TODO: handle exception
			logger.error("Exception occured in addTradeService");
			e.printStackTrace();
			return e.getMessage() ;
		}
		
	}

	//method to validate maturity date
	public boolean checkMaturityDate(LocalDate maturityDate) {
		try {
			logger.info("Checking Maturity Date Validation");
			if (maturityDate.isAfter(LocalDate.now())) {
				logger.debug("Maturity Date After" + maturityDate + "SystemDate" + LocalDate.now());
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error("Exception occured in Maturity Date Validation" + e.getMessage());
			return false;
		}
	}
}