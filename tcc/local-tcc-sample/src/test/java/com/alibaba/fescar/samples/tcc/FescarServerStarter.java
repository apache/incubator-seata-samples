package com.alibaba.fescar.samples.tcc;


import java.io.IOException;

/**
 * Mock a fescar server
 *
 * @author zhangsen
 */
public class FescarServerStarter {

    public static void main(String[] args) throws IOException {
        new FescarServer().init();
    }



}
