package brs.db.store;

import brs.Pool;
import brs.db.BurstKey;
import brs.db.sql.EntitySqlTable;

public interface PoolStore {
  BurstKey.LongKeyFactory<Pool> getPoolDbKeyFactory();

  EntitySqlTable<Pool> getPoolTable();

}
