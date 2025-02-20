package brs.http.common;

public class ResultFields {
  private ResultFields() {
  }

  public static final String ERROR_CODE_RESPONSE = "errorCode";
  public static final String ERROR_DESCRIPTION_RESPONSE = "errorDescription";
  public static final String DECRYPTED_MESSAGE_RESPONSE = "decryptedMessage";
  public static final String DEPRECATION_RESPONSE = "deprecation";
  public static final String BALANCE_NQT_RESPONSE = "balanceNQT";
  public static final String BALANCE_QNT_RESPONSE = "balanceQNT";
  public static final String UNCONFIRMED_BALANCE_NQT_RESPONSE = "unconfirmedBalanceNQT";
  public static final String UNCONFIRMED_BALANCE_QNT_RESPONSE = "unconfirmedBalanceQNT";
  public static final String FORGED_BALANCE_NQT_RESPONSE = "forgedBalanceNQT";
  public static final String GUARANTEED_BALANCE_NQT_RESPONSE = "guaranteedBalanceNQT";
  public static final String TRANSACTION_RESPONSE = "transaction";
  public static final String FULL_HASH_RESPONSE = "fullHash";
  public static final String ERROR_RESPONSE = "error";
  public static final String ASK_ORDERS_RESPONSE = "askOrders";
  public static final String ASK_ORDER_IDS_RESPONSE = "askOrderIds";
  public static final String BLOCKS_RESPONSE = "blocks";
  public static final String DONE_RESPONSE = "done";
  public static final String SCAN_TIME_RESPONSE = "scanTime";
  public static final String BROADCASTED_RESPONSE = "broadcasted";
  public static final String NUMBER_PEERS_SENT_TO_RESPONSE = "numberPeersSentTo";
  public static final String UNSIGNED_TRANSACTION_BYTES_RESPONSE = "unsignedTransactionBytes";
  public static final String TRANSACTION_JSON_RESPONSE = "transactionJSON";
  public static final String TRANSACTION_BYTES_RESPONSE = "transactionBytes";
  public static final String SIGNATURE_RESPONSE = "signature";
  public static final String SIGNATURE_HASH_RESPONSE = "signatureHash";
  public static final String VERIFY_RESPONSE = "verify";
  public static final String VALIDATE_RESPONSE = "validate";
  public static final String PUBLIC_KEY_RESPONSE = "publicKey";
  public static final String NAME_RESPONSE = "name";
  public static final String DESCRIPTION_RESPONSE = "description";
  public static final String COMMITMENT_NQT_RESPONSE = "commitmentNQT";
  public static final String COMMITTED_NQT_RESPONSE = "committedBalanceNQT";
  public static final String ASSET_RESPONSE = "asset";
  public static final String ASSET_BALANCES_RESPONSE = "assetBalances";
  public static final String UNCONFIRMED_ASSET_BALANCES_RESPONSE = "unconfirmedAssetBalances";
  public static final String ACCOUNT_RESPONSE = "account";
  public static final String REWARD_RECIPIENT_RESPONSE = "rewardRecipient";
  public static final String BLOCK_RESPONSE = "block";
  public static final String HEIGHT_RESPONSE = "height";
  public static final String GENERATOR_RESPONSE = "generator";
  public static final String GENERATOR_PUBLIC_KEY_RESPONSE = "generatorPublicKey";
  public static final String DATA_RESPONSE = "data";
  public static final String NONCE_RESPONSE = "nonce";
  public static final String SCOOP_NUM_RESPONSE = "scoopNum";
  public static final String TIMESTAMP_RESPONSE = "timestamp";
  public static final String NUMBER_OF_TRANSACTIONS_RESPONSE = "numberOfTransactions";
  public static final String TOTAL_AMOUNT_NQT_RESPONSE = "totalAmountNQT";
  public static final String TOTAL_FEE_NQT_RESPONSE = "totalFeeNQT";
  public static final String TOTAL_FEE_CASH_BACK_NQT_RESPONSE = "totalFeeCashBackNQT";
  public static final String TOTAL_FEE_BURNT_NQT_RESPONSE = "totalFeeBurntNQT";
  public static final String BLOCK_REWARD_RESPONSE = "blockReward";
  public static final String BLOCK_REWARD_NQT_RESPONSE = "blockRewardNQT";
  public static final String LAST_BLOCK_REWARD_RESPONSE = "lastBlockReward";
  public static final String LAST_BLOCK_REWARD_NQT_RESPONSE = "lastBlockRewardNQT";
  public static final String PAYLOAD_LENGTH_RESPONSE = "payloadLength";
  public static final String VERSION_RESPONSE = "version";
  public static final String BASE_TARGET_RESPONSE = "baseTarget";
  public static final String AVERAGE_COMMITMENT_NQT_RESPONSE = "averageCommitmentNQT";
  public static final String PREVIOUS_BLOCK_RESPONSE = "previousBlock";
  public static final String NEXT_BLOCK_RESPONSE = "nextBlock";
  public static final String PAYLOAD_HASH_RESPONSE = "payloadHash";
  public static final String GENERATION_SIGNATURE_RESPONSE = "generationSignature";
  public static final String PREVIOUS_BLOCK_HASH_RESPONSE = "previousBlockHash";
  public static final String BLOCK_SIGNATURE_RESPONSE = "blockSignature";
  public static final String TRANSACTIONS_RESPONSE = "transactions";
  public static final String ASSETS_RESPONSE = "assets";
  public static final String OPEN_ORDERS_RESPONSE = "openOrders";
  public static final String TYPE_RESPONSE = "type";
  public static final String SUBTYPE_RESPONSE = "subtype";
  public static final String ORDER_RESPONSE = "order";
  public static final String QUANTITY_QNT_RESPONSE = "quantityQNT";
  public static final String QUANTITY_CIRCULATING_QNT_RESPONSE = "quantityCirculatingQNT";
  public static final String UNCONFIRMED_QUANTITY_QNT_RESPONSE = "unconfirmedQuantityQNT";
  public static final String PRICE_NQT_RESPONSE = "priceNQT";
  public static final String DECIMALS_RESPONSE = "decimals";
  public static final String MINTABLE_RESPONSE = "mintable";
  public static final String NUMBER_OF_TRADES_RESPONSE = "numberOfTrades";
  public static final String NUMBER_OF_TRANSFERS_RESPONSE = "numberOfTransfers";
  public static final String NUMBER_OF_ACCOUNTS_RESPONSE = "numberOfAccounts";
  public static final String GOODS_RESPONSE = "goods";
  public static final String QUANTITY_RESPONSE = "quantity";
  public static final String SELLER_RESPONSE = "seller";
  public static final String TAGS_RESPONSE = "tags";
  public static final String DELISTED_RESPONSE = "delisted";
  public static final String ASSET_IDS_RESPONSE = "assetIds";
  public static final String QUANTITIES_QNT_RESPONSE = "quantitiesQNT";
  public static final String PURCHASE_RESPONSE = "purchase";
  public static final String BUYER_RESPONSE = "buyer";
  public static final String DELIVERY_DEADLINE_TIMESTAMP_RESPONSE = "deliveryDeadlineTimestamp";
  public static final String PENDING_RESPONSE = "pending";
  public static final String NOTE_RESPONSE = "note";
  public static final String GOODS_DATA_RESPONSE = "goodsData";
  public static final String GOODS_IS_TEXT_RESPONSE = "goodsIsText";
  public static final String FEEDBACK_NOTES_RESPONSE = "feedbackNotes";
  public static final String PUBLIC_FEEDBACKS_RESPONSE = "publicFeedbacks";
  public static final String REFUND_NOTE_RESPONSE = "refundNote";
  public static final String DISCOUNT_NQT_RESPONSE = "discountNQT";
  public static final String REFUND_NQT_RESPONSE = "refundNQT";
  public static final String PURCHASES_RESPONSE = "purchases";
  public static final String ALIAS_NAME_RESPONSE = "aliasName";
  public static final String ALIAS_URI_RESPONSE = "aliasURI";
  public static final String ALIAS_RESPONSE = "alias";
  public static final String ID_RESPONSE = "id";
  public static final String SENDER_RESPONSE = "sender";
  public static final String SENDER_RS_RESPONSE = "senderRS";
  public static final String RECIPIENT_RESPONSE = "recipient";
  public static final String RECIPIENT_RS_RESPONSE = "recipientRS";
  public static final String AMOUNT_NQT_RESPONSE = "amountNQT";
  public static final String FEE_NQT_RESPONSE = "feeNQT";
  public static final String REQUIRED_SIGNERS_RESPONSE = "requiredSigners";
  public static final String DEADLINE_RESPONSE = "deadline";
  public static final String DEADLINE_ACTION_RESPONSE = "deadlineAction";
  public static final String ID_RS_RESPONSE = "idRS";
  public static final String DECISION_RESPONSE = "decision";
  public static final String SIGNERS_RESPONSE = "signers";
  public static final String ATS_RESPONSE = "ats";
  public static final String AT_IDS_RESPONSE = "atIds";
  public static final String VALUE_RESPONSE = "value";
  public static final String TRADES_RESPONSE = "trades";
  public static final String ASK_ORDER_RESPONSE = "askOrder";
  public static final String BID_ORDER_RESPONSE = "bidOrder";
  public static final String ASK_ORDER_HEIGHT_RESPONSE = "askOrderHeight";
  public static final String BID_ORDER_HEIGHT_RESPONSE = ASK_ORDER_HEIGHT_RESPONSE;
  public static final String TRADE_TYPE_RESPONSE = "tradeType";
  public static final String ASSET_TRANSFER_RESPONSE = "assetTransfer";
  public static final String CONFIRMATIONS_RESPONSE = "confirmations";
  public static final String BLOCK_TIMESTAMP_RESPONSE = "blockTimestamp";
  public static final String SENDER_PUBLIC_KEY_RESPONSE = "senderPublicKey";
  public static final String REFERENCED_TRANSACTION_FULL_HASH_RESPONSE = "referencedTransactionFullHash";
  public static final String ATTACHMENT_RESPONSE = "attachment";
  public static final String ATTACHMENT_BYTES_RESPONSE = "attachmentBytes";
  public static final String EC_BLOCK_ID_RESPONSE = "ecBlockId";
  public static final String EC_BLOCK_HEIGHT_RESPONSE = "ecBlockHeight";
  public static final String CASH_BACK_ID_RESPONSE = "cashBackId";
  public static final String FREQUENCY_RESPONSE = "frequency";
  public static final String TIME_NEXT_RESPONSE = "timeNext";
  public static final String BLOCK_IDS_RESPONSE = "blockIds";
  public static final String BID_ORDERS_RESPONSE = "bidOrders";
  public static final String TIME_RESPONSE = "time";
  public static final String ALIASES_RESPONSE = "aliases";
  public static final String BID_ORDER_IDS_RESPONSE = "bidOrderIds";
  public static final String TRANSFERS_RESPONSE = "transfers";
  public static final String DELTA_QUANTITY_RESPONSE = "deltaQuantity";
  public static final String URI_RESPONSE = "uri";
  public static final String PERIOD_RESPONSE = "period";
  public static final String COMMENT_RESPONSE = "comment";
  public static final String GOODS_NONCE_RESPONSE = "goodsNonce";
  public static final String ESCROW_ID_RESPONSE = "escrowId";
  public static final String SUBSCRIPTION_ID_RESPONSE = "subscriptionId";
  public static final String CREATION_BYTES_RESPONSE = "creationBytes";
  public static final String LAST_UNCONFIRMED_TRANSACTION_TIMESTAMP_RESPONSE = "lastUnconfirmedTransactionTimestamp";
  public static final String UNCONFIRMED_TRANSACTIONS_RESPONSE = "unconfirmedTransactions";
  public static final String UNCONFIRMED_TRANSACTIONS_IDS_RESPONSE = "unconfirmedTransactionIds";
  public static final String CHEAP_FEE_RESPONSE = "cheap";
  public static final String STANDARD_FEE_RESPONSE = "standard";
  public static final String PRIORITY_FEE_RESPONSE = "priority";
}
