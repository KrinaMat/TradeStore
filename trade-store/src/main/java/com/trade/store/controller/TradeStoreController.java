package com.trade.store.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.trade.store.entity.TradeStore;
import com.trade.store.service.TradeStoreService;

@RestController
public class TradeStoreController {
	public static final Logger logger = LoggerFactory.getLogger(TradeStoreController.class);
	@Autowired
	private TradeStoreService tradeStoreService;

	@PostMapping(value = "/add")

	public ResponseEntity<String> addToStore(@RequestBody TradeStore tradeStore) {
		try {
			logger.info("API called to add Trade");
			String response = tradeStoreService.addTrade(tradeStore);
			return new ResponseEntity<String>(response, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception occured in adding Trade");
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.OK);
		}
	}

}
