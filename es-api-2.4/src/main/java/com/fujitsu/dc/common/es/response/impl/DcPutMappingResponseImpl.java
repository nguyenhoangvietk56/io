/**
 * personium.io
 * Copyright 2014 FUJITSU LIMITED
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fujitsu.dc.common.es.response.impl;

import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;

import com.fujitsu.dc.common.es.response.DcPutMappingResponse;


/**
 * IndexResponseのラッパークラス.
 */
public class DcPutMappingResponseImpl extends DcActionResponseImpl implements DcPutMappingResponse {

    /**
     * .
     */
    private DcPutMappingResponseImpl() {
        super(null);
        throw new IllegalStateException();
    }

    /**
     * GetResponseを指定してインスタンスを生成する.
     * @param response ESからのレスポンスオブジェクト
     */
    private DcPutMappingResponseImpl(PutMappingResponse response) {
        super(response);
    }

    /**
     * .
     * @param response .
     * @return .
     */
    public static DcPutMappingResponse getInstance(PutMappingResponse response) {
        if (response == null) {
            return null;
        }
        return new DcPutMappingResponseImpl(response);
    }
}
