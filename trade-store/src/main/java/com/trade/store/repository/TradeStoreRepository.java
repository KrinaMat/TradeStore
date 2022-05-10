package com.trade.store.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trade.store.entity.TradeStore;

public interface TradeStoreRepository extends JpaRepository<TradeStore, Integer> {

	List<TradeStore> findByTradeId(String tradeId);
	Optional<TradeStore> findByTradeIdAndVersion(String tradeId,int version);
}
