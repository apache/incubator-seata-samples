package io.seata.samples.saga.action;

/**
 * Inventory Actions
 */
public interface InventoryAction {

    /**
     * reduce
     *
     * @param count
     * @return
     */
    boolean reduce(String businessKey, int count);

    /**
     * increase
     *
     * @return
     */
    boolean compensateReduce(String businessKey);
}
