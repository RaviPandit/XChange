package org.knowm.xchange.bitso.service;

import java.io.IOException;
import java.math.BigDecimal;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitso.BitsoAuthenticated;
import org.knowm.xchange.bitso.dto.trade.BitsoAllOrders;
import org.knowm.xchange.bitso.dto.trade.BitsoCacleOrderResponse;
import org.knowm.xchange.bitso.dto.trade.BitsoOrder;
import org.knowm.xchange.bitso.dto.trade.BitsoOrderResponse;
import org.knowm.xchange.bitso.dto.trade.BitsoPlaceOrder;
import org.knowm.xchange.bitso.dto.trade.BitsoUserTransaction;
import org.knowm.xchange.client.ExchangeRestProxyBuilder;

/** @author Piotr Ładyżyński */
public class BitsoTradeServiceRaw extends BitsoBaseService {

  private final BitsoAuthenticated bitsoAuthenticated;
  private final BitsoDigest signatureCreator;

  /** @param exchange */
  public BitsoTradeServiceRaw(Exchange exchange) {

    super(exchange);
    this.bitsoAuthenticated =
        ExchangeRestProxyBuilder.forInterface(
                BitsoAuthenticated.class, exchange.getExchangeSpecification())
            .build();
    this.signatureCreator =
        BitsoDigest.createInstance(
            exchange.getExchangeSpecification().getSecretKey(),
            exchange.getExchangeSpecification().getUserName(),
            exchange.getExchangeSpecification().getApiKey());
  }

  public BitsoAllOrders getBitsoOpenOrders() throws IOException {
    return bitsoAuthenticated.getOpenOrders(signatureCreator);
  }

  public BitsoOrderResponse placeBitsOrder(BitsoPlaceOrder bitsoPlaceOrder) throws IOException {
    String auth = signatureCreator.digestParams("POST", "/v3/orders/", bitsoPlaceOrder);
    System.out.println("Authorization Code issss...........");
    System.out.println(auth);

    return bitsoAuthenticated.placeOrder(auth, bitsoPlaceOrder);
  }

  public BitsoOrder sellBitsoOrder(BigDecimal originalAmount, BigDecimal price) throws IOException {

    return bitsoAuthenticated.sell(
        exchange.getExchangeSpecification().getApiKey(),
        signatureCreator,
        exchange.getNonceFactory(),
        originalAmount,
        price);
  }

  public BitsoOrder buyBitoOrder(BigDecimal originalAmount, BigDecimal price) throws IOException {

    return bitsoAuthenticated.buy(
        exchange.getExchangeSpecification().getApiKey(),
        signatureCreator,
        exchange.getNonceFactory(),
        originalAmount,
        price);
  }

  public boolean cancelBitsoOrder(String orderId) throws IOException {
    String auth = signatureCreator.digestParams("DELETE", "/v3/orders/", orderId);
    BitsoCacleOrderResponse bitsoCacleOrderResponse = bitsoAuthenticated.cancelOrder(auth, orderId);
    if (bitsoCacleOrderResponse.isSuccess()) {
      return true;
    } else {
      return false;
    }
  }

  public BitsoUserTransaction[] getBitsoUserTransactions(Long numberOfTransactions)
      throws IOException {

    return bitsoAuthenticated.getUserTransactions(
        exchange.getExchangeSpecification().getApiKey(),
        signatureCreator,
        exchange.getNonceFactory(),
        numberOfTransactions);
  }

  public BitsoUserTransaction[] getBitsoUserTransactions(
      Long numberOfTransactions, Long offset, String sort) throws IOException {

    return bitsoAuthenticated.getUserTransactions(
        exchange.getExchangeSpecification().getApiKey(),
        signatureCreator,
        exchange.getNonceFactory(),
        numberOfTransactions,
        offset,
        sort);
  }
}
