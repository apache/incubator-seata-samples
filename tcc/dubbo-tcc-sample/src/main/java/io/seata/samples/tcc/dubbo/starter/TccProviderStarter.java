package io.seata.samples.tcc.dubbo.starter;

import io.seata.samples.jit.AbstractStarter;
import io.seata.samples.jit.ApplicationKeeper;
import org.springframework.context.support.ClassPathXmlApplicationContext;
/**
 * The type Dubbo tcc provider starter.
 *
 * @author zhangsen
 * @author ppf
 */
public class TccProviderStarter  extends AbstractStarter {
    public static void main(String[] args) throws Exception {
        new TccProviderStarter().start0(args);
    }

    @Override
    protected void start0(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"spring/seata-tcc.xml", "spring/seata-dubbo-provider.xml"});
        new ApplicationKeeper().keep();
    }
}
