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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fujitsu.dc.common.es.EsBulkRequest;
import com.fujitsu.dc.core.DcCoreException;

/**
 * DavのNodeデータを扱うクラス.
 */
public class DavNode implements EsBulkRequest {

    String id;
    String cellId;
    String boxId;
    String nodeType;
    String parentId;
    Map<String, String> children;
    JSONObject acl;
    JSONObject properties;
    long published;
    long updated;
    Map<String, Object> file;
    private BULK_REQUEST_TYPE requestType = BULK_REQUEST_TYPE.INDEX;

    /** ES上のDavNode格納においてCellの内部IDを保存するJSONキー. */
    public static final String KEY_CELL_ID = "c";

    /** ES上のDavNode格納においてBoxの内部IDを保存するJSONキー. */
    public static final String KEY_BOX_ID = "b";

    /** ES上のDavNode格納においてノードのタイプを保存するJSONキー. */
    public static final String KEY_NODE_TYPE = "t";

    /** ES上のDavNode格納において親ノードのIDを保存するJSONキー. */
    public static final String KEY_PARENT = "s";

    /** ES上のDavNode格納において子ノードのリストを保存するJSONキー. */
    public static final String KEY_CHILDREN = "o";

    /** ES上のDavNode格納においてACLを保存するJSONキー. */
    public static final String KEY_ACL = "a";

    /** ES上のDavNode格納においてPROPSを保存するJSONキー. */
    public static final String KEY_PROPS = "d";

    /** ES上のDavNode格納において作成日時を保存するJSONキー. */
    public static final String KEY_PUBLISHED = "p";

    /** ES上のDavNode格納において更新日時を保存するJSONキー. */
    public static final String KEY_UPDATED = "u";

    /** ES上のDavNode格納においてファイルを保存するJSONキー. */
    public static final String KEY_FILE = "f";

    /**
     * コンストラクタ.
     */
    public DavNode() {
        long date = new Date().getTime();
        this.id = null;
        this.cellId = null;
        this.boxId = null;
        this.nodeType = null;
        this.parentId = null;
        this.children = new HashMap<String, String>();
        this.acl = new JSONObject();
        this.properties = new JSONObject();
        this.published = date;
        this.updated = date;
        this.file = null;
    }

    /**
     * コンストラクタ.
     * @param cellId セルID
     * @param boxId ボックスID
     * @param nodeType ノードタイプ
     */
    public DavNode(String cellId, String boxId, String nodeType) {
        long date = new Date().getTime();
        this.id = null;
        this.cellId = cellId;
        this.boxId = boxId;
        this.nodeType = nodeType;
        this.parentId = null;
        this.children = new HashMap<String, String>();
        this.acl = new JSONObject();
        this.properties = new JSONObject();
        this.published = date;
        this.updated = date;
        this.file = null;
    }

    /**
     * コンストラクタ.
     * @param id ID
     * @param jsonStr ソース
     * @return DavNode
     */
    @SuppressWarnings("unchecked")
    public static DavNode createFromJsonString(String id, String jsonStr) {
        if (jsonStr == null) {
            return null;
        }
        JSONParser parser = new JSONParser();
        JSONObject source;
        try {
            source = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            // ESのJSONが壊れている状態。
            throw DcCoreException.Dav.DAV_INCONSISTENCY_FOUND.reason(e);
        }
        DavNode davNode = new DavNode();
        davNode.setId(id);
        davNode.setCellId((String) source.get(KEY_CELL_ID));
        davNode.setBoxId((String) source.get(KEY_BOX_ID));
        davNode.setNodeType((String) source.get(KEY_NODE_TYPE));
        if (source.get(KEY_PARENT) instanceof String) {
            davNode.setParentId((String) source.get(KEY_PARENT));
        }
        davNode.setChildren((Map<String, String>) source.get(KEY_CHILDREN));
        davNode.setAcl((JSONObject) source.get(KEY_ACL));
        davNode.setProperties((JSONObject) source.get(KEY_PROPS));
        davNode.setCellId((String) source.get(KEY_CELL_ID));
        davNode.setPublished(((Long) source.get(KEY_PUBLISHED)).longValue());
        davNode.setUpdated(((Long) source.get(KEY_UPDATED)).longValue());
        if (source.containsKey(KEY_FILE)) {
            davNode.setFile((Map<String, Object>) source.get(KEY_FILE));
        }
        return davNode;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the cellId
     */
    public String getCellId() {
        return cellId;
    }

    /**
     * @param cellId the cellId to set
     */
    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    /**
     * @return the boxId
     */
    public String getBoxId() {
        return boxId;
    }

    /**
     * @param boxId the boxId to set
     */
    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    /**
     * @return the nodeType
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * @param nodeType the nodeType to set
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * @return the parentId
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * @param parentId the parentId to set
     */
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the children
     */
    public Map<String, String> getChildren() {
        return children;
    }

    /**
     * @param children the children to set
     */
    public void setChildren(Map<String, String> children) {
        this.children = children;
    }

    /**
     * @return the acl
     */
    public JSONObject getAcl() {
        return acl;
    }

    /**
     * @param acl the acl to set
     */
    public void setAcl(JSONObject acl) {
        this.acl = acl;
    }

    /**
     * @return the properties
     */
    public JSONObject getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(JSONObject properties) {
        this.properties = properties;
    }

    /**
     * @return the published
     */
    public Long getPublished() {
        return published;
    }

    /**
     * @param published the published to set
     */
    public void setPublished(long published) {
        this.published = published;
    }

    /**
     * @return the updated
     */
    public Long getUpdated() {
        return this.updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(long updated) {
        this.updated = updated;
    }

    /**
     * @return the file
     */
    public Map<String, Object> getFile() {
        return this.file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(Map<String, Object> file) {
        this.file = file;
    }

    /**
     * JSONオブジェクトに変換してDavNodeの情報を返却する.
     * @return JSONObject
     */
    @SuppressWarnings("unchecked")
    public JSONObject getSource() {
        JSONObject json = new JSONObject();
        json.put(KEY_CELL_ID, this.cellId);
        json.put(KEY_BOX_ID, this.boxId);
        json.put(KEY_NODE_TYPE, this.nodeType);
        json.put(KEY_PARENT, this.parentId);
        json.put(KEY_CHILDREN, this.children);
        json.put(KEY_ACL, this.acl);
        json.put(KEY_PROPS, this.properties);
        json.put(KEY_PUBLISHED, this.published);
        json.put(KEY_UPDATED, this.updated);
        if (this.file != null) {
            json.put(KEY_FILE, this.file);
        }
        return json;
    }

    @Override
    public String getType() {
        return "dav";
    }

    @Override
    public BULK_REQUEST_TYPE getRequestType() {
        return requestType;
    }

    /**
     * リクストの種別（INDEX / DELETE)を設定する. <br />
     * 既存の処理への影響を考慮して、デフォルト値は INDEX にしておく。
     * @param request リクエスト種別
     */
    public void setRequestType(BULK_REQUEST_TYPE request) {
        this.requestType = request;
    }
}