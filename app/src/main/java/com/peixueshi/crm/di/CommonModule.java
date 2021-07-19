package com.peixueshi.crm.di;

import com.jess.arms.di.scope.ActivityScope;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class CommonModule {

    @Contract(pure = true)
    public CommonModule() {

    }

    @Provides
    @ActivityScope
    static List<String> provideUserList() {
        return new ArrayList<>();
    }


}
