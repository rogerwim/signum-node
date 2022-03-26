package brs.db.store;

import java.util.Collection;

import brs.Pool;
import brs.db.BurstKey;
import brs.db.sql.EntitySqlTable;

public interface PoolStore {
  BurstKey.LongKeyFactory<Pool> getPoolDbKeyFactory();

  EntitySqlTable<Pool> getPoolTable();
  
  Collection<Pool> getLPsByPlatform(long platformId, int from, int to);

}
