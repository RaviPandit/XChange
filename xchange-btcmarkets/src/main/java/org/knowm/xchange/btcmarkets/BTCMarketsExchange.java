package org.knowm.xchange.btcmarkets;

import java.util.concurrent.TimeUnit;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.btcmarkets.service.BTCMarketsAccountService;
import org.knowm.xchange.btcmarkets.service.BTCMarketsMarketDataService;
import org.knowm.xchange.btcmarkets.service.BTCMarketsTradeService;
import org.knowm.xchange.utils.nonce.CurrentTimeIncrementalNonceFactory;
import si.mazi.rescu.SynchronizedValueFactory;

/** @author Matija Mazi */
public class BTCMarketsExchange extends BaseExchange implements Exchange {

  public static final String CURRENCY_PAIR = "CURRENCY_PAIR";

  // change from new CurrentTimeNonceFactory(); to   new CurrentTimeIncrementalNonceFactory(null);
  private SynchronizedValueFactory<Long> nonceFactory =
      new CurrentTimeIncrementalNonceFactory(TimeUnit.SECONDS);

  @Override
  public void applySpecification(ExchangeSpecification exchangeSpecification) {
    super.applySpecification(exchangeSpecification);
  }

  @Override
  protected void initServices() {
    this.marketDataService = new BTCMarketsMarketDataService(this);
    if (exchangeSpecification.getApiKey() != null && exchangeSpecification.getSecretKey() != null) {
      this.tradeService = new BTCMarketsTradeService(this);
      this.accountService = new BTCMarketsAccountService(this);
    }
  }

  @Override
  public ExchangeSpecification getDefaultExchangeSpecification() {
    ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass());
    exchangeSpecification.setSslUri("https://api.btcmarkets.net");
    exchangeSpecification.setHost("btcmarkets.net");
    exchangeSpecification.setPort(80);
    exchangeSpecification.setExchangeName("BTCMarkets");
    return exchangeSpecification;
  }
}
