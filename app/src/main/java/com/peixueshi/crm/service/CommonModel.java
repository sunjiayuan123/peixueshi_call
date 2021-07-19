/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peixueshi.crm.service;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ================================================
 * 展示 Model 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.3">Model wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
@ActivityScope
public class CommonModel extends com.jess.arms.mvp.BaseModel {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public CommonModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    public Observable<String> sendCode(String phone) {
        return mRepositoryManager.obtainRetrofitService(CommonService.class)
                .sendCode(phone);
    }

}
