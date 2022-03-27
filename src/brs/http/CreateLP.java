package brs.http;

import brs.*;
import brs.fluxcapacitor.FluxValues;
import brs.services.ParameterService;
import brs.util.Convert;
import brs.util.TextUtils;
import com.google.gson.JsonElement;

import javax.servlet.http.HttpServletRequest;

import static brs.http.JSONResponses.*;
import static brs.http.common.Parameters.*;

import java.util.ArrayList;
import java.util.List;

public final class CreateLP extends CreateTransaction {

  private final ParameterService parameterService;

  CreateLP(ParameterService parameterService, Blockchain blockchain, APITransactionManager apiTransactionManager) {
    super(new APITag[]{APITag.AE, APITag.CREATE_TRANSACTION}, apiTransactionManager,
        NAME_PARAMETER, SWAP_FEE_PARAMETER, PLATFORM_FEE_PARAMETER, PLATFORM_ACCOUNT_ID_PARAMETER, ASSETS_PARAMETER, FACTORS_PARAMETER);
    this.parameterService = parameterService;
  }

  @Override
  protected
  JsonElement processRequest(HttpServletRequest req) throws BurstException {

    if(!Burst.getFluxCapacitor().getValue(FluxValues.NEXT_FORK)) {
      return ERROR_NOT_ALLOWED;
    }

    String name = req.getParameter(NAME_PARAMETER);
    if (name == null) {
      return MISSING_NAME;
    }
    name = name.trim();
    if (name.length() < Constants.MIN_ASSET_NAME_LENGTH || name.length() > Constants.MAX_ASSET_NAME_LENGTH-2 || !TextUtils.isInAlphabet(name)) {
      return JSONResponses.incorrect(NAME_PARAMETER);
    }
    
    String swapFeeParameter = req.getParameter(SWAP_FEE_PARAMETER);
    if(swapFeeParameter == null || swapFeeParameter.isEmpty()) {
      return JSONResponses.missing(SWAP_FEE_PARAMETER);
    }
    int swapFee = Integer.parseInt(swapFeeParameter);
    if(swapFee < 0 || swapFee > 50) {
      return JSONResponses.incorrect(SWAP_FEE_PARAMETER);      
    }
    
    String platformFeeParameter = req.getParameter(PLATFORM_FEE_PARAMETER);
    if(platformFeeParameter == null || platformFeeParameter.isEmpty()) {
      return JSONResponses.missing(PLATFORM_FEE_PARAMETER);
    }
    int platformFee = Integer.parseInt(platformFeeParameter);
    if(platformFee < 0 || platformFee > 50) {
      return JSONResponses.incorrect(SWAP_FEE_PARAMETER);      
    }
    
    long platformId = Convert.parseUnsignedLong(req.getParameter(PLATFORM_ACCOUNT_ID_PARAMETER));
    if(platformId == 0L) {
      return JSONResponses.incorrect(PLATFORM_ACCOUNT_ID_PARAMETER);
    }
    
    List<Asset> assets = parameterService.getAssets(req);
    List<Integer> factors = parameterService.getFactors(req);
    if(assets.size() != factors.size()) {
      return JSONResponses.incorrect(FACTORS_PARAMETER);      
    }
    for(Integer f : factors) {
      if(f <= 0 || f > 100) {
        return JSONResponses.incorrect(FACTORS_PARAMETER);
      }
    }
    
    if(assets.size() < 2 || assets.size() > 8) {
      return JSONResponses.incorrect(ASSETS_PARAMETER);
    }
    
    ArrayList<Long> assetIds = new ArrayList<>();
    for(Asset a : assets) {
      assetIds.add(a == null ? 0L : a.getId());
    }

    Account account = parameterService.getSenderAccount(req);
    Attachment attachment = new Attachment.ColoredCoinsLPCreation(name, swapFee, platformFee, platformId, assetIds, factors);
    return createTransaction(req, account, attachment);

  }

}
