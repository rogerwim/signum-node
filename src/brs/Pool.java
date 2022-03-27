package brs;

import java.util.Collection;

import brs.db.BurstKey;

public class Pool {

  private final byte version;
  public final BurstKey dbKey;
  private final long id;
  private final String name;
  private final int swapFee;
  private final int platformFee;
  private final long platformAccountId;
  private final Collection<Long> assetIds;
  private final Collection<Integer> factors;

  public Pool(byte version, long id, BurstKey dbKey, String name, int swapFee, int platformFee, long platformAccountId,
      Collection<Long> assetIds, Collection<Integer> factors) {
    this.version = version;
    this.dbKey = dbKey;
    this.name = name;
    this.id = id;
    this.swapFee = swapFee;
    this.platformFee = platformFee;
    this.platformAccountId = platformAccountId;
    this.assetIds = assetIds;
    this.factors = factors;
  }

  public Pool(BurstKey dbKey, Transaction transaction, Attachment.ColoredCoinsLPCreation attachment) {
    this.version = (byte)1;
    this.dbKey = dbKey;
    this.id = transaction.getId();
    this.name = attachment.getName();
    this.swapFee = attachment.getSwapFee();
    this.platformFee = attachment.getPlatformFee();
    this.platformAccountId = attachment.getPlatformAccountId();
    this.assetIds = attachment.getAssetIds();
    this.factors = attachment.getFactors();
  }
  
  public byte getVersion() {
    return version;
  }
  
  public long getId() {
    return id;
  }

  public long getPlatformAccountId() {
    return platformAccountId;
  }

  public String getName() {
    return name;
  }
  
  public int getSwapFee() {
    return swapFee;
  }
  
  public int getPlatformFee() {
    return platformFee;
  }

  public Collection<Long> getAssetIds() {
    return this.assetIds;
  }
  
  public Collection<Integer> getFactors() {
    return this.factors;
  }
}
