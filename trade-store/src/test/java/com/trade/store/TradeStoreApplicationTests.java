package com.trade.store;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.trade.store.controller.TradeStoreController;
import com.trade.store.entity.TradeStore;

@SpringBootTest
class TradeStoreApplicationTests {
	
	@Autowired
	TradeStoreController tradeStoreController;

	@Test
	void contextLoads() {
	}

	@Test
	void checkMaturityDate() {
		assertEquals(true,LocalDate.of(2022,06,10).isAfter(LocalDate.now()));
	}
	@Test
	void addTrade() {
		TradeStore tradeStore=new TradeStore();
		tradeStore.setBookId("B1");
		tradeStore.setCounterPartyId("CP-1");
		tradeStore.setMaturityDate(LocalDate.of(2022,05,17));
		tradeStore.setTradeId("T1");
		tradeStore.setVersion(2);
		assertEquals("Trade added Succesfully!!",tradeStoreController.addToStore(tradeStore).getBody());
		assertEquals("Trade added Succesfully!!",tradeStoreController.addToStore(tradeStore).getBody());
		tradeStore.setVersion(1);
		assertEquals("Cannot Accept Lower Version",tradeStoreController.addToStore(tradeStore).getBody());
		tradeStore.setMaturityDate(LocalDate.of(2022,04,12));
		assertEquals("Maturity date has passed!!",tradeStoreController.addToStore(tradeStore).getBody());
	}
}
