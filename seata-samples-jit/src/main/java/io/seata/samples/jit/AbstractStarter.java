package io.seata.samples.jit;

/**
 * @author ppf
 */
public abstract class AbstractStarter implements Starter{

    protected abstract void start0(String[] args) throws Exception;


    @Override
    public void start(String[] args) {
        try {
            start0(args);
        } catch (Exception e) {
            throw new RuntimeException("Seata test failed");
        }
    }
}
