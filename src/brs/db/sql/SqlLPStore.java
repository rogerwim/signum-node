package brs.db.sql;

import java.util.Collection;
import java.util.Iterator;

import org.jooq.DSLContext;
import org.jooq.Record;

import brs.Burst;
import brs.Pool;
import brs.db.BurstKey;
import brs.db.store.DerivedTableManager;
import brs.db.store.PoolStore;

import static brs.schema.tables.Lp.LP;
import static brs.schema.tables.LpAssets.LP_ASSETS;

public class SqlLPStore implements PoolStore {

  private final BurstKey.LongKeyFactory<Pool> assetDbKeyFactory = new DbKey.LongKeyFactory<Pool>(LP.LP_ID) {

      @Override
      public BurstKey newKey(Pool asset) {
        return asset.dbKey;
      }

    };
  private final EntitySqlTable<Pool> lpTable;

  public SqlLPStore(DerivedTableManager derivedTableManager) {
    lpTable = new EntitySqlTable<Pool>("lp", brs.schema.Tables.LP, assetDbKeyFactory, derivedTableManager) {

      @Override
      protected Pool load(DSLContext ctx, Record record) {
        return loadPool(ctx, record);
      }

      @Override
      protected void save(DSLContext ctx, Pool pool) {
        savePool(ctx, pool);
      }
    };
  }

  private void savePool(DSLContext ctx, Pool pool) {
    ctx.insertInto(LP).
      set(LP.VERSION, pool.getVersion()).
      set(LP.LP_ID, pool.getId()).
      set(LP.SWAP_FEE, pool.getSwapFee()).
      set(LP.PLATFORM_FEE, pool.getPlatformFee()).
      set(LP.PLATFORM_ACCOUNT_ID, pool.getPlatformAccountId()).
      set(LP.HEIGHT, Burst.getBlockchain().getHeight()).execute();
    
    Iterator<Integer> factorIt = pool.getFactors().iterator();
    Iterator<Long> assetIt = pool.getAssetIds().iterator();
    while(factorIt.hasNext()) {
        ctx.insertInto(LP_ASSETS).
        set(LP_ASSETS.LP_ID, pool.getId()).
        set(LP_ASSETS.ASSET_ID, assetIt.next()).
        set(LP_ASSETS.FACTOR, factorIt.next()).
        set(LP.HEIGHT, Burst.getBlockchain().getHeight()).execute();
    }
  }
  
  private Pool loadPool(DSLContext ctx, Record record) {
    
    long id = record.get(LP.LP_ID);
    
    Collection<Long> assetIds = ctx.selectFrom(LP_ASSETS)
          .where(LP_ASSETS.LP_ID.equal(id)).and(LP_ASSETS.LATEST.isTrue())
          .orderBy(LP_ASSETS.DB_ID.desc()).fetch().getValues(LP_ASSETS.ASSET_ID);
    Collection<Integer> factors = ctx.selectFrom(LP_ASSETS)
          .where(LP_ASSETS.LP_ID.equal(id)).and(LP_ASSETS.LATEST.isTrue())
          .orderBy(LP_ASSETS.DB_ID.desc()).fetch().getValues(LP_ASSETS.FACTOR);
    
    return new Pool(record.get(LP.VERSION), id, assetDbKeyFactory.newKey(record.get(LP.LP_ID)),
        record.get(LP.NAME), record.get(LP.SWAP_FEE), record.get(LP.PLATFORM_FEE), record.get(LP.PLATFORM_ACCOUNT_ID),
        assetIds, factors);
  }

  @Override
  public BurstKey.LongKeyFactory<Pool> getPoolDbKeyFactory() {
    return assetDbKeyFactory;
  }

  @Override
  public EntitySqlTable<Pool> getPoolTable() {
    return lpTable;
  }
  
  @Override
  public Collection<Pool> getLPsByPlatform(long platformId, int from, int to){
    return lpTable.getManyBy(LP.PLATFORM_ACCOUNT_ID.eq(platformId), from, to);
  }
}
