package brs.http;

import brs.*;
import brs.assetexchange.AssetExchange;
import brs.deeplink.DeeplinkQRCodeGenerator;
import brs.feesuggestions.FeeSuggestionCalculator;
import brs.props.PropertyService;
import brs.props.Props;
import brs.services.*;
import brs.util.JSON;
import brs.util.Subnet;
import signum.net.NetworkParameters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.util.*;

import static brs.http.JSONResponses.*;

public final class APIServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(APIServlet.class);

  private final Set<Subnet> allowedBotHosts;
  private final boolean acceptSurplusParams;

  public APIServlet(TransactionProcessor transactionProcessor, Blockchain blockchain, BlockchainProcessor blockchainProcessor, ParameterService parameterService,
                    AccountService accountService, AliasService aliasService, AssetExchange assetExchange,
                    EscrowService escrowService, DGSGoodsStoreService digitalGoodsStoreService,
                    SubscriptionService subscriptionService, ATService atService, TimeService timeService, EconomicClustering economicClustering, TransactionService transactionService,
                    BlockService blockService, Generator generator, PropertyService propertyService, APITransactionManager apiTransactionManager, FeeSuggestionCalculator feeSuggestionCalculator,
                    DeeplinkQRCodeGenerator deeplinkQRCodeGenerator, IndirectIncomingService indirectIncomingService, Set<Subnet> allowedBotHosts, NetworkParameters params) {

    enforcePost = propertyService.getBoolean(Props.API_SERVER_ENFORCE_POST);
    allowedOrigins = propertyService.getString(Props.API_ALLOWED_ORIGINS);
    this.allowedBotHosts = allowedBotHosts;
    this.acceptSurplusParams = propertyService.getBoolean(Props.API_ACCEPT_SURPLUS_PARAMS);

    final Map<String, HttpRequestHandler> map = new HashMap<>();

    map.put("broadcastTransaction", new BroadcastTransaction(transactionProcessor, parameterService, transactionService));
    map.put("calculateFullHash", new CalculateFullHash());
    map.put("cancelAskOrder", new CancelAskOrder(parameterService, blockchain, assetExchange, apiTransactionManager));
    map.put("cancelBidOrder", new CancelBidOrder(parameterService, blockchain, assetExchange, apiTransactionManager));
    map.put("decryptFrom", new DecryptFrom(parameterService));
    map.put("dgsListing", new DGSListing(parameterService, blockchain, apiTransactionManager));
    map.put("dgsDelisting", new DGSDelisting(parameterService, blockchain, apiTransactionManager));
    map.put("dgsDelivery", new DGSDelivery(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("dgsFeedback", new DGSFeedback(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("dgsPriceChange", new DGSPriceChange(parameterService, blockchain, apiTransactionManager));
    map.put("dgsPurchase", new DGSPurchase(parameterService, blockchain, accountService, timeService, apiTransactionManager));
    map.put("dgsQuantityChange", new DGSQuantityChange(parameterService, blockchain, apiTransactionManager));
    map.put("dgsRefund", new DGSRefund(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("encryptTo", new EncryptTo(parameterService, accountService));
    map.put("generateToken", new GenerateToken(timeService));
    map.put("getAccount", new GetAccount(parameterService, accountService, blockchain, generator));
    map.put("getAccountsWithName", new GetAccountsWithName(accountService));
    map.put("getAccountBlockIds", new GetAccountBlockIds(parameterService, blockchain));
    map.put("getAccountBlocks", new GetAccountBlocks(blockchain, parameterService, blockService));
    map.put("getAccountId", new GetAccountId());
    map.put("getAccountPublicKey", new GetAccountPublicKey(parameterService));
    map.put("getAccountTransactionIds", new GetAccountTransactionIds(parameterService, blockchain));
    map.put("getAccountTransactions", new GetAccountTransactions(parameterService, blockchain));
    map.put("getAccountAssets", new GetAccountAssets(parameterService, accountService));
    map.put("sellAlias", new SellAlias(parameterService, blockchain, apiTransactionManager));
    map.put("buyAlias", new BuyAlias(parameterService, blockchain, aliasService, apiTransactionManager));
    map.put("getAlias", new GetAlias(parameterService, aliasService));
    map.put("getAliases", new GetAliases(parameterService, aliasService));
    map.put("getAliasesOnSale", new GetAliasesOnSale(aliasService));
    map.put("getAllAssets", new GetAllAssets(assetExchange));
    map.put("getAsset", new GetAsset(parameterService, assetExchange));
    map.put("getAssetIds", new GetAssetIds(assetExchange));
    map.put("getAssetsByIssuer", new GetAssetsByIssuer(parameterService, assetExchange));
    map.put("getAssetAccounts", new GetAssetAccounts(parameterService, assetExchange));
    map.put("getBalance", new GetBalance(parameterService));
    map.put("getBlock", new GetBlock(blockchain, blockService));
    map.put("getBlockId", new GetBlockId(blockchain));
    map.put("getBlocks", new GetBlocks(blockchain, blockService));
    map.put("getBlockchainStatus", new GetBlockchainStatus(blockchainProcessor, blockchain, timeService));
    map.put("getConstants", GetConstants.instance);
    map.put("getDGSGoods", new GetDGSGoods(digitalGoodsStoreService));
    map.put("getDGSGood", new GetDGSGood(parameterService));
    map.put("getDGSPurchases", new GetDGSPurchases(digitalGoodsStoreService));
    map.put("getDGSPurchase", new GetDGSPurchase(parameterService));
    map.put("getDGSPendingPurchases", new GetDGSPendingPurchases(digitalGoodsStoreService));
    map.put("getECBlock", new GetECBlock(blockchain, timeService, economicClustering));
    map.put("getMyInfo", GetMyInfo.instance);
    map.put("getPeer", GetPeer.instance);
    map.put("getMyPeerInfo", new GetMyPeerInfo(transactionProcessor));
    map.put("getPeers", GetPeers.instance);
    map.put("getState", new GetState(blockchain, assetExchange, accountService, escrowService, aliasService, timeService, atService, generator, propertyService));
    map.put("getTime", new GetTime(timeService));
    map.put("getTrades", new GetTrades(parameterService, assetExchange));
    map.put("getAllTrades", new GetAllTrades(assetExchange));
    map.put("getAssetTransfers", new GetAssetTransfers(parameterService, accountService, assetExchange));
    map.put("getTransaction", new GetTransaction(transactionProcessor, blockchain));
    map.put("getIndirectIncoming", new GetIndirectIncoming(blockchain, parameterService));
    map.put("getTransactionBytes", new GetTransactionBytes(blockchain, transactionProcessor));
    map.put("getTransactionIds", new GetTransactionIds(parameterService, blockchain));
    map.put("getUnconfirmedTransactionIds", new GetUnconfirmedTransactionIds(transactionProcessor, indirectIncomingService, parameterService));
    map.put("getUnconfirmedTransactions", new GetUnconfirmedTransactions(transactionProcessor, indirectIncomingService, parameterService));
    map.put("getAccountCurrentAskOrderIds", new GetAccountCurrentAskOrderIds(parameterService, assetExchange));
    map.put("getAccountCurrentBidOrderIds", new GetAccountCurrentBidOrderIds(parameterService, assetExchange));
    map.put("getAccountCurrentAskOrders", new GetAccountCurrentAskOrders(parameterService, assetExchange));
    map.put("getAccountCurrentBidOrders", new GetAccountCurrentBidOrders(parameterService, assetExchange));
    map.put("getAllOpenAskOrders", new GetAllOpenAskOrders(assetExchange));
    map.put("getAllOpenBidOrders", new GetAllOpenBidOrders(assetExchange));
    map.put("getAskOrder", new GetAskOrder(assetExchange));
    map.put("getAskOrderIds", new GetAskOrderIds(parameterService, assetExchange));
    map.put("getAskOrders", new GetAskOrders(parameterService, assetExchange));
    map.put("getBidOrder", new GetBidOrder(assetExchange));
    map.put("getBidOrderIds", new GetBidOrderIds(parameterService, assetExchange));
    map.put("getBidOrders", new GetBidOrders(parameterService, assetExchange));
    map.put("suggestFee", new SuggestFee(feeSuggestionCalculator));
    map.put("issueAsset", new IssueAsset(parameterService, blockchain, apiTransactionManager));
    map.put("mintAsset", new MintAsset(parameterService, blockchain, apiTransactionManager, assetExchange));
    map.put("distributeToAssetHolders", new DistributeToAssetHolders(parameterService, blockchain, apiTransactionManager, assetExchange, accountService));
    map.put("addAssetTreasuryAccount", new AddAssetTreasuryAccount(parameterService, blockchain, apiTransactionManager, accountService));
    map.put("longConvert", LongConvert.instance);
    map.put("parseTransaction", new ParseTransaction(parameterService, transactionService));
    map.put("placeAskOrder", new PlaceAskOrder(parameterService, blockchain, apiTransactionManager, accountService));
    map.put("placeBidOrder", new PlaceBidOrder(parameterService, blockchain, apiTransactionManager));
    map.put("rsConvert", RSConvert.instance);
    map.put("readMessage", new ReadMessage(blockchain, accountService));
    map.put("sendMessage", new SendMessage(parameterService, apiTransactionManager));
    map.put("sendMoney", new SendMoney(parameterService, apiTransactionManager));
    map.put("sendMoneyMulti", new SendMoneyMulti(parameterService, blockchain, apiTransactionManager));
    map.put("sendMoneyMultiSame", new SendMoneyMultiSame(parameterService, blockchain, apiTransactionManager));
    map.put("setAccountInfo", new SetAccountInfo(parameterService, blockchain, apiTransactionManager));
    map.put("setAlias", new SetAlias(parameterService, blockchain, aliasService, apiTransactionManager));
    map.put("signTransaction", new SignTransaction(parameterService, transactionService));
    map.put("transferAsset", new TransferAsset(parameterService, blockchain, apiTransactionManager, accountService));
    map.put("transferAssetMulti", new TransferAssetMulti(parameterService, blockchain, apiTransactionManager, accountService));
    map.put("getMiningInfo", new GetMiningInfo(blockchain, blockService, generator));
    map.put("submitNonce", new SubmitNonce(propertyService, accountService, blockchain, generator));
    map.put("getRewardRecipient", new GetRewardRecipient(parameterService, blockchain, accountService));
    map.put("setRewardRecipient", new SetRewardRecipient(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("addCommitment", new AddCommitment(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("removeCommitment", new RemoveCommitment(parameterService, blockchain, accountService, apiTransactionManager));
    map.put("getAccountsWithRewardRecipient", new GetAccountsWithRewardRecipient(parameterService, accountService));
    map.put("sendMoneyEscrow", new SendMoneyEscrow(parameterService, blockchain, apiTransactionManager));
    map.put("escrowSign", new EscrowSign(parameterService, blockchain, escrowService, apiTransactionManager));
    map.put("getEscrowTransaction", new GetEscrowTransaction(escrowService));
    map.put("getAccountEscrowTransactions", new GetAccountEscrowTransactions(parameterService, escrowService));
    map.put("sendMoneySubscription", new SendMoneySubscription(parameterService, blockchain, apiTransactionManager));
    map.put("subscriptionCancel", new SubscriptionCancel(parameterService, subscriptionService, blockchain, apiTransactionManager));
    map.put("getSubscription", new GetSubscription(subscriptionService));
    map.put("getAccountSubscriptions", new GetAccountSubscriptions(parameterService, subscriptionService));
    map.put("getSubscriptionsToAccount", new GetSubscriptionsToAccount(parameterService, subscriptionService));
    map.put("createATProgram", new CreateATProgram(parameterService, blockchain, apiTransactionManager));
    map.put("getAT", new GetAT(parameterService, blockchain));
    map.put("getATDetails", new GetATDetails(parameterService));
    map.put("getATIds", new GetATIds(atService));
    map.put("getATLong", GetATLong.instance);
    map.put("getATMapValue", new GetATMapValue());
    map.put("getAccountATs", new GetAccountATs(parameterService, atService));
    map.put("generateSendTransactionQRCode", new GenerateDeeplinkQRCode(deeplinkQRCodeGenerator));
    map.put("generateDeeplink", GenerateDeeplink.instance);
    map.put("generateDeeplinkQRCode", GenerateDeeplinkQR.instance);

    // Calls that require an admin api key:
    map.put("clearUnconfirmedTransactions", new ClearUnconfirmedTransactions(transactionProcessor, propertyService));
    map.put("fullReset", new FullReset(blockchainProcessor, propertyService));
    map.put("popOff", new PopOff(blockchainProcessor, blockchain, blockService, propertyService));
    map.put("backupDB", new BackupDB(propertyService));

    // Extra api for the custom network parameters
    if(params != null) {
      params.adjustAPIs(map);
    }

    apiRequestHandlers = Collections.unmodifiableMap(map);
  }

  public abstract static class JsonRequestHandler extends HttpRequestHandler {

    public JsonRequestHandler(APITag[] apiTags, String... parameters) {
      super(apiTags, parameters);
    }

    @Override
    protected void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
      long startTime = System.currentTimeMillis();

      JsonElement response;
      try {
        response = processRequest(req);
      } catch (ParameterException e) {
        response = e.getErrorResponse();
      } catch (BurstException | RuntimeException e) {
        logger.debug("Error processing API request", e);
        response = ERROR_INCORRECT_REQUEST;
      }

      long processingTime = System.currentTimeMillis() - startTime;
      if (response instanceof JsonObject) {
        JSON.getAsJsonObject(response).addProperty("requestProcessingTime", processingTime);
      }
      if(logger.isDebugEnabled() && processingTime > 20){
        logger.debug("{} ms - {}", processingTime, req.getParameter("requestType"));
      }

      writeJsonToResponse(resp, response);
    }

    protected abstract JsonElement processRequest(HttpServletRequest request) throws BurstException;
  }

  public abstract static class HttpRequestHandler {

    private final List<String> parameters;
    private final Set<APITag> apiTags;

    HttpRequestHandler(APITag[] apiTags, String... parameters) {
      this.parameters = Collections.unmodifiableList(Arrays.asList(parameters));
      this.apiTags = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(apiTags)));
    }

    final List<String> getParameters() {
      return parameters;
    }

    final Set<APITag> getAPITags() {
      return apiTags;
    }

    protected abstract void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException;

    void addErrorMessage(HttpServletResponse resp, JsonElement msg) throws IOException {
      writeJsonToResponse(resp, msg);
    }

    final void validateParams(HttpServletRequest req) throws ParameterException {
      for (String parameter : req.getParameterMap().keySet()) {
        // _ is a parameter used in eg. jquery to avoid caching queries
        if (!this.parameters.contains(parameter) && !parameter.equals("_") && ! parameter.equals("requestType"))
          throw new ParameterException(JSONResponses.incorrectUnknown(parameter));
      }
    }

    boolean requirePost() {
      return false;
    }
  }

  private static void writeJsonToResponse(HttpServletResponse resp, JsonElement msg) throws IOException {
    resp.setContentType("application/json; charset=UTF-8");
    try (Writer writer = resp.getWriter()) {
      JSON.writeTo(msg, writer);
    }
  }

  private final boolean enforcePost;
  private final String allowedOrigins;

  public final Map<String, HttpRequestHandler> apiRequestHandlers;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    try {
      process(req, resp);
    } catch (Exception e) { // We don't want to send exception information to client...
      resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
      logger.warn("Error handling request", e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    doGet(req, resp);
  }

  @Override
  protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    doGet(req, resp);
  }

  private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, HEAD");
    resp.setHeader("Access-Control-Allow-Origin", allowedOrigins);
    resp.setHeader("Access-Control-Allow-Headers", allowedOrigins);
    resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, private");
    resp.setHeader("Pragma", "no-cache");
    resp.setDateHeader("Expires", 0);

    if (allowedBotHosts != null) {
      InetAddress remoteAddress = InetAddress.getByName(req.getRemoteHost());
      boolean allowed = false;
      for (Subnet allowedSubnet : allowedBotHosts) {
        if (allowedSubnet.isInNet(remoteAddress)) {
          allowed = true;
          break;
        }
      }
      if (!allowed) {
        resp.setStatus(HttpStatus.FORBIDDEN_403);
        writeJsonToResponse(resp, ERROR_NOT_ALLOWED);
        return;
      }
    }

    if("OPTIONS".equals(req.getMethod())) {
      // For HTTP OPTIONS reply with ACCEPTED status code -- per CORS handshake
      resp.setStatus(HttpServletResponse.SC_ACCEPTED);
      return;
    }

    String requestType = req.getParameter("requestType");
    if (requestType == null) {
      resp.setStatus(HttpStatus.NOT_FOUND_404);
      writeJsonToResponse(resp, ERROR_MISSING_REQUEST);
      return;
    }

    HttpRequestHandler apiRequestHandler = apiRequestHandlers.get(requestType);
    if (apiRequestHandler == null) {
      resp.setStatus(HttpStatus.NOT_FOUND_404);
      writeJsonToResponse(resp, ERROR_MISSING_REQUEST);
      return;
    }

    if (enforcePost && apiRequestHandler.requirePost() && !"POST".equals(req.getMethod())) {
      resp.setStatus(HttpStatus.METHOD_NOT_ALLOWED_405);
      writeJsonToResponse(resp, ERROR_NOT_ALLOWED);
      return;
    }

    try {
      if (!acceptSurplusParams) apiRequestHandler.validateParams(req);
      apiRequestHandler.processRequest(req, resp);
    } catch (ParameterException e) {
      writeJsonToResponse(resp, e.getErrorResponse());
    } catch (RuntimeException e) {
      logger.debug("Error processing API request", e);
      resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
      writeJsonToResponse(resp, ERROR_INCORRECT_REQUEST);
    }
  }
}
