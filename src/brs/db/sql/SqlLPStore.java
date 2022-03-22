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
import static brs.schema.tables.LpTokens.LP_TOKENS;

public class SqlLPStore implements PoolStore {

  private final BurstKey.LongKeyFactory<Pool> assetDbKeyFactory = new DbKey.LongKeyFactory<Pool>(LP.LP_ID) {

      @Override
      public BurstKey newKey(Pool asset) {
        return asset.dbKey;
      }

    };
  private final EntitySqlTable<Pool> assetTable;

  public SqlLPStore(DerivedTableManager derivedTableManager) {
    assetTable = new EntitySqlTable<Pool>("lp", brs.schema.Tables.LP, assetDbKeyFactory, derivedTableManager) {

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
    Iterator<Long> tokenIt = pool.getTokens().iterator();
    while(factorIt.hasNext()) {
        ctx.insertInto(LP_TOKENS).
        set(LP_TOKENS.LP_ID, pool.getId()).
        set(LP_TOKENS.TOKEN_ID, tokenIt.next()).
        set(LP_TOKENS.FACTOR, factorIt.next()).
        set(LP.HEIGHT, Burst.getBlockchain().getHeight()).execute();
    }
  }
  
  private Pool loadPool(DSLContext ctx, Record record) {
    
    long id = record.get(LP.LP_ID);
    
    Collection<Long> tokens = ctx.selectFrom(LP_TOKENS)
          .where(LP_TOKENS.LP_ID.equal(id)).and(LP_TOKENS.LATEST.isTrue())
          .orderBy(LP_TOKENS.DB_ID.desc()).fetch().getValues(LP_TOKENS.TOKEN_ID);
    Collection<Integer> factors = ctx.selectFrom(LP_TOKENS)
          .where(LP_TOKENS.LP_ID.equal(id)).and(LP_TOKENS.LATEST.isTrue())
          .orderBy(LP_TOKENS.DB_ID.desc()).fetch().getValues(LP_TOKENS.FACTOR);
    
    return new Pool(record.get(LP.VERSION), id, assetDbKeyFactory.newKey(record.get(LP.LP_ID)),
        record.get(LP.NAME), record.get(LP.SWAP_FEE), record.get(LP.PLATFORM_FEE), record.get(LP.PLATFORM_ACCOUNT_ID),
        tokens, factors);
  }

  @Override
  public BurstKey.LongKeyFactory<Pool> getPoolDbKeyFactory() {
    return assetDbKeyFactory;
  }

  @Override
  public EntitySqlTable<Pool> getPoolTable() {
    return assetTable;
  }
}
