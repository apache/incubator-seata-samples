/*
 *  Copyright 1999-2021 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.samples.jit;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The type Application keeper.
 *
 * @author ppf
 */
public class ApplicationKeeper {

    private final ReentrantLock LOCK = new ReentrantLock();
    private final Condition STOP = LOCK.newCondition();

    /**
     * Instantiates a new Application keeper.
     */
    public ApplicationKeeper() {
        addShutdownHook();
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    LOCK.lock();
                    STOP.signal();
                } finally {
                    LOCK.unlock();
                }
            }
        }));
    }

    /**
     * Keep.
     */
    public void keep() {
        keep(TimeUnit.MINUTES.toMillis(30));
    }

    /**
     * Keep.
     */
    public void keep(long timeout) {
        synchronized (LOCK) {
            try {
                LOCK.wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
