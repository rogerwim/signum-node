package brs.assetexchange;

import brs.Account.AccountAsset;
import brs.*;
import brs.db.BurstKey;
import brs.db.sql.EntitySqlTable;
import brs.db.store.AssetStore;
import brs.db.store.PoolStore;

import java.util.Collection;

class AssetServiceImpl {

  private final AssetStore assetStore;
  private final PoolStore lpStore;
  private final AssetAccountServiceImpl assetAccountService;
  private final TradeServiceImpl tradeService;
  private final AssetTransferServiceImpl assetTransferService;

  private final EntitySqlTable<Asset> assetTable;
  private final EntitySqlTable<Pool> lpTable;

  private final BurstKey.LongKeyFactory<Asset> assetDbKeyFactory;

  public AssetServiceImpl(AssetAccountServiceImpl assetAccountService, TradeServiceImpl tradeService, AssetStore assetStore, PoolStore lpStore, AssetTransferServiceImpl assetTransferService) {
    this.assetAccountService = assetAccountService;
    this.tradeService = tradeService;
    this.assetStore = assetStore;
    this.lpStore = lpStore;
    this.assetTable = assetStore.getAssetTable();
    this.lpTable = lpStore.getPoolTable();
    this.assetDbKeyFactory = assetStore.getAssetDbKeyFactory();
    this.assetTransferService = assetTransferService;
  }

  public Asset getAsset(long id) {
    return assetTable.get(assetDbKeyFactory.newKey(id));
  }

  public Collection<AccountAsset> getAccounts(Asset asset, boolean filterIgnored, long minimumQuantity, int from, int to) {
    return assetAccountService.getAssetAccounts(asset, filterIgnored, minimumQuantity, from, to);
  }

  public Collection<Trade> getTrades(long assetId, int from, int to) {
    return tradeService.getAssetTrades(assetId, from, to);
  }

  public Collection<AssetTransfer> getAssetTransfers(long assetId, int from, int to) {
    return assetTransferService.getAssetTransfers(assetId, from, to);
  }

  public Collection<Asset> getAllAssets(int from, int to) {
    return assetTable.getAll(from, to);
  }

  public Collection<Asset> getAssetsIssuedBy(long accountId, int from, int to) {
    return assetStore.getAssetsIssuedBy(accountId, from, to);
  }

  public Collection<Pool> getLPsByPlatform(long platformId, int from, int to) {
    return lpStore.getLPsByPlatform(platformId, from, to);
  }

  public int getAssetsCount() {
    return assetTable.getCount();
  }
  
  public int getLPsCount() {
    return lpTable.getCount();
  }

  public void addAsset(Transaction transaction, Attachment.ColoredCoinsAssetIssuance attachment) {
    final BurstKey dbKey = assetDbKeyFactory.newKey(transaction.getId());
    assetTable.insert(new Asset(dbKey, transaction, attachment));
  }
  
  public void addLP(Transaction transaction, Attachment.ColoredCoinsLPCreation attachment) {
    final BurstKey dbKey = assetDbKeyFactory.newKey(transaction.getId());

    // we add both, the asset and the LP
    assetTable.insert(new Asset(dbKey, transaction, attachment));
    lpTable.insert(new Pool(dbKey, transaction, attachment));
  }

}
