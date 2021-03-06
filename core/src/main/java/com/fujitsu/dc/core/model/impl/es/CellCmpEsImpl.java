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
package com.fujitsu.dc.core.model.impl.es;

import java.util.HashMap;
import java.util.Map;

import org.apache.wink.webdav.model.ObjectFactory;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fujitsu.dc.common.es.response.DcGetResponse;
import com.fujitsu.dc.common.es.response.DcIndexResponse;
import com.fujitsu.dc.common.es.response.DcSearchResponse;
import com.fujitsu.dc.core.DcCoreException;
import com.fujitsu.dc.core.model.Cell;
import com.fujitsu.dc.core.model.CellCmp;
import com.fujitsu.dc.core.model.impl.es.accessor.EntitySetAccessor;
import com.fujitsu.dc.core.model.impl.es.cache.CellCache;
import com.fujitsu.dc.core.model.impl.es.doc.CellDocHandler;
import com.fujitsu.dc.core.model.lock.Lock;
import com.fujitsu.dc.core.model.lock.LockManager;

/**
 * Cellに対応した処理を扱う部品のESを使った実装.
 */
public class CellCmpEsImpl extends DavCmpEsImpl implements CellCmp {
    EntitySetAccessor cellAcceccor;

    /**
     * ログ.
     */
    static Logger log = LoggerFactory.getLogger(CellCmpEsImpl.class);

    CellDocHandler docHandler;

    /**
     * コンストラクタ.
     * @param cell ellオブジェクト
     */
    public CellCmpEsImpl(final Cell cell) {
        this.of = new ObjectFactory();
        this.cell = cell;
        this.cellAcceccor = EsModel.cell();
        this.nodeId = cell.getId();
        this.docHandler = new CellDocHandler(getNode());

        try {
            this.load();
        } catch (Exception e) {
            log.debug("Exception occured. Maybe Dav index is not present so creating..");
        }

    }

    /**
     * CellEntityのアクセッサーを取得する.
     * @return cellAcceccor
     */
    private EntitySetAccessor getCellAccessor() {
        return this.cellAcceccor;
    }

    /**
     * Nodeのデータを取得する.
     * @return Node取得結果
     */
    @Override
    public DcGetResponse getNode() {
        DcGetResponse res = this.getCellAccessor().get(this.nodeId);
        return res;
    }

    /**
     * バージョン指定でNodeの情報を更新する.
     * @return 更新結果.
     */
    @Override
    public DcIndexResponse updateNodeWithVersion() {
        DcIndexResponse resp;
        try {
            resp = this.getCellAccessor().update(this.nodeId, this.docHandler, this.version);
        } finally {
            CellCache.clear(this.cell.getName());
        }
        return resp;
    }

    /**
     * Nodeの情報を更新する.
     * @return 更新結果.
     */
    @Override
    public DcIndexResponse updateNode() {
        DcIndexResponse resp;
        try {
            resp = this.getCellAccessor().update(this.nodeId, this.docHandler);
        } finally {
            CellCache.clear(this.cell.getName());
        }
        return resp;
    }

    /**
     * Node情報を作成する.
     * @return 作成結果
     */
    @Override
    public DcIndexResponse createNode() {
        DcIndexResponse res;
        res = this.getCellAccessor().create(this.docHandler);
        return res;
    }

    /**
     * Nodeを削除する.
     */
    public void deleteNode() {
        try {
            this.getCellAccessor().delete(this.docHandler);
        } finally {
            CellCache.clear(this.cell.getName());
        }
    }
    @Override
    public void makeEmpty() {
        // TODO Impl
    }
    /**
     * 子リソースの情報を取得する.
     * @return 子リソースの検索結果
     */
    @Override
    public DcSearchResponse getChildResource() {
        // 子リソースの情報を取得する。
        Map<String, Object> query = new HashMap<String, Object>();
        Map<String, Object> query2 = new HashMap<String, Object>();
        Map<String, Object> query3 = new HashMap<String, Object>();
        query3.put(DavNode.KEY_PARENT, this.nodeId);
        query2.put("term", query3);
        query.put("query", query2);

        DcSearchResponse resp = this.getCellAccessor().search(query);
        return resp;
    }

    /**
     * jsonにACL情報を設定する.
     * @param aclJson ACL
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void setAclToJson(JSONObject aclJson) {
        this.docHandler.setAclFields(aclJson);
    }

    /**
     * jsonにProp情報を設定する.
     * @param propsJson Prop
     */
    protected void setPropToJson(Map<String, Object> propsJson) {
        this.docHandler.setDynamicFields(propsJson);
    }

    /**
     * Cellをロックする.
     * @return 自ノードのロック
     */
    @Override
    public Lock lock() {
        return LockManager.getLock(Lock.CATEGORY_CELL, this.cell.getId(), null, null);
    }

    @Override
    public DcCoreException getNotFoundException() {
        return DcCoreException.Dav.CELL_NOT_FOUND;
    }
}
