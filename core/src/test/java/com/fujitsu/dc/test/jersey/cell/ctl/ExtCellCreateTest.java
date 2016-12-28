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
package com.fujitsu.dc.test.jersey.cell.ctl;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.fujitsu.dc.test.categories.Integration;
import com.fujitsu.dc.test.categories.Regression;
import com.fujitsu.dc.test.categories.Unit;
import com.fujitsu.dc.test.jersey.AbstractCase;
import com.fujitsu.dc.test.jersey.ODataCommon;
import com.fujitsu.dc.test.unit.core.UrlUtils;
import com.fujitsu.dc.test.utils.ExtCellUtils;

/**
 * ExtCell作成のテスト.
 */
@Category({Unit.class, Integration.class, Regression.class })
public class ExtCellCreateTest extends ODataCommon {

    private static String cellName = "testcell1";
    private final String token = AbstractCase.MASTER_TOKEN_NAME;
    private static final String TRAILING_SLASH = "/";

    /**
     * コンストラクタ. テスト対象のパッケージをsuperに渡す必要がある
     */
    public ExtCellCreateTest() {
        super("com.fujitsu.dc.core.rs");
    }

    /**
     * ExtCell作成の正常系のテスト.
     */
    @Test
    public final void ExtCell作成の正常系のテスト() {
        String extCellUrl = UrlUtils.cellRoot("cellHoge1");

        try {
            ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_CREATED);
        } finally {
            ExtCellUtils.delete(token, cellName, extCellUrl, -1);
        }
    }

    /**
     * TrailingSlashの無いURLを指定した場合400エラーを返却すること.
     */
    @Test
    public final void TrailingSlashの無いURLを指定した場合400エラーを返却すること() {
        String extCellUrl = "http://localhost:9998/testCell1";
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Urlが空文字の場合400エラーを返却すること.
     */
    @Test
    public final void Urlが空文字の場合400エラーを返却すること() {
        String extCellUrl = "";
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * TrailingSlashの無いURLを指定した場合400エラーを返却すること.
     */
    @Test
    public final void 正規化されていないパスを含むURLを指定した場合400エラーを返却すること() {
        String extCellUrl = "http://localhost:8080/dc1-core/testcell1/../test/./box/../";
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Urlが1024文字の場合正常に作成されること.
     */
    @Test
    public final void Urlが1024文字の場合正常に作成されること() {
        String extCellUrl = "http://localhost:8080/dc1-core/testcell1" + StringUtils.repeat("a", 983) + TRAILING_SLASH;

        try {
            ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_CREATED);
        } finally {
            ExtCellUtils.delete(token, cellName, extCellUrl, -1);
        }
    }

    /**
     * Urlが1025文字以上の場合400エラーを返却すること.
     */
    @Test
    public final void Urlが1025文字以上の場合400エラーを返却すること() {
        String extCellUrl = "http://localhost:8080/dc1-core/testcell1" + StringUtils.repeat("a", 984) + TRAILING_SLASH;
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * UrlのschemeがFTPの場合400エラーを返却すること.
     */
    @Test
    public final void UrlのschemeがFTPの場合400エラーを返却すること() {
        String extCellUrl = "ftp://localhost:21/dc1-core/testcell1/";
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * UrlのschemeがURNの場合400エラーを返却すること.
     */
    @Test
    public final void UrlのschemeがURNの場合400エラーを返却すること() {
        String extCellUrl = "urn:x-dc1:test";
        ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_BAD_REQUEST);
    }

    /**
     * Urlにローカルユニットを指定した場合正常に作成されること.
     */
    @Test
    public final void ExtCell作成時Urlをローカルユニット指定した場合正常に作成されることを確認() {
        String extCellUrl = "personium-localunit:/testcell2/";
        try {
            ExtCellUtils.create(token, cellName, extCellUrl, HttpStatus.SC_CREATED);
        } finally {
            System.out.println("delete extCell");
            ExtCellUtils.delete(token, cellName, extCellUrl, -1);
        }
    }
}
