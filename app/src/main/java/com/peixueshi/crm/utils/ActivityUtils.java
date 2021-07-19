package com.peixueshi.crm.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


/**
 * @author hyx
 * @description
 * @date 2018/6/18.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     */
    public static void addOrShowFragmentToActivity(@NonNull FragmentManager fragmentManager,
                                                   @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (fragmentManager.getFragments() != null) {
            for (Fragment f : fragmentManager.getFragments()) {
                if(!f.toString().contains("OneFragment") && !f.toString().contains("OneFragmentOrder") && !f.toString().contains("OneFragmentHandMaster") && !f.toString().contains("OneFragmentRanking")){//
                    transaction.hide(f);
                }
            }
            if (fragmentManager.getFragments().contains(fragment)) {
                transaction.show(fragment);
                transaction.commitAllowingStateLoss();
                return;
            }
        }

        transaction.add(frameId, fragment);
        transaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

}
