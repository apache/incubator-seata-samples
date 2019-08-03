/*
 *  Copyright 1999-2018 Alibaba Group Holding Ltd.
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

package io.seata.samples.springboot.service.impl;

import java.math.BigDecimal;

import com.alibaba.dubbo.config.annotation.Service;

import io.seata.core.context.RootContext;
import io.seata.samples.springboot.service.AssetService;
import io.seata.samples.springboot.sys.domain.Asset;
import io.seata.samples.springboot.sys.repository.AssetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Asset service.
 */
@Service(interfaceClass = AssetService.class, timeout = 10000)
@Component
public class AssetServiceImpl implements AssetService {

    /**
     * The constant LOGGER.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(AssetService.class);

    /**
     * The constant ASSET_ID.
     */
    public static final String ASSET_ID = "DF001";

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public int increase() {
        LOGGER.info("Asset Service Begin ... xid: " + RootContext.getXID() + "\n");
        Asset asset = assetRepository.findById(ASSET_ID).get();
        asset.setAmount(asset.getAmount().add(new BigDecimal("1")));
        assetRepository.save(asset);
        throw new RuntimeException("test exception for seata, your transaction should be rollbacked,asset=" + asset);
    }
}
